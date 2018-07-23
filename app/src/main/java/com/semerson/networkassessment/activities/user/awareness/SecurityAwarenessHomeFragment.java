package com.semerson.networkassessment.activities.user.awareness;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.controller.FragmentHost;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecurityAwarenessHomeFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private FragmentHost fragmentHost;
    private LinearLayout mainLayout;

    private static final String title = "Security Awareness";

    public SecurityAwarenessHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security_awareness_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstancesState) {

        mainLayout = view.findViewById(R.id.mainLayout);
    }


    public static SecurityAwarenessHomeFragment newInstance() {
        SecurityAwarenessHomeFragment fragment = new SecurityAwarenessHomeFragment();
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

    @Override
    public void onClick(View v) {
    }
}
