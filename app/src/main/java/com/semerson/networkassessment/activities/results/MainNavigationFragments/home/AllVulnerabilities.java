package com.semerson.networkassessment.activities.results.MainNavigationFragments.home;


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
import android.widget.RadioButton;

import com.github.mikephil.charting.charts.PieChart;
import com.semerson.networkassessment.activities.results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.results.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.controller.FragmentHost;
import com.semerson.networkassessment.activities.results.MainNavigationFragments.home.singleview.VulnerabilityDetailsFragment;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ResultLevels;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.storage.results.VulnerabilityResult;
import com.semerson.networkassessment.utils.StyledText;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;
import com.semerson.networkassessment.utils.table.TableRow;
import com.semerson.networkassessment.utils.table.TableRowData;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllVulnerabilities extends Fragment implements View.OnClickListener {

    private LinearLayout mainLayout;
    private ScanResults scanResults;
    private View rootview;
    private ResultController resultController;
    private Context context;
    private FragmentHost fragmentHost;

    private RadioButton radioThreatLevel;
    private RadioButton radioVulnerabilityCategory;
    private RadioButton radioComplexity;
    private RadioButton radioVulnerableHosts;
    private RadioButton radioAllVulnerabilities;

    boolean advancedMode;

    public AllVulnerabilities() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = getActivity().getWindow().getDecorView().getRootView();
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        resultController = new ResultController(scanResults.getHosts());

        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);


        PieChartCreator pieChartCreator = new PieChartCreator();


        PieChart chart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.THREAT_LEVEL, true, LegendHeadings.THREAT_LEVEL, chart, resultController.getThreatLevel(), PieChartCreator.LOW_TO_CRITICAL);

        mainLayout.addView(chart);
        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, LegendHeadings.VULN_NAME, LegendHeadings.VULN_CATEGORY, LegendHeadings.THREAT_LEVEL);

        List<VulnerabilityResult> highRisk = resultController.getVulnerabiltiesFilterByThreatLevel(ResultLevels.HIGH);
        List<VulnerabilityResult> mediumRisk = resultController.getVulnerabiltiesFilterByThreatLevel(ResultLevels.MEDIUM);
        List<VulnerabilityResult> lowRisk = resultController.getVulnerabiltiesFilterByThreatLevel(ResultLevels.LOW);


        Table table = new Table();

        prepareRow(highRisk, table);
        prepareRow(mediumRisk, table);
        prepareRow(lowRisk, table);


        tableCreator.createTableViews(context, mainLayout, customBoarder, table);

        radioThreatLevel = (RadioButton) view.findViewById(R.id.radio_threat_levels);
        radioVulnerabilityCategory = (RadioButton) view.findViewById(R.id.radio_vulnerability_categories);
        radioComplexity = (RadioButton) view.findViewById(R.id.radio_attack_complexity);
        radioVulnerableHosts = (RadioButton) view.findViewById(R.id.radio_vulnerable_hosts);
        radioAllVulnerabilities = (RadioButton) view.findViewById(R.id.radio_all_vulnerabilities);
        radioAllVulnerabilities.setChecked(true);
        // txtMoreDetails = UiObjectCreator.createTextView(R.id.osText, UiObjectCreator.txtMoreDetails, context, this);
        // mainLayout.addView(txtMoreDetails);
    }

    private void prepareRow(List<VulnerabilityResult> results, Table table) {
        for (VulnerabilityResult result : results) {
            StyledText styledRating = result.getThreatRatingStyledText();

            TableRowData tableRowDataHost = new TableRowData(result.getNvtName(), Gravity.CENTER);
            TableRowData tableRowDataLastScanned = new TableRowData(result.getVulnFamily(), Gravity.CENTER);
            TableRowData tableRowDataRisksFound = new TableRowData(styledRating.getText(), styledRating.getStyle(), Gravity.CENTER);

            TableRow tableRow = new TableRow(tableRowDataHost, tableRowDataLastScanned, tableRowDataRisksFound);
            tableRow.setOnClickListener(this);
            tableRow.setTag(result);
            table.appendTableRow(tableRow);
        }
    }

    public static AllVulnerabilities newInstance(ScanResults scanResults) {
        AllVulnerabilities fragment = new AllVulnerabilities();
        Bundle bundle = new Bundle();
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
    public void onPause() {
        super.onPause();
        radioThreatLevel.setChecked(false);
        radioVulnerabilityCategory.setChecked(false);
        radioAllVulnerabilities.setChecked(false);
        radioComplexity.setChecked(false);
        radioVulnerableHosts.setChecked(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        radioAllVulnerabilities.setChecked(true);
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
