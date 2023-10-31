package com.spiritlight.fishutils.logging;

import java.io.File;

public final class Loggers {

    public static final ILogger NO_OP = new ILogger() {
        @Override public void newline() {}
        @Override public void success(String message, Throwable t) {}
        @Override public void info(String message, Throwable t) {}
        @Override public void warn(String message, Throwable t) {}
        @Override public void error(String message, Throwable t) {}
        @Override public void fatal(String message, Throwable t) {}
        @Override public void debug(String message) {}
    };

    private static File DEFAULT = null;

    private Loggers() {

    }

    public static void setDefault(File out) {
        if(DEFAULT != null) throw new IllegalStateException("Log already set");
        DEFAULT = out;
    }

    public static Logger getThreadLogger() {
        return getThreadLogger(DEFAULT);
    }

    public static Logger getThreadLogger(File out) {
        return new Logger("Thread #" + Thread.currentThread().getId() + "/" + Thread.currentThread().getName(), out);
    }

    public static Logger getLogger(String name) {
        return new Logger(name, DEFAULT);
    }

    public static Logger getLogger(String name, File out) {
        return new Logger(name, out);
    }

    public static ILogger noop() {
        return NO_OP;
    }
}
