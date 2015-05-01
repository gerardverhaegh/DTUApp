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
    private String mTitle = "No title";
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

        Bundle bundle = getArguments();
        mFragTitle = bundle.getString("header");
        //Log.d("GVE", "mFragTitle: " + mFragTitle);
        mTitle = bundle.getString("text");
        mButtonTexts = bundle.getStringArrayList("buttons");

        for (int i = 0; i < mButtonTexts.size(); i++) {
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

    private void UpdateStrings() {
        if (mtv != null) {
            mtv.setText(mTitle);
        }

        if (mRadioGroup != null && mButtonTexts != null) {
            for (int i = 0; i < mButtonTexts.size(); i++) {
                ((RadioButton) mRadioGroup.getChildAt(i)).setText(mButtonTexts.get(i));
            }
        }
    }
}
