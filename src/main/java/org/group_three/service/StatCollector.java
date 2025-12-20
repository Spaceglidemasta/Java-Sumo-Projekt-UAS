package org.group_three.service;


import org.group_three.utils.Formatting;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

/**<h1>StatCollector</h1>
 * A Collection of Stats. Supports additional features like group-exporting to .tar.gz.
 * @see Statistic
 * @author Luca
 * */
public class StatCollector {

    private static final Logger log =
            Logger.getLogger(StatCollector.class.getName());


    private String name;
    private List<Statistic<?>> statistics;

    public StatCollector(String name, Statistic<?> ... args) {
        this.name = name;
        //List.of is immutable -> pass it into ArrayList<> init to make it mutable.
        this.statistics = new ArrayList<>(List.of(args));
    }

    /**
     * Adds a <code>Statistic</code> to the Collector
     * @param stat The Statistic to Add
     * @return the length of the collector-array after insertion
     * @author Luca
     * */
    public int addStatistic(Statistic<?> stat){
        statistics.add(stat);
        return statistics.size();
    }


    public void setName(String name) {this.name = name;}

    public String getName() {
        return name;
    }

    /**
     * Prints the whole Statistic Collection.
     * @see Statistic#print()
     * @author Luca
     * */
    public void print(){
        System.out.println(name + ": ");
        for(Statistic<?> stat : statistics ){
            stat.print();
        }
    }



    /**<h2>exportAsZip</h2>
     * Exports the Statistics contained by this StatCollector to one zipped folder in output.
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * @see Statistic#outAsZippedCSV(ZipOutputStream)
     * @author Luca
     * */
    public boolean exportAsZip() {

        String filename = Formatting.uniquegen(name, ".zip");

        try (FileOutputStream fos = new FileOutputStream("output/" + filename);
             ZipOutputStream zip = new ZipOutputStream(fos)) {

            int i = 0;
            for (Statistic<?> stat : statistics) {
                stat.outAsZippedCSV(zip);
                i++;

            }



            log.info("Exporting (" + i + ") Tables to output/" + filename + " was successful.");
            return true;


        } catch (Exception e) {
            log.warning("Exporting to a zipped folder failed: " + Arrays.toString(e.getStackTrace()));
            return false;
        }


    }




}
