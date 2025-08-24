package com.project.myapplication.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {


    private static final String PREF_NAME = "user_pref";
    private static final String KEY_LAST_PAGE = "last_page_";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void clear() {
        editor.clear().apply();
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public void saveLastPage(String pdfTitle, int pageNumber) {
        sharedPreferences.edit().putInt(KEY_LAST_PAGE + pdfTitle, pageNumber).apply();
    }

    public int getLastPage(String pdfTitle) {
        return sharedPreferences.getInt(KEY_LAST_PAGE + pdfTitle, 0);
    }
}

