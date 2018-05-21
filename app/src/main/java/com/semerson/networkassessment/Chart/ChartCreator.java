package com.semerson.networkassessment.Chart;

import android.content.Context;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Map;

public interface ChartCreator {
    PieChart createChart(Context context, int minHeight, int minWidth);
    PieChart setChartConfig(String description, String legendLabel, Chart chart, Map<String, Float> dataset);
}
