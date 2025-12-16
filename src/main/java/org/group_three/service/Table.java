package org.group_three.service;

import com.sun.jdi.InvalidTypeException;
import org.group_three.debug.Debug;
import org.group_three.debug.annotations.CreatesFiles;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.debug.exceptions.InvalidArgumentCount;
import org.group_three.utils.Formatting;

import javax.management.relation.InvalidRelationTypeException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Table<T> {

    protected final int attributeCount;
    protected List<String> attributeNames;

    /// The whole content of the table.<br> <code>attributeCount</code> * content.size() in size.
    protected List<List<T>> content;


    public Table(String ... atts){

        this.attributeCount = atts.length;

        attributeNames = new ArrayList<>(List.of(atts));
        content = new ArrayList<>();

    }

    public List<String> getAttributeNames() {
        return attributeNames;
    }

    public boolean hasAttribute(String attribute){
        return attributeNames.contains(attribute);
    }



    /**
     * Adds a tuple / row to the table.
     * @param atts The values of the attributes. These need to match with the quantity of the given attributes in the
     *             Constructor.
     * @return true if success, false if not.
     * @author Luca
     * */
    public final boolean add(T... atts) throws InvalidArgumentCount, InvalidTypeException {

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
    public List<T> getRow(int index){
        return content.get(index);
    }


    public List<List<T>> getContent() {return content;}


    /**
     * @param attribute The Name of the Attribute / Column
     * @return The Column as a List of Objects
     * @author Luca
     * */
    @MayReturnNull
    public List<T> getColumn(String attribute){

        int index = attributeNames.indexOf(attribute);

        if(index == -1){
            Debug.print("Attribute " + attribute + " is not Part of the table.");
            return null;
        }

        List<T> column = new ArrayList<>();

        for(List<T> row : content){
            column.add(row.get(index));
        }

        return column;
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
    public List<T> getRowWhere(String attribute, T target) {

        int index = attributeNames.indexOf(attribute);

        for(List<T> row : content) {
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

        for(List<T> row : content) {
            for(T _attr : row){
                System.out.print("| " + _attr.toString() + " |");
            }
            System.out.println();
        }

    }


    /**
     * Writes the table to a CSV file into ./output
     * @return <code>true</code> if success, <code>false</code> if not.
     * @source <a href="https://stackoverflow.com/a/10667865">Stack Overflow Answer by "Addicted"</a>
     * @author Luca
     * */
    @CreatesFiles
    public boolean outAsCSV(){

        // Source - https://stackoverflow.com/a/10667865
        // Posted by Addicted, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-12-14, License - CC BY-SA 4.0

        BufferedWriter out = null;
        String filename = Formatting.uniquegen("output/tout_", ".csv");

        if(content == null || content.isEmpty()) {
            Debug.print("Table is empty. CSV file was not outputted.");
            return false;
        }

        try {
            FileWriter fstream = new FileWriter(filename, true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(Formatting.toCSVformat(attributeNames));

            for(List<T> row : content) {
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

       return true;
    }


    /**
     * Writes the table to a CSV file into ./output/...
     * @param pathstr Path relative to ./output. <br>pathstr="foo" outputs to output/foo/tout_[...], so don't append /
     * @return <code>true</code> if success, <code>false</code> if not.
     * @source <a href="https://stackoverflow.com/a/10667865">Stack Overflow Answer by "Addicted"</a>
     * @author Luca
     * */
    @CreatesFiles
    public boolean outAsCSV(String pathstr){

        Path target = Path.of("output", pathstr);

        if(!Files.exists(target)){
            Debug.print("Target directory \"" + target + "\" does not exist.");
            return false;
        };

        // Source - https://stackoverflow.com/a/10667865
        // Posted by Addicted, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-12-14, License - CC BY-SA 4.0

        BufferedWriter out = null;
        String filename = Formatting.uniquegen("output/" + pathstr + "/tout_", ".csv");

        if(content == null || content.isEmpty()) {
            Debug.print("Table is empty. CSV file was not outputted.");
            return false;
        }

        try {
            FileWriter fstream = new FileWriter(filename, true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(Formatting.toCSVformat(attributeNames));

            for(List<T> row : content) {
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


        return true;
    }


    /**<h2>outAsZippedCSV</h2>
     * Writes the table to a CSV file into ./output/...
     * @param zos The ZipOutputStream to stream the table content to.
     * @param pathstr Path relative to ./output. <br>pathstr="foo" outputs to output/foo/tout_[...], so don't append /
     * @return <code>true</code> if success, <code>false</code> if not.
     * @sources <a href="https://stackoverflow.com/a/10667865">Stack Overflow Answer by "Addicted"</a>
     *          <a href="https://stackoverflow.com/a/18571348">Stack Overflow Answer by "Stewart"</a>
     * @author Luca
     * */
    @CreatesFiles
    public boolean outAsZippedCSV(ZipOutputStream zos, String pathstr){

        Path target = Path.of("output", pathstr);

        if(!Files.exists(target)){
            Debug.print("Target directory \"" + target + "\" does not exist.");
            return false;
        };

        // Source - https://stackoverflow.com/a/10667865
        // Posted by Addicted, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-12-14, License - CC BY-SA 4.0

        // Source - https://stackoverflow.com/a/18571348
        // Posted by Stewart, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-12-16, License - CC BY-SA 3.0


        String filename = Formatting.uniquegen("output/" + pathstr + "/tout_", ".csv");

        if(content == null || content.isEmpty()) {
            Debug.print("Table is empty. CSV file was not outputted.");
            return false;
        }

        try {
            zos.putNextEntry(new ZipEntry(filename));
            zos.write(Formatting.toCSVformat(attributeNames).getBytes());

            for(List<T> row : content) {
                zos.write(Formatting.toCSVformat(row).getBytes());
            }

        }

        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            zos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Debug.print("Table was saved as: " + filename + ". This may take a second to load.");


        return true;
    }

}
