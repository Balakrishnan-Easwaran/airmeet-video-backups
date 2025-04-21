package com.airmeet.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class JsonToSheetHelper {

    public static List<List<Object>> convertJsonArrayToSheetData(JSONArray jsonArray, int pageNumber) {
        List<List<Object>> sheetData = new ArrayList<>();

        if (jsonArray.isEmpty()) return sheetData;

        // Extract all keys from first object
        JSONObject first = jsonArray.getJSONObject(0);
        List<String> headers = new ArrayList<>(first.keySet());
        headers.add("sourcePage"); // add a page tag
        sheetData.add(new ArrayList<>(headers));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            List<Object> row = new ArrayList<>();

            for (String key : headers) {
                if (key.equals("sourcePage")) {
                    row.add(pageNumber);
                } else {
                    row.add(obj.optString(key, ""));
                }
            }

            sheetData.add(row);
        }

        return sheetData;
    }
}
