package com.semerson.networkassessment.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.semerson.networkassessment.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText networkName = (EditText) findViewById(R.id.txtUsername);

        final TextView networkNameSelected = (TextView) findViewById(R.id.tvNetworkNameDisplay);
        networkNameSelected.setText(networkName.getText());

    }
}
