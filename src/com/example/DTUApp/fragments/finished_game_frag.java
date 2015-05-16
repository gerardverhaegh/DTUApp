package com.example.DTUApp.fragments;

//import android.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.DTUApp.R;
import com.example.DTUApp.fragments.base_frag;

/**
 * Created by gve on 18-03-2015.
 */
public class finished_game_frag extends base_frag {

    ImageView iv = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v = inflater.inflate(R.layout.finished_game_frag, container, false);

/*        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        iv.setImageResource(R.raw.finished_game);*/

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        iv = null;
    }
}