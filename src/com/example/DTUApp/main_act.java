package com.example.DTUApp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
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
    //private pageradapter mPagerAdapter = null;
    private GoogleMusicAdapter mPagerAdapter = null;
    private PagerSlidingTabStrip mTitleIndicator = null;
    private ViewPager mPager = null;
    private int cnt = 0;

    private static List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.main_act);

        if (Build.VERSION.SDK_INT > 10) {
            ActionBar bar = getActionBar();
        /*  bar.setTitle("");*/
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));

        }
/*            TabWidget btn_speak = (TabWidget) findViewById(R.id.btn_speak);
            btn_speak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), communication_viewpager_act.class);
                    startActivity(i);
                }
            });

            TabWidget btn_preferences = (TabWidget) findViewById(R.id.btn_preferences);
            btn_preferences.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), preferences_act.class);
                    startActivity(i);
                }
            });*/

        m_tv = (TextView) findViewById(R.id.tv);

        initialisePaging();

        if (savedInstanceState == null) {
            fragments.clear();
            addView(new start_frag());
        }
    }

    private void AddEvaluationFrags() {
        // 1
        ArrayList<String> s = new ArrayList<String>() {{
            add("Excellent");
            add("Very good");
            add("Good");
            add("Fair");
            add("Poor");
        }};

        AddOneEvaluation("Evaluation 1", "1. In general, would you say your health is:", s);

        // 2
        s = new ArrayList<String>() {{
            add("Yes, a lot");
            add("Yes, a little");
            add("Not limited at all");
        }};

        AddOneEvaluation("Evaluation 2a", "2. a). Does your health now limit you in moderate activities:", s);

        // 3
        s = new ArrayList<String>() {{
            add("Yes, a lot");
            add("Yes, a little");
            add("Not limited at all");
        }};

        AddOneEvaluation("Evaluation 2b", "2. b) Does your health now limit you in climbing several flights of stairs:", s);

        // 4
        AddOneEvaluation("Evaluation 3a", "3. a) During the past 4 weeks, have you as a result of your physical health accomplished less than you would like?", s);

        // 5
        AddOneEvaluation("Evaluation 3b", "3. b) During the past 4 weeks, have you as a result of your physical health been limited in the kind of work or activities you could do?", s);

        // 6
        AddOneEvaluation("Evaluation 4a", "4. a) During the past 4 weeks, have you as as as result of any emotional problems accomplished less than you would like?", s);

        // 7
        AddOneEvaluation("Evaluation 4b", "4. b) During the past 4 weeks, have you as as as result of any emotional problems done work or other activities less carefully than usual?", s);

        // 8
        s = new ArrayList<String>() {{
            add("Not at all");
            add("A little bit");
            add("Moderately");
            add("Quite a bit");
            add("Extremely");
        }};

        AddOneEvaluation("Evaluation 5", "5. During the past 4 weeks, how much did pain interfere with your normal activities?", s);

        // 9
        s = new ArrayList<String>() {{
            add("All of the time");
            add("Most of the time");
            add("A good bit of time");
            add("Some time");
            add("A little time");
            add("None");
        }};

        AddOneEvaluation("Evaluation 6a", "6. a) How much of the time during the past 4 weeks have you felt calm and peaceful?", s);

        // 10
        AddOneEvaluation("Evaluation 6b", "6. b) How much of the time during the past 4 weeks did you have a lot of energy?", s);

        // 11
        AddOneEvaluation("Evaluation 6c", "6. c) How much of the time during the past 4 weeks have you felt downhearted and blue?", s);

        // 11
        AddOneEvaluation("Evaluation 6c", "7. During the past 4 weeks, how much of the time has your physical health or emotional problems interfered with your social activities (like visiting friends, relatives, etc.)?", s);
    }

    private void AddOneEvaluation(String header, String text, ArrayList<String> buttons) {
        radiogroup_base_frag f = new radiogroup_base_frag();

        //Log.d("GVE", "header: " + header);

        Bundle bundle = new Bundle();
        bundle.putString("header", header);
        bundle.putString("text", text);

        bundle.putStringArrayList("buttons", buttons);

        f.setArguments(bundle);
        addView(f);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i1 = new Intent(getApplicationContext(), preferences_act.class);
                startActivity(i1);
                return true;
            case R.id.communication:
                Intent i2 = new Intent(getApplicationContext(), communication_viewpager_act.class);
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();

        mPagerAdapter = new GoogleMusicAdapter(getSupportFragmentManager());
        //mPagerAdapter = new pageradapter(super.getSupportFragmentManager(), fragments);

        mPager = (ViewPager) super.findViewById(R.id.viewpager);
        mPager.setAdapter(mPagerAdapter);

        //Bind the title indicator to the adapter
        mTitleIndicator = (PagerSlidingTabStrip) findViewById(R.id.titles);
        mTitleIndicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;

        mTitleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d("GVE", "page: " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void addView(Fragment newPage) {

        //Log.d("GVE", "addView : " + cnt + "-" + ((base_frag) newPage).GetTitle());
        mPagerAdapter.addView(newPage, mPagerAdapter.getCount());

        cnt++;
        mTitleIndicator.notifyDataSetChanged();
    }

    public void setResult(String answer) {
        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof start_frag) {
            addView(new question_evaluation_frag());
        }

        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof question_evaluation_frag) {
            if (answer == "yes") {
                AddEvaluationFrags();
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

    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Log.d("GVE", "getPageTitle " + position);
            if (fragments.get(position) instanceof base_frag) {
                String title = ((base_frag) fragments.get(position)).GetTitle();
                Log.d("GVE", "title: " + title);
                return title;
            } else {
                return "no base_frag";
            }
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addView(Fragment v, int position) {

            fragments.add(position, v);
            Log.d("GVE", "add fragment " + position);
            notifyDataSetChanged();
        }
    }
}
