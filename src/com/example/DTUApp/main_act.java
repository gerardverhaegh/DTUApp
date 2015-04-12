package com.example.DTUApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

//import android.app.Fragment;
//import android.app.FragmentManager;

// added comment or checkin in GIT

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class main_act extends FragmentActivity {
    private Fragment current_frag = null;
    private int m_current_id = 0;
    private TextView m_tv = null;
    private pageradapter mPagerAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.main_act);

            TabWidget btn_speak = (TabWidget) findViewById(R.id.btn_speak);
            btn_speak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowCommunication();
                }
            });

            TabWidget btn_preferences = (TabWidget) findViewById(R.id.btn_preferences);
            btn_preferences.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), preferences_act.class);
                    startActivity(i);
                }
            });

            m_tv = (TextView) findViewById(R.id.tv);

            if (savedInstanceState == null) {
                initialisePaging();
            }
        }
    }

    private void ShowCommunication() {
        Intent i = new Intent(this, communication_viewpager_act.class);
        startActivity(i);
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();
        mPagerAdapter = new pageradapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
        addView(new start_frag());
    }

    public void addView(Fragment newPage) {
        mPagerAdapter.addView(newPage, mPagerAdapter.getCount());
/*        CharSequence title = mPagerAdapter.getPageTitle(mPagerAdapter.getCount());
        Log.d("GVE", "title: " + title);*/

        mPagerAdapter.notifyDataSetChanged();
    }

    public void setResult(String answer) {
        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof start_frag) {
            addView(new question_evaluation_frag());
        }

        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof question_evaluation_frag) {
            if (answer == "yes") {
                addView(new evaluation_frag());
            } else if (answer == "no") {
                addView(new find_location1_frag());
            }
        }

        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof evaluation_frag) {
            addView(new find_location1_frag());
        }

        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof find_location1_frag) {
            if (answer == "start") {
                addView(new map_frag());
            }
        }

        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof map_frag) {
            if (answer == "done") {
                addView(new letter_frag());
            }
        }

        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof letter_frag) {
            if (answer == "done") {
                addView(new finished_game_frag());
            }
        }
    }
}