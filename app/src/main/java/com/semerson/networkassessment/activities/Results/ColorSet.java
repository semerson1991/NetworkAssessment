package com.semerson.networkassessment.activities.Results;

import android.graphics.Color;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public enum ColorSet {

    LOW_TO_CRITICAL(Color.rgb(153, 0, 0), Color.rgb(255, 51, 51),
            Color.rgb(255, 255, 0), Color.rgb(255, 153, 51)),

    DEFAULT(-1);

    private ArrayList<Integer> colors;

    ColorSet(Integer... colors) {
        this.colors = new ArrayList<>();
        if (colors[0] == -1) {
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                this.colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                this.colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                this.colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                this.colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                this.colors.add(c);
        } else {
            this.colors.addAll(Arrays.asList(colors));
        }
    }

    public int getYellow() {
        return Color.rgb(255, 255, 0);
    }

    public int getNone() {
        return Color.rgb(178, 255, 102);
    }

    public int getOrange() {
        return Color.rgb(255, 153, 51);
    }

    public int getRed() {
        return Color.rgb(255, 51, 51);
    }

    public int getDarkRed() {
        return Color.rgb(153, 0, 0);
    }

    public ArrayList<Integer> getColours() {
        return colors;
    }


}
