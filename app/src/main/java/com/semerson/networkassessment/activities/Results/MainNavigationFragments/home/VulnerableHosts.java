package com.semerson.networkassessment.activities.Results.MainNavigationFragments.home;

import android.app.Activity;
import android.content.Context;
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
import com.semerson.networkassessment.Chart.ChartDescription;
import com.semerson.networkassessment.Chart.LegendHeadings;
import com.semerson.networkassessment.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.HostVulnerabilityDetailsFragment;
import com.semerson.networkassessment.results.Host;
import com.semerson.networkassessment.results.ResultController;
import com.semerson.networkassessment.results.ResultScoreMetrics;
import com.semerson.networkassessment.results.ScanResults;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;
import com.semerson.networkassessment.utils.table.TableHeadings;

import java.util.List;
import java.util.Map;


public class VulnerableHosts extends Fragment implements View.OnClickListener {
    private LinearLayout mainLayout;
    private ScanResults scanResults;
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

    public VulnerableHosts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        resultController = new ResultController(scanResults.getHosts());
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainLayout = view.findViewById(R.id.mainLayout);


        PieChartCreator pieChartCreator = new PieChartCreator();

        PieChart vulnerableHosts = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.VULNERABLE_HOSTS, true, LegendHeadings.VULNERABLE_HOSTS, vulnerableHosts, resultController.getNumberOfHostVulnerabilities(), PieChartCreator.DEFAULT_MIXED);
        mainLayout.addView(vulnerableHosts);
        // vulnText = UiObjectCreator.createTextView(R.id.vulnCategoryText, UiObjectCreator.txtMoreDetails, context, this);
        // mainLayout.addView(vulnText);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, TableHeadings.HOST, TableHeadings.HIGH,
                TableHeadings.MEDIUM, TableHeadings.LOW, TableHeadings.TOTAL);

        Map<String, ResultScoreMetrics> threatLevels = resultController.getVulnThreatLevelCount();

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        Table table = pieChartCreator.prepareTableLegendOccurrences(threatLevels, this);
        table.setSortedHighToLow(true);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);

        radioOS = (RadioButton) view.findViewById(R.id.radio_operating_system);
        radioThreatLevel = (RadioButton) view.findViewById(R.id.radio_threat_levels);
        radioVulnerabilityCategory = (RadioButton) view.findViewById(R.id.radio_vulnerability_categories);
        radioComplexity = (RadioButton) view.findViewById(R.id.radio_attack_complexity);
        radioVulnerableHosts = (RadioButton) view.findViewById(R.id.radio_vulnerable_hosts);
        radioAllVulnerabilities = (RadioButton) view.findViewById(R.id.radio_all_vulnerabilities);
        radioVulnerableHosts.setChecked(true);
        // txtMoreDetails = UiObjectCreator.createTextView(R.id.osText, UiObjectCreator.txtMoreDetails, context, this);
        // mainLayout.addView(txtMoreDetails);
    }

    public static VulnerableHosts newInstance(ScanResults scanResults) {
        VulnerableHosts fragment = new VulnerableHosts();
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
        radioVulnerableHosts.setChecked(true);
    }


    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String textValue = ((TextView) v).getText().toString();
            List<Host> hosts = scanResults.getHosts();
            for (Host theHost : hosts) {
                if (theHost.getHostname(false).equals(textValue)) {
                    HostVulnerabilityDetailsFragment hostVulnerabilityDetailsFragment = HostVulnerabilityDetailsFragment.newInstance(scanResults, theHost.getHostname(false));
                    fragmentHost.setFragment(hostVulnerabilityDetailsFragment, true);
                    break;
                }
            }
        }
    }
}