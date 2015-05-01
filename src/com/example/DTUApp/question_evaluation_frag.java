package com.example.DTUApp;


//import android.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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

        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        iv.setImageResource(R.raw.question_evaluation);

        Button btn_yes = (Button) v.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GVE", "btn_yes pressed");
                global_app.GetPref().edit().putString(constants.CHOSE_EVALUATION, "yes").commit();
                main_act activity = (main_act) getActivity();
                activity.toNextFragment(true);
            }
        });

        Button btn_no = (Button) v.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GVE", "btn_no pressed");
                global_app.GetPref().edit().putString(constants.CHOSE_EVALUATION, "no").commit();
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