package com.example.DTUApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Gerard Verhaegh on 3/24/2015.
 */

public class alarm_act extends BroadcastReceiver {

    // this constructor is called by the alarm manager.
    public alarm_act(){
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // here you can get the extras you passed in when creating the alarm

        Log.d("GVE", "ALARM RECEIVED");

        Intent i=new Intent(context, start_act.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}

