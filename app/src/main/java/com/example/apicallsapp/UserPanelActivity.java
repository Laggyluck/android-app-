package com.example.apicallsapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.apicallsapp.classes.ApiCall;
import com.example.apicallsapp.interfaces.ServerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class UserPanelActivity extends AppCompatActivity {
    String key;
    ApiCall apiCall;
    String sNoPosts = "U have no posts yet.";
    String author, content, date;
    JSONArray jsonArrayResponse;
    Context context;
    String sEdit = "Edit";
    LinearLayout linearLayout;

    public void addPost(View view) {

    }

    // Showing text view that there are no posts
    private void setNoPosts() {
        TextView noPostsTV = new TextView(context);
        ConstraintLayout.LayoutParams noPostsParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        noPostsParams.setMargins(15, 25, 15, 25);
        noPostsTV.setText(sNoPosts);
        noPostsTV.setTextSize(40);
        noPostsTV.setLayoutParams(noPostsParams);
        linearLayout.addView(noPostsTV);
    }

    // TODO: making it with real id of posts
    private void createPost(String author, String content, String date, int id) {
        int layoutId = id;
        int btnId = id + 1000;
        int postInfoId = id + 2000;
        int contentId = id + 3000;

        ConstraintLayout layout = new ConstraintLayout(context);
        Button button = new Button(context);
        TextView postInfoTV = new TextView(context);
        TextView contentTV = new TextView(context);
        ConstraintSet constraintSet = new ConstraintSet();
        // Setting up layout:
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 30);
        layout.setLayoutParams(layoutParams);
        layout.setId(layoutId);
        // Setting up button:
        ConstraintLayout.LayoutParams btnParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        button.setBackgroundResource(R.drawable.buttonshape);
        btnParams.width = 100;
        btnParams.height = 100;
        btnParams.setMargins(5,5,0,0);
        button.setText(sEdit);
        button.setTextSize(12);
        button.setId(btnId);
        button.setLayoutParams(btnParams);
        // Setting up author text view
        ConstraintLayout.LayoutParams postInfoParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        postInfoParams.setMargins(5,10,0,0);
        postInfoTV.setId(postInfoId);
        postInfoTV.setLayoutParams(postInfoParams);
        postInfoTV.append(author);
        postInfoTV.append("\n");
        postInfoTV.append(date);
        // Setting up content text view
        ConstraintLayout.LayoutParams contentParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        contentParams.setMargins(60,15,0,0);
        contentTV.setText(content);
        contentTV.setId(contentId);
        contentTV.setLayoutParams(contentParams);
        // Setting up elements to layout
        layout.addView(button);
        layout.addView(postInfoTV);
        layout.addView(contentTV);
        layout.invalidate();
        // Setting up constraint TODO: attach all by using constraintSet.connect() method
        constraintSet.clone(layout);
        constraintSet.connect(btnId, ConstraintSet.START, layoutId, ConstraintSet.START, 0);
        constraintSet.connect(btnId, ConstraintSet.TOP, layoutId, ConstraintSet.TOP, 0);
        constraintSet.connect(postInfoId, ConstraintSet.START, layoutId, ConstraintSet.START, 0);
        constraintSet.connect(postInfoId, ConstraintSet.TOP, btnId, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(contentId, ConstraintSet.START, postInfoId, ConstraintSet.END, 0);
        constraintSet.connect(contentId, ConstraintSet.TOP, layoutId, ConstraintSet.TOP, 0);
        constraintSet.applyTo(layout);
        linearLayout.addView(layout);
    }

    private void dataDisplay(JSONArray data) {
        try {
            for (int i=0; i<data.length(); i++) {
                JSONObject post = data.getJSONObject(i);
                author = post.getString("author");
                content = post.getString("content");
                date = post.getString("date").substring(0, 10);
                createPost(author, content, date, i);
            }
        } catch (NullPointerException e) {
            // TODO: display there are no posts
            e.printStackTrace();
            setNoPosts();
//            noPosts.setText(sNoPosts);
//            noPosts.setVisibility(View.VISIBLE);
        }
        catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = findViewById(R.id.linearLayout);

        context = getApplicationContext();

        // Getting key value
        key = getIntent().getStringExtra("key");
        // TODO: make user panel: operations on posts (show, add, delete, patch)

        apiCall = new ApiCall(getApplicationContext());
        apiCall.getPostsReq(key, new ServerCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                jsonArrayResponse = result;
                dataDisplay(jsonArrayResponse);
                Log.i("dsa", String.valueOf(result.length()));
                if (result.length() == 0) {
//                    noPosts.setText(sNoPosts);
//                    noPosts.setVisibility(View.VISIBLE);
                    setNoPosts();
                }
            }
            @Override
            public void onSuccess(JSONObject result) {

            }
        });
            // TODO: Make it clear: constraint layout shouldn't exists by itself - must
            //  be initialized by onSuccess function (showing all posts). If there are no posts
            //  it should show textview that says you have no posts or smth

    }
}
