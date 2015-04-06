package com.example.DTUApp;


//import android.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class letter_frag extends Fragment {

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
                activity.setResult("share");
            }
        });

        final DecelerateInterpolator sDecelerator = new DecelerateInterpolator();
        final OvershootInterpolator sOvershooter = new OvershootInterpolator(10f);
        tv2 = (TextView) v.findViewById(R.id.tv2);
        Random rnd = new Random();
        char s = randomLetters.charAt(rnd.nextInt(randomLetters.length()));
        tv2.setText(String.valueOf(s));
        tv2.animate().setInterpolator(sDecelerator).rotationX(360).alpha(10).setDuration(5000);


/*
                tv2.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        final int width = right - left;
/*                System.out.println("The width is == " + width);

                Log.d("GVE", "-----width=" + width);
                Log.d("GVE", "-----width=" + tv2.getMeasuredWidth());
                tv2.setText("A");
                Log.d("GVE", "-----width=" + tv2.getMeasuredWidth());
                Log.d("GVE", "-----size=" + tv2.getTextSize());
        tv2.setTextSize(width);
    };
});


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v.findViewById(R.id.tv);
                cnt++;

                switch (cnt % 4) {
                    case 0:
                        //tv.setText("B");
                        tv.animate().translationY(-100).translationX(100)
                                .setInterpolator(new OvershootInterpolator())
                                .rotationX(180).alpha(1)
                                .setDuration(750);
                        break;
                    case 1:
                        //tv.setText("C");
                        tv.animate().translationX(100).alpha(10.0f).setDuration(200);
                        break;
                    case 2:
                        //tv.setText("D");
                        tv.animate().setInterpolator(sDecelerator).scaleX(.7f).scaleY(1.3f).setDuration(500);
                        break;
                    case 3:
                        //tv.setText("E");
                        tv.animate().translationY(0).translationX(0).setInterpolator(sOvershooter).scaleX(1f).scaleY(1f).setDuration(2000);
                        break;
                }
            }
        });*/

/*        v.post(new Runnable() {
            int s = 6;
            @Override
            public void run() {
                tv2.setTextSize(s);
                s++;
                v.postDelayed(this, 10);
            }
        });*/
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        tv2 = null;
    }
}
