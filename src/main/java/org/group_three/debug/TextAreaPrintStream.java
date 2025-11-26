package org.group_three.debug;

import java.io.OutputStream;
import java.io.PrintStream;
import javafx.scene.control.TextArea;

public class TextAreaPrintStream extends PrintStream {

    private TextArea textArea;

    // Constructor that takes a TextArea and creates an OutputStream that redirects to the TextArea
    public TextAreaPrintStream(TextArea textArea) {
        super(new OutputStream() {
            @Override
            public void write(int b) {
                textArea.appendText(String.valueOf((char) b));
                textArea.setScrollTop(Double.MAX_VALUE);  // Scroll to the bottom
            }
        });
        this.textArea = textArea;
    }

    // Override the println() method to ensure that it prints a complete line
    @Override
    public void println(String s) {
        textArea.appendText(s + "\n");
        textArea.setScrollTop(Double.MAX_VALUE);  // Scroll to the bottom
    }

    // Override the print() method as well, to ensure it prints properly
    @Override
    public void print(String s) {
        textArea.appendText(s);
    }
}
