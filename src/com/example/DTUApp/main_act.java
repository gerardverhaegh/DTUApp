package com.example.DTUApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TabWidget;
import android.widget.TextView;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;
import java.util.Vector;


//import android.app.Fragment;
//import android.app.FragmentManager;

// added comment or checkin in GIT

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class main_act extends FragmentActivity {
    private static final String[] CONTENT = new String[]{"Recent", "Artists", "Albums", "Songs", "Playlists", "Genres"};

    private Fragment current_frag = null;
    private int m_current_id = 0;
    private TextView m_tv = null;
    //private pageradapter mPagerAdapter = null;
    private GoogleMusicAdapter mPagerAdapter = null;
    private TabPageIndicator mTitleIndicator = null;
    private ViewPager mPager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
/*            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

            setContentView(R.layout.main_act);

            TabWidget btn_speak = (TabWidget) findViewById(R.id.btn_speak);
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
            });

            m_tv = (TextView) findViewById(R.id.tv);

            //if (savedInstanceState == null)
            {
                initialisePaging();
            }
        }
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();

        mPagerAdapter = new GoogleMusicAdapter(getSupportFragmentManager(), fragments);
        //mPagerAdapter = new pageradapter(super.getSupportFragmentManager(), fragments);

        mPager = (ViewPager) super.findViewById(R.id.viewpager);
        mPager.setAdapter(mPagerAdapter);
/*        addView(new find_location1_frag());
        addView(new finished_game_frag());
        addView(new letter_frag());
        addView(new map_frag());
        addView(new evaluation_frag());
        addView(new question_evaluation_frag());*/

        //Bind the title indicator to the adapter
        mTitleIndicator = (TabPageIndicator) findViewById(R.id.titles);
        mTitleIndicator.setViewPager(mPager);
        addView(new start_frag());
        final float density = getResources().getDisplayMetrics().density;
        mTitleIndicator.setBackgroundColor(0xFFFF0000);
        /*mTitleIndicator.setFooterColor(0xFFAA2222);
        mTitleIndicator.setFooterLineHeight(1 * density);
        mTitleIndicator.setFooterIndicatorHeight(3 * density);
        mTitleIndicator.setFooterIndicatorStyle(TabPageIndicator.);
        mTitleIndicator.setTextColor(0xAA000000);
        mTitleIndicator.setSelectedColor(0xFF000000);
        mTitleIndicator.setSelectedBold(true);*/


        mTitleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d("GVE", "page: " + position);

/*                TestFragmentPage current = (TestFragmentPage)mPagerAdapter.getItem(position);
                current.onPageSelected();*/

                // Store the position of current page
                //PrefUtils.setInt(MainActivity.this, R.string.pref_last_tab, position);
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
        mPagerAdapter.addView(newPage, mPagerAdapter.getCount());
/*        CharSequence title = mPagerAdapter.getPageTitle(mPagerAdapter.getCount());
        Log.d("GVE", "title: " + title);*/

        mPagerAdapter.notifyDataSetChanged();
        mTitleIndicator.notifyDataSetChanged();
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

/*    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TestFragment2.newInstance(CONTENT[position % CONTENT.length]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }*/

    class GoogleMusicAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public GoogleMusicAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            //return TestFragment2.newInstance(CONTENT[position % CONTENT.length]);
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Log.d("GVE", "getPageTitle " + position);
            if (fragments.get(position) instanceof base_frag)
            {
                return ((base_frag) fragments.get(position)).GetTitle();
            }
            else
            {
                return "no base_frag";
            }
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        //@Override
        public void addView(Fragment v, int position) {
            if (fragments != null) {
                fragments.add(position, v);
                Log.d("GVE", "add fragment " + position);
            }
        }
    }
}
