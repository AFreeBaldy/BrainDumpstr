package com.capedbaldy.braindumpstr;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class TestUtils {
    public static boolean jsonArrayContains(JSONArray jsonArray, String string) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.get(i).equals(string)) {
                return true;
            }
        }
        return false;
    }

    public static boolean jsonArrayContains(JSONArray jsonArray, Long num) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (Long.valueOf(((Integer) jsonArray.get(i)).longValue()).equals(num)) {
                return true;
            }
        }
        return false;
    }
}
