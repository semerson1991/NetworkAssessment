package com.semerson.networkassessment.utils.table;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class TableRow {
    private Float totalOccurrences;
    private List<TableRowData> tableRowData;
    private LinearLayout layout;

    private Float totalValue = 0.0f;

    public TableRow(TableRowData... tableRowData) {
        this.tableRowData = new ArrayList<>();

        for (TableRowData data : tableRowData) {
            this.tableRowData.add(data);
        }
    }

    public void setRowLayout(LinearLayout tableRowLayout) {
        layout = tableRowLayout;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    /**
     * This is for setting the total occurences that is stored within the piechart data.
     *
     * @param value
     */
    public void setRowValue(Float value) {
        this.totalValue = value;
    }

    public Float getRowValue() {
        return totalValue;
    }

    public List<TableRowData> getTableDataRows(){
        return tableRowData;
    }
}
