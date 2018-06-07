package com.semerson.networkassessment.activities.Results.MainNavigationFragments.home;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.semerson.networkassessment.activities.Results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.Results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.Results.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.PieChartDetailsActivity;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.VulnerabilityFilterComplexity;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttackComplexity extends Fragment implements View.OnClickListener {

    private LinearLayout mainLayout;
    private ScanResults scanResults;
    private View rootview;
    private ResultController resultController;
    private Context context;
    private FragmentHost fragmentHost;

    private TextView txtMoreDetails;

    private RadioButton radioOS;
    private RadioButton radioThreatLevel;
    private RadioButton radioVulnerabilityCategory;
    private RadioButton radioComplexity;
    private RadioButton radioVulnerableHosts;
    private RadioButton radioAllVulnerabilities;

    public AttackComplexity() {
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
        pieChartCreator.setChartConfig(ChartDescription.ATTACK_COMPLEXITY_LEVEL, false, LegendHeadings.ATTACK_COMPLEXITY_LEVEL, chart, resultController.getAttackComplexityLevel(), PieChartCreator.LOW_TO_CRITICAL);

        mainLayout.addView(chart);
        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, LegendHeadings.ATTACK_COMPLEXITY_LEVEL,
                LegendHeadings.TOTAL_OCCURRENCES, LegendHeadings.OCCURRENCES_AS_PERCENT);

        Table table = pieChartCreator.prepareTableLegendForPieChart(chart, this);
        table.setSortedHighToLow(true);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);

        radioOS = (RadioButton) view.findViewById(R.id.radio_operating_system);
        radioThreatLevel = (RadioButton) view.findViewById(R.id.radio_threat_levels);
        radioVulnerabilityCategory = (RadioButton) view.findViewById(R.id.radio_vulnerability_categories);
        radioComplexity = (RadioButton) view.findViewById(R.id.radio_attack_complexity);
        radioVulnerableHosts = (RadioButton) view.findViewById(R.id.radio_vulnerable_hosts);
        radioAllVulnerabilities = (RadioButton) view.findViewById(R.id.radio_all_vulnerabilities);
        radioComplexity.setChecked(true);
        // txtMoreDetails = UiObjectCreator.createTextView(R.id.osText, UiObjectCreator.txtMoreDetails, context, this);
        // mainLayout.addView(txtMoreDetails);
    }

    public static AttackComplexity newInstance(ScanResults scanResults) {
        AttackComplexity fragment = new AttackComplexity();
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
        radioOS.setChecked(false);
        radioThreatLevel.setChecked(false);
        radioVulnerabilityCategory.setChecked(false);
        radioAllVulnerabilities.setChecked(false);
        radioComplexity.setChecked(false);
        radioVulnerableHosts.setChecked(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        radioComplexity.setChecked(true);
    }


    @Override
    public void onClick(View v) {
        Intent singleChartDisplay = new Intent(context, PieChartDetailsActivity.class);
        singleChartDisplay.putExtra("scan-results", scanResults);
        if (v instanceof TextView) {
            String threatLevel = ((TextView) v).getText().toString();
            Fragment fragment = VulnerabilityFilterComplexity.newInstance(scanResults, threatLevel);
            fragmentHost.setFragment(fragment, true);
        }
    }
}

