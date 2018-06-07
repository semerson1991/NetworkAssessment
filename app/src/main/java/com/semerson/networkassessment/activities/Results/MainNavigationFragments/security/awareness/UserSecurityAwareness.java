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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSecurityAwareness extends Fragment implements View.OnClickListener{


    private static final String TITLE = "Recommended Areas of Improvement";
    private Context context;
    private ScanResults scanResults;
    private ResultController resultController;
    private LinearLayout mainLayout;

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
        TextView vulnCategoryStatistics = view.findViewById(R.id.txtStatistics);
        TextView textViewTitle = view.findViewById(R.id.secure_awareness_title);
        textViewTitle.setText(TITLE);

        List<VulnerabilityResult> allVulnerabilties = resultController.getAllVulnerabilities();
        Integer totalVulnerabilities = allVulnerabilties.size();

        String newline = System.getProperty("line.separator");
        String newParagraph = System.getProperty("line.separator") + " " + System.getProperty("line.separator");


        List<ResultController.VulnerabilityCategoryOccrences> topVulnerabilityCategories = resultController.getTopVulnerabilityCategories(3);


        StringBuilder resultCategoriesStatistics = new StringBuilder();
        resultCategoriesStatistics.append("Total vulnerabilities detected: "+ totalVulnerabilities.toString() + newParagraph);
        resultCategoriesStatistics.append("Top categories: " + newline);
        for (ResultController.VulnerabilityCategoryOccrences vulnerabilityCategoryOccrences : topVulnerabilityCategories){
            resultCategoriesStatistics.append(vulnerabilityCategoryOccrences.getVulnerabilityFamily() + ": "+resultController.getCategoryOccurrenceAsPercent(vulnerabilityCategoryOccrences.getVulnerabilityFamily()) + " of vulnerabilties." +  newline);
        }

        resultCategoriesStatistics.append(newParagraph + newParagraph);
        resultCategoriesStatistics.append("It is highly recommended to check the following security awarenes sections: "+ newParagraph);
        resultCategoriesStatistics.append("<section> "+ newParagraph);
        resultCategoriesStatistics.append("<section> "+ newParagraph);
        resultCategoriesStatistics.append("<section> "+ newParagraph);

        resultCategoriesStatistics.append(newParagraph);
        resultCategoriesStatistics.append("Please complete the following quiz: "+ newParagraph);
        resultCategoriesStatistics.append("<quiz> "+ newParagraph);

        vulnCategoryStatistics.setText(resultCategoriesStatistics.toString());
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
