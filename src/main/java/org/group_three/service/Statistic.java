package org.group_three.service;


import org.group_three.debug.Debug;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.utils.Formatting;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**<h1>Statistic</h1>
 * Class for 1 singular Statistic, e.g. 1 Graph, 1 Table, etc. <br>
 * Extends to Table
 * @see Table
 * @author Luca
 * */
public class Statistic<T extends Record> extends Table<T> {

    private static final Logger log =
            Logger.getLogger(Statistic.class.getName());

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

    /**
     * Prints out the Statistic in the follow format:
     * <code>name</code>:
     * | Head1  | Head2  | ... <br>
     * ------------------- ...<br>
     * | value1 | value2 | ...<br>
     * | ...<br>
     * @see Table#print()
     * @author Luca
     * */
    @Override
    public void print() throws Exception {
        System.out.println(name + ": ");
        super.print();
    }


    /**<h2>outAsZippedCSV</h2>
     * <p> WARNING Creates Files </p>
     * Writes the table to a CSV file into ./output/...
     * @param zos The ZipOutputStream to stream the table content to.
     * @return <code>true</code> if success, <code>false</code> if not.
     * @sources <a href="https://stackoverflow.com/a/10667865">Stack Overflow Answer by "Addicted"</a>
     *          <a href="https://stackoverflow.com/a/18571348">Stack Overflow Answer by "Stewart"</a>
     * @author Luca
     * */
    public boolean outAsZippedCSV(ZipOutputStream zos) {

        String filename = Formatting.uniquegen(name, ".csv");

        if (content == null || content.isEmpty()) {
            log.warning("Table is empty.");
            return false;
        }

        try {
            zos.putNextEntry(new ZipEntry(filename));

            // UTF-8 BOM (Excel)
            zos.write(new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF});

            Writer writer = new OutputStreamWriter(zos, StandardCharsets.UTF_8);

            writer.write(Formatting.toCSVformat(attributeNames));
            for (T row : content) {
                writer.write(Formatting.toCSVformat(rowToStringList(row)));
            }

            writer.flush();
            zos.closeEntry();

            return true;

        } catch (IOException e) {
            log.warning("Zipping CSV failed: " + e.getMessage());
            return false;
        }
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
     */
    @MayReturnNull
    public Function<Object, Object> getGraphOf(String x_attribute, String y_attribute) {
        // Source - https://stackoverflow.com/a/29584084
        // Posted by satnam, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-12-14, License - CC BY-SA 3.0

        if(!hasAttribute(x_attribute)){
            log.warning(name + ": Attribute \"" + x_attribute + "\" is not a valid attribute of the Table.\n" +
                        "Valid attributes are:\n" +
                        getAttributeNames().toString());
            return null;
        }
        if(!hasAttribute(y_attribute)){
            log.warning(name + ": Attribute \"" + y_attribute + "\" is not a valid attribute of the Table.\n" +
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
