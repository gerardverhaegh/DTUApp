package com.example.DTUApp;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by gve on 18-03-2015.
 */
public class find_location2_frag extends Fragment {
    /**
     * Inflate the layout for this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.find_location2_frag, container, false);

        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        iv.setImageResource(R.raw.find_location2);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}