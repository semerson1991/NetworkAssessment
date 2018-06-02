package com.semerson.networkassessment.activities.Results.MainNavigationFragments.impact;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
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
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.PieChartDetailsActivity;
import com.semerson.networkassessment.results.Host;
import com.semerson.networkassessment.results.ResultScoreMetrics;
import com.semerson.networkassessment.results.ResultController;
import com.semerson.networkassessment.results.ScanResults;
import com.semerson.networkassessment.utils.UiObjectCreator;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;
import com.semerson.networkassessment.utils.table.TableHeadings;
import com.semerson.networkassessment.utils.table.TableRow;
import com.semerson.networkassessment.utils.table.TableRowData;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfidentialityFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private FragmentHost fragmentHost;

    private LinearLayout mainLayout;
    private ScanResults scanResults;
    private View rootview;
    private ResultController resultController;
    private TextView confidentualityChartText;

    private RadioButton radioButtonConfidentiality;

    public ConfidentialityFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = getActivity().getWindow().getDecorView().getRootView();
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        resultController = new ResultController(scanResults.getHosts());
        return inflater.inflate(R.layout.fragment_confidentiality, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstancesState) {
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);

        PieChartCreator pieChartCreator = new PieChartCreator();

        PieChart confidentualityChart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.CONFIDENTIALITY, true, LegendHeadings.CONFIDENIALITY, confidentualityChart, resultController.getConfidentialityScore(), PieChartCreator.LOW_TO_CRITICAL);
        mainLayout.addView(confidentualityChart);
        confidentualityChartText = UiObjectCreator.createTextView(R.id.confidentialityImpact, UiObjectCreator.txtMoreDetails, context, this);
        mainLayout.addView(confidentualityChartText);

        radioButtonConfidentiality = (RadioButton) view.findViewById(R.id.radio_confidentiality);
        radioButtonConfidentiality.setChecked(true);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, TableHeadings.HOST, TableHeadings.HIGH, TableHeadings.LOW,
                TableHeadings.TOTAL);

        Map<String, ResultScoreMetrics> confidentialityImpactScore = resultController.getConfidentialityScoreCount();

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        Table table = new Table();
        for (String host : confidentialityImpactScore.keySet()) {
            ResultScoreMetrics confidentialityScoreCount = confidentialityImpactScore.get(host);

            table.prepareTableRow(new TableRow(new TableRowData(host, Gravity.CENTER, this),
                    new TableRowData(String.valueOf(confidentialityScoreCount.getHighCount()), Gravity.CENTER, this),
                    new TableRowData(String.valueOf(confidentialityScoreCount.getLowCount()), Gravity.CENTER, this),
                    new TableRowData(String.valueOf(confidentialityScoreCount.getTotal()), Gravity.CENTER, this)));
        }
        table.setSortedHighToLow(true);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);
    }

    public static ConfidentialityFragment newInstance(ScanResults scanResults) {
        ConfidentialityFragment fragment = new ConfidentialityFragment();
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
        radioButtonConfidentiality.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        Intent singleChartDisplay = new Intent(context, PieChartDetailsActivity.class);
        singleChartDisplay.putExtra("scan-results", scanResults);
        if (v instanceof TextView) {
            String textValue = ((TextView) v).getText().toString();
            List<Host> hosts = scanResults.getHosts();
            for (Host theHost : hosts) {
                if (theHost.getHostname(false).equals(textValue)) {
                    Fragment fragment = HostVulnerabilityDetailsFragment.newInstance(scanResults, theHost.getHostname(false));
                    fragmentHost.setFragment(fragment, false);
                    break;
                }
            }
        }
    }
}
