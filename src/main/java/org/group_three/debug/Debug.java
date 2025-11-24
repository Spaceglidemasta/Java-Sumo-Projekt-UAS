package org.group_three.debug;

import javax.swing.*;

public class Debug {

    private static final boolean do_debug = true;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String BOLD = "\033[0;1m";

    // The JTextArea where debug messages will be displayed
    private static JTextArea debugTextArea;

    // Set the JTextArea (called by the main window to provide the text area)
    public static void setDebugTextArea(JTextArea textArea) {
        debugTextArea = textArea;
    }

    public static void print(Object value){
        if(do_debug){
            StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
            String className = caller.getClassName().substring(16);

            System.out.println(BOLD + "[" + ANSI_BLUE + "DEBUG" + ANSI_RESET + BOLD + "](" + ANSI_CYAN + className + ANSI_RESET + ") " + ANSI_RESET + String.valueOf(value));
        }

        // Optionally, print messages to the debug window as well
        if (debugTextArea != null) {
            debugTextArea.append(String.valueOf(value) + "\n");
            debugTextArea.setCaretPosition(debugTextArea.getDocument().getLength());  // Scroll to the bottom
        }
    }
}
