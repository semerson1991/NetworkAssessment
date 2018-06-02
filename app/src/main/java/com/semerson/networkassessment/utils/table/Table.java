package com.semerson.networkassessment.utils.table;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<TableRow> tableRows;
    private Float totalOccurences;
    boolean sortedHighToLow = false;

    public Table(){
        tableRows = new ArrayList<>();
    }
    
    public void appendTableRow(TableRow tableRow){
        tableRows.add(tableRow);
    }

    public void prepareTableRow(TableRow... tableRows){
        for (TableRow tableRow : tableRows) {
            this.tableRows.add(tableRow);
        }
    }



    public List<TableRow> getTableRows(){
        return tableRows;
    }
    /**
     * The main use for the total occurrences is for setting the values within the table from high to low.
     */
    public void setTotalNumOfOccurences(Float numOfOccurences) {
        totalOccurences = numOfOccurences;
    }

    public Float getTotalOccurences(){
        return totalOccurences;
    }

    public void setSortedHighToLow(boolean highToLow){
        sortedHighToLow = highToLow;
    }

    public boolean getSortedOccurrencesHighToLow(){
        return sortedHighToLow;
    }
}
