package org.group_three.utils;


import org.group_three.debug.annotations.StaticClass;

import java.util.List;

/**
 * Utility class for formatting.
 * @author Luca
 * */
@StaticClass
public abstract class Formatting {


    /**Turns a List of Objects into a String, with elements separated by commas and ended by a newline.
     * @param touple The List of Objects.
     * @return the formatted String
     * @author Luca
     * */
    public static String toCSVformat(List<?> touple){
        StringBuilder formatted = new StringBuilder();
        int i = 0;

        for(Object element : touple){
            i++;
            formatted.append("\"");
            formatted.append(element.toString());
            formatted.append("\"");

            if(touple.size() != i) formatted.append(",");
        }
        formatted.append("\n");

        return formatted.toString();
    }

    /**
     * Generates a unique name.
     * @param prefix the String before the 'random' iD
     * @param suffix the String after the 'random' iD
     * @return prefix + System.currentTimeMillis() + suffix
     * @author Luca
     * */
    public static String uniquegen(String prefix, String suffix){
        return prefix + System.currentTimeMillis() + suffix;
    }
}
