package com.semerson.networkassessment.activities.network;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.Results.ResultsActivity;
import com.semerson.networkassessment.testdata.TestData;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.DynamicUI;
import com.semerson.networkassessment.activities.Results.FragmentName;
import com.semerson.networkassessment.activities.WelcomeActivity;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.storage.results.Host;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.utils.BottomNavigationViewHelper;
import com.semerson.networkassessment.utils.ProcessHttpResponse;
import com.semerson.networkassessment.utils.RequestBuilder;
import com.semerson.networkassessment.service.ServerCommunicationService;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.UiObjectCreator;

import java.util.ArrayList;
import java.util.List;


public class NetworkDevices extends BaseActivity implements RequestBuilder, ProcessHttpResponse, View.OnClickListener, FragmentHost, BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "NetworkDevices";

    public static final String SERVER_ACTION_NMAP_STARTED = "nmap_scan_started";
    public static final String SERVER_ACTION_OPENVAS_STARTED = "openvas_scan_started";
    public static final String SERVER_ACTION_SCAN_STATUS = "scan-status";
    public static final String SERVER_ACTION_RESULTS_COLLECTED = "results-collected";

    public static boolean activityActive = false;

    private BottomNavigationView bottomNavigationView;
    private String scanType = "high"; //TODO CHANGE

    private ResultController resultController;

    private List<Host> hosts;
    private ScanResults scanResults;

    private OperatingSystemsFragment operatingSystemsFragment;
    private NetworkDevicesFragment networkDevicesFragment;
    private ServicesFragment servicesFragment;
    private SingleNetworkDeviceFragment singleNetworkDeviceFragment;

    private Fragment activeFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_scanner);

        if (AppStorage.getValue(this, AppStorage.DEVICE_CONNECTED, false)) {
            scanResults = AppStorage.getScanResults(this);
            hosts = scanResults.getHosts();
            resultController = new ResultController(hosts);

            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
            BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);

            servicesFragment = new ServicesFragment();
            operatingSystemsFragment = new OperatingSystemsFragment();
            networkDevicesFragment = new NetworkDevicesFragment();
            if (savedInstanceState == null) {
                Log.i(TAG, "Saved Instance State Null");
                setFragment(networkDevicesFragment, false);
            } else {
                Log.i(TAG, "Saved Instance State True");
            }
        } else {
            LinearLayout headerView = findViewById(R.id.headerOpsView);
            headerView.addView(UiObjectCreator.createNetworkRequiredTextView(this));
        }
    }


    public void performVulnerabilityScan(Host... host) {
        AppStorage.putValue(this, AppStorage.ACTIVITY_REQUESTING_SERVER, ServerCommunicationService.NETWORK_DEVICES_ACTIVITY);
        ArrayList<String> hostsToScan = new ArrayList<>();
        for (Host hostToScan : host) {
            hostsToScan.add(hostToScan.getIp());
        }
        AppStorage.putValue(this, AppStorage.CURRENT_SCAN, AppStorage.OPENVAS_SCAN);
        AppStorage.putValue(this, AppStorage.HOSTS_BEING_VULN_SCANNED, hostsToScan);
        if (WelcomeActivity.OPENVAS_TEST_MODE) { //TODO Remove
            processResponse(TestData.getVulnerabilityTestData(this));
        } else {
            AppStorage.putValue(this, AppStorage.SERVER_ACTION, AppStorage.OPENVAS_SCAN_REQUEST);
            final ServerCommunicationService requester = new ServerCommunicationService(NetworkDevices.this);
            requester.execute(ServerCommunicationService.URL_RUN_VULNERABILITY_SCAN);
        }
    }

    public void displayResults(Host host) {
        ScanResults scanResultsToDisplay = new ScanResults();
        ScanResults allScanResults = AppStorage.getScanResults(this);

        Host hostToDisplay = allScanResults.getHost(host.getIp());
        scanResultsToDisplay.appendHost(hostToDisplay);
        AppStorage.storeScanResultsToDisplay(this, scanResultsToDisplay);

        Intent activity_settings = new Intent(NetworkDevices.this, ResultsActivity.class);
        NetworkDevices.this.startActivity(activity_settings);
    }

    public void displayResultsAll() {
        ScanResults scanResultsToDisplay = new ScanResults();
        ScanResults allScanResults = AppStorage.getScanResults(this);
        List<Host> hostsToDisplay = allScanResults.getHosts();

        for (Host host : hostsToDisplay) {
            scanResultsToDisplay.appendHost(host);
        }
        AppStorage.storeScanResultsToDisplay(this, scanResultsToDisplay);
        Intent activity_settings = new Intent(NetworkDevices.this, ResultsActivity.class);
        NetworkDevices.this.startActivity(activity_settings);
    }

    public void discoverNetworkDevices() {
        AppStorage.putValue(this, AppStorage.ACTIVITY_REQUESTING_SERVER, ServerCommunicationService.NETWORK_DEVICES_ACTIVITY);
        AppStorage.putValue(this, AppStorage.CURRENT_SCAN, AppStorage.NMAP_SCAN);
        if (WelcomeActivity.NMAP_TEST_MODE) { //TODO Remove
            processResponse(TestData.getNetworkDiscoveryTestData(this));
        } else {
            AppStorage.putValue(this, AppStorage.SERVER_ACTION, AppStorage.NMAP_SCAN_REQUEST);
            final ServerCommunicationService requester = new ServerCommunicationService(NetworkDevices.this);
            requester.execute(ServerCommunicationService.URL_RUN_HOST_DISCOVERY);
        }
    }

    @Override
    public RequestBody buildRequestBody() {
        String action = AppStorage.getValue(this, AppStorage.SERVER_ACTION, "");

        RequestBody requestBody = null;
        switch (action) {
            case (AppStorage.NMAP_SCAN_REQUEST):
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("network-scan-type", AppStorage.getValue(this, AppStorage.NETWORK_SCAN_TYPE, ScanTypes.NETWORK_SCAN_DEFAULT))
                        .addFormDataPart("network-scan-technique", AppStorage.getValue(this, AppStorage.NETWORK_SCAN_TECHNIQUE, ""))
                        .addFormDataPart("port-range", AppStorage.getValue(this, AppStorage.PORT_RANGE, ""))
                        .addFormDataPart("network-scan-hosts", AppStorage.getValue(this, AppStorage.NETWORK_SCAN_HOSTS, ""))
                        .addFormDataPart("network-detection-ops", AppStorage.getValue(this, AppStorage.NETWORK_MAPPING_DETECTION, ""))
                        .addFormDataPart("network-scan-custom-args", AppStorage.getValue(this, AppStorage.NETWORK_SCAN_CUSTOM_ARGS, ""))
                        .build();
                break;
            case (AppStorage.OPENVAS_SCAN_REQUEST):
                List<String> hosts = AppStorage.getValue(this, AppStorage.HOSTS_BEING_VULN_SCANNED, new ArrayList());
                StringBuilder sb = new StringBuilder();
                for (String host : hosts) {
                    sb.append(host + ",");
                }
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("hosts", sb.toString())
                        .addFormDataPart("vulnerability-scan-type", AppStorage.getValue(this, AppStorage.VULN_SCAN_TYPE, ScanTypes.VULN_SCAN_FULL_FAST))
                        .build();
                break;
            case (AppStorage.NMAP_SCAN_STARTED):
            case (AppStorage.OPENVAS_SCAN_STARTED):
            case (AppStorage.GETTING_RESULTS):
                String resultType = "N/A";
                switch (AppStorage.getValue(this, AppStorage.CURRENT_SCAN, "")) {
                    case AppStorage.NMAP_SCAN:
                        resultType = "nmap-results";
                        break;
                    case AppStorage.OPENVAS_SCAN:
                        resultType = "openvas-results";
                        break;
                }
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("scan-id", AppStorage.getValue(this, AppStorage.SCAN_ID, "")) //TODO scan type will be low medium or high.
                        .addFormDataPart("result-type", resultType)
                        .build();
                break;

        }
        return requestBody;
    }

    public void processResponse(String response) {
        Log.v(TAG, response);
        if (AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR, false)) {
            networkDevicesFragment.setConnectionError(AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE, "Error connecting to server. Unable to identify reason, please check the log file or contact us for support"));
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR);
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE);
            cleanScanningPreferenceOps();
        } else {
            Log.i(TAG, "adding retrieved scan results");
            ScanResults scanResults = AppStorage.getScanResults(this);
            try {
                JSONObject jsonResponse = new JSONObject(response);
                if (!jsonResponse.isNull("error") && !jsonResponse.getString("error").equals("")) {
                    networkDevicesFragment.setConnectionError(jsonResponse.getString("error"));
                    cleanScanningPreferenceOps();
                } else {
                    String action = "";
                    if (jsonResponse.get("action") != null) {
                        action = jsonResponse.getString("action");
                    }

                    switch (action) {
                        case SERVER_ACTION_NMAP_STARTED:
                            Log.i(TAG, "Nmap scan has been started.");
                            AppStorage.putValue(this, AppStorage.SERVER_ACTION, AppStorage.NMAP_SCAN_STARTED);
                            AppStorage.putValue(this, AppStorage.SCAN_ID, jsonResponse.getString("scan-id"));
                            checkScanStatus();
                            break;
                        case SERVER_ACTION_OPENVAS_STARTED:
                            Log.i(TAG, "Openvas scan has been started.");
                            AppStorage.putValue(this, AppStorage.SERVER_ACTION, AppStorage.OPENVAS_SCAN_STARTED);
                            AppStorage.putValue(this, AppStorage.SCAN_ID, jsonResponse.getString("scan-id"));
                            checkScanStatus();
                            break;
                        case SERVER_ACTION_SCAN_STATUS:
                            String scanFinished = jsonResponse.getString("scan-finished");
                            if (scanFinished.equals("false")) {
                                Log.i(TAG, "Scan results are not ready to be collected, retrying");
                                checkScanStatus();
                            } else if (scanFinished.equals("true")) {
                                Log.i(TAG, "Scan results are ready to be collected");
                                AppStorage.putValue(this, AppStorage.SERVER_ACTION, AppStorage.GETTING_RESULTS);
                                final ServerCommunicationService requester = new ServerCommunicationService(NetworkDevices.this);
                                requester.execute(ServerCommunicationService.URL_GET_SCAN_RESULTS);
                            }
                            break;
                        case SERVER_ACTION_RESULTS_COLLECTED:
                            Log.i(TAG, "Scan results have been collected");

                            //Nmap Results
                            if (!jsonResponse.isNull("nmap_result") &&
                                    AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.CURRENT_SCAN, "").equals(AppStorage.NMAP_SCAN)) {

                                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                                AppStorage.putValue(this, AppStorage.LAST_DISCOVERY_SCAN_DATE, dateTime.toString("dd-MM-yyyy"));

                                JSONObject nmapResult = jsonResponse.getJSONObject("nmap_result");
                                try {
                                    JSONArray hosts = nmapResult.getJSONArray("hosts");
                                    if (hosts.length() > 0) {
                                        for (int i = 0; i < hosts.length(); i++) {
                                            JSONObject host = hosts.getJSONObject(i);
                                            scanResults.appendHost(host);
                                        }
                                    }
                                } catch (JSONException exception) {
                                    Log.i(TAG, "No Nmap Results");
                                    break;
                                }
                            }

                            //OpenVas Results
                            if (!jsonResponse.isNull("openvas_result") &&
                                    AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.CURRENT_SCAN, "").equals(AppStorage.OPENVAS_SCAN)) {

                                JSONArray openvasResult = jsonResponse.getJSONArray("openvas_result");

                                if (openvasResult.length() > 0) {
                                    JSONArray openvasResults = openvasResult.getJSONArray(0);
                                    for (int i = 0; i < openvasResults.length(); i++) {
                                        JSONObject ovasResult = openvasResults.getJSONObject(i);
                                        scanResults.appendOpenVasResults(ovasResult);
                                    }
                                }
                                List<String> hostsBeingVulnScanned = AppStorage.getValue(this, AppStorage.HOSTS_BEING_VULN_SCANNED, new ArrayList());
                                if (!hostsBeingVulnScanned.isEmpty()) {
                                    for (String hostsScanned : hostsBeingVulnScanned) {
                                        Host host = scanResults.getHost(hostsScanned);
                                        host.setScanID(AppStorage.getValue(this, AppStorage.SCAN_ID, ""));
                                        String lastScannedRisks = host.getVulnerabilityResults().size() == 0 ? Host.NO_RISKS_FOUND : Host.RISKS_FOUND;
                                        host.setLastScannedResult(lastScannedRisks);

                                        host.setLastScanDate(new DateTime(DateTimeZone.UTC));
                                    }
                                }
                            }

                            cleanScanningPreferenceOps();

                            AppStorage.storeScanResults(this, scanResults);

                            if (activeFragment != null) {
                                if (activeFragment instanceof DynamicUI) {
                                    DynamicUI dynamicUI = (DynamicUI) activeFragment;
                                    Log.i(TAG, "Scanning finished, updating user interface");
                                    dynamicUI.updateUI(); //DEBUGGING CHECK IF SCAN RESULTS HAVE BEEN UPDATED!!
                                }
                            }
                            break;
                    }
                }
                //TODO SET LAST_DISCOVERY_SCAN_DATE WHEN A VULN SCAN PERFORMED
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void cleanScanningPreferenceOps() {
        //Clean shared preference
        AppStorage.removeValue(this, AppStorage.ACTIVITY_REQUESTING_SERVER);
        AppStorage.removeValue(this, AppStorage.CURRENT_SCAN);
        AppStorage.removeValue(this, AppStorage.HOSTS_BEING_VULN_SCANNED);
        AppStorage.removeValue(this, AppStorage.SERVER_ACTION);
        AppStorage.removeValue(this, AppStorage.SCAN_ID);
        AppStorage.removeValue(this, AppStorage.REQUEST_DELAY);

        AppStorage.removeValue(this, AppStorage.NETWORK_SCAN_TYPE);
        AppStorage.removeValue(this, AppStorage.VULN_SCAN_TYPE);
        AppStorage.removeValue(this, AppStorage.PORT_RANGE);
        AppStorage.removeValue(this, AppStorage.NETWORK_SCAN_TECHNIQUE);
        AppStorage.removeValue(this, AppStorage.NETWORK_SCAN_HOSTS);
        AppStorage.removeValue(this, AppStorage.NETWORK_SCAN_CUSTOM_ARGS);
        AppStorage.removeValue(this, AppStorage.NETWORK_MAPPING_DETECTION);
    }

    private void checkScanStatus() throws InterruptedException {
        switch (AppStorage.getValue(this, AppStorage.SERVER_ACTION, "")) {
            case AppStorage.NMAP_SCAN_STARTED:
                AppStorage.putValue(this, AppStorage.REQUEST_DELAY, 5 * 1000);
                break;
            case AppStorage.OPENVAS_SCAN_STARTED:
                AppStorage.putValue(this, AppStorage.REQUEST_DELAY, 60 * 1000);
                break;
            default:
                AppStorage.putValue(this, AppStorage.REQUEST_DELAY, 1 * 1000);
        }
        final ServerCommunicationService requester = new ServerCommunicationService(NetworkDevices.this);
        requester.execute(ServerCommunicationService.URL_CHECK_SCAN_RESULTS);
    }

    @Override
    public Fragment getFragment(int fragmentID) {
        switch (fragmentID) {
            case FragmentName.NETWORK_DEVICES:
                return networkDevicesFragment;
            case FragmentName.NETWORK_SERVICES:
                return servicesFragment;
            case FragmentName.OPERATING_SYSTEMS:
                return operatingSystemsFragment;

        }
        return null;
    }

    @Override
    public void setFragment(Fragment fragment, boolean addToBackStack) {
        activeFragment = fragment;
        if (fragment.isAdded()){
            Log.i(TAG, "Fragment Already Added");
            return;
        } else {
            Log.i(TAG, "Fragment Doesn't Exist");
        }
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
            case R.id.bottomNavNetworkHome:
                setFragment(networkDevicesFragment, false);
                return true;
            case R.id.bottomNavOperatingSystems:
                setFragment(operatingSystemsFragment, false);
                return true;
            case R.id.bottomNavServices:
                setFragment(servicesFragment, false);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        boolean found = false;

        if (v instanceof LinearLayout) {
            Object object = v.getTag();
            if (object instanceof Host) {
                Log.d(TAG, "Selected Host: " + ((Host) object).getHostname(false));
                SingleNetworkDeviceFragment singleNetworkDeviceFragment = SingleNetworkDeviceFragment.newInstance((Host) object);
                setFragment(singleNetworkDeviceFragment, true);
            }
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "Resumed");
        super.onResume();
        activityActive = true;
        if (AppStorage.checkExists(this, AppStorage.SERVER_ACTION)) {
            Log.i(TAG, "HTTP Response pending: Calling function");
            processResponse(AppStorage.getValue(this, AppStorage.SERVER_RESPONSE, ""));
        }
    }

    @Override
    public void onStop() {
        Log.i(TAG, "Stopped");
        super.onStop();
        activityActive = false;
    }

    @Override
    public void onPause() {
        Log.i(TAG, "Paused");
        super.onPause();
        activityActive = false;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
