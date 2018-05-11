package com.semerson.networkassessment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.utils.ProcessHttpResponse;
import com.semerson.networkassessment.service.ServerCommunicationService;
import com.semerson.networkassessment.utils.RequestBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterUserActivity extends AppCompatActivity implements RequestBuilder, ProcessHttpResponse {

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);

        final Button btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ServerCommunicationService requester = new ServerCommunicationService(RegisterUserActivity.this);
                requester.execute(ServerCommunicationService.URL_REGISTER);
            }
        });



        /*
        enable2FA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((CheckedTextView) v).toggle();
            }
        });
        */
    }

    @Override
    public RequestBody buildRequestBody() {
        String usernameInput, passwordInput;
        if (!username.getText().toString().isEmpty()) {
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
        return null; //TODO If these are empty, display a message to the user and stop the service executor thread. Throw EXCEPTION!
    }

    @Override
    public void processResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");
            if (success) {
                Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setMessage("Register Failed")
                        .setNegativeButton("retry", null)
                        .create()
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
