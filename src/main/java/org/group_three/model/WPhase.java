package org.group_three.model;

import java.util.List;

/**
 * Simple Phase Class for WTrafficLight
 * @author Luca
 * */
public class WPhase {
    public double duration;
    public List<Character> program;

    public WPhase(double duration, List<Character> program) {
        this.duration = duration;
        this.program = program;
    }

    public void print(){
        System.out.println("Duration: " + duration + ", Program: " + program);
    }
}
