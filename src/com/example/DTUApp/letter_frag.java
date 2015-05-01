package com.example.DTUApp;


//import android.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class letter_frag extends base_frag {

    private int cnt = 0;
    private TextView tv2 = null;
    String randomLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZÆÅØ";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        final View v = inflater.inflate(R.layout.letter_frag, container, false);

        Button btnShare = (Button) v.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_act activity = (main_act) getActivity();
                activity.toNextFragment();
            }
        });

        final DecelerateInterpolator sDecelerator = new DecelerateInterpolator();
        //final OvershootInterpolator sOvershooter = new OvershootInterpolator(10f);
        tv2 = (TextView) v.findViewById(R.id.tv2);
        Random rnd = new Random();
        char s = randomLetters.charAt(rnd.nextInt(randomLetters.length()));
        tv2.setText(String.valueOf(s));
        if (Build.VERSION.SDK_INT > 11) {
            tv2.animate().setInterpolator(sDecelerator).rotationX(360).alpha(10).setDuration(5000);
        }
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tv2 = null;
    }
}
