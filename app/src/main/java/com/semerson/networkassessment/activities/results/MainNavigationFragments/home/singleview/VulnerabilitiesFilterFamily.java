package com.semerson.networkassessment.activities.results.MainNavigationFragments.home.singleview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.semerson.networkassessment.activities.results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.results.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.controller.FragmentHost;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.storage.results.VulnerabilityResult;
import com.semerson.networkassessment.utils.StyledText;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;
import com.semerson.networkassessment.utils.table.TableHeadings;
import com.semerson.networkassessment.utils.table.TableRow;
import com.semerson.networkassessment.utils.table.TableRowData;

import java.util.List;

public class VulnerabilitiesFilterFamily extends Fragment implements View.OnClickListener {

    private static final String VULN_FAMILY = "Vulnerabilities for the category: ";
    private Context context;
    private ScanResults scanResults;
    private ResultController resultController;
    private LinearLayout mainLayout;

    private FragmentHost fragmentHost;
    private TextView vulnText;
    private String vulnerabilityFamily;

    public VulnerabilitiesFilterFamily() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        vulnerabilityFamily = getArguments().getString("vuln-family");
        resultController = new ResultController(scanResults.getHosts());

        return inflater.inflate(R.layout.fragment_host_vulnerability_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainLayout = view.findViewById(R.id.mainLayout);
        TextView textViewTitle = (TextView) view.findViewById(R.id.txtVulnsTitle);
        textViewTitle.setText(VULN_FAMILY + vulnerabilityFamily);

        PieChartCreator pieChartCreator = new PieChartCreator();



        PieChart chart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.THREAT_LEVEL, true, LegendHeadings.THREAT_LEVEL, chart, resultController.getThreatLevelFilterByVulnFamily(vulnerabilityFamily), PieChartCreator.LOW_TO_CRITICAL);
        mainLayout.addView(chart);
        //vulnText = UiObjectCreator.createTextView(R.id.vulnCategoryText, UiObjectCreator.txtMoreDetails, context, this);
        //mainLayout.addView(vulnText);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, TableHeadings.VULN_NAME, TableHeadings.THREAT_LEVEL, TableHeadings.RISK_SCORE);

        List<VulnerabilityResult> vulnResult = resultController.getVulnerabiltiesFilterByCategory(vulnerabilityFamily);
        resultController.filterByRiskScoreHighToLow(vulnResult);

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);
        Table table = new Table();
        for (VulnerabilityResult result : vulnResult) {
            StyledText styledRating = result.getThreatRatingStyledText();
            StyledText styledRiskScore = result.getRiskScoreStyledText();

            TableRowData tableRowDataHost = new TableRowData(result.getNvtName(), Gravity.LEFT);
            TableRowData tableRowDataLastScanned = new TableRowData(styledRating.getText(), styledRating.getStyle(), Gravity.CENTER);
            TableRowData tableRowDataRisksFound = new TableRowData(styledRiskScore.getText(), styledRiskScore.getStyle(), Gravity.CENTER);

            TableRow tableRow = new TableRow(tableRowDataHost, tableRowDataLastScanned, tableRowDataRisksFound);
            tableRow.setOnClickListener(this);
            tableRow.setTag(result);
            table.appendTableRow(tableRow);
        }
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);
    }

    public static VulnerabilitiesFilterFamily newInstance(ScanResults scanResults, String vulnFamily) {
        VulnerabilitiesFilterFamily fragment = new VulnerabilitiesFilterFamily();
        Bundle bundle = new Bundle();
        bundle.putString("vuln-family", vulnFamily);
        bundle.putParcelable("scan-results", scanResults);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

        try {
            fragmentHost = (FragmentHost) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FragmentHost");
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof LinearLayout) {
            Object object = v.getTag();
            if (object != null && object instanceof VulnerabilityResult) {
                Fragment fragment = VulnerabilityDetailsFragment.newInstance(scanResults, (VulnerabilityResult) object);
                fragmentHost.setFragment(fragment, true);
            }
        }
    }
}
