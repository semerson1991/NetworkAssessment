package com.semerson.networkassessment.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.semerson.networkassessment.storage.results.ScanResults;

import static android.content.Context.MODE_PRIVATE;

public class AppStorage {

    public static final String APP_PREFERENCE = "com.semerson.networkassessment.storage";
    public static final String SERVER_COMMUNICATION_ERROR = "Server_Communication_error";
    public static final String SERVER_COMMUNICATION_ERROR_MESSAGE = "Server_Communication_error_message";
    public static final String LOGIN_NAME = "loginName";
    public static final String ALIAS_NAME = "alias";
    public static final String NETWORK_NAME = "networkName";
    public static final String NETWORK_TYPE = "networkType";
    public static final String SESSION = "sessions";
    public static final String SCAN_RESULTS = "scan-results";


    public static final String ADVANCED_MODE = "advanced_mode";
    public static final String RESULTS_HOME_CUSTOM_CHARTS = "results_home_custom_charts";

    public static final String RISK_SCORE_HISTORY = "Risk Score History";


    public static final String LAST_SCAN_DATE = "Last Scan Date";

    public static final String DATABASE_CREATED = "DatabaseCreated";

    public static final String HOST_DISCOVERY_PERFORMED = "Host Discov Performed";

    public static final String SERVER_ACTION = "Server Action";
    public static final String SCANNING_RESULTS = "ScanningResults";

    public static final String NMAP_SCAN_STARTED = "nmapScanStarted";
    public static final String NMAP_SCAN_REQUEST = "nmapScanRequest";
    public static final String GETTING_RESULTS = "gettingScanResults";
    public static final String SCAN_ID = "ScanId";

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

    public static void removeValue(Context context, final String key) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        preferences.edit().remove(key).commit();
    }

    public static String getValue(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
        String value = preferences.getString(key, defaultValue);
        return value;
    }

    public static Boolean getValue(Context context, final String key, final boolean isTrue) {
        SharedPreferences preferences = context.getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
        Boolean value = preferences.getBoolean(key, isTrue);
        return value;
    }

    public static void putValue(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        if (preferences.contains(key)) {
            removeValue(context, key);
        }
        preferences.edit().putString(key, value).commit();
    }

    public static void putValue(Context context, final String key, final boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCE, MODE_PRIVATE);
        if (preferences.contains(key)) {
            removeValue(context, key);
        }
        preferences.edit().putBoolean(key, value).commit();
    }
}
