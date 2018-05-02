package com.semerson.networkassessment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.semerson.networkassessment.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText networkName = (EditText) findViewById(R.id.txtNetworkName);
        final EditText password = (EditText) findViewById(R.id.txtPassword);

        final Button btnRegister = (Button) findViewById(R.id.btnRegister);


        /*
        enable2FA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((CheckedTextView) v).toggle();
            }
        });
        */
    }
}
