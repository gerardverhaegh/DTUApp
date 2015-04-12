package com.example.DTUApp;

/**
 *
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.Vector;

/**
 * The <code>ViewPagerFragmentActivity</code> class is the fragment activity hosting the ViewPager
 * @author mwho
 */
public class viewpagerfragment_act extends FragmentActivity {
    /** maintains the pager adapter*/
    private pageradapter mPagerAdapter;
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.viewpager);
        //initialsie the pager
        this.initialisePaging();
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, communication_frag.class.getName()));
        fragments.add(Fragment.instantiate(this, find_location1_frag.class.getName()));
        fragments.add(Fragment.instantiate(this, find_location2_frag.class.getName()));
        this.mPagerAdapter  = new pageradapter(super.getSupportFragmentManager(), fragments);
        //
        ViewPager pager = (ViewPager)super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
    }
}
