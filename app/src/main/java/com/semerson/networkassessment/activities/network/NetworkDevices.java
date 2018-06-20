package com.semerson.networkassessment.activities.network;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semerson.networkassessment.R;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.StyledText;

import java.io.InputStream;
import java.util.List;


public class NetworkDevices extends AppCompatActivity implements RequestBuilder, ProcessHttpResponse, View.OnClickListener, FragmentHost, BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "NetworkDevices";
    public static final String NEVER = "Never";
    public static final String NEVER_SCANNED = NEVER + "Scanned";
    public static final String LAST_SCANNED_NEVER = "Last Scanned: " + NEVER;
    public static final String NO_RISKS_FOUND = "No Risks Found";
    public static final String RISKS_FOUND = "Risks found";

    public static final String SERVER_ACTION_NMAP_STARTED = "nmap_scan_started";
    public static final String SERVER_ACTION_SCAN_STATUS = "scan-status";
    public static final String SERVER_ACTION_RESULTS_COLLECTED = "results-collected";

    private BottomNavigationView bottomNavigationView;
    private String scanType = "high"; //TODO CHANGE

    private ResultController resultController;

    private List<Host> hosts;
    private ScanResults scanResults;

    OperatingSystemsFragment operatingSystemsFragment;
    NetworkDevicesFragment networkDevicesFragment;
    ServicesFragment servicesFragment;
    SingleNetworkDeviceFragment singleNetworkDeviceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_scanner);

        scanResults = AppStorage.getScanResults(this);
        hosts = scanResults.getHosts();
        resultController = new ResultController(hosts);

        /*
        final Button btnRunScan = (Button) findViewById(R.id.btnRunScan);

        btnRunScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (WelcomeActivity.TEST_MODE) {
                    AssetManager assetManager = getAssets();
                    InputStream input;
                    try {
                        input = assetManager.open("sampleJson.txt");
                        int size = input.available();
                        byte[] buffer = new byte[size];
                        input.read(buffer);
                        input.close();

                        // byte buffer into a string
                        String text = new String(buffer);
                        processResponse(text);

                    } catch (Exception e) {

                    }
                    ;

                } else {
                    SharedPreferences preferences = getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
                    preferences.edit().putString(AppStorage.SERVER_ACTION, AppStorage.NMAP_SCAN_REQUEST).commit();

                    final ServerCommunicationService requester = new ServerCommunicationService(NetworkDevices.this);
                    //requester.execute(ServerCommunicationService.URL_RUN_SCAN);
                    requester.execute(ServerCommunicationService.URL_GET_SCAN_RESULTS);
                }
            }
        });
        */


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        servicesFragment = new ServicesFragment();
        operatingSystemsFragment = new OperatingSystemsFragment();
        networkDevicesFragment = new NetworkDevicesFragment();
        if (savedInstanceState == null) {
            setFragment(networkDevicesFragment, true);
        }
    }

    public void discoverNetworkDevices() {
        if (WelcomeActivity.TEST_MODE) {
            AssetManager assetManager = getAssets();
            InputStream input;
            try {
                input = assetManager.open("sampleJson.txt");
                int size = input.available();
                byte[] buffer = new byte[size];
                input.read(buffer);
                input.close();

                // byte buffer into a string
                String text = new String(buffer);
                processResponse(text);
            } catch (Exception e) {
                Log.e(TAG, "Error opening test data");
            }
        } else {
            SharedPreferences preferences = getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
            preferences.edit().putString(AppStorage.SERVER_ACTION, AppStorage.NMAP_SCAN_REQUEST).commit();

            final ServerCommunicationService requester = new ServerCommunicationService(NetworkDevices.this);
            requester.execute(ServerCommunicationService.URL_RUN_HOST_DISCOVERY);
        }
    }

    @Override
    public RequestBody buildRequestBody() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
        String action = sharedPreferences.getString(AppStorage.SERVER_ACTION, "");

        RequestBody requestBody = null;
        switch (action) {
            case (AppStorage.NMAP_SCAN_REQUEST):
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("action", "run-nmap-scan")
                        .build();
                break;
            case (AppStorage.NMAP_SCAN_STARTED):
            case (AppStorage.GETTING_RESULTS):
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("scan-id", sharedPreferences.getString(AppStorage.SCAN_ID, "")) //TODO scan type will be low medium or high.
                        .build();
                break;

        }


        return requestBody;
    }

    /*
    @Override
    public void processResponse(String response) {
        Log.i(TAG, "adding retrieved scan results");
        ScanResults scanResults = new ScanResults();
        try {
//TODO Error checking - need to ensure it is a 200 response with Json data
            JSONObject jsonResponse = new JSONObject(response);
            String id;
            String name;
            JSONArray services;

            //Nmap Results
            JSONObject nmapResult = jsonResponse.getJSONObject("nmap_result");
            JSONArray hosts = nmapResult.getJSONArray("hosts");

            for (int i = 0; i < hosts.length(); i++) {
                JSONObject host = hosts.getJSONObject(i);

                scanResults.appendHost(host);
            }

            //OpenVas Results
            JSONArray openvasResult = jsonResponse.getJSONArray("openvas_result");
            JSONArray openvasResults = openvasResult.getJSONArray(0);
            for (int i = 0; i < openvasResults.length(); i++) {
                JSONObject ovasResult = openvasResults.getJSONObject(i);
                scanResults.appendOpenVasResults(ovasResult);
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Intent resultActivity = new Intent(NetworkDevices.this, ResultsActivity.class);
        resultActivity.putExtra("scan-results", scanResults);
        startActivity(resultActivity);
    }
*/

    public void processResponse(String response) {//TODO REMOVE
        if (AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR, false)) {
            networkDevicesFragment.setConnectionError(AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE, "Error connecting to server. Unable to identify reason, please check the log file or contact us for support"));
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR);
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE);
        } else {


            Log.i(TAG, "adding retrieved scan results");
            ScanResults scanResults = AppStorage.getScanResults(this);
            try {
                //TODO Error checking - need to ensure it is a 200 response with Json data
                JSONObject jsonResponse = new JSONObject(response);
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
                        if (jsonResponse.getJSONObject("nmap_result") != null) {
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
                                Log.i(TAG, "No Nmap Reulsts");
                                break;
                            }
                        }

                        //OpenVas Results
                        if (jsonResponse.getJSONArray("openvas_result") != null) {
                            JSONArray openvasResult = jsonResponse.getJSONArray("openvas_result");

                            if (openvasResult.length() > 0) {
                                JSONArray openvasResults = openvasResult.getJSONArray(0);
                                for (int i = 0; i < openvasResults.length(); i++) {
                                    JSONObject ovasResult = openvasResults.getJSONObject(i);
                                    scanResults.appendOpenVasResults(ovasResult);
                                }
                            }
                        }
                        //Clean shared preference
                        AppStorage.removeValue(this, AppStorage.SERVER_ACTION);
                        AppStorage.removeValue(this, AppStorage.SCAN_ID);
                        AppStorage.storeScanResults(this, scanResults);

                        networkDevicesFragment.updateNetworkDeviceLayout();
                        break;
                }
                //TODO SET LAST_SCAN_DATE WHEN A VULN SCAN PERFORMED
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void checkScanStatus() throws InterruptedException {
        Log.i(TAG, "Waiting 5 seconds then checking results");
        Thread.sleep(5000);
        final ServerCommunicationService requester = new ServerCommunicationService(NetworkDevices.this);
        requester.execute(ServerCommunicationService.URL_CHECK_SCAN_RESULTS);
    }

    public String getHostLastScanned(Host host) {
        return AppStorage.getValue(this, AppStorage.LAST_SCAN_DATE + host.getHostname(false), LAST_SCANNED_NEVER);
    }

    public StyledText getRisksFound(Host host) {
        String dateScanned = AppStorage.getValue(this, AppStorage.LAST_SCAN_DATE + host.getHostname(false), NEVER);
        if (dateScanned.equals(NEVER)) {
            return new StyledText(NEVER_SCANNED, R.style.never_scanned_text);
        } else {
            Integer numberOfVulns = resultController.getVulnerabilitiesFilterByHost(host.getHostname(true)).size();

            if (numberOfVulns == 0) {
                return new StyledText(NO_RISKS_FOUND, R.style.no_risks_found_text);
            } else {
                return new StyledText(numberOfVulns.toString() + RISKS_FOUND, R.style.risks_found_text);
            }
        }
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
}
