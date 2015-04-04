package com.example.DTUApp;


//import android.app.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Calendar;

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class start_frag extends Fragment {
    EditText mtxtDate = null;
    EditText mtxtTime = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v = inflater.inflate(R.layout.start_frag, container, false);

        ImageView iv = (ImageView)v.findViewById(R.id.iv);
        iv.setImageResource(R.raw.start);

/*        Button btnSetAlarm = (Button) v.findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAlarm();
            }
        });*/

        mtxtDate = (EditText) v.findViewById(R.id.txtDate);
        mtxtDate.setInputType(0);
        mtxtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePicker(getActivity());
            }
        });

        mtxtTime = (EditText) v.findViewById(R.id.txtTime);
        mtxtTime.setInputType(0);
        mtxtTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker(getActivity());
            }
        });

        return v;
    }

    private void showDatePicker(Activity context) {

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.calender, null,false);
        // Creating the PopupWindow
        final PopupWindow popupWindow = new PopupWindow(
                layout,400,400);

        popupWindow.setContentView(layout);
        popupWindow.setHeight(500);
        popupWindow.setOutsideTouchable(false);
        // Clear the default translucent background
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        CalendarView cv = (CalendarView) layout.findViewById(R.id.calendarView);
        cv.setBackgroundColor(Color.GREEN);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                popupWindow.dismiss();
                Log.d("date selected", "date selected " + year + " " + month + " " + dayOfMonth);
                mtxtDate.setText(new StringBuilder().append(pad(year))
                        .append(":").append(pad(month)).append(":").append(dayOfMonth));
            }
        });

        popupWindow.showAtLocation(layout, Gravity.TOP,5,170);
    }

    private void showTimePicker(Activity context) {

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.timepicker, null,false);
        // Creating the PopupWindow
        final PopupWindow popupWindow = new PopupWindow(
                layout,400,400);

        popupWindow.setContentView(layout);
        popupWindow.setHeight(500);
        popupWindow.setOutsideTouchable(false);
        // Clear the default translucent background
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        TimePicker tp = (TimePicker) layout.findViewById(R.id.timeView);
        tp.setBackgroundColor(Color.GREEN);

        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                popupWindow.dismiss();
                Log.d("time selected", "time selected " + hourOfDay + " " + minute);
                mtxtTime.setText(new StringBuilder().append(pad(hourOfDay))
                        .append(":").append(pad(minute)));
            }

        });

        popupWindow.showAtLocation(layout, Gravity.TOP,5,170);
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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