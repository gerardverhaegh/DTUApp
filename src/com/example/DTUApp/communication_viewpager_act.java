package com.example.DTUApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Gerard Verhaegh on 4/12/2015.
 */
public class communication_viewpager_act extends FragmentActivity {

    private static List<Fragment> fragments = new ArrayList<Fragment>();

    private GoogleMusicAdapter mPagerAdapter = null;
    private TabPageIndicator mTitleIndicator = null;
    private ViewPager mPager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if (savedInstanceState == null) {
/*            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

            setContentView(R.layout.communication_viewpager);
        //}

        initialisePaging();

        if (savedInstanceState == null) {
            fragments.clear();
            addView(new communication_frag());
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
/*        addView(new find_location1_frag());
        addView(new finished_game_frag());
        addView(new letter_frag());
        addView(new map_frag());
        addView(new evaluation_frag());
        addView(new question_evaluation_frag());*/

        //Bind the title indicator to the adapter
        mTitleIndicator = (TabPageIndicator) findViewById(R.id.titles);
        mTitleIndicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        //mTitleIndicator.setBackgroundColor(0x007777FF);
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

        //Log.d("GVE", "addView : " + cnt + "-" + ((base_frag) newPage).GetTitle());
        mPagerAdapter.addView(newPage, mPagerAdapter.getCount());
/*        CharSequence title = mPagerAdapter.getPageTitle(mPagerAdapter.getCount());
        Log.d("GVE", "title: " + title);*/

        mTitleIndicator.notifyDataSetChanged();
    }

    public void setResult(String answer) {
        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof communication_frag) {
            if (answer == "ok") {
                Log.d("GVE", "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                addView(new listofusers_frag());
            }
        }
    }

    class GoogleMusicAdapter extends FragmentPagerAdapter {


        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return TestFragment2.newInstance(CONTENT[position % CONTENT.length]);
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


        //@Override
        public void addView(Fragment v, int position) {

            fragments.add(position, v);
            Log.d("GVE", "add fragment " + position);
            notifyDataSetChanged();
        }
    }
}
