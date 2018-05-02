package com.semerson.networkassessment.service;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonParser implements Parser {


    @Override
    public JSONArray parse(String data) {
        try {
            JSONArray retrievedObjects = new JSONArray(data);
            return retrievedObjects;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
