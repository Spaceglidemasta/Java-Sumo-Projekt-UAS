package org.group_three.utils;


import org.group_three.service.Table;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for formatting.
 * @author Luca
 * */
public final class Formatting {


    /**Turns a List of Objects into a String, with elements separated by commas and ended by a newline.
     * @param tuple The List of Objects.
     * @return the formatted String
     * @author Luca
     * */
    public static String toCSVformat(List<?> tuple){
        StringBuilder formatted = new StringBuilder();
        int i = 0;

        for(Object element : tuple){
            i++;
            formatted.append("\"");
            formatted.append(element.toString());
            formatted.append("\"");

            if(tuple.size() != i) formatted.append(",");
        }
        formatted.append("\n");

        return formatted.toString();
    }

    /**<h2>arrayToString</h2>
     * Method to determine how an Array should be cast into a String.
     * <br>Looks messy at first glance, but is actually very clean
     * and deterministic.
     * @param array The array to be converted
     * @return the cast String
     * @author Luca
     * @see Table#print()
     * */
    public static String arrayToString(Object array) {
        if (array instanceof Object[] o) return Arrays.deepToString(o);
        if (array instanceof int[] a) return Arrays.toString(a);
        if (array instanceof long[] a) return Arrays.toString(a);
        if (array instanceof double[] a) return Arrays.toString(a);
        if (array instanceof float[] a) return Arrays.toString(a);
        if (array instanceof boolean[] a) return Arrays.toString(a);
        if (array instanceof char[] a) return Arrays.toString(a);
        if (array instanceof byte[] a) return Arrays.toString(a);
        if (array instanceof short[] a) return Arrays.toString(a);

        return "<unknown array>";
    }

    /**
     * Generates a unique name.
     * @param prefix the String before the 'random' iD
     * @param suffix the String after the 'random' iD
     * @return prefix + System.currentTimeMillis() + suffix
     * @author Luca
     * */
    //TODO Make this truly unique
    public static String uniquegen(String prefix, String suffix){

        long id = (long) (System.currentTimeMillis() * Math.random());

        //replaces all consecutive whitespaces with one underscore
        return prefix.replaceAll("\\s+", "_") + id + suffix;
    }
}
