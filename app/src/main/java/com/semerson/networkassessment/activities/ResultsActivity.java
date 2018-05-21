package com.semerson.networkassessment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.semerson.networkassessment.Chart.ChartDescription;
import com.semerson.networkassessment.Chart.LegendHeadings;
import com.semerson.networkassessment.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.single.chart.PieChartDetailsActivity;
import com.semerson.networkassessment.results.Host;
import com.semerson.networkassessment.results.ScanResults;
import com.semerson.networkassessment.results.OpenVasResult;

import java.util.List;

public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Host> hosts;
    private List<OpenVasResult> openVasResultList;
    private ScanResults scanResults;


    private final static String TAG = "ResultActivity";

    private TextView osText;
    private TextView vulnText;
    private TextView threatLevelText;
    private TextView skillLevelText;

    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scanResults = bundle.getParcelable("scan-results");
            hosts = scanResults.getHosts();
            openVasResultList = scanResults.getOpenVasResults();
        }

        //setChartConfig(pieSkillLevel, "Attack Complexity");
        //setChartConfig(pieVulnLevel, "Operating Systems");

        PieChartCreator pieChartCreator = new PieChartCreator();

        PieChart chartOS = pieChartCreator.createChart(this, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.OS, LegendHeadings.OS, chartOS, scanResults.getOperatingSystems() );
        mainLayout.addView(chartOS);
        osText = createChartMoreDetails(R.id.osText);
        mainLayout.addView(osText);

        PieChart vulnFamily = pieChartCreator.createChart(this, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.VULN_CATEGORY, LegendHeadings.VULN_CATEGORY, vulnFamily, scanResults.getVulnFamily() );
        mainLayout.addView(vulnFamily);
        vulnText = createChartMoreDetails(R.id.vulnCategoryText);
        mainLayout.addView(vulnText);

        PieChart threatLevel = pieChartCreator.createChart(this, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.THREAT_LEVEL, LegendHeadings.THREAT_LEVEL, threatLevel, scanResults.getThreatLevel());
        mainLayout.addView(threatLevel);
        threatLevelText = createChartMoreDetails(R.id.threatLevelText);
        mainLayout.addView(threatLevelText);

        PieChart skillLevel = pieChartCreator.createChart(this, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.ATTACK_COMPLEXITY_LEVEL, LegendHeadings.ATTACK_COMPLEXITY_LEVEL, skillLevel, scanResults.getAttackComplexityLevel());
        mainLayout.addView(skillLevel);
        skillLevelText = createChartMoreDetails(R.id.attackComplexity);
        mainLayout.addView(skillLevelText);

        /* DONT DELETE - THIS IS AN EXAMPLE OF A HYPERLINK TO WEBSITE!!!
        TextView textView = new TextView(this);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.google.com'> More detail </a>";
        textView.setText(Html.fromHtml(text));
        mainLayout.addView(textView);
        */

        /*
        PieChart vulnFamily = pieChartCreator.createChart(this, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.VULN_CATEGORY, LegendHeadings.VULN_CATEGORY, vulnFamily, scanResults.getVulnFamily() );
        mainLayout.addView(vulnFamily);

        pieChartCreator.appendChartTableHeader(this, vulnFamily, mainLayout, LegendHeadings.VULN_CATEGORY,
                LegendHeadings.TOTAL_OCCURRENCES, LegendHeadings.OCCURRENCES_AS_PERCENT );

        Drawable legendIcon = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);
        pieChartCreator.appendChartTableDataRows(this, vulnFamily, legendIcon, mainLayout);
        */
    }

    @Override
    public void onClick(View v) {
        Intent singleChartDisplay = new Intent(ResultsActivity.this, PieChartDetailsActivity.class);
        switch (v.getId()){
            case R.id.osText:
                singleChartDisplay.putExtra("scan-results", scanResults);
                singleChartDisplay.putExtra("chart", PieChartCreator.OS);
                startActivity(singleChartDisplay);
                break;
            case R.id.vulnCategoryText:
                singleChartDisplay.putExtra("scan-results", scanResults);
                singleChartDisplay.putExtra("chart", PieChartCreator.VULN_CATEGORY);
                startActivity(singleChartDisplay);
                break;
                default:
            case R.id.threatLevelText:
                singleChartDisplay.putExtra("scan-results", scanResults);
                singleChartDisplay.putExtra("chart", PieChartCreator.THREAT_LEVEL);
                startActivity(singleChartDisplay);
                break;
            case R.id.attackComplexity:
                singleChartDisplay.putExtra("scan-results", scanResults);
                singleChartDisplay.putExtra("chart", PieChartCreator.ATTACK_COMPLEXITY);
                startActivity(singleChartDisplay);
                break;

        }
    }

    private TextView createChartMoreDetails(@IdRes int id) {
        TextView textView = new TextView(this);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnClickListener(this);
        textView.setId(id);

        String text = "<a> More detail </a>";
        textView.setText(Html.fromHtml(text));
        return textView;
    }
     /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IDataSet<?> set : pieChartOs.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                pieChartOs.invalidate();
                break;
            }
            case R.id.actionToggleIcons: {
                for (IDataSet<?> set : pieChartOs.getData().getDataSets())
                    set.setDrawIcons(!set.isDrawIconsEnabled());

                pieChartOs.invalidate();
                break;
            }
            case R.id.actionToggleHole: {
                if (pieChartOs.isDrawHoleEnabled())
                    pieChartOs.setDrawHoleEnabled(false);
                else
                    pieChartOs.setDrawHoleEnabled(true);
                pieChartOs.invalidate();
                break;
            }
            case R.id.actionDrawCenter: {
                if (pieChartOs.isDrawCenterTextEnabled())
                    pieChartOs.setDrawCenterText(false);
                else
                    pieChartOs.setDrawCenterText(true);
                pieChartOs.invalidate();
                break;
            }
            case R.id.actionToggleXVals: {

                pieChartOs.setDrawEntryLabels(!mChart.isDrawEntryLabelsEnabled());
                pieChartOs.invalidate();
                break;
            }
            case R.id.actionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
                pieChartOs.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.actionTogglePercent:
                pieChartOs.setUsePercentValues(!mChart.isUsePercentValuesEnabled());
                pieChartOs.invalidate();
                break;
            case R.id.animateX: {
                pieChartOs.animateX(1400);
                break;
            }
            case R.id.animateY: {
                pieChartOs.animateY(1400);
                break;
            }
            case R.id.animateXY: {
                pieChartOs.animateXY(1400, 1400);
                break;
            }
            case R.id.actionToggleSpin: {
                pieChartOs.spin(1000, pieChartOs.getRotationAngle(), pieChartOs.getRotationAngle() + 360, Easing.EasingOption.EaseInCubic);
                break;
            }
        }
        return true;
    }
    */

}
