package com.example.DTUApp;

import android.app.ActionBar;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Gerard Verhaegh on 4/12/2015.
 */
public class communication_viewpager_act extends FragmentActivity {

    private static List<Fragment> fragments = new ArrayList<Fragment>();

    private GoogleMusicAdapter mPagerAdapter = null;
    private PagerSlidingTabStrip mTitleIndicator = null;
    private ViewPager mPager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 /*           requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.communication_viewpager);

        if (Build.VERSION.SDK_INT > 10) {
            ActionBar bar = getActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));
        }

        initialisePaging();
        if (savedInstanceState == null) {
            fragments.clear();
            addView(new chat_frag());
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

    private void addView(Fragment newPage) {
        mPagerAdapter.addView(newPage, mPagerAdapter.getCount());
        mTitleIndicator.notifyDataSetChanged();
    }

    public void setResult(String answer) {
        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof chat_frag) {
            if (answer == "ok") {
                Log.d("GVE", "start listofusers_frag");
                addView(new listofusers_frag());
            }
        }

/*        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof listofusers_frag) {
            if (answer == "ok") {
                Log.d("GVE", "start video_frag");
                addView(new video_frag());
            }
        }*/

/*        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof chat_frag) {
            if (answer == "ok") {
                Log.d("GVE", "start video_frag");
                addView(new video_frag());
            }
        }*/
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
