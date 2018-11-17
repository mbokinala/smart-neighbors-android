package com.mbokinala.smartneighbors;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference
{
    static final String PREF_ID = "userID";
    static final String PREF_NAME = "userName";
    static final String PREF_EMAIL = "userEmail";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setID(Context ctx, String id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ID, id);
        editor.commit();
    }

    public static String getID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_ID, "");
    }
    public static void setName(Context ctx, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NAME, name);
        editor.commit();
    }

    public static String getName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_NAME, "");
    }

    public static void setEmail(Context ctx, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, email);
        editor.commit();
    }

    public static String getEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
    }
}