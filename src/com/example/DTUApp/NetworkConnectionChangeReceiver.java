package com.example.DTUApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Vector;

public class NetworkConnectionChangeReceiver extends BroadcastReceiver
{
    private static NetworkConnectionChangeReceiver _instance = null;
    private static Vector<base_frag> m_ListOfSubscribers = new Vector<base_frag>();

    public static NetworkConnectionChangeReceiver getInstance() {
        return _instance;
    }

    public static void AddSubscriber(base_frag bf) {
        m_ListOfSubscribers.add(bf);
    }

    public static void RemoveSubscriber(base_frag bf) {
        m_ListOfSubscribers.remove(bf);
    }

    private static void NotifySubscribers() {
        for (int i = 0; i < m_ListOfSubscribers.size(); i++) {
            m_ListOfSubscribers.get(i).Notify();
        }
    }

    public static void Notify() {
        NotifySubscribers();
    }
    @Override
    public void onReceive( Context context, Intent intent )
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ( activeNetInfo != null )
        {
            Log.d("GVE", "activeNetInfo.getTypeName(): " + activeNetInfo.getTypeName());
            Log.d("GVE", "activeNetInfo.isAvailable(): " + activeNetInfo.isAvailable());
            Log.d("GVE", "activeNetInfo.isConnected: " + activeNetInfo.isConnected());

            //Toast.makeText( context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
        }
        if( mobNetInfo != null )
        {
            Log.d("GVE", "mobNetInfo.getTypeName(): " + mobNetInfo.getTypeName());
            Log.d("GVE", "mobNetInfo.isAvailable(): " + mobNetInfo.isAvailable());
            Log.d("GVE", "mobNetInfo.isConnected(): " + mobNetInfo.isConnected());
            //Toast.makeText( context, "Mobile Network Type : " + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
        }
    }
}
 



