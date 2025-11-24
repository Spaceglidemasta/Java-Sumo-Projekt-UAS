package org.group_three.debug;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Debug {

    private static final boolean do_debug = true;
    private static TextArea debugTextArea;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String BOLD = "\033[0;1m";

    // Set the JTextArea (called by the main window to provide the text area)
    public static void setDebugTextArea(TextArea textArea) {
        debugTextArea = textArea;
    }

    // Print to the terminal (standard output)
    public static void print(Object value) {
        if (do_debug) {
            StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
            String className = caller.getClassName().substring(16);
            System.out.println(BOLD + "[" + ANSI_BLUE + "DEBUG" + ANSI_RESET + BOLD + "](" + ANSI_CYAN + className + ANSI_RESET + ") " + ANSI_RESET + String.valueOf(value));
        }
    }

    // Print to the console (JavaFX TextArea)
    public static void toConsole(String message) {
        if (debugTextArea != null) {
            // Use Platform.runLater to ensure that the UI update happens on the JavaFX application thread
            Platform.runLater(() -> {
                debugTextArea.appendText(message + "\n");
                debugTextArea.setScrollTop(Double.MAX_VALUE);  // Scroll to the bottom
            });
        }
    }
}
