package com.example.apicallsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apicallsapp.classes.ApiCall;
import com.example.apicallsapp.interfaces.ServerCallback;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddingPostActivity extends AppCompatActivity {
    String key;
    ApiCall apiCall;
    EditText content;
    JSONObject reqBody;
    Context context;

    public void sendPost(View view) {
        try {
            reqBody.put("content", content.getText().toString());
            apiCall = new ApiCall(context);
            apiCall.postPostReq(key,
                    new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            Toast.makeText(context, "Added post.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, UserPanelActivity.class);
                            intent.putExtra("key", key);
                            startActivity(intent);
                        }

                        @Override
                        public void onSuccess(JSONArray result) {

                        }
                    }, reqBody);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        key = getIntent().getStringExtra("key");

        content = findViewById(R.id.contentET);

        reqBody = new JSONObject();

        context = getApplicationContext();
    }
}
