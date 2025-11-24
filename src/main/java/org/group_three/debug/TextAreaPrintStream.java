package org.group_three.debug;

import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;

public class TextAreaPrintStream extends PrintStream {

    private JTextArea textArea;

    // Constructor that takes a JTextArea and creates an OutputStream that redirects to the JTextArea
    public TextAreaPrintStream(JTextArea textArea) {
        super(new OutputStream() {
            @Override
            public void write(int b) {
                // Append the character to the JTextArea
                textArea.append(String.valueOf((char) b));
                textArea.setCaretPosition(textArea.getDocument().getLength());  // Scroll to the bottom
            }
        });
        this.textArea = textArea;
    }

    // Override the println() method to ensure that it prints a complete line
    @Override
    public void println(String s) {
        textArea.append(s + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());  // Scroll to the bottom
    }

    // Override the print() method as well, to ensure it prints properly
    @Override
    public void print(String s) {
        textArea.append(s);
    }
}
