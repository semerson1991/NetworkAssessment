package com.semerson.networkassessment.activities.authentication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.user.awareness.UserAwareness;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.utils.UiObjectCreator;

public class AccountActivity extends BaseActivity {

    private TextView accountName;
    private TextView networkType;
    private TextView networkName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (!AppStorage.getValue(this, AppStorage.LOGGED_IN, false)) {
            Intent activity_account = new Intent(AccountActivity.this, LoginActivity.class);
            AccountActivity.this.startActivity(activity_account);
        } else {
            SpannableStringBuilder spannableNetworkConfigBuilder = new SpannableStringBuilder();

            accountName = findViewById(R.id.accountName);
            accountName.setText(AppStorage.getValue(this, AppStorage.LOGIN_NAME, ""));

            TextView textViewDeviceConnected = findViewById(R.id.deviceConnectedTxt);
            if (!AppStorage.getValue(this, AppStorage.DEVICE_CONNECTED, false)) {
                SpannableString spannableString = UiObjectCreator.createSpannableString(this, new NetworkConfiguration(),"Click here" );

                spannableNetworkConfigBuilder.append("The home security box is not connected. ");
                spannableNetworkConfigBuilder.append(spannableString);
                spannableNetworkConfigBuilder.append(" to create a new network configuration");
                textViewDeviceConnected.setText(spannableNetworkConfigBuilder);
                textViewDeviceConnected.setClickable(true);
                textViewDeviceConnected.setMovementMethod(LinkMovementMethod.getInstance());

                TextView networkNamelbl = findViewById(R.id.networkNamelbl);
                networkNamelbl.setVisibility(View.GONE);
            } else {
                networkName = findViewById(R.id.networkNameTxt);
                networkType = findViewById(R.id.networkType);
                networkName.setText(AppStorage.getValue(this, AppStorage.NETWORK_NAME, ""));
                networkType.setText(AppStorage.getValue(this, AppStorage.NETWORK_TYPE, ""));
            }

        }
    }
}
