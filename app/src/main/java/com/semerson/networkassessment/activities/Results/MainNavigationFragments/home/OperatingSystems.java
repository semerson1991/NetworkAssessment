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
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.HostVulnerabilityDetailsFragment;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.PieChartDetailsActivity;
import com.semerson.networkassessment.activities.network.HostsFilterByOSFragment;
import com.semerson.networkassessment.activities.network.SingleNetworkDeviceFragment;
import com.semerson.networkassessment.storage.results.Host;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OperatingSystems extends Fragment implements View.OnClickListener {

    private LinearLayout mainLayout;
    private ScanResults scanResults;
    private View rootview;
    private ResultController resultController;
    private Context context;
    private FragmentHost fragmentHost;

    private TextView txtMoreDetails;

    private RadioButton radioThreatLevel;
    private RadioButton radioVulnerabilityCategory;
    private RadioButton radioComplexity;
    private RadioButton radioVulnerableHosts;
    private RadioButton radioAllVulnerabilities;

    public OperatingSystems() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = getActivity().getWindow().getDecorView().getRootView();
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        if (scanResults != null) {
            resultController = new ResultController(scanResults.getHosts());
        }
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);

        PieChartCreator pieChartCreator = new PieChartCreator();


        PieChart chart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.OS, false, LegendHeadings.OS, chart, resultController.getOperatingSystems(), PieChartCreator.DEFAULT_MIXED);

        mainLayout.addView(chart);
        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, LegendHeadings.OS,
                LegendHeadings.TOTAL_OCCURRENCES, LegendHeadings.OCCURRENCES_AS_PERCENT);

        Table table = pieChartCreator.prepareTableLegendForPieChart(chart, this);
        table.setSortedHighToLow(true);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);

        radioThreatLevel = (RadioButton) view.findViewById(R.id.radio_threat_levels);
        radioVulnerabilityCategory = (RadioButton) view.findViewById(R.id.radio_vulnerability_categories);
        radioComplexity = (RadioButton) view.findViewById(R.id.radio_attack_complexity);
        radioVulnerableHosts = (RadioButton) view.findViewById(R.id.radio_vulnerable_hosts);
        radioAllVulnerabilities = (RadioButton) view.findViewById(R.id.radio_all_vulnerabilities);
        // txtMoreDetails = UiObjectCreator.createTextView(R.id.osText, UiObjectCreator.txtMoreDetails, context, this);
        // mainLayout.addView(txtMoreDetails);
    }

    public static OperatingSystems newInstance(ScanResults scanResults) {
        OperatingSystems fragment = new OperatingSystems();
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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rowListener) {
            Object operatingSystem = v.getTag();
            if (operatingSystem != null) {
                if (operatingSystem instanceof String) {
                    Fragment fragment = HostsFilterByOSFragment.newInstance((String) operatingSystem);
                    fragmentHost.setFragment(fragment, true);
                }
            }
        }
    }
}

