package com.semerson.networkassessment.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.semerson.networkassessment.activities.WelcomeActivity;
import com.semerson.networkassessment.storage.results.ScanResults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class AppStorage {

    public static final String APP_PREFERENCE = "com.semerson.networkassessment.storage";
    public static final String SERVER_COMMUNICATION_ERROR = "Server_Communication_error";
    public static final String SERVER_COMMUNICATION_ERROR_MESSAGE = "Server_Communication_error_message";
    public static final String SERVER_RESPONSE = "serverResponse";
    public static final String LOGIN_NAME = "loginName";
    public static final String NETWORK_NAME = "networkName";
    public static final String NETWORK_TYPE = "networkType";
    public static final String SESSION = "sessions";

    public static final String ADVANCED_MODE = "advanced_mode";
    public static final String RESULTS_HOME_CUSTOM_CHARTS = "results_home_custom_charts";

    public static final String RISK_SCORE_HISTORY = "Risk Score History";


    public static final String LAST_DISCOVERY_SCAN_DATE = "Last Scan Date";

    public static final String DATABASE_CREATED = "DatabaseCreated";

    public static final String HOST_DISCOVERY_PERFORMED = "Host Discov Performed";

    public static final String SERVER_ACTION = "Server Action";
    public static final String SCANNING_RESULTS = "ScanningResults";
    public static final String SCAN_RESULTS_TO_DISPLAY = "ScanningResultsToDisplay";

    public static final String NMAP_SCAN_REQUEST = "nmapScanRequest";
    public static final String NMAP_SCAN_STARTED = "nmapScanStarted";
    public static final String GETTING_RESULTS = "gettingScanResults";

    public static final String OPENVAS_SCAN_REQUEST = "openvasscan_request";
    public static final String OPENVAS_SCAN_STARTED = "openvasscan_started";

    public static final String SCAN_ID = "ScanId";
    public static final String REQUEST_DELAY = "request-delay";
    public static final String HOSTS_BEING_VULN_SCANNED = "hosts-being-scanned";
    public static final String CURRENT_SCAN = "current-scan";
    public static final String OPENVAS_SCAN = "openvas-scan";
    public static final String NMAP_SCAN = "nmap-scan";

    public static final String NETWORK_SCAN_TECHNIQUE = "network-scan-technique";
    public static final String NETWORK_SCAN_TYPE = "network-scan-type";
    public static final String PORT_RANGE = "port-range";
    public static final String NETWORK_SCAN_HOSTS = "hosts";
    public static final String NETWORK_SCAN_CUSTOM_ARGS = "network-custom-scan-args";
    public static final String NETWORK_MAPPING_DETECTION = "network-mapping-detection-ops";
    public static final String VULN_SCAN_TYPE = "vulnerability-scan-type";
    public static final String RISK_SCORE_FORMULA = "risk-score-formula";
    public static final String FORMULA_MAXIMUM_TEN = "risk-max-ten";
    public static final String FORMULA_MAXIMUM_DATE = "risk-by-date";
    public static final String VULN_SCAN_PERFORMED = "vuln-scan-performed";

    public static final String DEVICE_CONNECTED = "logged-in-state";
    public static final String ACTIVITY_REQUESTING_SERVER = "Activity-Requesting";
    public static final String LOGGED_IN = "Logged In";
    public static ScanResults getScanResults(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(SCANNING_RESULTS, "").equals("")) {
            return new ScanResults();
        }
        Gson gson = new Gson();
        String scanResultsJson = sharedPreferences.getString(SCANNING_RESULTS, "");
        ScanResults scanResults = gson.fromJson(scanResultsJson, ScanResults.class);
        return scanResults;
    }

    public static void storeScanResults(Context context, ScanResults scanResults) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String scanningResults = gson.toJson(scanResults);
        sharedPreferences.edit().putString(SCANNING_RESULTS, scanningResults).commit();
    }

    public static void storeScanResultsToDisplay(Context context, ScanResults scanResults) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String scanningResults = gson.toJson(scanResults);
        sharedPreferences.edit().putString(SCAN_RESULTS_TO_DISPLAY, scanningResults).commit();
    }

    public static ScanResults getScanResultsToDisplay(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(SCAN_RESULTS_TO_DISPLAY, "").equals("")) {
            return new ScanResults();
        }
        Gson gson = new Gson();
        String scanResultsJson = sharedPreferences.getString(SCAN_RESULTS_TO_DISPLAY, "");
        ScanResults scanResults = gson.fromJson(scanResultsJson, ScanResults.class);
        return scanResults;
    }

    public static void removeValue(Context context, final String key) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        preferences.edit().remove(key).commit();
    }

    public static String getValue(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
        String value = preferences.getString(key, defaultValue);
        return value;
    }



    public static void putValue(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        if (preferences.contains(key)) {
            removeValue(context, key);
        }
        preferences.edit().putString(key, value).commit();
    }

    public static void putValue(Context context, String key, List<String> value) {
        Set<String> set = new HashSet<String>();
        set.addAll(value);
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        if (preferences.contains(key)) {
            removeValue(context, key);
        }
        preferences.edit().putStringSet(key, set).commit();
    }

    public static List<String> getValue(Context context, final String key, List value) {
        Set<String> set = new HashSet<String>();
        set.addAll(value);

        SharedPreferences preferences = context.getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
        Set<String> storedSet = preferences.getStringSet(key, set);

        List<String> list = new ArrayList<>();
        for (String str : storedSet) {
            list.add(str);
        }
        return list;
    }


    public static void putValue(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        if (preferences.contains(key)) {
            removeValue(context, key);
        }
        preferences.edit().putInt(key, value).commit();
    }

    public static int getValue(Context context, final String key, int defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
        int value = preferences.getInt(key, defaultValue);
        return value;
    }

    public static boolean checkExists(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        if (preferences.contains(key)) {
            return true;
        }
        return false;
    }

    public static void putValue(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        if (preferences.contains(key)) {
            removeValue(context, key);
        }
        preferences.edit().putBoolean(key, value).commit();
    }
    public static Boolean getValue(Context context, final String key, boolean isTrue) {
        SharedPreferences preferences = context.getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);

        Boolean value = preferences.getBoolean(key, isTrue);
        return value;
    }

}
