package com.example.DTUApp;


//import android.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class letter_frag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v = inflater.inflate(R.layout.letter_frag, container, false);

        TextView tv = (TextView)v.findViewById(R.id.tv);
        tv.setText("B");

        return v;
    }
}