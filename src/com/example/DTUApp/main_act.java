package com.example.DTUApp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabWidget;

// added comment or checkin in GIT
/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class main_act extends Activity {
    private Fragment current_frag = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main_act);

        TabWidget btn_backward = (TabWidget)findViewById(R.id.btn_backward);
        btn_backward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GetPreviousFragment();
            }
        });

        TabWidget btn_forward = (TabWidget)findViewById(R.id.btn_forward);
        btn_forward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                GetNextFragment();
            }
        });

        TabWidget btn_speak = (TabWidget)findViewById(R.id.btn_speak);
        btn_speak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                current_frag = new communication_frag();
                SetFragment();
            }
        });

        if (savedInstanceState == null) {
            current_frag = new start_frag();
            SetFragment();

            global_app ga = global_app.getInstance();
        }
    }

    private void GetNextFragment() {

        if (current_frag instanceof start_frag)
        {
            current_frag = new question_evaluation_frag();
            SetFragment();
        }
        else if (current_frag instanceof question_evaluation_frag)
        {
            current_frag = new evaluation_frag();
            SetFragment();
        }
        else if (current_frag instanceof evaluation_frag)
        {
            current_frag = new find_location1_frag();
            SetFragment();
        }
        else if (current_frag instanceof find_location1_frag)
        {
            current_frag = new find_location2_frag();
            SetFragment();
        }
    }

    private void GetPreviousFragment() {
        current_frag = new start_frag();
        SetFragment();
    }

    private void SetFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, current_frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setAnswer(String answer)
    {
        if (current_frag instanceof question_evaluation_frag)
        {
            if (answer == "yes")
            {
                current_frag = new evaluation_frag();
                SetFragment();
            }
            else if (answer == "no")
            {
                current_frag = new find_location1_frag();
                SetFragment();
            }
        }
    }
}