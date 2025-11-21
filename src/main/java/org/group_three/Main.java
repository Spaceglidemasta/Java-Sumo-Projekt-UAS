package org.group_three;



//external libraries
import org.group_three.api.SimController;
import org.group_three.ui.MainApp;


//packages
import org.group_three.debug.Debug;

public class Main {
    public static void main(String[] args) {
        Debug.print("Programm Start");

        SimController sim = new SimController();


        MainApp aMainGui = new MainApp();
        aMainGui.start(args);

    }
}