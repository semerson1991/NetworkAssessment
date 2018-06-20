package com.semerson.networkassessment.utils.table;

import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class TableRow {
    private Float totalOccurrences;
    private List<TableRowData> tableRowData;
    private LinearLayout layout;

    private Object tag;

    private Float totalValue = 0.0f;

    private int id = -1;
    private View.OnClickListener onClickListener;

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

    public void setRowId(int rowId){
        this.id = rowId;
    }

    public int getRowId(){
        return id;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
