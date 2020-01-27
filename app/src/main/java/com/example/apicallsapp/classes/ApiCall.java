package com.example.apicallsapp.classes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.apicallsapp.LogInActivity;
import com.example.apicallsapp.interfaces.ServerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;

public class ApiCall {
    private static RequestQueue requestQueue;
    private Context context;
    String url;

    public ApiCall(Context context) {
        this.context = context;
    }

    // POST request (sign up)
    public void signUpPostReq(JSONObject requestBody) {
        url = "http://51.178.50.253/users";
        requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context,
                                "Succesfully registered!",
                                Toast.LENGTH_SHORT).show();
                        Log.i("Response code: ", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (error.networkResponse.statusCode == 409) {
                            Toast.makeText(context,
                                    "Such user already exists.",
                                    Toast.LENGTH_LONG).show();
                        }
                        if (error.networkResponse.statusCode == 500) {
                            Toast.makeText(context,
                                    "Wrong format of email.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    // POST request (log in)
    public void loginPostReq(JSONObject requestBody, final ServerCallback callback) {
        url = "http://51.178.50.253/users/login";
        requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (error.networkResponse.statusCode==401) Toast.makeText(context,
                                "Wrong e-mail or password.", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    // GET request (all posts of logged user)
    public void getPostsReq(final String key, final ServerCallback callback) {
        url = "http://51.178.50.253/posts/";
        requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Conent-Type", "application/json");
                headers.put("Authorization", "Bearer " + key);
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    // POST request (posting post)
    public void postPostReq(final String key, final  ServerCallback callback, JSONObject requestBody) {
        url = "http://51.178.50.253/posts/";
        requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO: handle exceptions
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Conent-Type", "application/json");
                headers.put("Authorization", "Bearer " + key);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    // PATCH request (editing post)
    public void editPost(final String key, String id, JSONObject newContent, final ServerCallback callback) {
        url = "http://51.178.50.253/posts/" + id;
        requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH,
                url,
                newContent,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Conent-Type", "application/json");
                headers.put("Authorization", "Bearer " + key);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
