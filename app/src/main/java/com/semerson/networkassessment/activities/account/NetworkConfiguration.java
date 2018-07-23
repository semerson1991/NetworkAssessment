package com.semerson.networkassessment.activities.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.home.WelcomeActivity;
import com.semerson.networkassessment.service.ServerCommunicationService;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.utils.ProcessHttpResponse;
import com.semerson.networkassessment.utils.RequestBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NetworkConfiguration extends BaseActivity implements RequestBuilder, ProcessHttpResponse {
    private static final String TAG = "Network Configuration";
    public static final String INCORRECT_AUTH = "incorrect-authentication";
    private EditText name;
    private EditText password;
    private TextView errorText;

    private RadioButton radioHome;
    private RadioButton radioPublic;

    private Button btnRegister;
    private String networkType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_configuration);

        radioHome = findViewById(R.id.homeNetwork);
        radioHome.setChecked(true);
        radioPublic = findViewById(R.id.publicNetwork);
        name = findViewById(R.id.txtFriendlyName);
        password = findViewById(R.id.txtPassword);
        errorText = findViewById(R.id.txtError);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Register user requested");
                if (name.getText().toString().isEmpty() || password.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Required fields cannot be empty");
                } else {
                    if (WelcomeActivity.TEST_DEVICE) {
                        processResponse(new String("{success : true}"));
                    }
                    AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.ACTIVITY_REQUESTING_SERVER, ServerCommunicationService.REGISTER_USER_ACTIVITY);
                    final ServerCommunicationService requester = new ServerCommunicationService(NetworkConfiguration.this);
                    requester.execute(ServerCommunicationService.URL_REGISTER_NETWORK);
                }
            }
        });


    }

    @Override
    public RequestBody buildRequestBody() {
        networkType = radioHome.isChecked() ? "Private Home Network" : "Public Network";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", AppStorage.getValue(this, AppStorage.LOGIN_NAME, ""))
                .addFormDataPart("password", password.getText().toString())
                .addFormDataPart("config-name", name.getText().toString())
                .addFormDataPart("network-type", networkType)
                .build();
        return requestBody;
    }

    @Override
    public void processResponse(String response) {
        AppStorage.removeValue(this, AppStorage.ACTIVITY_REQUESTING_SERVER);
        Log.i(TAG, "Register Network response received");
        if (AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR, false)) {
            errorText.setText(AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE, "Error connecting to server. Unable to identify reason, please check the log file or contact us for support"));
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR);
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE);
            Log.e(TAG, "Register Network Device error");
        } else {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NetworkConfiguration.this);
                    AppStorage.putValue(this, AppStorage.NETWORK_NAME, name.getText().toString());
                    AppStorage.putValue(this, AppStorage.NETWORK_TYPE, networkType);
                    AppStorage.putValue(this, AppStorage.DEVICE_CONNECTED, true);
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(NetworkConfiguration.this, WelcomeActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setMessage("Well done! You are now ready to secure your network. Press continue to go to the home page")
                            .setTitle("Registration Successful")
                            .create()
                            .show();
                } else {
                    String messageToDisplay = "Error registering device configuration, please try again.";
                    if (!jsonResponse.isNull("reason")) {
                        String reason = jsonResponse.getString("reason");
                        if (reason.equals(INCORRECT_AUTH)) {
                            messageToDisplay = "Incorrect Authentication details";
                        }
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(NetworkConfiguration.this);
                    builder.setMessage(messageToDisplay)
                            .setTitle("Network Device Register Information")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
