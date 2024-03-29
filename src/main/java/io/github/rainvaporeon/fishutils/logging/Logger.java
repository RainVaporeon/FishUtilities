package io.github.rainvaporeon.fishutils.logging;

import io.github.rainvaporeon.fishutils.action.Result;
import io.github.rainvaporeon.fishutils.utils.noop.io.NoOpPrintStream;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Logger implements ILogger {

    private static final String RESET = "\033[0m";

    private static final SimpleDateFormat DEFAULT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    // 1.2.8: Removed creator thread control, simply redundancy.

    private final File out;

    private final String name;

    private final SimpleDateFormat format;

    // 1.2.6: Logger enhancements
    private PrintStream stream;

    // 1.2.7: Further enhancements
    private PrintStream errStream;
    private PrintStream debugStream;

    public Logger(String name) {
        this(name, null);
    }

    public Logger(String name, File out) {
        this(name, out, DEFAULT);
    }

    public Logger(String name, File out, SimpleDateFormat dateFormat) {
        this(name, out, dateFormat, null);
    }

    public Logger(String name, File out, SimpleDateFormat dateFormat, PrintStream stream) {
        this.name = name;
        this.out = LogInternals.getSharedObjectIfPresent(out);
        this.format = Objects.requireNonNull(dateFormat);
        this.stream = stream;
        this.errStream = stream;
        this.debugStream = stream;

        LogInternals.appendFileIfAbsent(out);
    }

    /**
     * Configures this stream to direct all default outputs to {@link System#out}, and
     * all errors to {@link System#err}
     */
    public void configured() {
        setOutputStream(System.out);
        setErrStream(System.err);
        setDebugStream(System.out);
    }

    public Result setOut(File out) {
        try {
            if(!out.exists()) out.createNewFile();
            Field field = this.getClass().getDeclaredField("out");
            field.setAccessible(true);
            field.set(this, out);
            return Result.SUCCESS;
        } catch (ReflectiveOperationException | IOException e) {
            this.fatal("Could not set out of " + this.name + ":", e);
            return Result.ERROR;
        }
    }

    /**
     * Sets the output stream source
     * @param stream the stream
     */
    public void setOutputStream(PrintStream stream) {
        this.stream = stream;
    }

    /**
     * Sets the error stream
     * @param errStream the stream
     * @apiNote this will forward anything at severity {@code ERROR} or {@code FATAL}
     * to this stream.
     * @since 1.2.8
     */
    public void setErrStream(PrintStream errStream) {
        this.errStream = errStream;
    }

    /**
     * Sets the debug stream
     * @param debugStream the stream
     * @apiNote this will forward anything at severity {@code DEBUG} to this stream.
     * @since 1.2.8
     */
    public void setDebugStream(PrintStream debugStream) {
        this.debugStream = debugStream;
    }

    /**
     * Directs all streams to this output stream
     * @param stream the stream
     * @since 1.2.8
     */
    public void setAllOutputStream(PrintStream stream) {
        this.stream = stream;
        this.errStream = stream;
        this.debugStream = stream;
    }

    public void log(Severity severity, String message, Throwable t) {
        PrintStream stream = this.getOutput(severity);

        stream.println("[" + name + "] " + severity + message + RESET);

        if(out != null)
            writeLog("[" + name + "] " + severity + ": " + message);
        if (t != null) {
            stream.print(severity);
            t.printStackTrace(getOutput(Severity.ERROR));
            stream.println(RESET);
            if(out != null)
                writeError(t);
        }
    }

    // Convenience methods to log with severity and all those boring things,
    // Although these are "convenience methods", the logging method was
    // written the last.

    @Override
    public void newline() {
        log(Severity.INFO, "", null);
    }

    public void logStackTrace() {
        Throwable t = new Throwable("Stacktrace dump requested: ");
        this.debug("Stacktrace requested:", t);
    }

    @Override
    public void success(String message) {
        log(Severity.SUCCESS, message, null);
    }

    @Override
    public void success(String message, Throwable t) {
        log(Severity.SUCCESS, message, t);
    }

    @Override
    public void info(String message) {
        log(Severity.INFO, message, null);
    }

    @Override
    public void info(String message, Throwable t) {
        log(Severity.INFO, message, t);
    }

    @Override
    public void warn(String message) {
        log(Severity.WARN, message, null);
    }

    @Override
    public void warn(String message, Throwable t) {
        log(Severity.WARN, message, t);
    }

    @Override
    public void error(String message) {
        log(Severity.ERROR, message, null);
    }

    @Override
    public void error(String message, Throwable t) {
        log(Severity.ERROR, message, t);
    }

    @Override
    public void fatal(String message) {
        log(Severity.FATAL, message, null);
    }

    @Override
    public void fatal(String message, Throwable t) {
        log(Severity.FATAL, message, t);
    }

    @Override
    public void debug(String message) {
        log(Severity.DEBUG, message, null);
    }

    @Override
    public void debug(String message, Throwable t) {
        log(Severity.DEBUG, message, t);
    }

    // Since the log is same across all threads, we use static

    /**
     * Writes to the logging file. This will get the
     * logging file, append a new line and then
     * append the provided contents.
     *
     * @param contents The contents to append to
     */
    private void writeLog(String... contents) {
        if(out == null) throw new NullPointerException();
        try {
            append(out.toPath(), contents);
        } catch (Exception e) {
            e.printStackTrace(getOutput(Severity.ERROR));
        }
    }

    /**
     * Prints the specified throwable instance
     * to the logging file. This always append
     * a new line before printing the exception.
     *
     * @param t The throwable to print
     */
    private void writeError(Throwable t) {
        if(out == null) throw new NullPointerException();
        synchronized (out) {
            try(PrintStream stream = new PrintStream(new FileOutputStream(out, true))) {
                newline(out.toPath());
                t.printStackTrace(stream);
            } catch (FileNotFoundException e) {
                getOutput(Severity.ERROR).println("Cannot write to error: ");
                e.printStackTrace(getOutput(Severity.ERROR));
            }
        }
    }

    private PrintStream getOutput(Severity severity) {
        return switch (severity) {
            case INFO, SUCCESS, WARN -> {
                if (stream == null) yield new NoOpPrintStream();
                yield stream;
            }
            case ERROR, FATAL -> {
                if (errStream == null) yield new NoOpPrintStream();
                yield errStream;
            }
            case DEBUG -> {
                if (debugStream == null) yield new NoOpPrintStream();
                yield debugStream;
            }
        };
    }

    // Creates a new line
    private void newline(Path p) {
        append(p, false, false, "\n");
    }

    // Appends to the given path, providing date to log, and adds a new line before printing the content.
    // Each new entry will be printed a new line if the newLine option is true
    private void append(Path path, String... content) {
        append(path, true, true, content);
    }

    /**
     * Appends content to the log
     * @param path The path to append to
     * @param date Whether to timestamp
     * @param newLine Whether a new line is required
     * @param content Content to append
     */
    private void append(Path path, boolean date, boolean newLine, String... content) {
        try {
            synchronized (out) {
                for(String line : content) {
                    if(newLine && !line.equals("\n")) {
                        newline(path);
                    }
                    Files.writeString(path, date ? "[" + format.format(new Date()) + "] " + line : line, StandardOpenOption.APPEND, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(getOutput(Severity.ERROR));
        }
    }

    // https://en.wikipedia.org/wiki/ANSI_escape_code
    public enum Severity {
        SUCCESS(32),
        INFO(0),
        WARN(33),
        ERROR(91),
        FATAL(31),
        DEBUG(35);

        private final int color;

        Severity(int color) {
            this.color = color;
        }

        Severity() {
            this.color = 97;
        }

        public String getColor() {
            return "\033[" + this.color + "m";
        }

        @Override
        public String toString() {
            return this.getColor();
        }
    }
}
