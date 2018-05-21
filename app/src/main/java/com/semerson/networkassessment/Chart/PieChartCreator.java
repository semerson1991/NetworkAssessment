package com.semerson.networkassessment.Chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.ResultsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PieChartCreator implements ChartCreator,OnChartValueSelectedListener {

    public static final String OS = "Operating System";
    public static final String VULN_CATEGORY = "Vulnerability Category";
    public static final String THREAT_LEVEL = "Vulnerability Level";
    public static final String ATTACK_COMPLEXITY = "Attack Complexity";


    ArrayList<Integer> colors;

    public PieChartCreator(){
        setColors();
    }

    public PieChartCreator(int minWidth, int minHeight){
        setColors();
    }

    private void setColors(){
        // add colors
        colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
    }

    @Override
    public PieChart createChart(Context context, int minWidth, int minHeight) {
        PieChart chart = new PieChart(context);
        chart.setMinimumWidth(minWidth);
        chart.setMinimumHeight(minHeight);
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        return chart;
    }

    @Override
    public PieChart setChartConfig(String description, String legendLabel, Chart chart, Map<String, Float> dataset) {
        if (chart instanceof PieChart){
            PieChart piechart = (PieChart) chart;
            piechart.getDescription().setEnabled(false);
            //Description hostPiDesc = new Description();
            //hostPiDesc.setText(description);
            //piechart.setDescription(hostPiDesc);

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
            addDataSet(chart, dataset, legendLabel);

            Legend legend = chart.getLegend();
            legend.setWordWrapEnabled(true);

            return piechart;
        }
        return null;
    }

    private void addDataSet(Chart chart, Map<String, Float> dataset, String label){
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (String key : dataset.keySet()) {
            Float value = dataset.get(key);
            entries.add(new PieEntry(value, key));
        }
        PieDataSet chartDataSet = new PieDataSet(entries, "");
        chartDataSet.setSliceSpace(1f);
        chartDataSet.setSelectionShift(5f);
        chartDataSet.setValueTextSize(12);
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

    public void appendChartTableHeader(Context context, PieChart chart, LinearLayout view, Drawable leftTitleBoarder, Drawable middleTitleBoarder, Drawable rightTitleBoarder,
                                       String leftTitle, String middleTitle, String rightTitle){
        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setEnabled(false);
        LinearLayout row = addLegendTitle(context, leftTitleBoarder, middleTitleBoarder, rightTitleBoarder, leftTitle, middleTitle, rightTitle);
        view.addView(row);
    }

    private LinearLayout addLegendTitle(Context context, Drawable leftTitleBoarder, Drawable middleTitleBoarder, Drawable rightTitleBoarder,
                                        String leftTitle, String middleTitle, String rightTitle) {
        LinearLayout legendAreaTitle = new LinearLayout(context);
        legendAreaTitle.setOrientation(LinearLayout.HORIZONTAL);
        legendAreaTitle.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleLeftLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        titleLeftLayout.weight = 1F;
        LinearLayout leftTitleLayout = createLinearLayout(context, titleLeftLayout, LinearLayout.HORIZONTAL, Gravity.CENTER);
        leftTitleLayout.addView(createTextView(context, leftTitle, Gravity.CENTER));

        LinearLayout.LayoutParams titleMiddleLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        titleMiddleLayout.weight = 1F;
        LinearLayout middleTitleLayout = createLinearLayout(context, titleMiddleLayout, LinearLayout.HORIZONTAL, Gravity.CENTER);
        middleTitleLayout.addView(createTextView(context, middleTitle, Gravity.CENTER));

        LinearLayout.LayoutParams titleRightLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        titleRightLayout.weight = 1F;
        LinearLayout rightTitleLayout = createLinearLayout(context, titleRightLayout, LinearLayout.HORIZONTAL, Gravity.CENTER);
        rightTitleLayout.addView(createTextView(context, rightTitle, Gravity.CENTER));

       // leftTitleLayout.setBackground(leftTitleBoarder);
        //middleTitleLayout.setBackground(middleTitleBoarder);
        //rightTitleLayout.setBackground(rightTitleBoarder);

        legendAreaTitle.addView(leftTitleLayout);
        legendAreaTitle.addView(middleTitleLayout);
        legendAreaTitle.addView(rightTitleLayout);

        return legendAreaTitle;
    }

    private void setBackground(Drawable boarder, LinearLayout layout, LinearLayout... linearLayout) {
        for (int i = 0; i < linearLayout.length; i++){
            linearLayout[i].setBackground(boarder);
        }
    }

    public void appendChartTableDataRows(Context context, PieChart chart, Drawable customBoarder, LinearLayout view){
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

        class Compare implements Comparator<LegendTableSection> {
            public int compare(LegendTableSection p1, LegendTableSection p2) {
                return Float.compare(p1.totalvalue, p2.totalvalue);
            }
        };

        Collections.sort(legendTableSections, new Comparator<LegendTableSection>(){
            public int compare(LegendTableSection obj1, LegendTableSection obj2) {
                // ## Ascending order
                return Float.compare(obj2.totalvalue, obj1.totalvalue);
            }
        });

        for (LegendTableSection legendTableSection : legendTableSections){
            view.addView(legendTableSection.legendSection);
        }
    }

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
        leftLayout.setPadding(10,0,0,10);
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

    private float calcPercent(Float total, Float num){
        Float percent = num / total;
        percent = percent * 100;
        return percent;
    }

    public String getFormattedPercentage(Float totalnum, Float num){
        return String.format("%.0f%%", calcPercent(totalnum, num));
    }

    private List<LegendTableSection> createTableLegendRows(){

        return null;
    }

    private TextView createTextView(Context context, String text, int gravity) {
        TextView textviewText = new TextView(context);
        textviewText.setText(text);
        textviewText.setGravity(gravity);
        textviewText.setPadding(10, 0,10, 0);
        textviewText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return textviewText;
    }

    private LinearLayout createLinearLayout(Context context, ViewGroup.LayoutParams layoutParams, int gravity, int orientation){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientation);
        linearLayout.setGravity(gravity);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    public class LegendTableSection {
        private LinearLayout legendSection;
        private Float totalvalue;

        public LegendTableSection(LinearLayout theLegendSection, Float theTotalvalue){
            totalvalue = theTotalvalue;
            legendSection = theLegendSection;
        }

        public Float getTotalvalue(){
            return totalvalue;
        }

        public LinearLayout getLayout() {
            return legendSection;
        }
    }
}
