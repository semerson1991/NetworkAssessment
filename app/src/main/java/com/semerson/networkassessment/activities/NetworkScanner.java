package com.semerson.networkassessment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.semerson.networkassessment.R;


public class NetworkScanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_scanner);

        final Button btnRunScan = (Button) findViewById(R.id.btnRunScan);

        btnRunScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(NetworkScanner.this, RegisterActivity.class); //Change to the corrent activity
                NetworkScanner.this.startActivity(registerIntent);


            }
        });

    }
}
