package com.semerson.networkassessment.activities.user.awareness.categories;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.controller.FragmentHost;
import com.semerson.networkassessment.utils.UiObjectCreator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportCrime extends Fragment {


    public static final String TITLE = "Reporting Cyber Crime & Fraud";

    private Context context;
    private FragmentHost fragmentHost;
    private LinearLayout mainLayout;

    public ReportCrime() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_awareness, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstancesState) {
        mainLayout = view.findViewById(R.id.mainLayout);

        int mainbodyText = R.style.custom_mainbody_text;
        int mainBodyTitle = R.style.custom_mainbody_heading_centered;
        int mainBodyTitleMedium = R.style.custom_mainbody_heading_medium;
        int pageTitle = R.style.custom_page_title;

        TextView textViewTitle = view.findViewById(R.id.secure_awareness_title);
        textViewTitle.setText(TITLE);

        //Description
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.report_risk_description_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.report_risk_description), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.report_risk_resource_1_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.report_risk_resource_1_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.report_risk_resource_1), mainbodyText));

        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        photoView.setVisibility(View.GONE);
    }


    public static ReportCrime newInstance() {
        ReportCrime fragment = new ReportCrime();
        Bundle bundle = new Bundle();
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
}