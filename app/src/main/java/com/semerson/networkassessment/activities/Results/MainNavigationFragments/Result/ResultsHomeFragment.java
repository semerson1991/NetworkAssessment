package com.semerson.networkassessment.activities.Results.MainNavigationFragments.Result;


import android.content.Context;
import android.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.results.ResultController;
import com.semerson.networkassessment.results.ScanResults;

/**
 * A simple {@link Fragment} subclass.
 */

public class ResultsHomeFragment extends Fragment {

    private TextView osText;
    private TextView vulnText;
    private TextView threatLevelText;
    private TextView skillLevelText;
    private ScanResults scanResults;
    private ResultController resultController;

    private LinearLayout mainLayout;
    private Context context;

    private FragmentHost fragmentHost;
    public ResultsHomeFragment() {
        // Required empty public constructor
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        resultController = new ResultController(scanResults.getHosts());
        //View view = inflater.inflate(R.layout.activity_results, container, false);
        return inflater.inflate(R.layout.fragment_results_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        PieChartCreator pieChartCreator = new PieChartCreator();

        SharedPreferences preferences = context.getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
        boolean advancedMode = preferences.getBoolean(AppStorage.ADVANCED_MODE, false);
        boolean customCharts = preferences.getBoolean(AppStorage.RESULTS_HOME_CUSTOM_CHARTS, false);

        boolean createPieChartOs = true;
        boolean createPieChartVuln = true;
        boolean createPieChartThreat = true;
        boolean createPieChartComplexity = true;

        if (customCharts) {
            createPieChartOs = preferences.getBoolean(AppStorage.PIECHART_OS, false);
            createPieChartVuln = preferences.getBoolean(AppStorage.PIECHART_VULN, false);
            createPieChartThreat = preferences.getBoolean(AppStorage.PIECHART_THREAT, false);
            createPieChartComplexity = preferences.getBoolean(AppStorage.PIECHART_COMPLEX, false);
        }

        if (createPieChartOs) {
            PieChart chartOS = pieChartCreator.createChart(context, 1000, 1000);
            pieChartCreator.setChartConfig(ChartDescription.OS, false, LegendHeadings.OS, chartOS, resultController.getOperatingSystems(), PieChartCreator.DEFAULT_MIXED);
            mainLayout.addView(chartOS);
            osText = UiObjectCreator.createTextView(R.id.osText, UiObjectCreator.txtMoreDetails, context, this);
            mainLayout.addView(osText);
        }
        if (createPieChartVuln) {
            PieChart vulnFamily = pieChartCreator.createChart(context, 1000, 1000);
            pieChartCreator.setChartConfig(ChartDescription.VULN_CATEGORY, false, LegendHeadings.VULN_CATEGORY, vulnFamily, resultController.getVulnFamily(), PieChartCreator.DEFAULT_MIXED);
            mainLayout.addView(vulnFamily);
            vulnText = UiObjectCreator.createTextView(R.id.vulnCategoryText, "<a> More detail </a>", context, this);
            mainLayout.addView(vulnText);
        }
        if (createPieChartThreat) {
            PieChart threatLevel = pieChartCreator.createChart(context, 1000, 1000);
            pieChartCreator.setChartConfig(ChartDescription.THREAT_LEVEL, false, LegendHeadings.THREAT_LEVEL, threatLevel, resultController.getThreatLevel(), PieChartCreator.LOW_TO_CRITICAL);
            mainLayout.addView(threatLevel);
            threatLevelText = UiObjectCreator.createTextView(R.id.threatLevelText, "<a> More detail </a>", context, this);
            mainLayout.addView(threatLevelText);
        }
        if (createPieChartComplexity) {
            PieChart skillLevel = pieChartCreator.createChart(context, 1000, 1000);
            pieChartCreator.setChartConfig(ChartDescription.ATTACK_COMPLEXITY_LEVEL, false, LegendHeadings.ATTACK_COMPLEXITY_LEVEL, skillLevel, resultController.getAttackComplexityLevel(), PieChartCreator.LOW_TO_CRITICAL);
            mainLayout.addView(skillLevel);
            skillLevelText = UiObjectCreator.createTextView(R.id.attackComplexity, "<a> More detail </a>", context, this);
            mainLayout.addView(skillLevelText);
        }


        Button btnConfigureView = (Button) view.findViewById(R.id.btnConfigure);
        btnConfigureView.setOnClickListener(this);
    }

    public static ResultsHomeFragment newInstance(ScanResults scanResults) {
        ResultsHomeFragment fragment = new ResultsHomeFragment();
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
        switch (v.getId()) {
            case R.id.osText:
                singleChartDisplay.putExtra("scan-results", scanResults);
                singleChartDisplay.putExtra("chart", PieChartCreator.OS);
                startActivity(singleChartDisplay);
                break;
            case R.id.vulnCategoryText:
                singleChartDisplay.putExtra("scan-results", scanResults);
                singleChartDisplay.putExtra("chart", PieChartCreator.VULN_CATEGORY);
                startActivity(singleChartDisplay);
                break;
            default:
            case R.id.threatLevelText:
                singleChartDisplay.putExtra("scan-results", scanResults);
                singleChartDisplay.putExtra("chart", PieChartCreator.THREAT_LEVEL);
                startActivity(singleChartDisplay);
                break;
            case R.id.attackComplexity:
                singleChartDisplay.putExtra("scan-results", scanResults);
                singleChartDisplay.putExtra("chart", PieChartCreator.ATTACK_COMPLEXITY);
                startActivity(singleChartDisplay);
                break;
            case R.id.btnConfigure:
                getFragmentManager().beginTransaction().replace(R.id.mainFrame, new ResultConfiguration()).addToBackStack(null).commit();
                break;
        }
    }
    */
}
