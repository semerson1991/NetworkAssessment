package com.semerson.networkassessment.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.chrisbanes.photoview.PhotoView;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.Results.ResultsActivity;
import com.semerson.networkassessment.utils.ProcessHttpResponse;
import com.semerson.networkassessment.utils.RequestBuilder;
import com.semerson.networkassessment.service.ServerCommunicationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.semerson.networkassessment.storage.results.ScanResults;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class NetworkScanner extends AppCompatActivity implements RequestBuilder, ProcessHttpResponse {

    private static final String TAG = "NetworkScanner";

    private String scanType = "high"; //TODO CHANGE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_scanner);

        final Button btnRunScan = (Button) findViewById(R.id.btnRunScan);

        btnRunScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean test = true;
                if (test){
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

                    }  catch (Exception e) {

                    }
;
                } else {
                    final ServerCommunicationService requester = new ServerCommunicationService(NetworkScanner.this);
                    //requester.execute(ServerCommunicationService.URL_RUN_SCAN);
                    requester.execute(ServerCommunicationService.URL_GET_SCAN_RESULTS);
                }
            }
        });

    }

    @Override
    public RequestBody buildRequestBody() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("scan-type", scanType) //TODO scan type will be low medium or high.
                .build();
        return requestBody;
    }

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
        Intent resultActivity = new Intent(NetworkScanner.this, ResultsActivity.class);
        resultActivity.putExtra("scan-results", scanResults);
        startActivity(resultActivity);
    }
}
