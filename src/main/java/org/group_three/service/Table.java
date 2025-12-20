package org.group_three.service;

import com.sun.jdi.InvalidTypeException;
import org.group_three.constants.enums.ValueStyle;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.debug.annotations.PrintStyle;
import org.group_three.debug.exceptions.InvalidArgumentCount;
import org.group_three.utils.Formatting;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.logging.Logger;

import static org.group_three.utils.Formatting.arrayToString;

public class Table<T extends Record> {

    private static final Logger log =
            Logger.getLogger(Table.class.getName());

    protected final int attributeCount;
    protected List<String> attributeNames;

    protected List<T> content;


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
     * @param rec The values of the attributes. These need to match with the quantity of the given attributes in the
     *             Constructor.
     * @return true if success, false if not.
     * @author Luca
     * */
    public void add(T rec){

        content.add(rec);

    }


    /**
     * @param index the index
     * @return row at a certain index
     * @author Luca
     * */
    public T getRow(int index){
        return content.get(index);
    }

    /**
     * @param rowIndex the index of the row
     * @param columnIndex the index of the column
     * @return row at a certain index
     * @author Luca
     * */
    public Object getValue(int rowIndex, int columnIndex){

        RecordComponent[] components = content.get(rowIndex).getClass().getRecordComponents();

        try{
            return components[columnIndex].getAccessor().invoke(content.get(rowIndex));

        } catch (Exception e){
            log.warning("Error getting the Value: " + Arrays.toString(e.getStackTrace()));
            return null;
        }

    }


    public List<T> getContent() {return content;}


