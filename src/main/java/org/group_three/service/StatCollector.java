package org.group_three.service;


import java.util.List;

/**
 * A Collection of Stats. Supports additional features like group-exporting to .tar.gz.
 * @author Luca
 * */

public class StatCollector {


    private String name;
    private List<Statistic<?>> statistics;

    public StatCollector(String name, Statistic<?> ... args) {
        this.name = name;
        this.statistics = List.of(args);
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

    @Deprecated
    public void exportToGZ(){
        for(Statistic stat : statistics){
            stat.outAsCSV();
        }


    }



}
