package com.semerson.networkassessment.utils.table;

import android.view.View;

import com.github.mikephil.charting.charts.PieChart;

public class TableRowData {

    private String labelname = "";
    private View.OnClickListener onClickListener;
    private int color;
    private boolean includeLegend = false;
    private int gravity;

    public TableRowData(String labelname, int gravity, View.OnClickListener listener){
        setTableData(labelname, gravity, listener);
    }

    public TableRowData(String labelname, int gravity){
        this.labelname = labelname;
        this.gravity = gravity;
    }


    /**
     * Constructor to use if colun data should include a legend icon with the label.
     * @param labelname
     * @param listener
     * @param color
     */
    public TableRowData(String labelname, int gravity, View.OnClickListener listener, int color) {
        setTableData(labelname, gravity, listener);
        includeLegend = true;
        this.color = color;
    }

    private void setTableData(String labelname, int gravity, View.OnClickListener listener){
        this.labelname = labelname;
        this.onClickListener = listener;
        this.gravity = gravity;
    }

    public String getLabelname() {
        return labelname;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public int getColor() {
        return color;
    }

    public boolean isIncludeLegendIcon() {
        return includeLegend;
    }

    public int getGravity(){
        return gravity;
    }
}