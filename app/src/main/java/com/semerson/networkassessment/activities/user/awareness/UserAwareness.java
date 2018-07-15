package com.semerson.networkassessment.activities.user.awareness;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.Results.FragmentName;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.activities.user.awareness.categories.AuthenticationAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.BusinessSecurityAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.CommunityAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.FirewallAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.MalwareAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.PhishingAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.PracticalAndTechnicalEducation;
import com.semerson.networkassessment.activities.user.awareness.categories.ReportCrime;
import com.semerson.networkassessment.activities.user.awareness.categories.SecurityNews;
import com.semerson.networkassessment.activities.user.awareness.categories.UnsecureConfigurationAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.UpdatesAwareness;
import com.semerson.networkassessment.activities.user.awareness.categories.WebAppAwareness;
import com.semerson.networkassessment.utils.BottomNavigationViewHelper;

public class UserAwareness extends BaseActivity implements View.OnClickListener, FragmentHost, BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private SecurityAwarenessHomeFragment securityAwarenessHomeFragment;
    private SecurityAwarenessHomeMoreFragment securityAwarenessHomeMoreFragment;
    private SecurityAwarenessQuizFragment securityAwarenessQuizFragment;
    private Fragment activeFragment;
    private int currentNavSelection = -1;
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
        securityAwarenessHomeMoreFragment = SecurityAwarenessHomeMoreFragment.newInstance();
        securityAwarenessQuizFragment = SecurityAwarenessQuizFragment.newInstance();

        Bundle bundle = getIntent().getExtras();
        Fragment fragment = null;
        if (bundle != null) {
            Integer awarenessPageId = bundle.getInt(getString(R.string.awarenessKey), -1);
            fragment = findFragment(awarenessPageId);
            if (fragment != null) {
                setFragment(fragment, false);
            }
        }
        if (fragment == null) {
            if (savedInstanceState == null) {
                setFragment(securityAwarenessHomeFragment, false);
            }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SecurityAwarenessQuizFragment.REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    @Override
    public void setFragment(Fragment fragment, boolean addToBackStack) {
        activeFragment = fragment;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void setFragment(Fragment fragment, boolean addToBackStack, int fragmentIdToReplace) {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == currentNavSelection && currentNavSelection != R.id.bottomNavAwarenessHome) {
            return false;
        }
        currentNavSelection = item.getItemId();
        removeBackStacks();
        switch (item.getItemId()) {
            case R.id.bottomNavAwarenessHome:
                setFragment(securityAwarenessHomeFragment, false);
                return true;
            case R.id.bottomNavAwarenessQuizes:
                setFragment(securityAwarenessQuizFragment, true);
                //setFragment(securityAwarenessQuizFragment, false);
                //Intent activity_quiz = new Intent(UserAwareness.this, QuizHome.class);
                //UserAwareness.this.startActivity(activity_quiz);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        //Set Home selected
        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        int selectedItem = bottomNavigation.getSelectedItemId();
        if (selectedItem == R.id.bottomNavAwarenessQuizes) {
            bottomNavigation.setSelectedItemId(R.id.bottomNavAwarenessHome);
            currentNavSelection = R.id.bottomNavAwarenessHome;
        }

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        if (v instanceof LinearLayout) {
            fragment = findFragment(v.getId());
        }
        if (fragment != null) {
            setFragment(fragment, true);
        }
    }

    private Fragment findFragment(int fragmentName) {
        Fragment fragment = null;
        switch (fragmentName) {
            case (R.id.nav_passwords):
                fragment = AuthenticationAwareness.newInstance();
                break;
            case (R.id.nav_firewall):
                fragment = FirewallAwareness.newInstance();
                break;
            case (R.id.nav_malware):
                fragment = MalwareAwareness.newInstance();
                break;
            case (R.id.nav_phishing):
                fragment = PhishingAwareness.newInstance();
                break;
            case (R.id.nav_unsecure_configuration):
                fragment = UnsecureConfigurationAwareness.newInstance();
                break;
            case (R.id.nav_updates):
                fragment = UpdatesAwareness.newInstance();
                break;
            case (R.id.nav_web):
                fragment = WebAppAwareness.newInstance();
                break;
            case (R.id.nav_more):
                setFragment(securityAwarenessHomeMoreFragment, true);
                break;
            case (R.id.nav_back):
                    setFragment(securityAwarenessHomeFragment, true);
                break;
            case (R.id.nav_business_sec):
                fragment = BusinessSecurityAwareness.newInstance();
                break;
            case (R.id.nav_sec_news):
                fragment = SecurityNews.newInstance();
                break;
            case (R.id.nav_sec_community):
                fragment = CommunityAwareness.newInstance();
                break;
            case (R.id.nav_practical):
                fragment = PracticalAndTechnicalEducation.newInstance();
                break;
            case (R.id.nav_report):
                fragment = ReportCrime.newInstance();
                break;

        }
        return fragment;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
