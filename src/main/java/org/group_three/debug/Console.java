package org.group_three.debug;

import javax.swing.*;

public class Console {
    // Singleton instance of Console
    private static Console instance = null;
    private JTextArea debugTextArea;

    // Private constructor to prevent instantiation
    private Console() {
        // Create the Debug window
        JFrame debugFrame = new JFrame("Debug Window");
        debugTextArea = new JTextArea(20, 50);  // 20 rows, 50 columns
        debugTextArea.setEditable(false);  // Make sure the user can't edit this
        JScrollPane scrollPane = new JScrollPane(debugTextArea);
        debugFrame.add(scrollPane);
        debugFrame.setSize(600, 400);  // Adjust size as needed
        debugFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        debugFrame.setLocationRelativeTo(null);  // Center the window

        debugFrame.setVisible(true);

        System.setOut(new TextAreaPrintStream(debugTextArea));
    }

    // Method to get the singleton instance of Console
    public static Console getInstance() {
        if (instance == null) {
            instance = new Console();
        }
        return instance;
    }

    // Method to append messages to the debug window
    public void log(String message) {
        debugTextArea.append(message + "\n");
        debugTextArea.setCaretPosition(debugTextArea.getDocument().getLength());  // Scroll to the bottom
    }
}
