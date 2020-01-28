package com.example.apicallsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apicallsapp.classes.ApiCall;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpActivity extends AppCompatActivity {
    EditText usernameET, passwordET, emailET;
    JSONObject reqBody;

    public void submitSignUp (View view) {
        try {
            reqBody = new JSONObject();
            reqBody.put("userName", usernameET.getText().toString());
            reqBody.put("userPassword", passwordET.getText().toString());
            reqBody.put("userEmail", emailET.getText().toString());
        } catch (Error error) {
            error.printStackTrace();
        } catch (JSONException error) {
            error.printStackTrace();
        }
        try {
            ApiCall apiCall = new ApiCall(getApplicationContext());
            apiCall.signUpPostReq(reqBody);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Back arrow:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usernameET = findViewById(R.id.usernameSignupET);
        passwordET = findViewById(R.id.passwordSignupET);
        emailET = findViewById(R.id.emailSignupET);
    }
}
