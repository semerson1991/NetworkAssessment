package com.semerson.networkassessment.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.Results.ResultsActivity;
import com.semerson.networkassessment.activities.authentication.AccountActivity;
import com.semerson.networkassessment.activities.network.NetworkDevices;
import com.semerson.networkassessment.activities.settings.SettingsActivity;
import com.semerson.networkassessment.activities.user.awareness.UserAwareness;
import com.semerson.networkassessment.storage.AppStorage;

public class BaseActivity extends AppCompatActivity {
    private static String TAG = "BaseActivity";

    private static int activeActivity = R.id.nav_home; //This is the welcome activity so will be launched first
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setTitle("Network Security & Cyber Awareness");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // Setting Navigation View Item Selected Listener to handle the item
        // click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            public boolean onNavigationItemSelected(MenuItem menuItem) {

                // Checking if the item is in checked state or not, if not make
                // it in checked state
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);

                // Closing drawer on item click
                drawerLayout.closeDrawers();

                // Check to see which item was being clicked and perform
                // appropriate action
                Intent intent;
                int menuItemID = menuItem.getItemId();
                if (activeActivity == menuItemID){
                    return false;
                }
                switch (menuItemID) {

                    case R.id.nav_home:
                        intent = new Intent(BaseActivity.this, WelcomeActivity.class);
                        break;
                    case R.id.nav_my_network:
                        intent = new Intent(BaseActivity.this, NetworkDevices.class);
                        break;
                    case R.id.nav_assessment_results:
                        intent = new Intent(BaseActivity.this, ResultsActivity.class);
                        break;
                    case R.id.nav_sec_awareness:
                        intent = new Intent(BaseActivity.this, UserAwareness.class);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(BaseActivity.this, SettingsActivity.class);
                        break;
                    case R.id.nav_account:
                        intent = new Intent(BaseActivity.this, AccountActivity.class);
                        break;
                    default:

                        return true;
                }
                if (intent != null) {
                    activeActivity = menuItemID;
                    BaseActivity.this.startActivity(intent);
                }
                return false;
            }
        });

        // Setting the actionbarToggle to drawer layout

        // calling sync state is necessay or else your hamburger icon wont show
        // up
        drawerToggle.syncState();

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNav();

        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Fires after the OnStop() state
    @Override
    protected void onDestroy() {
        Log.i(TAG, "Clearing Data");
        super.onDestroy();
        try {
           /* activeActivity = -1;
            AppStorage.removeValue(this, AppStorage.SCAN_RESULTS_TO_DISPLAY);
            AppStorage.removeValue(this, AppStorage.SCANNING_RESULTS);
            AppStorage.removeValue(this, AppStorage.LOGGED_IN);
            AppStorage.removeValue(this, AppStorage.DEVICE_CONNECTED);
            AppStorage.removeValue(this, AppStorage.NETWORK_NAME);*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
