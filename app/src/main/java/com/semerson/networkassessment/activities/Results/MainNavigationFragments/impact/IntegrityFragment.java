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
import com.semerson.networkassessment.activities.Results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.Results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.Results.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.impact.singleview.HostVulnFilterIntegrity;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.PieChartDetailsActivity;
import com.semerson.networkassessment.storage.results.Host;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ResultScoreMetrics;
import com.semerson.networkassessment.storage.results.ScanResults;
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
public class IntegrityFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private FragmentHost fragmentHost;

    private LinearLayout mainLayout;
    private ScanResults scanResults;
    private View rootview;
    private ResultController resultController;
    private TextView integrityChartText;

    private RadioButton radioIntegrity;
    public IntegrityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = getActivity().getWindow().getDecorView().getRootView();
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        resultController = new ResultController(scanResults.getHosts());
        return inflater.inflate(R.layout.fragment_integrity, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstancesState) {
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);

        PieChartCreator pieChartCreator = new PieChartCreator();

        PieChart integrityChart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.INTEGRITY, true, LegendHeadings.INTEGRITY, integrityChart, resultController.getIntegrityScore(), PieChartCreator.LOW_TO_CRITICAL);
        mainLayout.addView(integrityChart);
        integrityChartText = UiObjectCreator.createTextView(R.id.confidentialityImpact, UiObjectCreator.txtMoreDetails, context, this);
        mainLayout.addView(integrityChartText);
        radioIntegrity = (RadioButton) view.findViewById(R.id.radio_integrity);
        radioIntegrity.setChecked(true);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, TableHeadings.HOST, TableHeadings.HIGH, TableHeadings.LOW,
                TableHeadings.TOTAL);

        Map<String, ResultScoreMetrics> integrityImpactScore = resultController.getIntegrityScoreCount();

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        Table table = new Table();
        for (String host : integrityImpactScore.keySet()) {
            ResultScoreMetrics integrityScoreCount = integrityImpactScore.get(host);

            table.prepareTableRow(new TableRow(new TableRowData(host, Gravity.CENTER,this),
                    new TableRowData(String.valueOf(integrityScoreCount.getHighCount()), Gravity.CENTER),
                    new TableRowData(String.valueOf(integrityScoreCount.getLowCount()), Gravity.CENTER),
                    new TableRowData(String.valueOf(integrityScoreCount.getTotal()), Gravity.CENTER)));
        }
        table.setSortedHighToLow(true);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);
    }

    public static IntegrityFragment newInstance(ScanResults scanResults) {
        IntegrityFragment fragment = new IntegrityFragment();
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
        radioIntegrity.setChecked(true);
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
                    Fragment fragment = HostVulnFilterIntegrity.newInstance(scanResults, theHost.getHostname(false));
                    fragmentHost.setFragment(fragment, true);
                    break;
                }
            }
        }
    }
}
