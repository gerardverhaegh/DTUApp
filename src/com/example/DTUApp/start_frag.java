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
    int m_year;
    int m_month;
    int m_dayOfMonth;
    int m_hourOfDay;
    int m_minute;

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

        Calendar c = Calendar.getInstance();
        DisplayDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        DisplayTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

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
                DisplayDate(year, month, dayOfMonth);
                popupWindow.dismiss();
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
                DisplayTime(hourOfDay, minute);
                popupWindow.dismiss();
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

    private void DisplayDate(int year, int month, int dayOfMonth)
    {
        m_year = year;
        m_month = month;
        m_dayOfMonth = dayOfMonth;

        Log.d("date selected", "date selected " + m_year + " " + m_month + " " + m_dayOfMonth);
        mtxtDate.setText(new StringBuilder().append(pad(m_dayOfMonth))
                .append("-").append(pad(m_month)).append("-").append(pad(m_year)));
    }

    private void DisplayTime(int hourOfDay, int minute)
    {
        m_hourOfDay = hourOfDay;
        m_minute = minute;

        Log.d("time selected", "time selected " + m_hourOfDay + " " + m_minute);
        mtxtTime.setText(new StringBuilder().append(pad(m_hourOfDay))
                .append(":").append(pad(m_minute)));
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
        time.set(Calendar.YEAR, m_year);
        time.set(Calendar.MONTH, m_month);
        time.set(Calendar.DAY_OF_MONTH, m_dayOfMonth);
        time.set(Calendar.HOUR_OF_DAY, m_hourOfDay);
        time.set(Calendar.MINUTE, m_minute);
        time.setTimeInMillis(System.currentTimeMillis());
        alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }
}