package com.example.apicallsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apicallsapp.classes.ApiCall;
import com.example.apicallsapp.interfaces.ServerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {
    EditText passwordET, emailET;
    JSONObject reqBody;
    String key;

    public void logIn(View view) {
        try {
            reqBody = new JSONObject();
            reqBody.put("userEmail", emailET.getText().toString());
            reqBody.put("userPassword", passwordET.getText().toString());
        } catch (Error error) {
            error.printStackTrace();
        } catch (JSONException error) {
            error.printStackTrace();
        }
        try {
            ApiCall apiCall = new ApiCall(getApplicationContext());
            apiCall.loginPostReq(reqBody, new ServerCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        key = response.getString("token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (key != null) {
                        Intent intent = new Intent(getApplicationContext(), UserPanelActivity.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }
                }
                @Override
                public void onSuccess(JSONArray response) {

                }
            });
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        passwordET = findViewById(R.id.passwordLoginET);
        emailET = findViewById(R.id.emailLoginET);

    }
}
