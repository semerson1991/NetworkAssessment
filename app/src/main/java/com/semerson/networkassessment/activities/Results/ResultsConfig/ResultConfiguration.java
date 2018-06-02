package com.semerson.networkassessment.activities.Results.ResultsConfig;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.storage.AppStorage;

import java.util.ArrayList;
import java.util.List;

import static com.semerson.networkassessment.storage.AppStorage.ADVANCED_MODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultConfiguration extends Fragment implements View.OnClickListener {

    public static final int CONFIG_OS = 0x0001;
    public static final int CONFIG_VULN = 0x0010;
    public static final int CONFIG_THREAT = 0x0011;
    public static final int CONFIG_COMPLEXITY = 0x0100;
    public static final int CONFIG_ADVANCED_VIEW = 0x0101;

    private CheckBox chkboxOs;
    private CheckBox chkboxVulns;
    private CheckBox chkboxThreat;
    private CheckBox chkboxComplexity;
    private CheckBox chkboxAdvancedView;
    private View rootview;
    private Context context;
    private FragmentHost fragmentHost;

    public ResultConfiguration() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = getActivity().getWindow().getDecorView().getRootView();
        return inflater.inflate(R.layout.fragment_result_configuration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button buttonSave = (Button) view.findViewById(R.id.btnSaveConfig);
        buttonSave.setOnClickListener(this);

        // Inflate the layout for this fragment

        chkboxOs = (CheckBox) rootview.findViewById(R.id.configOperatingSystems);
        chkboxVulns = (CheckBox) rootview.findViewById(R.id.configVulnerabilities);
        chkboxThreat = (CheckBox) rootview.findViewById(R.id.configThreatLevel);
        chkboxComplexity = (CheckBox) rootview.findViewById(R.id.configAttackComplexity);
        chkboxAdvancedView = (CheckBox) rootview.findViewById(R.id.advancedView);

        SharedPreferences preferences =  getActivity().getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
        boolean osSelected = preferences.getBoolean(AppStorage.PIECHART_OS, false);
        boolean vulnSelected = preferences.getBoolean(AppStorage.PIECHART_VULN, false);
        boolean threatSelected = preferences.getBoolean(AppStorage.PIECHART_THREAT, false);
        boolean complexSelected = preferences.getBoolean(AppStorage.PIECHART_COMPLEX, false);
        boolean advancedMode = preferences.getBoolean(AppStorage.ADVANCED_MODE, false);

        chkboxOs.setChecked(osSelected);
        chkboxVulns.setChecked(vulnSelected);
        chkboxThreat.setChecked(threatSelected);
        chkboxComplexity.setChecked(complexSelected);
        chkboxAdvancedView.setChecked(advancedMode);
    }

    @Override
    public void onClick(View v) {
        Log.d("ResultConfig", "Checking on Click");
        SharedPreferences preferences =  getActivity().getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (v.getId()){
            case R.id.btnSaveConfig:
                editor.putBoolean(AppStorage.RESULTS_HOME_CUSTOM_CHARTS, true);
                Log.d("ResultConfig", "Save button clicked");
                List<Integer> checkboxesSelected = new ArrayList<>();
                if (chkboxOs.isChecked()){
                    checkboxesSelected.add(CONFIG_OS);
                    editor.putBoolean(AppStorage.PIECHART_OS, true);
                } else {
                    editor.putBoolean(AppStorage.PIECHART_OS, false);
                }

                if (chkboxVulns.isChecked()) {
                    checkboxesSelected.add(CONFIG_VULN);
                    editor.putBoolean(AppStorage.PIECHART_VULN, true);
                } else {
                    editor.putBoolean(AppStorage.PIECHART_VULN, false);
                }

                if (chkboxThreat.isChecked()) {
                    checkboxesSelected.add(CONFIG_THREAT);
                    editor.putBoolean(AppStorage.PIECHART_THREAT, true);
                } else {
                    editor.putBoolean(AppStorage.PIECHART_THREAT, false);
                }

                if (chkboxComplexity.isChecked()) {
                    checkboxesSelected.add(CONFIG_COMPLEXITY);
                    editor.putBoolean(AppStorage.PIECHART_COMPLEX, true);
                } else {
                    editor.putBoolean(AppStorage.PIECHART_COMPLEX, false);
                }

                if (chkboxAdvancedView.isChecked()) {
                    checkboxesSelected.add(CONFIG_ADVANCED_VIEW);
                    editor.putBoolean(AppStorage.ADVANCED_MODE, true);
                } else {
                    editor.putBoolean(AppStorage.ADVANCED_MODE, false);
                }
                editor.commit();
                getFragmentManager().popBackStackImmediate();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

        try {
            fragmentHost = (FragmentHost) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ "must implement FragmentHost");
        }
    }
}
