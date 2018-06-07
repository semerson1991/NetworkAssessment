package com.semerson.networkassessment.activities.Results;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.semerson.networkassessment.R;
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
import com.semerson.networkassessment.storage.results.Host;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.BottomNavigationViewHelper;

import java.util.List;

public class ResultsActivity extends AppCompatActivity implements View.OnClickListener, FragmentHost, BottomNavigationView.OnNavigationItemSelectedListener {
    private List<Host> hosts;
    private ScanResults scanResults;


    private final static String TAG = "ResultActivity";

    private BottomNavigationView bottomNavigationView;
    private RelativeLayout bottomLayout;

    //Fragment displayed within the results main page
    private OperatingSystems operatingSystemsFragment;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomLayout = (RelativeLayout) findViewById(R.id.youtubeLayout);
        bottomLayout.setVisibility(View.INVISIBLE);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scanResults = bundle.getParcelable("scan-results");
            hosts = scanResults.getHosts();
            ResultController resultController = new ResultController(hosts);
        }

        operatingSystemsFragment = OperatingSystems.newInstance(scanResults);
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
            setFragment(operatingSystemsFragment, true);
        }


        // Fragment fragment = YoutubeFragment.newInstance();
        // setFragment(fragment);

        //  YoutubeFragment youtubeFragment = (YoutubeFragment) fragment;

        // mainLayout.addView(bottomNavigationView);
        /* DONT DELETE - THIS IS AN EXAMPLE OF A HYPERLINK TO WEBSITE!!!
        TextView textView = new TextView(this);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.google.com'> More detail </a>";
        textView.setText(Html.fromHtml(text));
        mainLayout.addView(textView);
        */


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public Fragment getFragment(int fragmentID) {
        switch (fragmentID) {
            case FragmentName.RESULTS_HOME:
                return operatingSystemsFragment;
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
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    //TODO MOVE YOUTUBE TO AWARENESS SECTION - RESULTS WILL LINK TO CERTAIN SECTIONS.
    public void setYoutubeFragment(final String url) {
        bottomLayout.setVisibility(View.VISIBLE);
        YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.youtubeFragment);
        youtubeFragment.initialize("AIzaSyACYXSWMJ0Zedh_oNLSohyG5D8a2QV-dCw",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        // do any work here to cue video, play video, etc.
                        // youTubePlayer.cueVideo("5xVh-7ywKpE");
                        youTubePlayer.cueVideo(url);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottomNavResults:
                setFragment(operatingSystemsFragment, false);
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
            case R.id.radio_operating_system:
                //  toggleHomeRadioButtons(false);
                setFragment(operatingSystemsFragment, false);
                break;
            case R.id.radio_threat_levels:
                //  radioButton.
                setFragment(threatLevelsFragment, false);
                break;

            case R.id.radio_vulnerability_categories:
                //  toggleHomeRadioButtons(false);
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
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();

        }
    }
}
