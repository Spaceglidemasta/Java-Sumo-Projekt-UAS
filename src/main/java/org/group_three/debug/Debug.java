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


    /**
     * Booleans to later enable or disable debugging if needed.
     *
     *
     * @author Leon
     * */
    @SuppressWarnings("JavadocDeclaration")
    private static final boolean MAIN_CON_DEBUG = true;
    public static boolean JAVAFX_FULL_DEBUG = false;

    /**
     * TextArea that is used as the custom debug console.
     * When set, all debug messages are appended to this TextArea on the JavaFX Application Thread.
     * If the TextArea is not yet initialized, messages are temporarily
     * buffered and flushed once this reference is set.
     *
     * @author Leon
     * */
    @SuppressWarnings("JavadocDeclaration")
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
            System.out.println(BOLD + "[" + ANSI_BLUE + "DEBUG" + ANSI_RESET + BOLD + "](" + ANSI_CYAN + className + ANSI_RESET + ") " + ANSI_RESET + value);
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
            // Schedule the text append
            Platform.runLater(() -> {
                debugTextArea.appendText(msg);
                // Move the caret to the end
                debugTextArea.positionCaret(debugTextArea.getLength());
            });
        } else {
            // If text area isn't ready yet, buffer the message for later
            synchronized (buffer) {
                buffer.append(msg);
            }
        }
    }

    /**
     * Sets the text area to the one in the console.
     * When the console is opened, flush any messages that were
     * buffered while the console was inactive.
     * Pass null to clear the reference when console closes.
     * @author Leon
     * */

    public static void setDebugTextArea(TextArea textArea) {
        // If textArea is null, clear the reference
        if (textArea == null) {
            debugTextArea = null;
            return;
        }
        
        // Set the text area reference so messages can be displayed
        debugTextArea = textArea;
        String pending;
        synchronized (buffer) {
            if (buffer.isEmpty()) return;
            // Get all buffered messages
            pending = buffer.toString();
            // Clear the buffer
            buffer.setLength(0);
        }
        // Schedule for the messages to be displayed
        Platform.runLater(() -> {
            debugTextArea.appendText(pending);
            debugTextArea.positionCaret(debugTextArea.getLength());
        });
    }

    /**
     * Clears the debug text area reference.
     * Used when the console window is closed, so that messages
     * are buffered until the console is opened again.
     * @author Leon
     * */
    public static void clearDebugTextArea() {
        setDebugTextArea(null);
    }

}
