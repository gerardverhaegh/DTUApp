package com.example.DTUApp.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.DTUApp.R;
import com.example.DTUApp.fragments.communication_viewpager_frag;
import com.example.DTUApp.fragments.main_eval_frag;
import com.example.DTUApp.fragments.main_frag;

import java.util.ArrayList;
import java.util.List;


//import android.app.Fragment;
//import android.app.FragmentManager;

// added comment or checkin in GIT

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class drawer_layout_act extends FragmentActivity {

    private Fragment current_frag = null;
    private int m_current_id = 0;
    private TextView m_tv = null;
    //private pageradapter mPagerAdapter = null;
    //private GoogleMusicAdapter mPagerAdapter = null;
    //private PagerSlidingTabStrip mTitleIndicator = null;
    //private ViewPager mPager = null;
    static boolean isSetting = false;

    private static List<Fragment> fragments = new ArrayList<Fragment>();

    private String[] mTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private Bundle mSavedInstanceState = null;

    Fragment mFragment = null;
    int m_position = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.drawer_layout);

        if (Build.VERSION.SDK_INT > 10) {
            ActionBar bar = getActionBar();
        /*  bar.setTitle("");*/
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));
        }

        m_tv = (TextView) findViewById(R.id.tv);

        // init drawer layout
        mTitles = new String[]{getString(R.string.PlayGame), getString(R.string.Evaluation), getString(R.string.Communication)};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        /*mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mTitles));*/
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mSavedInstanceState = savedInstanceState;



        mDrawerLayout.openDrawer(mDrawerList);

        //initialisePaging(savedInstanceState);
    }

    @Override
    public void finish() {
        if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.openDrawer(mDrawerList);
            return;
        }
        super.finish();
    }

/*
    private void AddEvaluationFrags() {
        AddOneEvaluation(false, "Evaluation", "Answer every question by selecting the answer as indicated. If you are unsure about how to answer a question, please give the best answer you can.", null);

        // 1
        ArrayList<String> s = new ArrayList<String>() {{
            add("Excellent");
            add("Very good");
            add("Good");
            add("Fair");
            add("Poor");
        }};

        AddOneEvaluation(false, "Evaluation 1", "1. In general, would you say your health is:", s);

        // 2
        s = new ArrayList<String>() {{
            add("Yes, a lot");
            add("Yes, a little");
            add("Not limited at all");
        }};

        AddOneEvaluation(false, "Evaluation 2a", "2. a). Does your health now limit you in moderate activities:", s);

        // 3
        s = new ArrayList<String>() {{
            add("Yes, a lot");
            add("Yes, a little");
            add("Not limited at all");
        }};

        AddOneEvaluation(false, "Evaluation 2b", "2. b) Does your health now limit you in climbing several flights of stairs:", s);

        // 4
        AddOneEvaluation(false, "Evaluation 3a", "3. a) During the past 4 weeks, have you as a result of your physical health accomplished less than you would like?", s);

        // 5
        AddOneEvaluation(false, "Evaluation 3b", "3. b) During the past 4 weeks, have you as a result of your physical health been limited in the kind of work or activities you could do?", s);

        // 6
        AddOneEvaluation(false, "Evaluation 4a", "4. a) During the past 4 weeks, have you as as as result of any emotional problems accomplished less than you would like?", s);

        // 7
        AddOneEvaluation(false, "Evaluation 4b", "4. b) During the past 4 weeks, have you as as as result of any emotional problems done work or other activities less carefully than usual?", s);

        // 8
        s = new ArrayList<String>() {{
            add("Not at all");
            add("A little bit");
            add("Moderately");
            add("Quite a bit");
            add("Extremely");
        }};

        AddOneEvaluation(false, "Evaluation 5", "5. During the past 4 weeks, how much did pain interfere with your normal activities?", s);

        // 9
        s = new ArrayList<String>() {{
            add("All of the time");
            add("Most of the time");
            add("A good bit of time");
            add("Some time");
            add("A little time");
            add("None");
        }};

        AddOneEvaluation(false, "Evaluation 6a", "6. a) How much of the time during the past 4 weeks have you felt calm and peaceful?", s);

        // 10
        AddOneEvaluation(false, "Evaluation 6b", "6. b) How much of the time during the past 4 weeks did you have a lot of energy?", s);

        // 11
        AddOneEvaluation(false, "Evaluation 6c", "6. c) How much of the time during the past 4 weeks have you felt downhearted and blue?", s);

        // 11
        AddOneEvaluation(true, "Evaluation 7", "7. During the past 4 weeks, how much of the time has your physical health or emotional problems interfered with your social activities (like visiting friends, relatives, etc.)?", s);
    }
*/

