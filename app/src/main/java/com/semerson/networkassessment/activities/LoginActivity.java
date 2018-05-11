package com.semerson.networkassessment.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.utils.ProcessHttpResponse;
import com.semerson.networkassessment.utils.RequestBuilder;
import com.semerson.networkassessment.service.ServerCommunicationService;
import com.semerson.networkassessment.storage.ApplicationStorage;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity  implements RequestBuilder, ProcessHttpResponse {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);

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
                final ServerCommunicationService requester = new ServerCommunicationService(LoginActivity.this);
                requester.execute(ServerCommunicationService.URL_LOGIN);
            }
        });
    }

    @Override
    public RequestBody buildRequestBody() {
        String usernameInput, passwordInput;
        if (!username.getText().toString().isEmpty()){
            usernameInput = username.getText().toString();
        } else {
            usernameInput = "registeredUser";
        }
        if (!password.getText().toString().isEmpty()) {
            passwordInput = password.getText().toString();
        } else {
            passwordInput = "passwordInput";
        }

        if (!usernameInput.isEmpty() && !passwordInput.isEmpty()) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", usernameInput)
                    .addFormDataPart("password", passwordInput)
                    .build();
            return requestBody;
        }
        return null; //TODO If these are empty, display a message to the user and stop the service executor thread.
    }

    @Override
    public void processResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");
            if (success) {
                SharedPreferences preferences = getSharedPreferences(ApplicationStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ApplicationStorage.LOGIN_NAME, username.getText().toString());
                Intent intent = new Intent(LoginActivity.this, NetworkScanner.class);
                startActivity(intent);
                editor.commit();
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
