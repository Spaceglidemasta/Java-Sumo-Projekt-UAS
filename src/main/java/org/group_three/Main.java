package org.group_three;



//external libraries
import org.eclipse.sumo.libsumo.*;
import org.group_three.api.SimController;
import org.group_three.basicGui.MainGui;


//packages
import org.group_three.api.SimSumo;
import org.group_three.debug.Debug;


public class Main {
    public static void main(String[] args) {
        Debug.print("Programm Start");

        SimController sim = new SimController();


        MainGui aMainGui = new MainGui();
        aMainGui.start(args);



    }
}