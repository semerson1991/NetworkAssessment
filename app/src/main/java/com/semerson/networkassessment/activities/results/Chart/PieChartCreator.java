package com.semerson.networkassessment.activities.results.Chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.results.ColorSet;
import com.semerson.networkassessment.storage.results.ResultScoreMetrics;
import com.semerson.networkassessment.utils.Utils;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;
import com.semerson.networkassessment.utils.table.TableRow;
import com.semerson.networkassessment.utils.table.TableRowData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PieChartCreator implements ChartCreator, OnChartValueSelectedListener {

    public static final String OS = "Operating System";
    public static final String VULN_CATEGORY = "Vulnerability Category";
    public static final String THREAT_LEVEL = "Vulnerability Level";
    public static final String ATTACK_COMPLEXITY = "Attack Complexity";

    public static final int LOW_TO_CRITICAL = 0x0001;
    public static final int DEFAULT_MIXED = 0x0010;

    public static final String CRITICAL = "Critical";
    public static final String HIGH = "High";
    public static final String MEDIUM = "Medium";
    public static final String LOW = "Low";
    public static final String NONE = "None";

    public ArrayList<Integer> colors = new ArrayList<>();
    public TableCreator tableCreator;

    public PieChartCreator() {
        tableCreator = new TableCreator();
    }

    @Override
    public PieChart createChart(Context context, int minWidth, int minHeight) {
        PieChart chart = new PieChart(context);
        chart.setMinimumWidth(minWidth);
        chart.setMinimumHeight(minHeight);
        chart.animateY(0, Easing.EasingOption.EaseInOutQuad);
        return chart;
    }

    @Override
    public PieChart setChartConfig(String description, boolean enableLegend, String legendLabel, Chart chart, Map<String, Float> dataset, int colorset) {

        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setEnabled(enableLegend);

        ArrayList<Integer> colors = new ArrayList<>();
        if (chart instanceof PieChart) {
            PieChart piechart = (PieChart) chart;
            piechart.getDescription().setEnabled(false);

            piechart.setExtraOffsets(5, 10, 10, 5);
            piechart.setDragDecelerationFrictionCoef(0.0f);

            //Center Text
            piechart.setDrawCenterText(true);
            piechart.setCenterText(description);

            //Center Hole
            piechart.setDrawHoleEnabled(true);
            piechart.setHoleColor(Color.WHITE);
            piechart.setHoleRadius(50f);
            piechart.setTransparentCircleRadius(55f);
            piechart.setOnChartValueSelectedListener(this);

            piechart.setDrawEntryLabels(false);
            addDataSet(chart, dataset, legendLabel, colorset, colors);

            return piechart;
        }
        return null;
    }

    private void addDataSet(Chart chart, Map<String, Float> dataset, String label, int colorset, List<Integer> colors) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (String key : dataset.keySet()) {
            Float value = dataset.get(key);
            entries.add(new PieEntry(value, key));
        }
        PieDataSet chartDataSet = new PieDataSet(entries, "");
        chartDataSet.setSliceSpace(1f);
        chartDataSet.setSelectionShift(5f);
        chartDataSet.setValueTextSize(12);
        colors = setColorSet(dataset, colorset, colors);
        chartDataSet.setColors(colors);

        PieData data = new PieData(chartDataSet);
        data.setDrawValues(false);
        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    /**
     * This method is mainly used for the "More Details" option where the charts need to be displayed with the occurrences as high to low.
     *
     * @param context
     * @param chart
     * @param customBoarder
     * @param view
     */
    public void appendChartTableDataRowsSorted(Context context, PieChart chart, Drawable customBoarder, LinearLayout view) {
        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setEnabled(false);

        LegendEntry[] legendEntries = chart.getLegend().getEntries();

        IDataSet data = chart.getData().getDataSet();


        Float totalSum = chart.getData().getYValueSum();
        List<LegendTableSection> legendTableSections = new ArrayList<>();

        for (int i = 0; i < legendEntries.length - 1; i++) {
            LegendTableSection section = createTableDataRow(data, context, legendEntries[i], customBoarder, i, totalSum);
            legendTableSections.add(section);
        }

        Collections.sort(legendTableSections, new Comparator<LegendTableSection>() {
            public int compare(LegendTableSection obj1, LegendTableSection obj2) {
                // ## Ascending order
                return Float.compare(obj2.totalvalue, obj1.totalvalue);
            }
        });

        for (LegendTableSection legendTableSection : legendTableSections) {
            view.addView(legendTableSection.legendSection);
        }
    }

    /**
     * This method can be made more flexible without so much boilerplate code. It adds two objects in the first table column.
     *
     * @param data
     * @param context
     * @param entry
     * @param customBoarder
     * @param index
     * @param totalSum
     * @return
     */
    private LegendTableSection createTableDataRow(IDataSet data, Context context, LegendEntry entry, Drawable customBoarder, int index, Float totalSum) {
        LegendTableSection section;
        Float entryTotalVal = data.getEntryForIndex(index).getY(); //Total val of slice data (Not the percentage)

        LinearLayout legendArea = new LinearLayout(context);
        legendArea.setOrientation(LinearLayout.HORIZONTAL);
        legendArea.setBackground(customBoarder);


        LinearLayout.LayoutParams paramsLeftLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        paramsLeftLayout.weight = 1F;
        LinearLayout leftLayout = new LinearLayout(context);
        leftLayout.setOrientation(LinearLayout.HORIZONTAL);
        leftLayout.setGravity(Gravity.LEFT);
        leftLayout.setPadding(10, 0, 0, 10);
        leftLayout.setLayoutParams(paramsLeftLayout);

        LinearLayout.LayoutParams paramsLegendLayout = new LinearLayout.LayoutParams(
                20, 20);
        paramsLegendLayout.setMargins(0, 10, 20, 0);
        LinearLayout legend_layout = new LinearLayout(context);
        legend_layout.setLayoutParams(paramsLegendLayout);
        legend_layout.setOrientation(LinearLayout.HORIZONTAL);
        legend_layout.setBackgroundColor(entry.formColor);
        leftLayout.addView(legend_layout);

        TextView txt_unit = new TextView(context);
        txt_unit.setText(entry.label);
        leftLayout.addView(txt_unit);

        LinearLayout.LayoutParams paramMiddleLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        paramMiddleLayout.weight = 1F;
        LinearLayout middleLayout = new LinearLayout(context);
        middleLayout.setOrientation(LinearLayout.HORIZONTAL);
        middleLayout.setGravity(Gravity.CENTER);
        middleLayout.setLayoutParams(paramMiddleLayout);

        TextView txtTotal = new TextView(context);
        txtTotal.setText(String.valueOf(data.getEntryForIndex(index).getY()));
        middleLayout.addView(txtTotal);

        LinearLayout.LayoutParams paramRightLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        paramRightLayout.weight = 1F;
        LinearLayout right_layout = new LinearLayout(context);
        right_layout.setOrientation(LinearLayout.HORIZONTAL);
        right_layout.setGravity(Gravity.CENTER);
        right_layout.setLayoutParams(paramRightLayout);

        TextView txtPercentage = new TextView(context);
        txtPercentage.setText(getFormattedPercentage(totalSum, entryTotalVal));
        right_layout.addView(txtPercentage);


        // legendArea.addView(pieVulnFamily);
        legendArea.addView(leftLayout);
        legendArea.addView(middleLayout);
        legendArea.addView(right_layout);
        section = new LegendTableSection(legendArea, entryTotalVal);
        return section;
    }

    private float calcPercent(Float total, Float num) {
        Float percent = num / total;
        percent = percent * 100;
        return percent;
    }

    public String getFormattedPercentage(Float totalnum, Float num) {
        return String.format("%.0f%%", calcPercent(totalnum, num));
    }

    private List<LegendTableSection> createTableLegendRows() {

        return null;
    }

    public List<Integer> setColorSet(Map<String, Float> map, int colorset, List<Integer> colors) {
        switch (colorset) {
            case LOW_TO_CRITICAL:
                for (String key : map.keySet()) {
                    Log.d("color method key", key);
                    switch (key) {
                        case CRITICAL:
                            colors.add(ColorSet.LOW_TO_CRITICAL.getDarkRed());
                            break;
                        case HIGH:
                            colors.add(ColorSet.LOW_TO_CRITICAL.getRed());
                            break;
                        case MEDIUM:
                            colors.add(ColorSet.LOW_TO_CRITICAL.getOrange());
                            break;
                        case LOW:
                            colors.add(ColorSet.LOW_TO_CRITICAL.getYellow());
                            break;
                        case NONE:
                            colors.add(ColorSet.LOW_TO_CRITICAL.getNone());
                            break;
                    }
                }
                break;
            case DEFAULT_MIXED:
                colors = ColorSet.DEFAULT.getColours();
                break;
            default:
                colors = ColorSet.DEFAULT.getColours();
        }

        return colors;
    }

    /**
     * Create a table with the vales from the piechart. Tbe Occurrences as a total, and percent are input into the table.
     *
     * @param chart
     * @param listener
     * @return
     */
    public Table prepareTableLegendForPieChart(PieChart chart, View.OnClickListener listener) {
        Table table = new Table();
        LegendEntry[] legendEntries = chart.getLegend().getEntries();
        Float totalSum = chart.getData().getYValueSum();

        IDataSet data = chart.getData().getDataSet();

        for (int i = 0; i < legendEntries.length - 1; i++) {
            LegendEntry entry = legendEntries[i];

            Float totalOccurrences = data.getEntryForIndex(i).getY();

            TableRow tableRow = new TableRow(new TableRowData(entry.label, Gravity.LEFT, listener, entry.formColor),
                    new TableRowData(String.valueOf(data.getEntryForIndex(i).getY()), Gravity.CENTER, listener),
                    new TableRowData(Utils.getFormattedPercentage(totalSum, totalOccurrences), Gravity.CENTER, listener));
            tableRow.setRowValue(totalOccurrences);

            tableRow.setRowId(R.id.rowListener);
            tableRow.setTag(entry.label.toString());
            tableRow.setOnClickListener(listener);
            table.prepareTableRow(tableRow);
        }
        return table;
    }

    /**
     *Prepares the table that has a host as the first column with the occurrences of the high medium and low threat levels
     * @param resultScoreMetrics
     * @param listener
     * @return
     */
    public Table prepareTableLegendOccurrences(Map<String, ResultScoreMetrics> resultScoreMetrics, View.OnClickListener listener) {
        Table table = new Table();
        for (String host : resultScoreMetrics.keySet()) {
            ResultScoreMetrics scoreMetrics = resultScoreMetrics.get(host);

            TableRow tableRow = new TableRow(
                    new TableRowData(host, Gravity.CENTER, listener),
                    new TableRowData(String.valueOf(scoreMetrics.getHighCount()), Gravity.CENTER, listener),
                    new TableRowData(String.valueOf(scoreMetrics.getMedCount()), Gravity.CENTER),
                    new TableRowData(String.valueOf(scoreMetrics.getLowCount()), Gravity.CENTER));
            tableRow.setRowValue((float) scoreMetrics.getTotal());
            table.appendTableRow(tableRow);

            tableRow.setRowId(R.id.rowListener);
            tableRow.setTag(host);
            tableRow.setOnClickListener(listener);
        }
        return table;
    }

    public class LegendTableSection {
        private LinearLayout legendSection;
        private Float totalvalue;

        public LegendTableSection(LinearLayout theLegendSection, Float theTotalvalue) {
            totalvalue = theTotalvalue;
            legendSection = theLegendSection;
        }

        public Float getTotalvalue() {
            return totalvalue;
        }

        public LinearLayout getLayout() {
            return legendSection;
        }
    }

}
