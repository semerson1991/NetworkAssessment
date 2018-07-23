package com.semerson.networkassessment.activities.results.MainNavigationFragments.home.singleview;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.semerson.networkassessment.activities.results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.results.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.table.TableCreator;

public class PieChartDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_details);

        PieChartCreator pieChartCreator = new PieChartCreator();

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            PieChart chart = null;
            ScanResults scanResults = bundle.getParcelable("scan-results");
            ResultController resultController = new ResultController(scanResults.getHosts());

            String chartType = bundle.getString("chart");
            String legendHeading = "";
            switch (chartType) {
                case PieChartCreator.OS:
                    chart = pieChartCreator.createChart(this, 1000, 1000);
                    pieChartCreator.setChartConfig(ChartDescription.OS, false, LegendHeadings.OS, chart, resultController.getOperatingSystems(), PieChartCreator.DEFAULT_MIXED);
                    legendHeading = LegendHeadings.OS;
                    break;
                case PieChartCreator.VULN_CATEGORY:
                    chart = pieChartCreator.createChart(this, 1000, 1000);
                    pieChartCreator.setChartConfig(ChartDescription.VULN_CATEGORY, false, LegendHeadings.VULN_CATEGORY, chart, resultController.getVulnFamily(), PieChartCreator.DEFAULT_MIXED );
                    legendHeading = LegendHeadings.VULN_CATEGORY;
                    break;
                case PieChartCreator.ATTACK_COMPLEXITY:
                    chart = pieChartCreator.createChart(this, 1000, 1000);
                    pieChartCreator.setChartConfig(ChartDescription.ATTACK_COMPLEXITY_LEVEL, false, LegendHeadings.ATTACK_COMPLEXITY_LEVEL, chart, resultController.getAttackComplexityLevel(), PieChartCreator.LOW_TO_CRITICAL);
                    legendHeading = LegendHeadings.ATTACK_COMPLEXITY_LEVEL;
                    break;
                case PieChartCreator.THREAT_LEVEL:
                    chart = pieChartCreator.createChart(this, 1000, 1000);
                    pieChartCreator.setChartConfig(ChartDescription.THREAT_LEVEL, false, LegendHeadings.THREAT_LEVEL, chart, resultController.getThreatLevel(), PieChartCreator.LOW_TO_CRITICAL);
                    legendHeading = LegendHeadings.THREAT_LEVEL;
                    break;
            }

            if (chart != null){
                mainLayout.addView(chart);
                Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

                TableCreator tableCreator = new TableCreator();
                tableCreator.appendTableHeader(this,  mainLayout, legendHeading,
                        LegendHeadings.TOTAL_OCCURRENCES, LegendHeadings.OCCURRENCES_AS_PERCENT );


                pieChartCreator.appendChartTableDataRowsSorted(this, chart, customBoarder, mainLayout);
            }
        }
    }
}
