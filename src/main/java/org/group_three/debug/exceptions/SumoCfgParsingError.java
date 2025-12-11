package org.group_three.debug.exceptions;

/**
 * to be thrown when there is an error or unexpected behavior when parsing .sumocfg files
 * @author Luca
 * */
public class SumoCfgParsingError extends RuntimeException {
    public SumoCfgParsingError(String message) {
        super(message);
    }
}
