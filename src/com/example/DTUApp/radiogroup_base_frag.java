package com.example.DTUApp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gerard Verhaegh on 4/30/2015.
 */
public class radiogroup_base_frag extends base_frag {

    private RadioGroup mRadioGroup = null;
    private TextView mtv = null;
    private String mTitle = "Title";
    private ArrayList<String> mButtonTexts = null;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v = inflater.inflate(R.layout.radiogroup_base_frag, container, false);

        mRadioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        mtv = (TextView) v.findViewById(R.id.radiotv);

        Log.d("GVE", "mtv: " + mtv);
        Log.d("GVE", "mRadioGroup: " + mRadioGroup);

        mButtonTexts = new ArrayList<String>();
        mButtonTexts.add("Test1");
        mButtonTexts.add("Test2");
        mButtonTexts.add("Test3");
        mButtonTexts.add("Test4");
        mButtonTexts.add("Test5");

        for (int i = 0; i < 5; i++) {
            // test adding a radio button programmatically
            RadioButton newRadioButton = new RadioButton(getActivity());
            newRadioButton.setText(mButtonTexts.get(i));
            newRadioButton.setId(i);
            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            mRadioGroup.addView(newRadioButton, i, layoutParams);
        }

        UpdateStrings();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                Log.d("GVE", "onCheckedChanged: " + checkedId);
                //global_app.GetPref().edit().putInt(constants.RADIO_HEALTH_VALUE, checkedId).commit();
            }
        });

        return v;
    }

    public void SetStrings() {
        mTitle = "Better title";
        if (mButtonTexts != null) {
            mButtonTexts.clear();
        } else {
            mButtonTexts = new ArrayList<String>();
        }
        mButtonTexts.add("Test11");
        mButtonTexts.add("Test21");
        mButtonTexts.add("Test31");
        mButtonTexts.add("Test41");
        mButtonTexts.add("Test51");
        UpdateStrings();
    }

    private void UpdateStrings() {
        if (mtv != null) {
            mtv.setText(mTitle);
        }

        Log.d("GVE", "mRadioGroup2: " + mRadioGroup);
        Log.d("GVE", "mButtonTexts: " + mButtonTexts);
        if (mRadioGroup != null && mButtonTexts != null) {
            for (int i = 0; i < 5; i++) {
                ((RadioButton) mRadioGroup.getChildAt(i)).setText(mButtonTexts.get(i));
            }
        }
    }

/*    public void onCheckedChanged(RadioGroup group, int checkedId) {
*//*        String selection = getString(R.string.radio_group_selection);
        String none = getString(R.string.radio_group_none);*//*
*//*        mChoice.setText(selection +
                (checkedId == View.NO_ID ? none : checkedId));*//*
        Log.d("GVE", "Selected: " + checkedId);
    }

    public void onClick(View v) {
        mRadioGroup.clearCheck();
    }*/
}
