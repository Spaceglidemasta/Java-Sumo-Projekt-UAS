package org.group_three.service;

import org.group_three.debug.Debug;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.debug.exceptions.InvalidArgumentCount;
import org.group_three.utils.Formatting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private final int attributeCount;
    private List<String> attributeNames;

    /// The whole content of the table.<br> <code>attributeCount</code> * content.size() in size.
    private List<List<Object>> content;

    public Table(String ... atts){

        this.attributeCount = atts.length;

        attributeNames = new ArrayList<>();
        content = new ArrayList<>();

        attributeNames.addAll(List.of(atts));
    }

    /**
     * Adds a tuple / row to the table.
     * @param atts The values of the attributes. These need to match with the quantity of the given attributes in the
     *             Constructor.
     * @return true if success, false if not.
     * @author Luca
     * */
    public boolean add(Object ... atts) throws InvalidArgumentCount {

        if(atts.length != attributeCount){
            throw new InvalidArgumentCount(atts.length + " arguments given, " + attributeCount + " expected.");
        }

        List<Object> _content = new ArrayList<>();

        for(Object att : atts) {
            if(att == null) return false;
        }

        return content.add(List.of(atts));


    }

    /**
     * @param index the index
     * @return row at a certain index
     * @author Luca
     * */
    public List<Object> getRow(int index){
        return content.get(index);
    }

    /**
     * Gets the Row where <code>attribute</code> is <code>target</code><br>
     * @example table.getRowWhere("plz", 63165) → List("Mühlheim am Main", ...)
     * @return The row itself
     * @param attribute the attribute name you want to search for
     * @param target the expected value of the attribute
     * @author Luca
     * */
    @MayReturnNull
    public List<Object> getRowWhere(String attribute, Object target) {

        int index = attributeNames.indexOf(attribute);

        for(List<Object> row : content) {
            if(row.get(index) == target) return row;
        }

        return null;
    }

    /**
     * Prints the table barely formatted to the std output
     * @author Luca
     * */
    public void print(){

        if(content == null) {
            Debug.print("Table is empty -> Table wasn't printed.");
            return;
        }

        for(String attr : attributeNames){
            System.out.print("| " + attr + " |");
        }

        System.out.println("\n" + "-".repeat(85));

        for(List<Object> row : content) {
            for(Object _attr : row){
                System.out.print("| " + _attr.toString() + " |");
            }
            System.out.println();
        }

    }


    /**
     * Writes the table to a CSV file into ./output
     * @source <a href="https://stackoverflow.com/a/10667865">Stack Overflow Awnser by "Addicted"</a>
     * @author Luca
     * */
    public void outAsCSV(){

        // Source - https://stackoverflow.com/a/10667865
        // Posted by Addicted, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-12-14, License - CC BY-SA 4.0

        BufferedWriter out = null;
        String filename = Formatting.uniquegen("output/tout_", ".csv");

        if(content == null || content.isEmpty()) {
            Debug.print("Table is empty. CSV file was not outputted.");
            return;
        }

        try {
            FileWriter fstream = new FileWriter(filename, true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(Formatting.toCSVformat(attributeNames));

            for(List<Object> row : content) {
                out.write(Formatting.toCSVformat(row));
            }

        }

        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if(out != null){
            try {
                out.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        Debug.print("Table was saved as: " + filename + ". This may take a second to load.");

    }

}
