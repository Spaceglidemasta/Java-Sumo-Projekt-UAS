package org.group_three.service;

import org.group_three.debug.Debug;
import org.group_three.debug.annotations.MayReturnNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * This was only created because I thought you need to sort a list to map it to another.
 * @author Luca
 * */
@Deprecated
public class GraphTable<T extends Comparable<T>> extends Table<T>{



    public GraphTable(String ... args){
        super(args);
    }


    /**
     * @param attribute The Name of the Attribute / Column
     * @param direction 0 for DESC, 1 for ASC
     * @return The Column as a List of Objects
     * @author Luca
     * */
    @MayReturnNull
    @Deprecated
    public List<? extends Comparable<T>> getSortedColumn(String attribute, int direction){

        int index = attributeNames.indexOf(attribute);

        if(index == -1){
            Debug.log("Attribute " + attribute + " is not Part of the table.", Level.FINE);
            return null;
        }

        List<T> column = new ArrayList<>();

        for(List<T> row : content){
            column.add(row.get(index));
        }

        return column;
    }


}
