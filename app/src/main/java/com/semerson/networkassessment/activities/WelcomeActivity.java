package com.semerson.networkassessment.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.Results.ResultsActivity;
import com.semerson.networkassessment.activities.settings.SettingsActivity;
import com.semerson.networkassessment.activities.user.awareness.UserAwareness;
import com.semerson.networkassessment.storage.AppStorage;

public class WelcomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public static boolean ADVANCED_MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        TextView tv = (TextView) findViewById(R.id.mainLayout);
        tv.setText("- Line Graph of Previous Scans "+"\n " + "- Tip of The Day" + "\n" + "- Date of Last Vulnerability Scan");

        final EditText networkName = (EditText) findViewById(R.id.txtUsername);

        SharedPreferences preferences = getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
        ADVANCED_MODE = preferences.getBoolean(AppStorage.ADVANCED_MODE, false);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                break;
                            case R.id.nav_vulnerability_assessment:
                                Intent activity_vulnScanner = new Intent(WelcomeActivity.this, NetworkScanner.class);
                                WelcomeActivity.this.startActivity(activity_vulnScanner);
                                break;
                            case R.id.nav_assessment_results:
                                Intent resultActivity = new Intent(WelcomeActivity.this, ResultsActivity.class);
                                startActivity(resultActivity);
                                break;
                            case R.id.nav_sec_awareness:
                                Intent activity_secAwareness = new Intent(WelcomeActivity.this, UserAwareness.class);
                                WelcomeActivity.this.startActivity(activity_secAwareness);
                                break;
                            case R.id.nav_settings:
                                Intent activity_settings = new Intent(WelcomeActivity.this, SettingsActivity.class);
                                WelcomeActivity.this.startActivity(activity_settings);
                                break;
                            case R.id.nav_account:
                                Intent activity_account = new Intent(WelcomeActivity.this, AccountActivity.class);
                                WelcomeActivity.this.startActivity(activity_account);
                                break;
                            default:
                        }
                        return true;
                    }
                });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // final TextView networkNameSelected = (TextView) findViewById(R.id.tvNetworkNameDisplay);
        //networkNameSelected.setText(networkName.getText());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
