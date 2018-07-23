package com.semerson.networkassessment.activities.network;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.results.Chart.PieChartCreator;
import com.semerson.networkassessment.controller.FragmentHost;
import com.semerson.networkassessment.activities.user.awareness.UserAwareness;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private FragmentHost fragmentHost; //The activity hosting the fragment
    private ScanResults scanResults;
    private ResultController resultController;

    private NetworkDevices networkDevices;
    private LinearLayout mainLayout;

    public ServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        scanResults = AppStorage.getScanResults(context);
        resultController = new ResultController(scanResults.getHosts());
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);

        ScrollView scrollView = getActivity().findViewById(R.id.scrollView);
        scrollView.scrollTo(0,0);


        TextView tvInfo = view.findViewById(R.id.info);
        tvInfo.setVisibility(View.VISIBLE);
        SpannableStringBuilder spannableStringBuilderInfo = new SpannableStringBuilder();
        spannableStringBuilderInfo.append(new SpannableString(Html.fromHtml(" <b> Reminder: </b>")));
        spannableStringBuilderInfo.append("Running unnecessary services can be a danger to your network. See our ");
        SpannableString unsecureConfig = new SpannableString(Html.fromHtml(" <u> 'Unsecure Configuration' </u>"));
        ClickableSpan clickableText = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent awarenessActivity = new Intent(context, UserAwareness.class);
                awarenessActivity.putExtra(getString(R.string.awarenessKey), R.id.nav_unsecure_configuration);
                startActivity(awarenessActivity);
            }
        };
        unsecureConfig.setSpan(clickableText, 0, unsecureConfig.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        unsecureConfig.setSpan(new ForegroundColorSpan(Color.BLUE), 0, unsecureConfig.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilderInfo.append(unsecureConfig);
        spannableStringBuilderInfo.append("for more information");
        tvInfo.setText(spannableStringBuilderInfo);
        tvInfo.setMovementMethod(LinkMovementMethod.getInstance());

        PieChartCreator pieChartCreator = new PieChartCreator();

        PieChart chart = pieChartCreator.createChart(context, 1000, 1000);
        Map<String, Float> services = resultController.getServices(AppStorage.getValue(networkDevices, AppStorage.ADVANCED_MODE, false));
        pieChartCreator.setChartConfig(ChartDescription.SERVICES, false, LegendHeadings.SERVICES, chart, services, PieChartCreator.DEFAULT_MIXED);

        mainLayout.addView(chart);
        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, LegendHeadings.SERVICES,
                LegendHeadings.TOTAL_OCCURRENCES, LegendHeadings.OCCURRENCES_AS_PERCENT);

        Table table = pieChartCreator.prepareTableLegendForPieChart(chart, this);
        table.setSortedHighToLow(true);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rowListener) {
            Object service = v.getTag();
            if (service != null) {
                if (service instanceof String) {
                    Fragment fragment = HostsFilterByServiceFragment.newInstance((String) service);
                    fragmentHost.setFragment(fragment, true);
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        try {
            fragmentHost = (FragmentHost) context;
            if (context instanceof NetworkDevices) {
                this.networkDevices = (NetworkDevices) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FragmentHost");
        }
    }
}
