package com.semerson.networkassessment.activities.Results.Chart;

import android.view.Gravity;

public class TableLegendValues {
    private String leftTitle = "";
    private String middleTitle = "";
    private String rightTitle = "";
    private int leftLayoutGravity = Gravity.NO_GRAVITY;
    private int leftLabelGravity = Gravity.NO_GRAVITY;
    private int middleLayoutGravity = Gravity.NO_GRAVITY;
    private int middleLabelGravity = Gravity.NO_GRAVITY;
    private int rightLayoutGravity = Gravity.NO_GRAVITY;
    private int rightLabelGravity = Gravity.NO_GRAVITY;


    public TableLegendValues(){

    }

    public TableLegendValues(String leftTitle, String middleTitle, String rightTitle) {
        this.leftTitle = leftTitle;
        this.middleTitle = middleTitle;
        this.rightTitle = rightTitle;
    }
    public String getLeftTitle() {
        return leftTitle;
    }
    public void setLeftTitle(String leftTitle) {
        this.leftTitle = leftTitle;
    }

    public String getMiddleTitle() {
        return middleTitle;
    }
    public void setMiddleTitle(String middleTitle) {
        this.middleTitle = middleTitle;
    }

    public String getRightTitle() {
        return rightTitle;
    }
    public void setRightTitle(String rightTitle) {
        this.rightTitle = rightTitle;
    }

    public void setLayoutGravity(int gravityLeft, int gravityMiddle, int gravityRight){
        this.leftLayoutGravity = gravityLeft;
        this.middleLayoutGravity = gravityMiddle;
        this.rightLayoutGravity = gravityRight;
    }

    public void setTitleGravity(int gravityLeft, int gravityMiddle, int gravityRight){
        this.leftLabelGravity = gravityLeft;
        this.middleLabelGravity = gravityMiddle;
        this.rightLabelGravity = gravityRight;
    }

    public int getLeftLayoutGravity() {
        return leftLayoutGravity;
    }
    public void setLeftLayoutGravity(int leftLayoutGravity) {
        this.leftLayoutGravity = leftLayoutGravity;
    }

    public int getLeftLabelGravity() {
        return leftLabelGravity;
    }
    public void setLeftLabelGravity(int leftLabelGravity) {
        this.leftLabelGravity = leftLabelGravity;
    }

    public int getMiddleLayoutGravity() {
        return middleLayoutGravity;
    }
    public void setMiddleLayoutGravity(int middleLayoutGravity) {
        this.middleLayoutGravity = middleLayoutGravity;
    }

    public int getMiddleLabelGravity() {
        return middleLabelGravity;
    }
    public void setMiddleLabelGravity(int middleLabelGravity) {
        this.middleLabelGravity = middleLabelGravity;
    }

    public int getRightLayoutGravity() {
        return rightLayoutGravity;
    }
    public void setRightLayoutGravity(int rightLayoutGravity) {
        this.rightLayoutGravity = rightLayoutGravity;
    }

    public int getRightLabelGravity() {
        return rightLabelGravity;
    }
    public void setRightLabelGravity(int rightLabelGravity) {
        this.rightLabelGravity = rightLabelGravity;
    }
}
