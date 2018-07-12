package com.semerson.networkassessment.activities.Results.MainNavigationFragments.security.awareness;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.storage.results.VulnerabilityResult;
import com.semerson.networkassessment.utils.UiObjectCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSecurityAwareness extends Fragment implements View.OnClickListener {


    private static final String TITLE = "Recommended Areas of Improvement";
    private Context context;
    private ScanResults scanResults;
    private ResultController resultController;
    private LinearLayout mainLayout;

    private LinearLayout layoutFirewall;
    private LinearLayout layoutMalware;
    private LinearLayout layoutPassword;
    private LinearLayout layoutPhishing;
    private LinearLayout layoutUnsecureConfig;
    private LinearLayout layoutUpdates;
    private LinearLayout layoutWeb;
    private LinearLayout layoutMore;

    private FragmentHost fragmentHost;

    public UserSecurityAwareness() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scanResults = (ScanResults) getArguments().getParcelable("scan-results");
        resultController = new ResultController(scanResults.getHosts());

        return inflater.inflate(R.layout.fragment_user_security_awareness, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainLayout = view.findViewById(R.id.mainLayout);
        TextView textViewTitle = view.findViewById(R.id.secure_awareness_title);
        textViewTitle.setText(TITLE);

        layoutFirewall = view.findViewById(R.id.nav_firewall);
        layoutMalware = view.findViewById(R.id.nav_malware);
        layoutPassword = view.findViewById(R.id.nav_passwords);
        layoutPhishing = view.findViewById(R.id.nav_phishing);
        layoutUnsecureConfig = view.findViewById(R.id.nav_unsecure_configuration);
        layoutUpdates = view.findViewById(R.id.nav_updates);
        layoutWeb = view.findViewById(R.id.nav_web);
        layoutMore = view.findViewById(R.id.secure_awareness_categories_more);

        int mainbodyText = R.style.custom_mainbody_text;
        int mainBodyTitle = R.style.custom_mainbody_heading_centered;
        int mainBodyTitleMedium = R.style.custom_mainbody_heading_medium;
        int pageTitle = R.style.custom_page_title;

        hideAwarenessImages();

        List<VulnerabilityResult> allVulnerabilties = resultController.getAllVulnerabilities();

        List<ResultController.VulnerabilityCategoryOccurrences> topVulnerabilityCategories = resultController.getTopVulnerabilityCategories(3);

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.total_vulns_title, String.valueOf(allVulnerabilties.size())), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.top_categories_title), mainBodyTitleMedium));

        List<String> vulnerabilityCategories = new ArrayList<>();
        for (ResultController.VulnerabilityCategoryOccurrences vulnerabilityCategoryOccurrences : topVulnerabilityCategories) {
            String vulnerabilityFamily = vulnerabilityCategoryOccurrences.getVulnerabilityFamily();
            mainLayout.addView(UiObjectCreator.createTextView(
                    context,
                    getString(R.string.top_vulnerability_category, vulnerabilityCategoryOccurrences.getVulnerabilityFamily(),
                            resultController.getCategoryOccurrenceAsPercent(vulnerabilityFamily)),
                    mainbodyText));
            vulnerabilityCategories.add(vulnerabilityFamily);
        }


        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.recommended_categories), mainBodyTitleMedium));
        for (String vulnerabilityCategory : vulnerabilityCategories) {
            displayAwarenessImage(vulnerabilityCategory);
        }

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.results_recommended_quiz), mainBodyTitleMedium));
    }

    private void displayAwarenessImage(String vulnCategory) {
        final String webApp = "Web application abuses";
        final String weakAuth = "Weak Authentication";
        final String obsoletePlateform = "Obsolete platform";
        final String unsecureSetting = "Unsecure Setting";
        final String serviceDetection = "Service detection";
        final String informationExposure = "Information Exposure";

        switch (vulnCategory) {
            case webApp:
                layoutWeb.setVisibility(View.VISIBLE);
                break;
            case weakAuth:
                layoutPassword.setVisibility(View.VISIBLE);
                break;
            case obsoletePlateform:
                layoutUpdates.setVisibility(View.VISIBLE);
                break;
            case unsecureSetting:
            case serviceDetection:
            case informationExposure:
                layoutUnsecureConfig.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void hideAwarenessImages() {
        layoutFirewall.setVisibility(View.GONE);
        layoutMalware.setVisibility(View.GONE);
        layoutPassword.setVisibility(View.GONE);
        layoutPhishing.setVisibility(View.GONE);
        layoutUnsecureConfig.setVisibility(View.GONE);
        layoutUpdates.setVisibility(View.GONE);
        layoutWeb.setVisibility(View.GONE);
        layoutMore.setVisibility(View.GONE);
    }

    public static UserSecurityAwareness newInstance(ScanResults scanResults) {
        UserSecurityAwareness fragment = new UserSecurityAwareness();
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {

        }
    }
}
