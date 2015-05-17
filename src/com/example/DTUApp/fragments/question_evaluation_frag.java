package com.example.DTUApp.fragments;


//import android.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.example.DTUApp.R;
import com.example.DTUApp.global.constants;
import com.example.DTUApp.global.global_app;

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class question_evaluation_frag extends base_frag {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v = inflater.inflate(R.layout.question_evaluation_frag, container, false);

        Log.d("GVE", "question_evaluation_frag onCreateView---------");

        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        iv.setImageResource(R.drawable.question_evaluation);

        Button btn_yes = (Button) v.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GVE", "btn_yes pressed");
                global_app.GetPref().edit().putString(constants.CHOSE_EVALUATION, "yes").commit();
                ToNextFragment();
            }
        });

        Button btn_no = (Button) v.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GVE", "btn_no pressed");
                global_app.GetPref().edit().putString(constants.CHOSE_EVALUATION, "no").commit();
                ToNextFragment();
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}