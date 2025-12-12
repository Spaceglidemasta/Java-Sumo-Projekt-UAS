package org.group_three.debug.exceptions;

/// To be thrown when a to-be-parsed element can be found, but is empty
/// @author Luca
public class XMLEmptyAttributeError extends RuntimeException {
    public XMLEmptyAttributeError(String message) {
        super(message);
    }
}
