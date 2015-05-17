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
import com.example.DTUApp.R;
import com.example.DTUApp.gui.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Gerard Verhaegh on 4/12/2015.
 */
public class communication_viewpager_frag extends base_frag {

    private static List<Fragment> fragments = new ArrayList<Fragment>();

    private GoogleMusicAdapter mPagerAdapter = null;
    private PagerSlidingTabStrip mTitleIndicator = null;
    private ViewPager mPager = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View v = inflater.inflate(R.layout.communication_viewpager, container, false);

 /*           requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        //setContentView(R.layout.communication_viewpager);

        if (Build.VERSION.SDK_INT > 10) {
            ActionBar bar = getActivity().getActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));
        }

        initialisePaging(v);

        if (savedInstanceState == null) {
            Log.d("GVE", "communication frag savedInstanceState null");
            mPagerAdapter.clear();
            chat_frag f = new chat_frag();
            addView(f);
            Log.d("GVE", "communication frag savedInstanceState null 2");
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
        super.onDestroyView();
        Log.d("GVE", "communication frag onDestroyView");
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging(View v) {
        List<Fragment> fragments = new Vector<Fragment>();

        mPagerAdapter = new GoogleMusicAdapter(getActivity().getSupportFragmentManager());
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

    public void toNextFragment(boolean setNewest) {
        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof chat_frag) {
            addView(new listofusers_frag());
        } else if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof listofusers_frag) {
            addView(new random_word_frag());
        }

/*        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof listofusers_frag) {
            if (answer == "ok") {
                Log.d("GVE", "start video_frag");
                addView(new video_frag());
            }
        }*/
    }

    public listofusers_frag getListOfUsersFrag() {
        for (Fragment f : fragments) {
            if (f instanceof listofusers_frag) {
                Log.d("GVE", "getListOfUsersFrag found");
                return (listofusers_frag) f;
            }
        }

        Log.d("GVE", "getListOfUsersFrag not found");
        return null;
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
            //Log.d("GVE", "add fragment " + position);
            notifyDataSetChanged();
        }

        public void clear() {
            for (Fragment f : fragments)
            {
                Log.d("GVE", "f == null: " + ((base_frag)f).GetTitle());
                f = null;
            }
            fragments.clear();
            notifyDataSetChanged();
        }
    }
}
