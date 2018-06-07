package com.semerson.networkassessment.activities.user.awareness;


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
import com.semerson.networkassessment.activities.user.awareness.categories.AuthenticationAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.FirewallAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.PhishingAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.UnsecureSettingsAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.WebAppAwareness;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecurityAwarenessHomeFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private FragmentHost fragmentHost;
    private LinearLayout mainLayout;

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

        TextView textView1 = new TextView(context);
        textView1.setText("Passwords");
        textView1.setOnClickListener(this);

        TextView textViewPhishing = new TextView(context);
        textViewPhishing.setText("Phishing");
        textViewPhishing.setOnClickListener(this);

        TextView textViewWebApp = new TextView(context);
        textViewWebApp.setText("Web Application Security");
        textViewWebApp.setOnClickListener(this);

        TextView textViewSocialEngineering = new TextView(context);
        textViewSocialEngineering.setText("Social Engineering");
        textViewSocialEngineering.setOnClickListener(this);

        TextView textViewInformationExposure = new TextView(context);
        textViewInformationExposure.setText("Information Exposure");
        textViewInformationExposure.setOnClickListener(this);

        TextView textViewUpgradePatching = new TextView(context);
        textViewInformationExposure.setText("Importance of Upgrading and Patching");
        textViewInformationExposure.setOnClickListener(this);

        TextView textViewSecureConfig = new TextView(context);
        textViewSecureConfig.setText("Secure Configuration");
        textViewSecureConfig.setOnClickListener(this);

        TextView textViewFirewall = new TextView(context);
        textViewFirewall.setText("Firewall");
        textViewFirewall.setOnClickListener(this);

        mainLayout.addView(textView1);
        mainLayout.addView(textViewPhishing);
        mainLayout.addView(textViewWebApp);
        mainLayout.addView(textViewSocialEngineering);
        mainLayout.addView(textViewInformationExposure);
        mainLayout.addView(textViewUpgradePatching);
        mainLayout.addView(textViewSecureConfig);
        mainLayout.addView(textViewFirewall);
    }


    public static SecurityAwarenessHomeFragment newInstance() {
        SecurityAwarenessHomeFragment fragment = new SecurityAwarenessHomeFragment();
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

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            String text = tv.getText().toString();

            Fragment fragment = null;

            switch (text) {
                case ("Passwords"):
                    fragment = AuthenticationAwareness.newInstance();
                    break;
                case ("Phishing"):
                    fragment = PhishingAwareness.newInstance();
                    break;
                case ("Web Application Security"):
                    fragment = WebAppAwareness.newInstance();
                    break;
                case ("Secure Configuration"):
                    fragment = UnsecureSettingsAwareness.newInstance();
                    break;
                case ("Firewall"):
                    fragment = FirewallAwareness.newInstance();
                    break;

            }
            if (fragment != null) {
                fragmentHost.setFragment(fragment, true);
            }


        }

    }
}
