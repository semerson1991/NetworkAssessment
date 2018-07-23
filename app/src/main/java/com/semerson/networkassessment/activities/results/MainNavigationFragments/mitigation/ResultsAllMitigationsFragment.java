package com.semerson.networkassessment.activities.results.MainNavigationFragments.mitigation;


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
import com.semerson.networkassessment.activities.results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.results.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.results.MainNavigationFragments.home.singleview.VulnerabilityDetailsFragment;
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
        PieChartCreator pieChartCreator = new PieChartCreator();

        radioAllMitigations =(RadioButton)view.findViewById(R.id.radio_all_mitigations);
        radioMitigationCategories =(RadioButton)view.findViewById(R.id.radio_mitigation_categories);
        radioAllMitigations.setChecked(true);

        PieChart chart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.MITIGATION_CATEGORY, true, LegendHeadings.MITIGATION_CATEGORY, chart, resultController.getMitigationCategories(), PieChartCreator.DEFAULT_MIXED);
        mainLayout.addView(chart);

        TextView textView = view.findViewById(R.id.txtDescription);
    //    textView.setText(R.string.IntegrityDescription);
        textView.setVisibility(View.GONE);

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        List<VulnerabilityResult> vulnerabilityResults = resultController.getAllVulnerabilities();
        resultController.filterByRiskScoreHighToLow(vulnerabilityResults);

        Table table = new Table();
        for (VulnerabilityResult result : vulnerabilityResults) {
            StyledText styledRiskScore = result.getRiskScoreStyledText();

            TableRowData tableRowDataVulnName = new TableRowData(result.getNvtName(), Gravity.LEFT);
            TableRowData tableRowDataMitigation = new TableRowData(result.getSolution(), Gravity.LEFT);
            TableRowData tableRowDataMitigationCategory = new TableRowData(result.getSolutionType(), Gravity.CENTER);
            TableRowData tableRowDataRisksScore = new TableRowData(styledRiskScore.getText(), styledRiskScore.getStyle(), Gravity.CENTER);

            TableRow tableRow = new TableRow(tableRowDataVulnName, tableRowDataMitigation, tableRowDataMitigationCategory, tableRowDataRisksScore);
            tableRow.setOnClickListener(this);
            tableRow.setTag(result);
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
        if (v instanceof LinearLayout) {
            Object object = v.getTag();
            if (object != null && object instanceof VulnerabilityResult) {
                Fragment fragment = VulnerabilityDetailsFragment.newInstance(scanResults, (VulnerabilityResult) object);
                fragmentHost.setFragment(fragment, true);
            }
        }
    }
}
