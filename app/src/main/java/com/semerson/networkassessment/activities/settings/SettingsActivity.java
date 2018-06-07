package com.semerson.networkassessment.activities.settings;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.semerson.networkassessment.activities.AccountActivity;
import com.semerson.networkassessment.activities.WelcomeActivity;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.storage.AppStorage;

import java.util.ArrayList;
import java.util.List;

import static com.semerson.networkassessment.storage.AppStorage.ADVANCED_MODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsActivity extends Activity implements View.OnClickListener {

    public static final int CONFIG_ADVANCED_VIEW = 0x0101;

    private CheckBox chkboxAdvancedView;
    private View rootview;
    private Context context;
    private FragmentHost fragmentHost;

    public SettingsActivity() {
        // Required empty public constructor
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_settings);

        Button buttonSave = (Button) findViewById(R.id.btnSaveConfig);
        buttonSave.setOnClickListener(this);

        // Inflate the layout for this fragment

        chkboxAdvancedView = (CheckBox) findViewById(R.id.advancedView);

        chkboxAdvancedView.setChecked(WelcomeActivity.ADVANCED_MODE);
    }

    @Override
    public void onClick(View v) {
        Log.d("ResultConfig", "Checking on Click");
        SharedPreferences preferences =  getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (v.getId()){
            case R.id.btnSaveConfig:
                editor.putBoolean(AppStorage.RESULTS_HOME_CUSTOM_CHARTS, true);
                Log.d("ResultConfig", "Save button clicked");
                List<Integer> checkboxesSelected = new ArrayList<>();

                if (chkboxAdvancedView.isChecked()) {
                    checkboxesSelected.add(CONFIG_ADVANCED_VIEW);
                    editor.putBoolean(AppStorage.ADVANCED_MODE, true);
                } else {
                    editor.putBoolean(AppStorage.ADVANCED_MODE, false);
                }
                editor.commit();
                Intent settingsActivity = new Intent(SettingsActivity.this, WelcomeActivity.class);
                SettingsActivity.this.startActivity(settingsActivity);
                break;
        }
    }
}
