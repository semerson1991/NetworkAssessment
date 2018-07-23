package com.semerson.networkassessment.activities.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.account.AccountActivity;
import com.semerson.networkassessment.activities.account.LoginActivity;
import com.semerson.networkassessment.activities.account.NetworkConfiguration;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.utils.UiObjectCreator;

public class AboutActivity extends BaseActivity {

    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mainLayout = findViewById(R.id.mainLayout);

        int mainbodyText = R.style.custom_mainbody_text;
        int mainBodyTitle = R.style.custom_mainbody_heading_centered;
        int mainBodyTitleMedium = R.style.custom_mainbody_heading_medium;
        int pageTitle = R.style.custom_page_title;

        //Description
        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_description), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_resource_1_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_resource_1_desc), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_resource_2_desc_heading), mainBodyTitle));
        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_resource_2_desc), mainbodyText));

        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_resource_3_desc_heading), mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_resource_3_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_resource_3), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_resource_3_1_desc), mainbodyText));
        mainLayout.addView(UiObjectCreator.createTextView(this, getString(R.string.about_resource_3_2), mainbodyText));

    }
}
