package com.spiritlight.fishutils.logging;

import java.util.function.Consumer;

public final class Loggers {
    private Loggers() {}

    private static final Logger FILE = new Logger("Files I/O");

    private static final Logger SYSTEM = new Logger("System");

    private static final Logger DISCORD = new Logger("Discord");

    public static Logger getFileLogger() {
        return FILE;
    }

    public static Logger getSystemLogger() {
        return SYSTEM;
    }

    public static Logger getDiscordLogger() {
        return DISCORD;
    }

    public static Consumer<Throwable> getDiscordActionFallback() {
        return t -> DISCORD.error("Failed to execute command: ", t);
    }

    public static Logger getThreadLogger() {
        return new Logger("Thread #" + Thread.currentThread().getId() + "/" + Thread.currentThread().getName());
    }

    public static Logger getLogger(String name) {
        return new Logger(name);
    }


}
