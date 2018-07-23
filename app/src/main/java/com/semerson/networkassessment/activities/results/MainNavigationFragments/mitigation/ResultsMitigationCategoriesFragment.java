package com.semerson.networkassessment.activities.results.MainNavigationFragments.mitigation;


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
import com.semerson.networkassessment.activities.results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.results.Chart.PieChartCreator;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.controller.FragmentHost;
import com.semerson.networkassessment.activities.results.MainNavigationFragments.home.singleview.VulnerabilityFilterMitigationType;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsMitigationCategoriesFragment extends Fragment implements View.OnClickListener {

    private static final String VULN_HOST_TITLE = "Vulnerability Mitigations";
    private Context context;
    private ScanResults scanResults;
    private ResultController resultController;
    private LinearLayout mainLayout;

    private FragmentHost fragmentHost;

    private RadioButton radioAllMitigations;
    private RadioButton radioMitigationCategories;


    public ResultsMitigationCategoriesFragment() {

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
        radioMitigationCategories.setChecked(true);

        ArrayList<Integer> colors = new ArrayList<>();
        PieChart chart = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.MITIGATION_CATEGORY, false, LegendHeadings.MITIGATION_CATEGORY, chart, resultController.getMitigationCategories(), PieChartCreator.DEFAULT_MIXED);
        mainLayout.addView(chart);

        TextView textView = view.findViewById(R.id.txtDescription);
        //textView.setText(R.string.IntegrityDescription);
        textView.setVisibility(View.GONE);

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, LegendHeadings.MITIGATION_CATEGORY,
                LegendHeadings.TOTAL_OCCURRENCES, LegendHeadings.OCCURRENCES_AS_PERCENT);

        Table table = pieChartCreator.prepareTableLegendForPieChart(chart, this);
        table.setSortedHighToLow(true);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);
    }


    public static ResultsMitigationCategoriesFragment newInstance(ScanResults scanResults) {
        ResultsMitigationCategoriesFragment fragment = new ResultsMitigationCategoriesFragment();
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
        radioMitigationCategories.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof LinearLayout) {
            Object object = v.getTag();
            if (object != null && object instanceof String) {
                Fragment fragment = VulnerabilityFilterMitigationType.newInstance(scanResults, (String) object);
                fragmentHost.setFragment(fragment, true);
            }
        }
    }
}
