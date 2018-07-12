package com.semerson.networkassessment.testdata;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.semerson.networkassessment.activities.network.NetworkDevices;
import com.semerson.networkassessment.storage.AppStorage;

import java.io.InputStream;

public class TestData {

    public static String getVulnerabilityTestData(Context context) {
        AppStorage.putValue(context, AppStorage.SERVER_ACTION, NetworkDevices.SERVER_ACTION_RESULTS_COLLECTED);
        AssetManager assetManager = context.getAssets();
        InputStream input;
        String text = "";
        try {
            input = assetManager.open("sample_vuln.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            text = new String(buffer);

        } catch (Exception e) {
            Log.e("Test Data", "Error opening vulnerability test data");
        }
        return text;
    }

    public static String getNetworkDiscoveryTestData(Context context) {
        AppStorage.putValue(context, AppStorage.SERVER_ACTION, NetworkDevices.SERVER_ACTION_RESULTS_COLLECTED);
        AppStorage.putValue(context, AppStorage.CURRENT_SCAN, AppStorage.NMAP_SCAN);
        AssetManager assetManager = context.getAssets();
        InputStream input;
        String text = "";
        try {
            input = assetManager.open("sample_nmap.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            text = new String(buffer);

        } catch (Exception e) {
            Log.e("Test Data", "Error opening network mapping test data");
        }
        Log.v("SampleData", text);
        return text;
    }
}
