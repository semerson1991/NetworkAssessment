package com.semerson.networkassessment.activities.Results.MainNavigationFragments;


import android.content.Context;
import android.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsVulnsFragment extends Fragment  {


    private LinearLayout mainLayout;
    private ScanResults scanResults;
    private View rootview;
    private ResultController resultController;
    private Context context;
    private FragmentHost fragmentHost;

    private TextView vulnText;

    public ResultsVulnsFragment() {
        // Required empty public constructor
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootview = getActivity().getWindow().getDecorView().getRootView();
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        resultController = new ResultController(scanResults.getHosts());
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);

        PieChartCreator pieChartCreator = new PieChartCreator();


        PieChart vulnFamily = pieChartCreator.createChart(context, 1000, 1000);
        pieChartCreator.setChartConfig(ChartDescription.VULN_CATEGORY, false, LegendHeadings.VULN_CATEGORY, vulnFamily, resultController.getVulnFamily(), PieChartCreator.DEFAULT_MIXED);
        mainLayout.addView(vulnFamily);
        vulnText = UiObjectCreator.createTextView(R.id.vulnCategoryText, UiObjectCreator.txtMoreDetails, context, this);
        mainLayout.addView(vulnText);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, TableHeadings.HOST, TableHeadings.HIGH,
                TableHeadings.MEDIUM, TableHeadings.LOW);

        Map<String, ResultScoreMetrics> threatLevels = resultController.getVulnThreatLevelCount();

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);
        int[] gravity = {Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER};
        for (String host : threatLevels.keySet()) {
            ResultScoreMetrics threatLevelsCount = threatLevels.get(host);
            tableCreator.appendTableRow(context, mainLayout, customBoarder, gravity,
                    new TableRowData(host, this),
                    new TableRowData(String.valueOf(threatLevelsCount.getHighCount())),
                    new TableRowData(String.valueOf(threatLevelsCount.getLowCount())),
                    new TableRowData(String.valueOf(threatLevelsCount.getMedCount())));
        }
    }

    public static ResultsVulnsFragment newInstance(ScanResults scanResults) {
        ResultsVulnsFragment fragment = new ResultsVulnsFragment();
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
            throw new ClassCastException(activity.toString()+ "must implement FragmentHost");
        }
    }

    @Override
    public void onClick(View v) {
        Intent singleChartDisplay = new Intent(context, PieChartDetailsActivity.class);
        singleChartDisplay.putExtra("scan-results", scanResults);
        if (v instanceof TextView){
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
    */
}
