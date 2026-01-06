package org.group_three.debug;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.logging.Logger;


/**
 * Debug class so nice debug messages can be displayed
 * in the Terminal and our custom console.
 * The Debug comment shows out ouf which class it was printed
 * and afterwords a custom debug message
 * @author Leon
 * */
public final class Debug {

    private static final Logger logger = Logger.getLogger(Debug.class.getName());

    private static final boolean MAIN_CON_DEBUG = true;
    public static boolean JAVAFX_FULL_DEBUG = false;
    private static TextArea debugTextArea;

    /**
     * Default constants
     *
     *
     * @author Leon
     * */
    @SuppressWarnings("JavadocDeclaration")
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String BOLD = "\033[0;1m";

    private static final StringBuilder buffer = new StringBuilder();

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
     * are displayed in console.
     * @param message takes in object to print out
     * @author Leon
     * */
    public static void toConsole(Object message) {
        String msg = message + "\n";
        if (debugTextArea != null) {
            Platform.runLater(() -> {
                debugTextArea.appendText(msg);
                debugTextArea.positionCaret(debugTextArea.getLength());
            });
        } else {
            synchronized (buffer) {
                buffer.append(msg);
            }
        }
    }

    /**
     * When the console is opened, flush any messages that were
     * buffered while the console was inactive
     * @author Leon
     * */

    public static void setDebugTextArea(TextArea textArea) {
        debugTextArea = textArea;
        String pending;
        synchronized (buffer) {
            if (buffer.isEmpty()) return;
            pending = buffer.toString();
            buffer.setLength(0);
        }
        Platform.runLater(() -> {
            debugTextArea.appendText(pending);
            debugTextArea.positionCaret(debugTextArea.getLength());
        });
    }

}