/*    private void AddOneEvaluation(boolean bLastEval, String header, String text, ArrayList<String> buttons) {
        radiogroup_base_frag f = new radiogroup_base_frag();

        //Log.d("GVE", "header: " + header);

        Bundle bundle = new Bundle();
        bundle.putString("header", header);
        bundle.putString("text", text);

        bundle.putStringArrayList("buttons", buttons);
        bundle.putBoolean("bLastEval", bLastEval);

        f.SetTitle(header); // header comes too late via bundle arguments
        f.IsLastEvaluation(bLastEval); // header comes too late via bundle arguments
        f.setArguments(bundle);
        addView(f);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        Log.d("GVE", "Menu CREATED");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("GVE", "Menu SELECTED");
        switch (item.getItemId()) {
            case R.id.settings:
                Log.d("GVE", "Menu SELECTED 1");
                Intent i1 = new Intent(getApplicationContext(), preferences_act.class);
                startActivity(i1);
                return true;
/*            case R.id.communication:
                Intent i2 = new Intent(getApplicationContext(), communication_viewpager_act.class);
                startActivity(i2);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initialise the fragments to be paged
     */
/*    private void initialisePaging() {
        final List<Fragment> fragments = new Vector<Fragment>();

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
*//*                Log.d("GVE", "page: " + position);
                Log.d("GVE", "---title: " + ((base_frag) mPagerAdapter.getItem(position)).GetTitle());*//*


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


        if (mSavedInstanceState == null) {
            fragments.clear();
            addView(new start_frag());

            // add last available frags
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
            }
        }
    }*/

/*    public void addView(Fragment newPage) {
        //Log.d("GVE", "addView : " + cnt + "-" + ((base_frag) newPage).GetTitle());
        mPagerAdapter.addView(newPage, mPagerAdapter.getCount());
        mTitleIndicator.notifyDataSetChanged();
    }*/

/*    public void toNextFragment(boolean setNewest) {
        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof start_frag) {
            addView(new question_evaluation_frag());
        } else if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof question_evaluation_frag) {
            String choice = global_app.GetPref().getString(constants.CHOSE_EVALUATION, "not yet");
            if (choice.equals("yes")) {
                AddEvaluationFrags();
            } else if (choice.equals("no")) {
                addView(new find_location1_frag());
            }
        } else if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof radiogroup_base_frag) {
            if (((radiogroup_base_frag) mPagerAdapter.getItem(mPagerAdapter.getCount() - 1)).IsLastEvaluation()) {
                addView(new find_location1_frag());
            }
        } else if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof find_location1_frag) {
            addView(new map_frag());
        } else if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof map_frag) {
            addView(new letter_frag());
        } else if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof letter_frag) {
            addView(new finished_game_frag());
        } else {
            //nothing
        }
*//*
        if (setNewest && !isSetting) {
            isSetting = true;
            SetNewestFrag();
            isSetting = false;
        }*//*
    }*/

/*    private void SetNewestFrag() {
        Log.d("GVE", "Setting");
        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
        mPagerAdapter.notifyDataSetChanged();
        mTitleIndicator.notifyDataSetChanged();
    }*/

 /*   class GoogleMusicAdapter extends FragmentPagerAdapter {
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
    }*/

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            /*Log.d("GVE", "position2: " + position);*/
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {

       /* Log.d("GVE", "position: " + position);*/

        m_position = position;

        final FragmentManager fragmentManager = getSupportFragmentManager();

/*        if (mFragment != null) {
            fragmentManager.beginTransaction()
                    .detach(mFragment)
                    .commit();
        }*/

        switch (position)
        {
            case 0:
                mFragment = null;
                mFragment = new main_frag();
                break;
            case 1:
                mFragment = null;
                mFragment = new main_eval_frag();
                break;
            case 2:
                mFragment = null;
                mFragment = new communication_viewpager_frag();
                break;
        }

        if (mFragment != null) {

            Log.d("GVE", "selectItem fm  -----------------------------");
/*        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);*/

            // Insert the fragment by replacing any existing fragment

            new AsyncTask() {
                @Override
                protected Object doInBackground(Object... arg0) {
                    try {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, mFragment)
                                .commit();

                        return "OK";
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Error: " + e;
                    }
                }

                @Override
                protected void onPostExecute(Object result) {

                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(m_position, true);
                    setTitle(mTitles[m_position]);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            }.execute();
        }
    }

/*    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }*/

    public Fragment GetFrag() {
        return mFragment;
    }
}
