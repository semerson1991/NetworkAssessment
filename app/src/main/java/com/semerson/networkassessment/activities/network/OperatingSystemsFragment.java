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
import com.semerson.networkassessment.activities.Results.Chart.ChartDescription;
import com.semerson.networkassessment.activities.Results.Chart.LegendHeadings;
import com.semerson.networkassessment.activities.Results.Chart.PieChartCreator;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.activities.user.awareness.UserAwareness;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class OperatingSystemsFragment extends Fragment implements View.OnClickListener {

    private LinearLayout mainLayout;
    private NetworkDevices networkDevices;
    private Context context;
    private FragmentHost fragmentHost; //The activity hosting the fragment
    private ScanResults scanResults;
    private ResultController resultController;

    public OperatingSystemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scanResults = AppStorage.getScanResults(context);
        resultController = new ResultController(scanResults.getHosts());
        return inflater.inflate(R.layout.fragment_operating_systems, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        TextView obsoleteOs = view.findViewById(R.id.warningText);

        ScrollView scrollView = getActivity().findViewById(R.id.scrollView);
        scrollView.scrollTo(0,0);

        List<String> obsoleteOperatingSystems = resultController.getUnsupportOperatingSystems();

        if (!obsoleteOperatingSystems.isEmpty()) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            obsoleteOs.setVisibility(View.VISIBLE);

            spannableStringBuilder.append(Html.fromHtml("<font color=\"red\"> <b>" + getString(R.string.obsolete_operating_systems_detected) + "</b>"));
            spannableStringBuilder.append(System.getProperty("line.separator"));
            for (String operatingSystem : obsoleteOperatingSystems) {
                spannableStringBuilder.append(System.getProperty("line.separator"));
                spannableStringBuilder.append("- " + operatingSystem);
            }
            SpannableString updatesAndPatching = new SpannableString(Html.fromHtml(" <u> 'Updates and Patching' </u>"));
            ClickableSpan clickableText = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent awarenessActivity = new Intent(context, UserAwareness.class);
                    awarenessActivity.putExtra(getString(R.string.awarenessKey), R.id.nav_updates);
                    startActivity(awarenessActivity);
                }
            };
            updatesAndPatching.setSpan(clickableText, 0, updatesAndPatching.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            updatesAndPatching.setSpan(new ForegroundColorSpan(Color.BLUE), 0, updatesAndPatching.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableStringBuilder.append(System.getProperty("line.separator"));
            spannableStringBuilder.append(System.getProperty("line.separator"));
            spannableStringBuilder.append("View our security awareness category ");
            spannableStringBuilder.append(updatesAndPatching);
            spannableStringBuilder.append("for information on the dangers, and solutions.");
            obsoleteOs.setText(spannableStringBuilder);
            obsoleteOs.setMovementMethod(LinkMovementMethod.getInstance());
            //   obsoleteOs.setText(updatesAndPatching);
        }

        PieChartCreator pieChartCreator = new PieChartCreator();

        PieChart chart = pieChartCreator.createChart(context, 1000, 1000);
        Map<String, Float> operatingSystems = resultController.getOperatingSystems();
        pieChartCreator.setChartConfig(ChartDescription.OS, false, LegendHeadings.OS, chart, operatingSystems, PieChartCreator.DEFAULT_MIXED);

        mainLayout.addView(chart);
        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        TableCreator tableCreator = new TableCreator();
        tableCreator.appendTableHeader(context, mainLayout, LegendHeadings.OS,
                LegendHeadings.TOTAL_OCCURRENCES, LegendHeadings.OCCURRENCES_AS_PERCENT);

        Table table = pieChartCreator.prepareTableLegendForPieChart(chart, this);
        table.setSortedHighToLow(true);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);

        // txtMoreDetails = UiObjectCreator.createTextView(R.id.osText, UiObjectCreator.txtMoreDetails, context, this);
        // mainLayout.addView(txtMoreDetails);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rowListener) {
            Object operatingSystem = v.getTag();
            if (operatingSystem != null) {
                if (operatingSystem instanceof String) {
                    Fragment fragment = HostsFilterByOSFragment.newInstance((String) operatingSystem);
                    fragmentHost.setFragment(fragment, true);
                }
            }
        }
        if (v.getId() == R.id.updates_and_patching_hyperlink) {
            Intent awarenessActivity = new Intent(context, UserAwareness.class);
            awarenessActivity.putExtra(getString(R.string.awarenessKey), "nav_updates");
            startActivity(awarenessActivity);
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
