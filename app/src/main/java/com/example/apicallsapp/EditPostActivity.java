package com.example.apicallsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apicallsapp.classes.ApiCall;
import com.example.apicallsapp.interfaces.ServerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditPostActivity extends AppCompatActivity {
    String postId, sContent, key;
    Button sendPostBtn;
    EditText patchContent;
    ApiCall apiCall;
    JSONObject newContent;

    public void sendPatch(View view) {
        apiCall = new ApiCall(getApplicationContext());

        newContent = new JSONObject();
        try {
            newContent.put("content", patchContent.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiCall.editPost(key, postId, newContent, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(getApplicationContext(), "Updated post.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserPanelActivity.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }

            @Override
            public void onSuccess(JSONArray result) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_post);

        postId = getIntent().getStringExtra("_id");
        sContent = getIntent().getStringExtra("content");
        key = getIntent().getStringExtra("key");

        sendPostBtn = findViewById(R.id.editPost);
        patchContent = findViewById(R.id.patchContentET);

        patchContent.setText(sContent);
    }
}
