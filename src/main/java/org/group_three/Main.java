package org.group_three;

import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.objects.SumoStringList;
import org.group_three.debug.Debug;
import org.group_three.api.SimController;
import org.group_three.debug.StaticTester;
import org.group_three.model.WVehicle;
import org.group_three.ui.MainApp;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Debug.print("Programm Start");

        //Tester, so this isn't full of random test calls
        new StaticTester();

        MainApp aMainGui = new MainApp();
        aMainGui.start(args);

    }
}