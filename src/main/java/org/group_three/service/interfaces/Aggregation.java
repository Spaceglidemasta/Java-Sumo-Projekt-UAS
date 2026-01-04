package org.group_three.service.interfaces;

/**
 * This was our first try of a generic solution for the aggregation problem. <br>
 * <p>We tried doing this even more generic than {@link org.group_three.service.Statistic} already is,
 * but it failed. We would've needed to make every Statistic extend to a Record which implements
 * Aggregation, or make a separate Aggregate-able-Statistic-Class, which would've been
 * very unclean and hard to deal with. The final solution we sticked to, is to just pass the
 * correct binary operation in the aggregation method, which gives the (theoretical) dev expanding
 * this project a bit more work, but way more clarity and <strong>type safety</strong>. </p>
 *
 * Original Description: <br>
 *
 * CRTP-Based Aggregation interface for Records.
 * This is the best way I found to implement static polymorphism with Compile Time typechecking.
 * <p>I used <a href="https://medium.com/@AbhineyKumar/why-use-the-curiously-recurring-template-pattern-crtp-in-java-a9a192022849">this</a>
 * article for Inspiration</p>
 * @author Luca
 * */
@Deprecated
public interface Aggregation<T extends Aggregation<T>> {

    /**
     * This does not have logic, as its only purpose is to be overridden.
     * @param other The other T to be aggregated on
     * @return A new Record T
     * */
    T aggregate(T other);

}
