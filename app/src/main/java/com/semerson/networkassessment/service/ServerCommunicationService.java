package com.semerson.networkassessment.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.semerson.networkassessment.activities.WelcomeActivity;
import com.semerson.networkassessment.activities.network.NetworkDevices;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.utils.ProcessHttpResponse;
import com.semerson.networkassessment.utils.RequestBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerCommunicationService extends AsyncTask<String, Integer, String> {

    public static final String NETWORK_DEVICES_ACTIVITY = "Network Devices";
    public static final String REGISTER_USER_ACTIVITY = "Register User";
    public static final String REGISTER_NETWORK_ACTIVITY = "Register Network";
    public static final String LOGIN_ACTIVITY = "Login";

    private static final String TAG = "CommunicationService";

    public static final String BASE_URL = "http://192.168.0.22:8000";
    public static final String URL_REGISTER = BASE_URL + "/register-user/";
    public static final String URL_RUN_HOST_DISCOVERY = BASE_URL + "/run-nmap-scan/";
    public static final String URL_RUN_VULNERABILITY_SCAN = BASE_URL + "/run-vulnerability-scan/";
    public static final String URL_REGISTER_NETWORK = BASE_URL+"/register-network-config/";

    public static final String URL_LOGIN = BASE_URL + "/login-user/";
    public static final String URL_RUN_SCAN = BASE_URL + "/run-scan/";
    public static final String URL_CHECK_SCAN_RESULTS = BASE_URL + "/check-scan-status/";
    public static final String URL_GET_SCAN_RESULTS = BASE_URL + "/get-results/";

    private RequestBody requestBody;
    private ProcessHttpResponse processHttpResponse;
    private Context context;

    public ServerCommunicationService(Context theContext) {
        this.context = theContext;
        if (theContext instanceof ProcessHttpResponse) {
            processHttpResponse = (ProcessHttpResponse) theContext;
        }
        if (theContext instanceof RequestBuilder) {
            requestBody = ((RequestBuilder) theContext).buildRequestBody();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... url) {
        if (url[0] != null && !url[0].isEmpty()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url[0])
                    .post(requestBody)
                    .addHeader("Content-Type", "application/javascript")
                    .build();
            Response response = null;

            try {
                Log.i(TAG, "Sending request to " + url[0]);
                Integer delay = AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.REQUEST_DELAY, 0);
                Log.i(TAG, "Delaying request " + delay.toString() + " seconds");
                Thread.sleep(delay);
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                AppStorage.putValue(context, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE, "Server connection error: " + e.getMessage());
                AppStorage.putValue(context, AppStorage.SERVER_COMMUNICATION_ERROR, true);
                Log.e(TAG, "Error connecting to server: " + e.getMessage());
            } catch (InterruptedException e) {
                Log.e(TAG, "Error request delay error: " + e.getMessage());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) { //called when 'doinbackground' is over .. this will get the String (Downloaded Data) from doInBackground
        super.onPostExecute(result);
        if (processHttpResponse != null) {
            Log.v(TAG, "result = " + result.toString());
            String activityRequestingServer = AppStorage.getValue(context, AppStorage.ACTIVITY_REQUESTING_SERVER, "");
            if (activityRequestingServer.equals(NETWORK_DEVICES_ACTIVITY)) {
                if (NetworkDevices.activityActive) {
                    Log.i(TAG, "Network Device activity is active, updating UI");
                    processHttpResponse.processResponse(result);
                } else {
                    Log.i(TAG, "Network Device activity is not active, storing results");
                    AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.SERVER_RESPONSE, result);
                }
            } else {
                processHttpResponse.processResponse(result);
            }

        }
    }

}
