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
public class VideoAwareness extends Fragment {

    public static final String TITLE = "Security Awareness Videos";

    private Context context;
    private FragmentHost fragmentHost;
    LinearLayout mainLayout;

    public VideoAwareness() {
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
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_description_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_description), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_1_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_1_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_1), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_2_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_2_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_2), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_3_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_3_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_3), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_4_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_4_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_4), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_5_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_5_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_5), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_6_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_6_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_6), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_7_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_7_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_7), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_8_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_8_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_8), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_9_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_9_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_9), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_10_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_10_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_10), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_11_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_11_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_11), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_12_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_12_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_12), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_13_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_13_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_13), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_14_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_14_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_14), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_15_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_15_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(context, getString(R.string.videos_resource_15), mainbodyText));

        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        photoView.setVisibility(View.GONE);

    }

    public static VideoAwareness newInstance() {
        VideoAwareness fragment = new VideoAwareness();
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
