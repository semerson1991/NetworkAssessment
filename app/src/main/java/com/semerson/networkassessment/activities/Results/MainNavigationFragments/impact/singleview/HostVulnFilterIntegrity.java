package com.semerson.networkassessment.activities.Results.MainNavigationFragments.impact.singleview;


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
import com.semerson.networkassessment.activities.Results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.Results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.Results.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.VulnerabilityDetailsFragment;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.storage.results.VulnerabilityResult;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;
import com.semerson.networkassessment.utils.table.TableHeadings;
import com.semerson.networkassessment.utils.table.TableRow;
import com.semerson.networkassessment.utils.table.TableRowData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HostVulnFilterIntegrity extends Fragment implements View.OnClickListener {


    private Context context;
    private FragmentHost fragmentHost;

    private LinearLayout mainLayout;
    private ScanResults scanResults;
    private String host;
    private View rootview;
    private ResultController resultController;
    private TextView title;

    public HostVulnFilterIntegrity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = getActivity().getWindow().getDecorView().getRootView();
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        resultController = new ResultController(scanResults.getHosts());
        host = getArguments().getString("host");
        return inflater.inflate(R.layout.fragment_vulns_filter_confidentiality, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstancesState) {
        mainLayout = view.findViewById(R.id.mainLayout);
        title = view.findViewById(R.id.txtImpactTitle);
        title.setText("Integrity Impact for host: " + host);

        PieChartCreator pieChartCreator = new PieChartCreator();

        Map<String, Float> integrityScore = resultController.getIntegrityScore(host);
        integrityScore.remove(VulnerabilityResult.NONE);

        PieChart confidentialityChart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.INTEGRITY, true, LegendHeadings.INTEGRITY, confidentialityChart, integrityScore, PieChartCreator.LOW_TO_CRITICAL);
        mainLayout.addView(confidentialityChart);


        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, TableHeadings.VULN_NAME, TableHeadings.INTEGRITY_IMPACT, TableHeadings.RISK_SCORE);

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        List<VulnerabilityResult> vulnerabilityResults = resultController.getVulnerabilitiesFilterByHost(host);
        List<VulnerabilityResult> resultsRemoveScoreNone = new ArrayList<>();

        for (VulnerabilityResult result : vulnerabilityResults) {
            if (!result.getIntegrityScore().equals(VulnerabilityResult.NONE)) {
                resultsRemoveScoreNone.add(result);
            }
        }

        Collections.sort(resultsRemoveScoreNone, new Comparator<VulnerabilityResult>() {
            public int compare(VulnerabilityResult v1, VulnerabilityResult v2) {
                return v1.getIntegrityScore().compareTo(v2.getIntegrityScore());
            }
        });


        Table table = new Table();
        for (VulnerabilityResult result : resultsRemoveScoreNone) {
            table.prepareTableRow(new TableRow(new TableRowData(result.getNvtName(), Gravity.CENTER, this),
                    new TableRowData(result.getIntegrityScore(), Gravity.CENTER),
                    new TableRowData(result.getRiskScoreAsString(), Gravity.CENTER)));
        }
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);
    }

    public static HostVulnFilterIntegrity newInstance(ScanResults scanResults, String host) {
        HostVulnFilterIntegrity fragment = new HostVulnFilterIntegrity();
        Bundle bundle = new Bundle();
        bundle.putParcelable("scan-results", scanResults);
        bundle.putString("host", host);
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
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String textValue = ((TextView) v).getText().toString();
            VulnerabilityResult vulnerability = scanResults.getVulnerabilityInfo(textValue);
            Fragment fragment = VulnerabilityDetailsFragment.newInstance(scanResults, vulnerability);
            fragmentHost.setFragment(fragment, true);
        }
    }


}