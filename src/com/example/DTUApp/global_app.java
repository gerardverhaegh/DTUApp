package com.example.DTUApp;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

/**
 * Created by gve on 18-03-2015.
 */
public class global_app extends Application /*implements SharedPreferences*/ {
    private static global_app _instance = null;
    private static SharedPreferences preferences = null;

    public static global_app getInstance() {
        return _instance;
    }

    public static SharedPreferences GetPref()
    {
        return preferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _instance = this;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
