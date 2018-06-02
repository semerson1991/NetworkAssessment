package com.semerson.networkassessment.Chart;

import android.content.Context;

import com.github.mikephil.charting.charts.PieChart;
import com.semerson.networkassessment.activities.Results.ResultCallback;
import com.semerson.networkassessment.results.ResultController;

public class PieChartFactory {

    public static PieChart createPieChartThreats(Context context, ResultController resultController){
        PieChartCreator pieChartCreator = new PieChartCreator();
        PieChart chart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.THREAT_LEVEL, false, LegendHeadings.THREAT_LEVEL, chart, resultController.getThreatLevel(), PieChartCreator.LOW_TO_CRITICAL);
        return chart;
    }
}