    /**
     * @param attribute The Name of the Attribute / Column
     * @return The Column as a List of Objects
     * @author Luca
     * */
    @MayReturnNull
    public List<Object> getColumn(String attribute){

        int index = attributeNames.indexOf(attribute);

        if(index == -1){
            log.warning("Attribute " + attribute + " is not Part of the table.");
            return null;
        }

        if (content.isEmpty()) return List.of();

        RecordComponent[] components = content.get(0).getClass().getRecordComponents();
        List<Object> column = new ArrayList<>();
        for (T row : content) {
            try {
                column.add(components[index].getAccessor().invoke(row));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
    public T getRowWhere(String attribute, Object target) {

        int index = attributeNames.indexOf(attribute);

        if(index == -1){
            log.warning("Attribute " + attribute + " is not Part of the table.");
            return null;
        }

        for(int i = 0; i < content.size(); i++) {
            if(getValue(i, index) == target) return getRow(i);
        }

        return null;
    }

    /**
     * Prints the table barely formatted to the std output
     * @author Luca
     * */
    public void print() throws Exception {

        if(content == null) {
            log.warning("Table is empty -> Table wasn't printed.");
            return;
        }

        for(String attr : attributeNames){
            System.out.print("| " + attr + " |");
        }

        System.out.println("\n" + "-".repeat(85));

        for(T r : content) {

            //I hate the way I have to do this, but this level of generality has its price.

            RecordComponent[] components = r.getClass().getRecordComponents();

            StringBuilder sb = new StringBuilder();
            sb.append("| ");

            for (RecordComponent comp : components) {

                Object value = comp.getAccessor().invoke(r);

                PrintStyle ann = comp.getAnnotation(PrintStyle.class);
                if (ann != null) {
                    switch (ann.value()) {
                        case VALUE -> sb.append(value);
                        case LIST  -> sb.append(Arrays.toString((int[]) value));
                        case COLUMN -> {
                            int[] arr = (int[]) value;
                            for (int v : arr) sb.append(v).append(" | ");
                        }
                    }
                } else {
                    sb.append(value);
                }
            }
            sb.append(" |");
            System.out.println(sb);
        }

    }

    /**
     * @param index the index of the Row
     * @return the row Record as List< String >
     * @author Luca
     * */
    public List<String> rowToStringList(int index){

        List<String> out = new ArrayList<>();

        T rec = content.get(index);

        RecordComponent[] components = rec.getClass().getRecordComponents();

        for(RecordComponent comp: components){

            try {

                Object value = comp.getAccessor().invoke(rec);

                PrintStyle ann = comp.getAnnotation(PrintStyle.class);
                if (ann != null) {
                    switch (ann.value()) {
                        case VALUE -> out.add(value.toString()); //technically just default
                        case LIST  -> out.add(Arrays.toString((int[]) value));
                        case COLUMN -> {
                            int[] arr = (int[]) value;
                            for (int v : arr) out.add(String.valueOf(v));
                        }
                    }
                } else {
                    out.add(value.toString());
                }

            } catch (Exception e) {
                out.add("<error>");
            }
        }

        return out;
    }

    /**
     * @param rec the Row
     * @return the rec Record as List< String >
     * @author Luca
     * */
    public List<String> rowToStringList(T rec){

        List<String> out = new ArrayList<>();

        RecordComponent[] components = rec.getClass().getRecordComponents();

        for(RecordComponent comp: components){

            try {

                Object value = comp.getAccessor().invoke(rec);

                PrintStyle ann = comp.getAnnotation(PrintStyle.class);
                if (ann != null) {
                    switch (ann.value()) {
                        case VALUE -> out.add(value.toString()); //technically just default
                        case LIST  -> out.add(Arrays.toString((int[]) value));
                        case COLUMN -> {
                            int[] arr = (int[]) value;
                            for (int v : arr) out.add(String.valueOf(v));
                        }
                    }
                } else {
                    out.add(value.toString());
                }

            } catch (Exception e) {
                out.add("<error>");
            }
        }

        return out;
    }

    /**
     * Writes the table to a CSV file into ./output
     * <p> WARNING Creates Files </p>
     * @return <code>true</code> if success, <code>false</code> if not.
     * @source <a href="https://stackoverflow.com/a/10667865">Stack Overflow Answer by "Addicted"</a>
     * @author Luca
     * */
    public boolean outAsCSV(){

        // Source - https://stackoverflow.com/a/10667865
        // Posted by Addicted, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-12-14, License - CC BY-SA 4.0

        BufferedWriter out = null;
        String filename = Formatting.uniquegen("output/tout_", ".csv");

        if(content == null || content.isEmpty()) {
            log.warning("Table is empty. CSV file was not outputted.");
            return false;
        }

        try {
            FileWriter fstream = new FileWriter(filename, true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(Formatting.toCSVformat(attributeNames));

            for(T row : content) {
                out.write(Formatting.toCSVformat(rowToStringList(row)));
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

        log.info("Table was saved as: " + filename + ". This may take a second to load.");

       return true;
    }


    /**
     * Writes the table to a CSV file into ./output/...
     * <p> WARNING Creates Files </p>
     * @param pathstr Path relative to ./output. <br>pathstr="foo" outputs to output/foo/tout_[...], so don't append /
     * @return <code>true</code> if success, <code>false</code> if not.
     * @source <a href="https://stackoverflow.com/a/10667865">Stack Overflow Answer by "Addicted"</a>
     * @author Luca
     * */
    public boolean outAsCSV(String pathstr){

        Path target = Path.of("output", pathstr);

        if(!Files.exists(target)){
            log.warning("Target directory \"" + target + "\" does not exist.");
            return false;
        };

        // Source - https://stackoverflow.com/a/10667865
        // Posted by Addicted, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-12-14, License - CC BY-SA 4.0

        BufferedWriter out = null;
        String filename = Formatting.uniquegen("output/" + pathstr + "/tout_", ".csv");

        if(content == null || content.isEmpty()) {
            log.warning("Table is empty. CSV file was not outputted.");
            return false;
        }

        try {
            FileWriter fstream = new FileWriter(filename, true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(Formatting.toCSVformat(attributeNames));

            for(T row : content) {
                out.write(Formatting.toCSVformat(rowToStringList(row)));
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

        log.info("Table was saved as: " + filename + ". This may take a second to load.");


        return true;
    }





}
