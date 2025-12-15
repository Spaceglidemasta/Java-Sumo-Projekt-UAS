package org.group_three.debug;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.util.Duration;


/**
 * Debug class so nice debug messages can be displayed
 * in the Terminal and our custom console.
 * The Debug comment shows out ouf which class it was printed
 * and afterwords a custom debug message
 * @author Leon
 * */

public class Debug {

    private static final boolean MAIN_CON_DEBUG = true;
    public static boolean JAVAFX_FULL_DEBUG = false;
    private static TextArea debugTextArea;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String BOLD = "\033[0;1m";

    private static final StringBuilder buffer = new StringBuilder();

    private static Timeline flushTimer;;

    private static final int MAX_CHUNK_SIZE = 2000;
    private static final int MAX_LINES = 500;

    public static void setDebugTextArea(TextArea textArea) {
        debugTextArea = textArea;
        startFlushTimer();
    }

    /**
     * Print function, so that messages are
     * displayed in terminal
     * @param value takes in object to print out
     * @author Leon
     * */
    public static void print(Object value) {
        if (MAIN_CON_DEBUG) {
            StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
            String className = caller.getClassName().substring(16);
            System.out.println(BOLD + "[" + ANSI_BLUE + "DEBUG" + ANSI_RESET + BOLD + "](" + ANSI_CYAN + className + ANSI_RESET + ") " + ANSI_RESET + String.valueOf(value));
        }
    }

    /**
     * Print function for custom console, so that messages
     * are displayed in console. Also implemented a flush function,
     * so that console does not lag the simulation when logging messages.
     * The message limit was set to 500 (might make it changeable later),
     * so that the simulation does not lag because too many messages are stored.
     * @param message takes in object to print out
     * @author Leon
     * */
    public static void toConsole(Object message) {

        //only one thread can execute at a time
        synchronized (buffer) {
            buffer.append(message).append("\n");
        }
    }

    /**
     * Function to start a flush timer that measures 200 milliseconds
     * and flushes the buffer after they have passed.
     * @author Leon
     * */
    private static void startFlushTimer() {
        //If a timer was already called, the if-check prevents multiple timers running at the same time
        if (flushTimer != null) return;

        flushTimer = new Timeline(new KeyFrame(Duration.millis(200), e -> flushBuffer()));
        flushTimer.setCycleCount(Timeline.INDEFINITE);
        flushTimer.play();
    }

    /**
     * Function to split very large messages into chunks,
     * set by max chunk size, to improve performance
     * @author Leon
     */
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

            Platform.runLater(() -> {   //pass lambda function to save writing another function
                debugTextArea.appendText(chunk);
                trimLines(debugTextArea);
                debugTextArea.positionCaret(debugTextArea.getLength());
            });
        }
    }

    /**
     * Function to immediately flush everything stored.
     * @author Leon
     */
    public static void flushEverything() {
        if (debugTextArea == null) return;

        String text;
        synchronized (buffer) {
            if (buffer.isEmpty()) return;   // If buffer is empty, nothing is done
            text = buffer.toString();
            buffer.setLength(0);
        }

        Platform.runLater(() -> {   //pass lambda function to save writing another function
            debugTextArea.appendText(text);
            debugTextArea.positionCaret(debugTextArea.getLength());
        });
    }


    /**
     * Function to trim the lines to the set limit,
     * to avoid performance issues if too many messages are kept in the log
     * @author Leon
     */
    public static void trimLines(TextArea area) {
        String[] lines = area.getText().split("\n");
        if (lines.length > MAX_LINES) {
            StringBuilder sb = new StringBuilder();
            for (int i = lines.length - MAX_LINES; i < lines.length; i++) {
                sb.append(lines[i]).append("\n");
            }
            area.setText(sb.toString());
            area.positionCaret(area.getLength());
        }
    }
}
