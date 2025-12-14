package org.group_three.debug.exceptions;

/**Is thrown when an illegal amount of arguments is given to a function.
 * @author Luca
 * */
public class InvalidArgumentCount extends RuntimeException {
    public InvalidArgumentCount(String message) {
        super(message);
    }
}
