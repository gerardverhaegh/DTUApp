package com.example.DTUApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Gerard Verhaegh on 4/14/2015.
 */
public class base_frag extends Fragment {
    protected String mTitle = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mTitle = "no title";
    }

    public String GetTitle() {
        return mTitle;
    }
}
