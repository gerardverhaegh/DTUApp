package com.example.DTUApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Gerard Verhaegh on 4/14/2015.
 */
public class base_frag extends Fragment {

    protected String mFragTitle = "";
    private static int mCnt = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mCnt++;
        Log.d("GVE", "adding: " + GetTitle());

        // see what last available frag was, if current is higher, remember it
        //if (global_app.GetPref().getInt(constants.LAST_AVAILABLE_FRAGMENT, 0) < mCnt) {
            global_app.GetPref().edit().putString(constants.LAST_AVAILABLE_FRAGMENT, mFragTitle).commit();
            Log.d("GVE", "Last Fragment : " + mFragTitle);
        //}

        super.onCreate(savedInstanceState);
    }

    public String GetTitle() {
        if (this instanceof chat_frag) {
            mFragTitle = "Chat";
        } else if (this instanceof communication_frag) {
            mFragTitle = "Communication";
        } else if (this instanceof evaluation_frag) {
            mFragTitle = "Evaluation";
        } else if (this instanceof find_location1_frag) {
            mFragTitle = "Location";
        } else if (this instanceof find_location2_frag) {
            mFragTitle = "Location";
        } else if (this instanceof finished_game_frag) {
            mFragTitle = "Finished";
        } else if (this instanceof letter_frag) {
            mFragTitle = "Letter";
        } else if (this instanceof listofusers_frag) {
            mFragTitle = "Users";
        } else if (this instanceof map_frag) {
            mFragTitle = "Map";
        } else if (this instanceof question_evaluation_frag) {
            mFragTitle = "Question";
        } else if (this instanceof radiogroup_base_frag) {
            // nothing
        } else if (this instanceof random_word_frag) {
            mFragTitle = "Words";
        }else if (this instanceof start_frag) {
            mFragTitle = "Start";
        } else if (this instanceof video_frag) {
            mFragTitle = "Video";
        } else {
            mFragTitle = "no title";
        }

        return mFragTitle;
    }

    public void Notify() {
        // nothing
    }

    public void SetTitle(String title) {
        mFragTitle = title;
    }
}
