package org.group_three.service.records;


/**A Record representing the target and length of a Route generation. <br>
 * (This is only used to map traveltime distribution properly)
 * @param edgeID the target Edge / the end of the Route as ID
 * @param length the length of the whole route.
 * @author Luca
 * */
public record RouteTarget(String edgeID, double length) {
}
