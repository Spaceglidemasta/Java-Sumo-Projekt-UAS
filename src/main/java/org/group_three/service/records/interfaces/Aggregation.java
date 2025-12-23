package org.group_three.service.records.interfaces;

/**
 * CRTP-Based Aggregation interface for Records.
 * This is the best way I found to implement static polymorphism with Compile Time typechecking.
 * <p>I used <a href="https://medium.com/@AbhineyKumar/why-use-the-curiously-recurring-template-pattern-crtp-in-java-a9a192022849">this</a>
 * article for Inspiration</p>
 * @author Luca
 * */
public interface Aggregation<T extends Aggregation<T>> {

    /**
     * This does not have logic, as its only purpose is to be overridden.
     * @param other The other T to be aggregated on
     * @return A new Record T
     * */
    T aggregate(T other);

}
