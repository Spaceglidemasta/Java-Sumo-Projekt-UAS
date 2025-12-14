package org.group_three.service;


import java.util.List;

/**
 * The Stat stands for StatUtils, or for Static. Your choice.
 * @author Luca
 * */
public class StatCollector {


    private String name;
    private List<Statistic> statistics;

    public StatCollector(String name, Statistic ... args) {
        this.name = name;
        this.statistics = List.of(args);
    }


    @Deprecated
    public void exportToGZ(){
        for(Statistic stat : statistics){
            stat.getTable().outAsCSV();
        }


    }



}
