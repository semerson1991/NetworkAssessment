package com.semerson.networkassessment.utils.table;

import android.view.View;

public class TableRowData {

    private String labelname = "";
    private View.OnClickListener onClickListener;
    private int color;
    private boolean includeLegend = false;
    private int gravity;
    private int textStyle = -1;
    private boolean clickable = false;

    private int id = -1;

    public TableRowData(String labelname, int gravity, View.OnClickListener listener) {
        setTableData(labelname, gravity, listener);
    }


    public TableRowData(String labelname, int gravity) {
        this.labelname = labelname;
        this.gravity = gravity;
    }

    public TableRowData(String labelname, int style, int gravity) {
        this.labelname = labelname;
        this.gravity = gravity;
        this.textStyle = style;
    }


    /**
     * Constructor to use if colun data should include a legend icon with the label.
     *
     * @param labelname
     * @param listener
     * @param color
     */
    public TableRowData(String labelname, int gravity, View.OnClickListener listener, int color) {
        setTableData(labelname, gravity, listener);
        includeLegend = true;
        this.color = color;
    }

    private void setTableData(String labelname, int gravity, View.OnClickListener listener) {
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

    public int getGravity() {
        return gravity;
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setRowId(int rowDataId) {
        this.id = rowDataId;
    }

    public int getRowDataId() {
        return id;
    }

    public boolean getClickable(){
        return clickable;
    }
}