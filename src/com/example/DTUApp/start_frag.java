package com.example.DTUApp;


//import android.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Calendar;

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class start_frag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v = inflater.inflate(R.layout.start_frag, container, false);

        ImageView iv = (ImageView)v.findViewById(R.id.iv);
        iv.setImageResource(R.raw.start);

        Button btnSetAlarm = (Button) v.findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAlarm();
            }
        });

        return v;
    }

    private void SetAlarm() {
        AlarmManager alarmMgr = (AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), alarm_act.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, 10);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }
}