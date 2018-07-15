package com.semerson.networkassessment.activities.authentication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.WelcomeActivity;
import com.semerson.networkassessment.activities.network.NetworkDevices;
import com.semerson.networkassessment.activities.user.awareness.SecurityAwarenessHomeFragment;
import com.semerson.networkassessment.utils.ProcessHttpResponse;
import com.semerson.networkassessment.utils.RequestBuilder;
import com.semerson.networkassessment.service.ServerCommunicationService;
import com.semerson.networkassessment.storage.AppStorage;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LoginActivity extends BaseActivity implements RequestBuilder, ProcessHttpResponse {
    private static final String TAG = "LoginActivity";
    private EditText username;
    private EditText password;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        errorText = (TextView) findViewById(R.id.txtError);
        errorText.setVisibility(View.GONE);

        final Button btnLogin = (Button) findViewById(R.id.btnLogin);

        final TextView tvRegister = (TextView) findViewById(R.id.tvRegisterHere);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Please complete the empty fields");
                } else {
                    if (WelcomeActivity.TEST_LOGIN == true) {
                        processResponse(new String("{ success : true, nickname: Stephen}"));
                    } else {
                        AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.ACTIVITY_REQUESTING_SERVER, ServerCommunicationService.LOGIN_ACTIVITY);
                        final ServerCommunicationService requester = new ServerCommunicationService(LoginActivity.this);
                        requester.execute(ServerCommunicationService.URL_LOGIN);
                    }
                }
            }
        });
    }

    @Override
    public RequestBody buildRequestBody() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", username.getText().toString())
                .addFormDataPart("password", password.getText().toString())
                .build();
        return requestBody;
    }

    @Override
    public void processResponse(String response) {
        AppStorage.removeValue(this, AppStorage.ACTIVITY_REQUESTING_SERVER);
        Log.i(TAG, "Login user response received");
        if (AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR, false)) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE, "Error connecting to server. Unable to identify reason, please check the log file or contact us for support"));
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR);
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE);
            Log.e(TAG, "Login error");
        } else {
            try {
                errorText.setVisibility(View.GONE);
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    String nickname = jsonResponse.getString("nickname");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setMessage("Press continue to navigate to the home page")
                            .setTitle("Login successful")
                            .create()
                            .show();
                    AppStorage.putValue(this, AppStorage.LOGIN_NAME, nickname);
                    AppStorage.putValue(this, AppStorage.LOGGED_IN, true);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this); //TODO Limit login attempts
                    builder.setMessage("Login Failed")
                            .setNegativeButton("retry", null)
                            .create()
                            .show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}
