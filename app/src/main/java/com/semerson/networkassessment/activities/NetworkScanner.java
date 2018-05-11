package com.semerson.networkassessment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.utils.ProcessHttpResponse;
import com.semerson.networkassessment.utils.RequestBuilder;
import com.semerson.networkassessment.service.ServerCommunicationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.semerson.networkassessment.results.Host;
import com.semerson.networkassessment.results.ScanResults;


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
                final ServerCommunicationService requester = new ServerCommunicationService(NetworkScanner.this);
                requester.execute(ServerCommunicationService.URL_RUN_SCAN);
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
        List<Host> parsedHosts = new ArrayList<>();
        ScanResults scanResults = new ScanResults();
        try {

            JSONObject jsonResponse = new JSONObject(response);
            String id;
            String name;
            JSONArray services;

            JSONArray hosts = jsonResponse.getJSONArray("hosts");

            for (int i = 0; i < hosts.length(); i++) {
                JSONObject host = hosts.getJSONObject(i);
                scanResults.appendHost(host);
//                Host parsedHost = scanResults.appendHost(host);
                // parsedHosts.add(parsedHost);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Intent resultActivity = new Intent(NetworkScanner.this, ResultsActivity.class);
        resultActivity.putExtra("scan-results", scanResults);
        startActivity(resultActivity);
    }
}
