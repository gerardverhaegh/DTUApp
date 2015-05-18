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
    private boolean mHasChosen = false;

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
        if (mHasChosen) {
            if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.openDrawer(mDrawerList);
                mHasChosen = false;
                return;
            }
        }
        super.finish();
    }

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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            /*Log.d("GVE", "position2: " + position);*/
            selectItem(position);
            mHasChosen = true;
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
