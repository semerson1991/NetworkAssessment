package com.semerson.networkassessment.activities.single.chart;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.semerson.networkassessment.Chart.ChartDescription;
import com.semerson.networkassessment.Chart.LegendHeadings;
import com.semerson.networkassessment.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.results.ScanResults;

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
            String chartType = bundle.getString("chart");
            String legendHeading = "";
            switch (chartType) {
                case PieChartCreator.OS:
                    chart = pieChartCreator.createChart(this, 1000, 1000);
                    pieChartCreator.setChartConfig(ChartDescription.OS, LegendHeadings.OS, chart, scanResults.getOperatingSystems() );
                    legendHeading = LegendHeadings.OS;
                    break;
                case PieChartCreator.VULN_CATEGORY:
                    chart = pieChartCreator.createChart(this, 1000, 1000);
                    pieChartCreator.setChartConfig(ChartDescription.VULN_CATEGORY, LegendHeadings.VULN_CATEGORY, chart, scanResults.getVulnFamily() );
                    legendHeading = LegendHeadings.VULN_CATEGORY;
                    break;
                case PieChartCreator.ATTACK_COMPLEXITY:
                    chart = pieChartCreator.createChart(this, 1000, 1000);
                    pieChartCreator.setChartConfig(ChartDescription.ATTACK_COMPLEXITY_LEVEL, LegendHeadings.ATTACK_COMPLEXITY_LEVEL, chart, scanResults.getAttackComplexityLevel() );
                    legendHeading = LegendHeadings.ATTACK_COMPLEXITY_LEVEL;
                    break;
                case PieChartCreator.THREAT_LEVEL:
                    chart = pieChartCreator.createChart(this, 1000, 1000);
                    pieChartCreator.setChartConfig(ChartDescription.THREAT_LEVEL, LegendHeadings.THREAT_LEVEL, chart, scanResults.getThreatLevel() );
                    legendHeading = LegendHeadings.THREAT_LEVEL;
                    break;
            }

            if (chart != null){
                mainLayout.addView(chart);
                Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible); //TODO May not bother with heading borader
                Drawable leftTitleBoarder = getResources().getDrawable(R.drawable.customboarder_table_header_left);
                Drawable middleTitleBoarder = getResources().getDrawable(R.drawable.customboarder_table_header_middle);
                Drawable rightTitleBoarder = getResources().getDrawable(R.drawable.customboarder_table_header_right);

                pieChartCreator.appendChartTableHeader(this, chart,  mainLayout, leftTitleBoarder, middleTitleBoarder, rightTitleBoarder, legendHeading,
                        LegendHeadings.TOTAL_OCCURRENCES, LegendHeadings.OCCURRENCES_AS_PERCENT );

                pieChartCreator.appendChartTableDataRows(this, chart, customBoarder, mainLayout);
            }
        }
    }
}
