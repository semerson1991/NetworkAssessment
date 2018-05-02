package com.semerson.networkassessment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.Utils.RequestBuilder;
import com.semerson.networkassessment.service.ServerCommunicationService;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity  implements RequestBuilder {

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText networkName = (EditText) findViewById(R.id.txtNetworkName);
        final EditText password = (EditText) findViewById(R.id.txtPassword);

        final Button btnLogin = (Button) findViewById(R.id.btnLogin);

        final TextView tvRegister = (TextView) findViewById(R.id.tvRegisterHere);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
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
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "steve")
                .build();
        return requestBody;
    }
}
