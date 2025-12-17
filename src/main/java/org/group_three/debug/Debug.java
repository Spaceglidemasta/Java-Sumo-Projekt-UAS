package org.group_three.debug;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.*;

/**
 * Centralized Debug utility using java.util.logging.
 */
public abstract class Debug {

    private static final Logger LOGGER = Logger.getLogger("org.group_three");

    private static TextArea debugTextArea;

    private static final String RESET  = "\u001B[0m";
    private static final String BLUE   = "\u001B[34m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";

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

    /**
     * Prints the message only to the console
     * @param message The chosen message
     * @return
     * @author Leon
     */
    public static void toConsole(Object message) {
        if (debugTextArea != null) {
            String className = getCallerClassName();
            String msg = "[" + className + "] " + String.valueOf(message);
            Platform.runLater(() -> debugTextArea.appendText(msg + "\n"));
        }
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
     * Sets a JavaFX TextArea (used in the console) as an additional log output target. <br>
     * Messages are appended without colors.
     * @param textArea The TextArea to append logs to
     * @author Leon
     */
    public static void setDebugTextArea(TextArea textArea) {
        debugTextArea = textArea;

        Handler textAreaHandler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (debugTextArea == null || !isLoggable(record)) return;
                String msg = getFormatter().format(record);
                Platform.runLater(() -> debugTextArea.appendText(msg));
            }

            @Override
            public void flush() { }

            @Override
            public void close() throws SecurityException { }
        };

        // Simplified formatter for Console
        textAreaHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                String cleanMsg = record.getMessage().replaceAll("\u001B\\[[;\\d]*m", "");
                return cleanMsg + "\n";
            }
        });

        textAreaHandler.setLevel(Level.ALL);
        LOGGER.addHandler(textAreaHandler);
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
