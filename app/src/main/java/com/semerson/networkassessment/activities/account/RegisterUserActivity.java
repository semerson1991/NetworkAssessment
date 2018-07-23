package com.semerson.networkassessment.activities.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.home.WelcomeActivity;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.utils.ProcessHttpResponse;
import com.semerson.networkassessment.service.ServerCommunicationService;
import com.semerson.networkassessment.utils.RequestBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterUserActivity extends BaseActivity implements RequestBuilder, ProcessHttpResponse {

    public static final String USER_EXISTS = "user-exists";
    public static final String ADMIN_REQUESTED = "admin-approval-required";
    public static final String TAG = "Register User";
    private EditText nickname;
    private EditText password;
    private EditText emailAddress;
    private CheckBox appAdmin;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailAddress = (EditText) findViewById(R.id.txtEmail);
        nickname = (EditText) findViewById(R.id.txtNickname);
        password = (EditText) findViewById(R.id.txtPassword);
        appAdmin = (CheckBox) findViewById(R.id.chkBoxEnableAdmin);
        errorText = (TextView) findViewById(R.id.txtError);
        errorText.setVisibility(View.GONE);
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Register user requested");
                if (nickname.getText().toString().isEmpty() || emailAddress.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Required fields cannot be empty");
                } else {
                    AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.ACTIVITY_REQUESTING_SERVER, ServerCommunicationService.REGISTER_USER_ACTIVITY);
                    final ServerCommunicationService requester = new ServerCommunicationService(RegisterUserActivity.this);
                    requester.execute(ServerCommunicationService.URL_REGISTER);
                }
            }
        });
    }

    @Override
    public RequestBody buildRequestBody() {
        String usernameInput, passwordInput;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", emailAddress.getText().toString())
                .addFormDataPart("password", password.getText().toString())
                .addFormDataPart("app-admin", appAdmin.isChecked() ? "True" : "False")
                .addFormDataPart("nickname", nickname.getText().toString())
                .build();
        return requestBody;
    }

    @Override
    public void processResponse(String response) {
        AppStorage.removeValue(this, AppStorage.ACTIVITY_REQUESTING_SERVER);
        Log.i(TAG, "Register user response received");
        if (AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR, false)) {
            errorText.setText(AppStorage.getValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE, "Error connecting to server. Unable to identify reason, please check the log file or contact us for support"));
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR);
            AppStorage.removeValue(this, AppStorage.SERVER_COMMUNICATION_ERROR_MESSAGE);
            Log.e(TAG, "Register error");
        } else {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(RegisterUserActivity.this, AccountActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setMessage("Press continue to login")
                            .setTitle("Registration Successful")
                            .create()
                            .show();
                } else {
                    String messageToDisplay = "Error registering new user, please try again.";
                    if (!jsonResponse.isNull("reason")) {
                        String reason = jsonResponse.getString("reason");
                        if (reason.equals(USER_EXISTS)) {
                            messageToDisplay = "A user with this email address already exists";
                        }
                        if (reason.equals(ADMIN_REQUESTED)) {
                            messageToDisplay = "An administrator account has been contacted to approve user registration";
                        }
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                    builder.setMessage(messageToDisplay)
                            .setTitle("User Register Information")
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
