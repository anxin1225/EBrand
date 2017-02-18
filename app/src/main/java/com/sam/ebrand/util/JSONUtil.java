package com.sam.ebrand.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam on 2016/11/20.
 */

public class JSONUtil
{
    public static double getDouble(final JSONObject jsonObject, final String s, final double n) {
        try {
            return jsonObject.getDouble(s);
        }
        catch (JSONException ex) {
            return n;
        }
    }

    public static int getInt(final JSONObject jsonObject, final String s, final int n) {
        try {
            return jsonObject.getInt(s);
        }
        catch (JSONException ex) {
            return n;
        }
    }

    public static JSONArray getJSONArray(final JSONObject jsonObject, final String s, final JSONArray jsonArray) {
        try {
            return jsonObject.getJSONArray(s);
        }
        catch (JSONException ex) {
            return jsonArray;
        }
    }

    public static long getLong(final JSONObject jsonObject, final String s, final long n) {
        try {
            return jsonObject.getLong(s);
        }
        catch (JSONException ex) {
            return n;
        }
    }

    public static String getString(final JSONObject jsonObject, final String s, final String s2) {
        try {
            return jsonObject.getString(s);
        }
        catch (JSONException ex) {
            return s2;
        }
    }
}
