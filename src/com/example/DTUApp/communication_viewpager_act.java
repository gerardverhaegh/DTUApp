package com.example.DTUApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.Vector;

/**
 * Created by Gerard Verhaegh on 4/12/2015.
 */
public class communication_viewpager_act extends FragmentActivity {

    private pageradapter mPagerAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
/*            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

            setContentView(R.layout.communication_viewpager);
            initialisePaging();
        }
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, communication_frag.class.getName()));
        mPagerAdapter = new pageradapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
    }

    public void addView(Fragment newPage) {
        mPagerAdapter.addView(newPage, mPagerAdapter.getCount());
/*        CharSequence title = mPagerAdapter.getPageTitle(mPagerAdapter.getCount());
        Log.d("GVE", "title: " + title);*/

        mPagerAdapter.notifyDataSetChanged();
    }

    public void setResult(String answer) {
        if (mPagerAdapter.getItem(mPagerAdapter.getCount() - 1) instanceof communication_frag) {
            if (answer == "ok") {
                addView(new listofusers_frag());
            }
        }
    }
}
