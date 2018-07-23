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
public class AuthenticationAwareness extends Fragment {

    public static final String TITLE = "Passwords";

    private Context context;
    private FragmentHost fragmentHost;
    LinearLayout mainLayout;
    public AuthenticationAwareness() {
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
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.passwords_description_title), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.passwords_description), mainbodyText));

        //Discovery Methods
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.passwords_discovery_title), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.passwords_discovery), mainbodyText));

        //Tips
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_title), mainBodyTitle));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_1_title), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_1), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_2_title), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_2), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_3_title), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_3), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_4_title), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_4), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tips_5_title), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_tip_5), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_resources), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_url1_desc), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_url1), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_url2_desc), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.password_url2), mainbodyText));

        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.ncsc_password_poster);

    }

    public static AuthenticationAwareness newInstance() {
        AuthenticationAwareness fragment = new AuthenticationAwareness();
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
