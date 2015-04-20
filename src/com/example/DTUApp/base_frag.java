package com.example.DTUApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Gerard Verhaegh on 4/14/2015.
 */
public class base_frag extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String GetTitle() {

        if (this instanceof communication_frag) {
            return "Communication";
        } else if (this instanceof evaluation_frag) {
            return "Evaluation";
        } else if (this instanceof find_location1_frag) {
            return "Location";
        } else if (this instanceof find_location2_frag) {
            return "Location";
        } else if (this instanceof finished_game_frag) {
            return "Finished";
        } else if (this instanceof letter_frag) {
            return "Letter";
        } else if (this instanceof listofusers_frag) {
            return "Users";
        } else if (this instanceof map_frag) {
            return "Map";
        } else if (this instanceof question_evaluation_frag) {
            return "Question";
        } else if (this instanceof start_frag) {
            return "Start";
        } else {
            return "no title";
        }
    }

    public void Notify() {
        // nothing
    }
}
