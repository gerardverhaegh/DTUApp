package com.example.DTUApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Gerard Verhaegh on 4/14/2015.
 */
public class base_frag extends Fragment {

    protected String mFragTitle = "no frag title";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String GetTitle() {

        if (this instanceof chat_frag) {
            mFragTitle = "Chat";
        } else if (this instanceof communication_frag) {
            mFragTitle =  "Communication";
        } else if (this instanceof evaluation_frag) {
            mFragTitle =  "Evaluation";
        } else if (this instanceof find_location1_frag) {
            mFragTitle =  "Location";
        } else if (this instanceof find_location2_frag) {
            mFragTitle =  "Location";
        } else if (this instanceof finished_game_frag) {
            mFragTitle =  "Finished";
        } else if (this instanceof letter_frag) {
            mFragTitle =  "Letter";
        } else if (this instanceof listofusers_frag) {
            mFragTitle =  "Users";
        } else if (this instanceof map_frag) {
            mFragTitle =  "Map";
        } else if (this instanceof question_evaluation_frag) {
            mFragTitle =  "Question";
        } else if (this instanceof radiogroup_base_frag) {
            //mFragTitle =  "RadioGroup";
        } else if (this instanceof start_frag) {
            mFragTitle =  "Start";
        } else if (this instanceof video_frag) {
            mFragTitle =  "Video";
        } else {
            mFragTitle =  "no title";
        }

        return mFragTitle;
    }

    public void Notify() {
        // nothing
    }
}
