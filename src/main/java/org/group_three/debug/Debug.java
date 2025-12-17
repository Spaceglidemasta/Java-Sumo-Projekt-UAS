package org.group_three.debug;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.*;


/**
 * Centralized Debug utility using java.util.logging.
 */
public abstract class Debug {

    private static final Logger LOGGER = Logger.getLogger("org.group_three");


    private static final String RESET  = "\u001B[0m";
    private static final String BLUE   = "\u001B[34m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";

    private static final StringBuilder buffer = new StringBuilder();
    private static Timeline flushTimer;
    private static final int MAX_CHUNK_SIZE = 2000;  // characters per flush
    private static final int MAX_LINES = 500;        // max lines kept
    private static TextArea debugTextArea;

    static {
        try {
            LogManager.getLogManager().reset();

            // Console handler with custom formatter
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    String color;
                    String levelLabel;

                    if (record.getLevel() == Level.INFO) {
                        color = CYAN;
                        levelLabel = "INFO";
                    } else if (record.getLevel() == Level.FINE) {
                        color = BLUE;
                        levelLabel = "FINE";
                    } else if (record.getLevel() == Level.WARNING) {
                        color = YELLOW;
                        levelLabel = "WARNING";
                    } else if (record.getLevel() == Level.SEVERE) {
                        color = RED;
                        levelLabel = "ERROR";
                    } else {
                        color = RESET;
                        levelLabel = record.getLevel().toString();
                    }

                    String levelOut = "[" + color + levelLabel + RESET + "]";

                    String msg = record.getMessage();
                    return levelOut + msg + "\n";
                }
            });

            // File handler
            FileHandler fileHandler = new FileHandler("application.log", false);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new Formatter() {
                private final SimpleDateFormat sdf =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                @Override
                public String format(LogRecord record) {
                    String timestamp = sdf.format(new java.util.Date(record.getMillis()));
                    String level = record.getLevel().getName();
                    String msg = record.getMessage();

                    // Strip ANSI escape sequences
                    msg = msg.replaceAll("\u001B\\[[;\\d]*m", "");

                    return String.format("%s %s %s%n", timestamp, level, msg);
                }
            });

            LOGGER.addHandler(consoleHandler);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }


    public static void setDebugTextArea(TextArea textArea) {
        debugTextArea = textArea;
        startFlushTimer();
    }

    /**
     * Print function for custom console. Messages are buffered and flushed
     * periodically to avoid UI lag. Old lines are trimmed to MAX_LINES.
     * @param message Message to print
     * @author Leon
     */
    public static void toConsole(Object message) {
        if (debugTextArea == null) return;
        String className = getCallerClassName();
        String formatted = "[" + className + "] " + message;
        synchronized (buffer) {
            buffer.append(formatted).append("\n");
        }
    }

    private static void startFlushTimer() {
        if (flushTimer != null) return; // prevent multiple timers
        flushTimer = new Timeline(new KeyFrame(Duration.millis(200), e -> flushBuffer()));
        flushTimer.setCycleCount(Timeline.INDEFINITE);
        flushTimer.play();
    }

    private static void flushBuffer() {
        if (debugTextArea == null) return;

        String text;
        synchronized (buffer) {
            if (buffer.isEmpty()) return;
            text = buffer.toString();
            buffer.setLength(0);
        }

        int length = text.length();
        int start = 0;
        while (start < length) {
            int end = Math.min(start + MAX_CHUNK_SIZE, length);
            String chunk = text.substring(start, end);
            start = end;

            Platform.runLater(() -> {
                debugTextArea.appendText(chunk);
                trimLines(debugTextArea);
                debugTextArea.positionCaret(debugTextArea.getLength());
            });
        }
    }

    public static void flushEverything() {
        if (debugTextArea == null) return;

        String text;
        synchronized (buffer) {
            if (buffer.isEmpty()) return;
            text = buffer.toString();
            buffer.setLength(0);
        }

        Platform.runLater(() -> {
            debugTextArea.appendText(text);
            debugTextArea.positionCaret(debugTextArea.getLength());
        });
    }

    private static void trimLines(TextArea area) {
        String[] lines = area.getText().split("\n");
        if (lines.length > MAX_LINES) {
            int startIndex = 0;
            for (int i = 0; i < lines.length - MAX_LINES; i++) {
                startIndex += lines[i].length() + 1; // +1 for newline
            }
            // delete old lines without resetting the whole text
            area.deleteText(0, startIndex);
        }
        area.positionCaret(area.getLength());
    }

    /**
     * Prints directly to the terminal (not into the logger).
     * @param value The message to be printed
     * @author Leon
     */
    public static void print(Object value) {
        String className = getCallerClassName();

        String nlDebug = "[" + YELLOW + "DEBUG" + RESET + "]";
        String classOut = "[" + GREEN + className + RESET + "]";
        String msg = String.valueOf(value);

        System.out.println(nlDebug + classOut + " " + msg);
    }

    /**
     * Generic log method: pass message and Level explicitly.
     * @param value The message to be logged
     * @param level The logging level (INFO, FINE, WARNING, SEVERE)
     * @author Leon
     */
    public static void log(Object value, Level level) {
        String className = getCallerClassName();
        String msg = "[" + GREEN + className + RESET + "] " + String.valueOf(value);
        LOGGER.log(level, msg);
    }

    /**
     * Shortens a fully qualified class name by removing the org.group_three.
     * @param fullName The full class name
     * @return The shortened class name
     * @author Leon
     */
    private static String shortenClassName(String fullName) {
        String prefix = "org.group_three.";
        if (fullName.startsWith(prefix)) {
            return fullName.substring(prefix.length());
        }
        return fullName;
    }

    /**
     * Helper to get the actual caller class name, skipping Debug and JDK internals.
     * @return The caller class name, or "Unknown" if not found
     * @author Leon
     */
    private static String getCallerClassName() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            String className = element.getClassName();
            if (!className.equals(Debug.class.getName())
                    && !className.startsWith("java.lang")) {
                return shortenClassName(className);
            }
        }
        return "Unknown";
    }

}
