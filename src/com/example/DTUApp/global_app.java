package com.example.DTUApp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gve on 18-03-2015.
 */
public class global_app extends Application /*implements SharedPreferences*/ {
    private static global_app _instance = null;
    private static SharedPreferences preferences = null;
    //private static communication comm = null;

    public static global_app getInstance() {
        return _instance;
    }

    public static SharedPreferences GetPref()
    {
        return preferences;
    }

/*
    public static communication GetComm()
    {
        return comm;
    }
*/

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString(constants.USERNAME, "NO STRING") == "NO STRING")
        {
            preferences.edit().putString(constants.USERNAME, getGoogleUsername()).putString(constants.PASSWORD, "longpassword123").commit();
        }

        //comm = new communication();
        Log.d("GVE", "---------global_app onCreate-----------");
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

    public String getGoogleUsername() {
        AccountManager manager = AccountManager.get(global_app.getInstance());
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type
            // values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");
            if (parts.length > 0 && parts[0] != null)
                return parts[0];
            else
                return null;
        } else
            return null;
    }
}
