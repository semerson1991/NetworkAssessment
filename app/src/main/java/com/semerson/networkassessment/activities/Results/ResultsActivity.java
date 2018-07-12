package com.semerson.networkassessment.activities.Results;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.AllVulnerabilities;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.AttackComplexity;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.OperatingSystems;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.ThreatLevels;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.VulnerabilityCategories;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.VulnerableHosts;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.impact.AvailabilityFragment;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.impact.ConfidentialityFragment;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.impact.IntegrityFragment;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.mitigation.ResultsAllMitigationsFragment;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.mitigation.ResultsMitigationCategoriesFragment;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.security.awareness.UserSecurityAwareness;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.activities.user.awareness.UserAwareness;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.storage.results.Host;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.BottomNavigationViewHelper;

import java.util.List;

public class ResultsActivity extends BaseActivity implements View.OnClickListener, FragmentHost, BottomNavigationView.OnNavigationItemSelectedListener {
    private List<Host> hosts;
    private ScanResults scanResults;


    private final static String TAG = "ResultActivity";

    private BottomNavigationView bottomNavigationView;
    private RelativeLayout bottomLayout;

    //Fragment displayed within the results main page
    //private OperatingSystems operatingSystemsFragment;
    private VulnerabilityCategories vulnerabilityCategoriesFragment;
    private ThreatLevels threatLevelsFragment;
    private AttackComplexity attackComplexityFragment;
    private VulnerableHosts vulnerableHostsFragment;
    private AllVulnerabilities allVulnerabilitiesFragment;

    private UserSecurityAwareness userAwarenessFragmentFragment;

    //Fragment for the Impacts page
    private AvailabilityFragment availabilityFragment;
    private IntegrityFragment integrityFragment;
    private ConfidentialityFragment confidentialityFragment;

    //Fragments for the mitigations section
    private ResultsMitigationCategoriesFragment mitigationCategoriesFragment;
    private ResultsAllMitigationsFragment mitigationFragment;

    private RadioButton radioOperatingSystems;
    private RadioButton radioThreatLevels;
    private RadioButton radioVulerabilityCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        scanResults = AppStorage.getScanResultsToDisplay(this);
        hosts = scanResults.getHosts();

        //operatingSystemsFragment = OperatingSystems.newInstance(scanResults);
        threatLevelsFragment = ThreatLevels.newInstance(scanResults);
        vulnerabilityCategoriesFragment = VulnerabilityCategories.newInstance(scanResults);
        attackComplexityFragment = AttackComplexity.newInstance(scanResults);
        vulnerableHostsFragment = VulnerableHosts.newInstance(scanResults);
        allVulnerabilitiesFragment = AllVulnerabilities.newInstance(scanResults);

        availabilityFragment = AvailabilityFragment.newInstance(scanResults);
        integrityFragment = IntegrityFragment.newInstance(scanResults);
        confidentialityFragment = ConfidentialityFragment.newInstance(scanResults);

        mitigationCategoriesFragment = ResultsMitigationCategoriesFragment.newInstance(scanResults);
        mitigationFragment = ResultsAllMitigationsFragment.newInstance(scanResults);

        userAwarenessFragmentFragment = UserSecurityAwareness.newInstance(scanResults);

        if (savedInstanceState == null) {
            setFragment(vulnerableHostsFragment, false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) v;
            switch (layout.getId()) {
                case R.id.nav_passwords:
                case R.id.nav_firewall:
                case R.id.nav_malware:
                case R.id.nav_phishing:
                case R.id.nav_unsecure_configuration:
                case R.id.nav_updates:
                case R.id.nav_web:
                    Intent awarenessActivity = new Intent(ResultsActivity.this, UserAwareness.class);
                    awarenessActivity.putExtra(getString(R.string.awarenessKey), v.getId());
                    startActivity(awarenessActivity);
                    break;
            }
        }
    }

    @Override
    public Fragment getFragment(int fragmentID) {
        switch (fragmentID) {
            case FragmentName.RESULTS_HOME:
                return vulnerableHostsFragment;
            case FragmentName.RESULTS_VULNS:
                return userAwarenessFragmentFragment;
            case FragmentName.RESULTS_IMPACT:
                return confidentialityFragment;
            case FragmentName.RESULTS_MITIGATION:
                return mitigationCategoriesFragment;
        }
        return null;
    }

    @Override
    public void setFragment(Fragment fragment, boolean addToBackStack) {
        setFragment(fragment, addToBackStack, R.id.mainFrame);
    }

    @Override
    public void setFragment(Fragment fragment, boolean addToBackStack, int fragmentIdToReplace) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragmentIdToReplace, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        removeBackStacks();
        switch (item.getItemId()) {
            case R.id.bottomNavResults:
                setFragment(vulnerableHostsFragment, false);
                return true;
            case R.id.bottomSecurityAwareness:
                setFragment(userAwarenessFragmentFragment, false);
                return true;
            case R.id.bottomNavImpactScope:
                setFragment(confidentialityFragment, false);
                return true;
            case R.id.bottomNavMitigation:
                setFragment(mitigationFragment, false);
                return true;
            default:
                return false;
        }
    }

    public void showImpactText(View view) {
        switch (view.getId()) {
            case R.id.impact_availability_help:
            case R.id.impact_integrity_help:
            case R.id.impact_confidentiality_help:
                TextView textView = findViewById(R.id.txtDescription);
                if (textView.getVisibility() == View.VISIBLE) {
                    textView.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        RadioButton radioButton = (RadioButton) view;

        switch (view.getId()) {
            case R.id.radio_confidentiality:
                if (checked) {
                    setFragment(confidentialityFragment, false);
                    break;
                }
                break;
            case R.id.radio_availability:
                if (checked) {
                    setFragment(availabilityFragment, false);
                    break;
                }
                break;
            case R.id.radio_integrity:
                if (checked) {
                    setFragment(integrityFragment, false);
                    break;
                }
            case R.id.radio_threat_levels:
                setFragment(threatLevelsFragment, false);
                break;

            case R.id.radio_vulnerability_categories:
                setFragment(vulnerabilityCategoriesFragment, false);
                break;
            case R.id.radio_attack_complexity:
                setFragment(attackComplexityFragment, false);
                break;
            case R.id.radio_vulnerable_hosts:
                setFragment(vulnerableHostsFragment, false);
                break;
            case R.id.radio_all_vulnerabilities:
                setFragment(allVulnerabilitiesFragment, false);
                break;
            case R.id.radio_all_mitigations:
                setFragment(mitigationFragment, false);
                break;
            case R.id.radio_mitigation_categories:
                setFragment(mitigationCategoriesFragment, false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("FragmentList", getSupportFragmentManager().getFragments().toString());
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.d("OnBackPressed", "Popping stack");
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();

        }


    }
}
