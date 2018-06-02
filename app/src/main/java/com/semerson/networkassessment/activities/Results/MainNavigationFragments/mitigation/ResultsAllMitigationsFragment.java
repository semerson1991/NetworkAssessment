package com.semerson.networkassessment.activities.Results.MainNavigationFragments.mitigation;


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
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.semerson.networkassessment.Chart.ChartDescription;
import com.semerson.networkassessment.Chart.LegendHeadings;
import com.semerson.networkassessment.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.VulnerabilityFilterMitigationType;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.results.ResultController;
import com.semerson.networkassessment.results.ScanResults;
import com.semerson.networkassessment.results.VulnerabilityResult;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;
import com.semerson.networkassessment.utils.table.TableHeadings;
import com.semerson.networkassessment.utils.table.TableRow;
import com.semerson.networkassessment.utils.table.TableRowData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsAllMitigationsFragment extends Fragment implements View.OnClickListener{


    private static final String VULN_HOST_TITLE = "Vulnerability Mitigations";
    private Context context;
    private ScanResults scanResults;
    private ResultController resultController;
    private LinearLayout mainLayout;

    private FragmentHost fragmentHost;

    private RadioButton radioAllMitigations;
    private RadioButton radioMitigationCategories;


    public ResultsAllMitigationsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        resultController = new ResultController(scanResults.getHosts());

        return inflater.inflate(R.layout.fragment_results_mitigation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainLayout = view.findViewById(R.id.mainLayout);
        TextView textViewTitle = view.findViewById(R.id.mitigation_title);
        textViewTitle.setText(VULN_HOST_TITLE);
        PieChartCreator pieChartCreator = new PieChartCreator();

        radioAllMitigations =(RadioButton)view.findViewById(R.id.radio_all_mitigations);
        radioMitigationCategories =(RadioButton)view.findViewById(R.id.radio_mitigation_categories);
        radioAllMitigations.setChecked(true);

        PieChart chart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.MITIGATION, true, LegendHeadings.MITIGATION, chart, resultController.getMitigationCategories(), PieChartCreator.DEFAULT_MIXED);
        mainLayout.addView(chart);

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        List<VulnerabilityResult> vulnerabilityResults = resultController.getAllVulnerabilities();
        resultController.filterByRiskScoreHighToLow(vulnerabilityResults);

        Table table = new Table();
        for (VulnerabilityResult result : vulnerabilityResults) {
            TableRow tableRow = new TableRow(
                    new TableRowData(result.getNvtName(), Gravity.LEFT, this),
                    new TableRowData(result.getSolution(), Gravity.CENTER),
                    new TableRowData(result.getSolutionType(), Gravity.CENTER),
                    new TableRowData(result.getRiskScoreAsString(), Gravity.CENTER));
            table.appendTableRow(tableRow);
        }

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, TableHeadings.VULN_NAME, TableHeadings.MITIGATION_NAME, TableHeadings.MITIGATION_CATEGORY, TableHeadings.RISK_SCORE);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);
    }


    public static ResultsAllMitigationsFragment newInstance(ScanResults scanResults) {
        ResultsAllMitigationsFragment fragment = new ResultsAllMitigationsFragment();
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
        radioAllMitigations.setChecked(false);
        radioMitigationCategories.setChecked(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        radioAllMitigations.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String textValue = ((TextView) v).getText().toString();
            android.app.Fragment fragment = VulnerabilityFilterMitigationType.newInstance(scanResults, textValue);
            fragmentHost.setFragment(fragment, true);
        }
    }
}
