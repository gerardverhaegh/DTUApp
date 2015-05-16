package com.example.DTUApp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.DTUApp.R;
import com.example.DTUApp.global.constants;
import com.example.DTUApp.global.global_app;
import com.example.DTUApp.activities.main_act;

//import android.app.Fragment;

/**
 * Created by gve on 18-03-2015.
 */
public class find_location1_frag extends base_frag {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.find_location1_frag, container, false);

        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        iv.setImageResource(R.drawable.find_location1);

        RadioButton radioMost = (RadioButton)v.findViewById(R.id.radioMost);
        RadioButton radioAvg = (RadioButton)v.findViewById(R.id.radioAvg);
        RadioButton radioLeast = (RadioButton)v.findViewById(R.id.radioLeast);

        switch (global_app.GetPref().getInt(constants.RADIO_KM_TO_WALK, R.id.radioMost))
        {
            case R.id.radioMost:
                radioMost.setChecked(true);
                break;
            case R.id.radioAvg:
                radioAvg.setChecked(true);
                break;
            case R.id.radioLeast:
                radioLeast.setChecked(true);
                break;
        }

        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioDistance);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                Log.d("GVE", "onCheckedChanged: " + checkedId);
                int dist = 5;
                switch(checkedId)
                {
                    case R.id.radioMost:
                        dist = 5;
                        break;
                    case R.id.radioAvg:
                        dist = 3;
                        break;
                    case R.id.radioLeast:
                        dist = 1;
                        break;
                }
                global_app.GetPref().edit().putInt(constants.RADIO_KM_TO_WALK, checkedId).putInt(constants.KM_TO_WALK, dist).commit();
            }
        });

        Button btnStart = (Button) v.findViewById(R.id.btnStartJagt);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_act activity = (main_act) getActivity();
                activity.toNextFragment(true);
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}