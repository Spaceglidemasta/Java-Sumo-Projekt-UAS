package org.group_three;



//external libraries
import org.eclipse.sumo.libsumo.*;
import org.group_three.basicGui.MainGui;

//packages
import org.group_three.api.SimSumo;


public class Main {
    public static void main(String[] args) {

        SimSumo sumosim = new SimSumo();

        System.out.println("Program start HERE");

        MainGui aMainGui = new MainGui();
        aMainGui.start(args);
        System.out.println("Program continue HERE");
    }
}