package com.example.DTUApp;


//import android.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class evaluation_frag extends base_frag {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v = inflater.inflate(R.layout.evaluation_frag, container, false);

        ImageView iv = (ImageView)v.findViewById(R.id.iv);
        iv.setImageResource(R.raw.evaluation);

        RadioButton radExcellent = (RadioButton)v.findViewById(R.id.radioExcellent);
        RadioButton radVeryGood = (RadioButton)v.findViewById(R.id.radioVeryGood);
        RadioButton radGood = (RadioButton)v.findViewById(R.id.radioGood);

        switch (global_app.GetPref().getInt(constants.RADIO_HEALTH_VALUE, R.id.radioExcellent))
        {
            case R.id.radioExcellent:
                radExcellent.setChecked(true);
                break;
            case R.id.radioVeryGood:
                radVeryGood.setChecked(true);
                break;
            case R.id.radioGood:
                radGood.setChecked(true);
                break;
        }

        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioHealth);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                Log.d("GVE", "onCheckedChanged: " + checkedId);
                global_app.GetPref().edit().putInt(constants.RADIO_HEALTH_VALUE, checkedId).commit();
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}