package com.example.apicallsapp.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess(JSONObject result);
    void onSuccess(JSONArray result);
}
