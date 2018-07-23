package com.semerson.networkassessment.activities.settings;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.home.WelcomeActivity;
import com.semerson.networkassessment.controller.FragmentHost;
import com.semerson.networkassessment.storage.AppStorage;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "Settings Activity";

    public static final int CONFIG_ADVANCED_VIEW = 0x0101;

    private CheckBox chkboxAdvancedView;
    private View rootview;
    private Context context;
    private FragmentHost fragmentHost;
    private TextView warningText;

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

        warningText = findViewById(R.id.advancedConfigWarning);
        warningText.setVisibility(View.GONE);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        spannableStringBuilder.append(Html.fromHtml("<font color=\"black\"> <b>" + "Warning: " + "</b>"));
        spannableStringBuilder.append(Html.fromHtml("<font color=\"red\"> <b>" + getString(R.string.advanced_mode_warning) + "</b>"));
        spannableStringBuilder.append(System.getProperty("line.separator"));
        warningText.setText(spannableStringBuilder);

        chkboxAdvancedView.setChecked(AppStorage.getValue(this, AppStorage.ADVANCED_MODE, false));
        chkboxAdvancedView.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.getId() == R.id.advancedView) {
                            if (isChecked) {
                                warningText.setVisibility(View.VISIBLE);
                            } else {
                                warningText.setVisibility(View.GONE);
                            }
                        }
                    }});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveConfig:
                if (chkboxAdvancedView.isChecked()) {
                    Log.i(TAG, "Advanced Mode = True");
                    AppStorage.putValue(this, AppStorage.ADVANCED_MODE, true);
                } else {
                    Log.i(TAG, "Advanced Mode = False");
                    AppStorage.putValue(this, AppStorage.ADVANCED_MODE, false);
                }
                Intent settingsActivity = new Intent(SettingsActivity.this, WelcomeActivity.class);
                SettingsActivity.this.startActivity(settingsActivity);
                break;
        }
    }
}
