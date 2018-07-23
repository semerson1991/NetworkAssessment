package com.semerson.networkassessment.activities.results.Chart;

import android.content.Context;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Map;

public interface ChartCreator {
    PieChart createChart(Context context, int minHeight, int minWidth);

    /**
     * Set the Pie Chart configuration. The database parameter is a map. The string are used for the legend labels, and the float values is used for the PieChart slice.
     * The if the colorset low-critical is used, the data must be inserted in the correct order (high to low)
     *
     * @param description The PieChart description
     * @param legendLabel The Legend label
     * @param chart       The PieChart to apply configuration to
     * @param dataset     The dataset to use.
     * @param colorset    The colorset to use.
     * @return The PieChart that is passed in, but has all the configurations applied.
     */
    PieChart setChartConfig(String description, boolean enableLegend, String legendLabel, Chart chart, Map<String, Float> dataset, int colorset);
}
