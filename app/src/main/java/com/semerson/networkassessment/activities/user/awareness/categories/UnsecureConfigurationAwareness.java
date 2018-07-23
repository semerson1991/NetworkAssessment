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
public class UnsecureConfigurationAwareness extends Fragment {

    public static final String TITLE = "Unsecure Configuration";

    private Context context;
    private FragmentHost fragmentHost;
    private LinearLayout mainLayout;

    public UnsecureConfigurationAwareness() {
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
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_description_title), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_description), mainbodyText));

        //Check Settings
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_check_settings_title), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_check_settings), mainbodyText));

        //Passwords
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_use_passwords_title), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_use_passwords), mainbodyText));

        //Extra Security
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_extra_security_title), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_extra_security), mainbodyText));

        //URL's
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_resources), mainBodyTitle));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_url1_description), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_url1), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_url2_description), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.unsecure_settings_url2), mainbodyText));

        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        photoView.setVisibility(View.GONE);
    }


    public static UnsecureConfigurationAwareness newInstance() {
        UnsecureConfigurationAwareness fragment = new UnsecureConfigurationAwareness();
        Bundle bundle = new Bundle();
        //bundle.putParcelable("scan-results", scanResults);
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
