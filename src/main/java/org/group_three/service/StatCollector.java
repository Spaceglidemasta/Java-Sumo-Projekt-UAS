package org.group_three.service;


import java.util.ArrayList;
import java.util.List;

/**<h1>StatCollector</h1>
 * A Collection of Stats. Supports additional features like group-exporting to .tar.gz.
 * @see Statistic
 * @author Luca
 * */
public class StatCollector {


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

    @Deprecated
    public void exportToGZ(){

        for(Statistic<?> stat : statistics){
            stat.outAsCSV();
        }

    }



}
