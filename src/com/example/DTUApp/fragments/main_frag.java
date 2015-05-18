package com.example.DTUApp.fragments;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.DTUApp.R;
import com.example.DTUApp.global.constants;
import com.example.DTUApp.global.global_app;
import com.example.DTUApp.gui.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


//import android.app.Fragment;
//import android.app.FragmentManager;

// added comment or checkin in GIT

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class main_frag extends base_frag {

    private Fragment current_frag = null;
    private int m_current_id = 0;
    private TextView m_tv = null;
    //private pageradapter mPagerAdapter = null;
    private GoogleMusicAdapter mPagerAdapter = null;
    private PagerSlidingTabStrip mTitleIndicator = null;
    private ViewPager mPager = null;
    static boolean isSetting = false;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("GVE", "onCreateView main_frag-----------------------------");

        final View v = inflater.inflate(R.layout.main_act, container, false);

/*        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        //setContentView(R.layout.main_act);

        if (Build.VERSION.SDK_INT > 10) {
            ActionBar bar = getActivity().getActionBar();
        /*  bar.setTitle("");*/
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));
        }

        m_tv = (TextView) v.findViewById(R.id.tv);

        if (savedInstanceState == null) {
            Log.d("GVE", "savedInstanceState null-----------------------------");

            final List<Fragment> fragments = new Vector<Fragment>();

            mPagerAdapter = new GoogleMusicAdapter(getChildFragmentManager());
            //mPagerAdapter = new pageradapter(super.getSupportFragmentManager(), fragments);

            mPager = (ViewPager) v.findViewById(R.id.viewpager);
            mPager.setAdapter(mPagerAdapter);

            //Bind the title indicator to the adapter
            mTitleIndicator = (PagerSlidingTabStrip) v.findViewById(R.id.titles);
            mTitleIndicator.setViewPager(mPager);
            final float density = getResources().getDisplayMetrics().density;

            mTitleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
/*                Log.d("GVE", "page: " + position);
                Log.d("GVE", "---title: " + ((base_frag) mPagerAdapter.getItem(position)).GetTitle());*/


                    // this is the current shown frag
                    global_app.GetPref().edit().putString(constants.LAST_VISIBLE_FRAGMENT, ((base_frag) mPagerAdapter.getItem(position)).GetTitle()).commit();
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });


            if (savedInstanceState == null) {
                mPagerAdapter.clear();
                addView(new start_frag());
                Log.d("GVE", "Adding start_frag ----------------------------------------------");

/*                // add last available frags
                String lastTitle = global_app.GetPref().getString(constants.LAST_AVAILABLE_FRAGMENT, "no title");
                Log.d("GVE", "-----lastTitle: " + lastTitle);
                String title = ((base_frag) (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1))).GetTitle();

                if (!lastTitle.equals("no title")) {
                    int cnt = 0; // avoid no response
                    while (!lastTitle.equals(title) && cnt < 100) {
                        toNextFragment(false);
                        title = ((base_frag) (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1))).GetTitle();
                        Log.d("GVE", "title: " + title);
                        cnt++;
                    }
                }

                // show last visible frag
                String lastVisibleTitle = global_app.GetPref().getString(constants.LAST_VISIBLE_FRAGMENT, "no title");

                Log.d("GVE", "lastVisibleTitle: " + lastVisibleTitle);
                if (!lastVisibleTitle.equals("no title")) {
                    for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                        Log.d("GVE", "title: " + ((base_frag) mPagerAdapter.getItem(i)).GetTitle());
                        if (((base_frag) mPagerAdapter.getItem(i)).GetTitle().equals(lastVisibleTitle)) {
                            mPager.setCurrentItem(i);
                            mPagerAdapter.notifyDataSetChanged();
                            mTitleIndicator.notifyDataSetChanged();
                            Log.d("GVE", "lastVisibleTitle: found");
                            break;
                        }
                    }
                }*/
            }
        }

        return v;
    }

    @Override
    public void onDestroyView()
    {
        mPagerAdapter.clear();
        mPagerAdapter = null;
        mTitleIndicator = null;
        mPager = null;
        fragments = null;
        super.onDestroyView();
        Log.d("GVE", "main_frag onDestroyView");
    }

    public void addView(Fragment newPage) {
        //Log.d("GVE", "addView : " + cnt + "-" + ((base_frag) newPage).GetTitle());
        mPagerAdapter.addView(newPage, mPagerAdapter.getCount());
        mTitleIndicator.notifyDataSetChanged();
    }

    public void toNextFragment(boolean setNewest) {
        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof start_frag) {
            addView(new find_location1_frag());
        } else if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof find_location1_frag) {
            addView(new map_frag());
        } else if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof map_frag) {
            addView(new letter_frag());
        } else if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof letter_frag) {
            addView(new finished_game_frag());
        } else {
            //nothing
        }
/*
        if (setNewest && !isSetting) {
            isSetting = true;
            SetNewestFrag();
            isSetting = false;
        }*/
    }

    private void SetNewestFrag() {
        Log.d("GVE", "Setting");
        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
        mPagerAdapter.notifyDataSetChanged();
        mTitleIndicator.notifyDataSetChanged();
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
            //Log.d("GVE", "getPageTitle " + position);
            if (fragments.get(position) instanceof base_frag) {
                String title = ((base_frag) fragments.get(position)).GetTitle();
                //Log.d("GVE", "title: " + title);
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
            //Log.d("GVE", "----add fragment " + ((base_frag) v).GetTitle());
            notifyDataSetChanged();
        }

        public void clear() {
            fragments.clear();
            notifyDataSetChanged();
        }
    }
}
