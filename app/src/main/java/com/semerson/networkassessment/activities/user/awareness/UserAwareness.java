package com.semerson.networkassessment.activities.user.awareness;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.Results.FragmentName;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.OperatingSystems;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.ThreatLevels;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.VulnerabilityCategories;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.utils.BottomNavigationViewHelper;

public class UserAwareness extends AppCompatActivity implements View.OnClickListener, FragmentHost, BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private SecurityAwarenessHomeFragment securityAwarenessHomeFragment;
    private SecurityAwarenessQuizFragment securityAwarenessQuizFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_awareness);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        securityAwarenessHomeFragment = SecurityAwarenessHomeFragment.newInstance();
        securityAwarenessQuizFragment = SecurityAwarenessQuizFragment.newInstance();

        if (savedInstanceState == null) {
            setFragment(securityAwarenessHomeFragment, true);
        }
    }

    @Override
    public Fragment getFragment(int fragmentID) {
        switch (fragmentID) {
            case FragmentName.AWARENESS_HOME:
                return securityAwarenessHomeFragment;
            case FragmentName.AWARENESS_QUIZ:
                return securityAwarenessQuizFragment;
        }
        return null;
    }

    @Override
    public void setFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottomNavAwarenessHome:
                setFragment(securityAwarenessHomeFragment, false);
                return true;
            case R.id.bottomNavAwarenessQuizes:
                setFragment(securityAwarenessQuizFragment, false);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
