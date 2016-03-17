package com.szbb.pro.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by ChanZeeBm on 2015/9/7.
 */
public class SharedPreferencesTools {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesTools(Context context) {
        sharedPreferences = context.getSharedPreferences("digital", Activity
                .MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public SharedPreferencesTools putString(String key, String value) {
        editor.putString(key, value);
        return this;
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public SharedPreferencesTools putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return this;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }


    public SharedPreferencesTools putInt(String key, int value) {
        editor.putInt(key, value);
        return this;
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public SharedPreferencesTools putSet(String key, Set<String> values) {
        editor.putStringSet(key, values);
        return this;
    }

    public Set<String> getSet(String key, Set<String> defaultValue) {
        return sharedPreferences.getStringSet(key, defaultValue);
    }

    public void commit() {
        editor.commit();
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
}
