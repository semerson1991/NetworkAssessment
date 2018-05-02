package com.semerson.networkassessment.service;
import android.content.Context;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.util.Log;

import com.semerson.networkassessment.Utils.RequestBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerCommunicationService extends AsyncTask<String, Integer, String>{

    private static final String TAG = "CommunicationService";
    public static final String BASE_URL = "http://localhost:8000";
    public static final String URL_RUN_SCAN = BASE_URL+"/perform-scan";
    public static final String URL_REGISTER = BASE_URL+"/register";
    public static final String URL_LOGIN = BASE_URL+"/login";

    private Context context;
    private RequestBody requestBody;
    private ProgressDialog progressDialog;
    public ServerCommunicationService(Context theContext) {
        context = theContext;
        if (theContext instanceof RequestBuilder){
            requestBody = (RequestBody) ((RequestBuilder) theContext).buildRequestBody();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Testing Dialog");
        progressDialog.setMessage("Testing Dialog");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... url) {
        if (url[0] != null && !url[0].isEmpty()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url[0])
                    .post(requestBody)
                    .build();
            Response response = null;

            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                String error = e.getCause().getMessage();
                Log.e(TAG, "Failed to retrieve data "+error);

            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) { //called when 'doinbackground' is over .. this will get the String (Downloaded Data) from doInBackground
        super.onPostExecute(s);
        progressDialog.dismiss();
    }

    public void performRequest(final String url) {

        if (!url.isEmpty()) {
            switch (url){
                case URL_RUN_SCAN:
                    sendRunScanRequest();
                    break;
                case URL_REGISTER:

                    break;
                case URL_LOGIN:

                    break;
                    default:
                        break;

            }
        }
    }

    private void sendRunScanRequest() {

    }
}
