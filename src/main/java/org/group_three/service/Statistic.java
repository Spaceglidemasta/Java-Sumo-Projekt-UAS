package org.group_three.service;


import org.group_three.debug.Debug;
import org.group_three.debug.annotations.MayReturnNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Class for 1 singular Statistic, e.g. 1 Graph, 1 Table, etc. <br>
 * @author Luca
 * */
public class Statistic<T> extends Table<T> {

    /// Name to be displayed.
    private String name;
    /// description for when showing the table.
    private String description = "N/A";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Statistic(String name, String ... atts){
        super(atts);
        this.name = name;
    }


    @Override
    public void print() {
        System.out.println(name + ": ");
        super.print();
    }

    /**
     * <h2>getGraphOf()</h2>
     * A method that turns a desired table into a Graph, mapping one integer attribute to another.
     *
     * @param x_attribute The name of the attribute that is supposed to be on the x-axis
     * @param y_attribute The name of the attribute that is supposed to be on the y-axis
     * @return a callabe function that takes x as an input and returns y.
     * @source <a href="https://stackoverflow.com/a/29584084">Posted by 'satnam' on Stackoverflow</a>
     * @author Luca
     *
     */
    @MayReturnNull
    public Function<Object, Object> getGraphOf(String x_attribute, String y_attribute) {
        // Source - https://stackoverflow.com/a/29584084
        // Posted by satnam, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-12-14, License - CC BY-SA 3.0

        if(!hasAttribute(x_attribute)){
            Debug.print(name + ": Attribute \"" + x_attribute + "\" is not a valid attribute of the Table.\n" +
                        "Valid attributes are:\n" +
                        getAttributeNames().toString());
            return null;
        }
        if(!hasAttribute(y_attribute)){
            Debug.print(name + ": Attribute \"" + y_attribute + "\" is not a valid attribute of the Table.\n" +
                    "Valid attributes are:\n" +
                    getAttributeNames().toString());
            return null;
        }
        
        List<?> xcolumn = getColumn(x_attribute);
        List<?> ycolumn = getColumn(y_attribute);


        return new Function<Object, Object>() {
            @Override
            public Object apply(Object xval) {

                int index = xcolumn.indexOf(xval);

                if(index == -1) return null;

                return ycolumn.get(index);
            }
        };

    }



}
