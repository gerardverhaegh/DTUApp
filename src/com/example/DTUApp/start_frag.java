package com.example.DTUApp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

        return v;
    }


}