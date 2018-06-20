package com.semerson.networkassessment.utils;

public class StyledText {
    private int style = -1;
    private String text = "";

    public StyledText(String text, int style){
        this.text = text;
        this.style = style;
    }

    public int getStyle() {
        return style;
    }

    public String getText() {
        return text;
    }
}
