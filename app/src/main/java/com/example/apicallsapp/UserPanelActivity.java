package com.example.apicallsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.apicallsapp.classes.ApiCall;
import com.example.apicallsapp.interfaces.ServerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class UserPanelActivity extends AppCompatActivity {
    String key;
    ApiCall apiCall;
    String sNoPosts = "U have no posts yet.";
    String author, content, date, time, realPostId;
    JSONArray jsonArrayResponse;
    Context context;
    String sEdit = "Edit";
    LinearLayout linearLayout;
    Date dDate;

    public void addPost(View view) {
        Intent intent = new Intent(context, AddingPostActivity.class);
        intent.putExtra("key", key);
        startActivity(intent);
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

    // Deleting post
    public void delPost(String key, String postId) {
        apiCall = new ApiCall(context);
        apiCall.delPost(key, postId, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(context, "Post deleted.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONArray result) {

            }
        });
    }


    private void createPost(String author, final String content, String date, String time, int id, final String realPostId) {
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
        layoutParams.setMargins(0, 15, 0, 30);
        layout.setMaxHeight(400);
        layout.setLayoutParams(layoutParams);
        layout.setId(layoutId);

        // Setting up edit button:
        ConstraintLayout.LayoutParams btnParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        button.setBackgroundResource(R.drawable.smallbuttonshape);
        btnParams.width = 100;
        btnParams.height = 90;
        btnParams.setMargins(5,5,0,0);
        button.setText(sEdit);
        button.setTextSize(12);
        button.setId(btnId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPostActivity.class);
                intent.putExtra("_id", realPostId);
                intent.putExtra("content", content);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });
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
        postInfoTV.append("\n");
        postInfoTV.append(time);

        // Setting up content text view
        ConstraintLayout.LayoutParams contentParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        contentParams.setMargins(70,15,0,0);
        contentTV.setText(content);
        contentTV.setTextSize(18);
        contentTV.setTextColor(Color.BLACK);
        contentTV.setId(contentId);
        contentTV.setLayoutParams(contentParams);

        // Setting up elements to layout
        layout.addView(button);
        layout.addView(postInfoTV);
        layout.addView(contentTV);
        layout.invalidate();

        // Setting up constraint
        constraintSet.clone(layout);
        constraintSet.connect(btnId, ConstraintSet.START, layoutId, ConstraintSet.START);
        constraintSet.connect(btnId, ConstraintSet.TOP, layoutId, ConstraintSet.TOP);
        constraintSet.connect(postInfoId, ConstraintSet.START, layoutId, ConstraintSet.START);
        constraintSet.connect(postInfoId, ConstraintSet.TOP, btnId, ConstraintSet.BOTTOM);
        constraintSet.connect(contentId, ConstraintSet.START, postInfoId, ConstraintSet.END);
        constraintSet.connect(contentId, ConstraintSet.TOP, layoutId, ConstraintSet.TOP);
        constraintSet.applyTo(layout);
        linearLayout.addView(layout);
    }

    private void dataDisplay(JSONArray data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.GERMANY);
        Date[] dates = new Date[data.length()];
        int counter=0;


        for (int j=0; j<data.length(); j++) {
            try {
                JSONObject jsonObject = data.getJSONObject(j);
                dates[j] = format.parse((jsonObject.getString("date")).substring(0, 16));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
                setNoPosts();
            }
        }
        try {
            Arrays.sort(dates, Collections.<Date>reverseOrder());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO COMMENT ALL THIS SHIT WITH SHOWING REVERSED POSTS
        try {
            while (counter<data.length()) {
                for (int i = 0; i < data.length(); i++) {
                    if (counter == data.length()) break;
                    JSONObject post = data.getJSONObject(i);

                    author = post.getString("author");
                    content = post.getString("content");
                    realPostId = post.getString("_id");

                    date = post.getString("date").substring(0, 10);
                    time = post.getString("date").substring(11, 16);

                    String dateTime = post.getString("date").substring(0, 16);
                    dDate = format.parse(dateTime);

                    if (dates[counter].equals(dDate)) {
                        createPost(author, content, date, time, i, realPostId);
                        counter++;
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            setNoPosts();
        }
        catch (org.json.JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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
                if (result.length() == 0) {
                    setNoPosts();
                }
            }
            @Override
            public void onSuccess(JSONObject result) {

            }
        });
    }
}
