package org.group_three.debug.exceptions;

/**
 * Exception to state that too much / not enough / wrong kind of Files
 * were selected to Start a Simulation
 * @author Luca
 * */
public class InvalidFilesSelected extends Exception{

    public InvalidFilesSelected(){}

    public InvalidFilesSelected(String msg) {
        super(msg);
    }

}
