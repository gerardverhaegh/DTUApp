package com.example.DTUApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

//import android.app.Fragment;
//import android.app.FragmentManager;

// added comment or checkin in GIT

/**
 * Created by Gerard Verhaegh on 3/14/2015.
 */
public class main_act extends FragmentActivity {
    private Fragment current_frag = null;
    private int m_current_id = 0;
    private TextView m_tv = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main_act);

        TabWidget btn_backward = (TabWidget) findViewById(R.id.btn_backward);
        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPreviousFragment();
            }
        });

        TabWidget btn_forward = (TabWidget) findViewById(R.id.btn_forward);
        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNextFragment();
            }
        });

/*        TabWidget btn_speak = (TabWidget) findViewById(R.id.btn_speak);
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_frag = new communication_frag();
                SetFragment();
            }
        });*/

        TabWidget btn_preferences = (TabWidget) findViewById(R.id.btn_preferences);
        btn_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), preferences_act.class);
                startActivity(i);
            }
        });

        m_tv = (TextView)findViewById(R.id.tv);

        if (savedInstanceState == null) {
            current_frag = GetLastFragment();
            if (current_frag == null) {
                current_frag = new communication_frag();
            }
            SetFragment();
        }
    }

    private Fragment GetLastFragment() {
        switch (global_app.GetPref().getInt(constants.LAST_FRAGMENT, -1)) {
            case -1:
                return null;
            case 0:
                return new communication_frag();
            case 1:
                return new listofusers_frag();
            case 2:
                return new start_frag();
            case 3:
                return new question_evaluation_frag();
            case 4:
                return new evaluation_frag();
            case 5:
                return new find_location1_frag();
            case 6:
                return new map_frag();
            case 7:
                return new letter_frag();
            case 8:
                return new finished_game_frag();
            default:
                Toast.makeText(getApplicationContext(), "GetLastFragment: Remember to add this fragment", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void SetLastFragment() {
        if (current_frag instanceof communication_frag) {
            m_current_id = 0;
        } else if (current_frag instanceof listofusers_frag) {
            m_current_id = 1;
        } else if (current_frag instanceof start_frag) {
            m_current_id = 2;
        } else if (current_frag instanceof question_evaluation_frag) {
            m_current_id = 3;
        } else if (current_frag instanceof evaluation_frag) {
            m_current_id = 4;
        } else if (current_frag instanceof find_location1_frag) {
            m_current_id = 5;
        } else if (current_frag instanceof map_frag) {
            m_current_id = 6;
        } else if (current_frag instanceof letter_frag) {
            m_current_id = 7;
        } else if (current_frag instanceof finished_game_frag) {
            m_current_id = 8;
        } else {
            Toast.makeText(getApplicationContext(), "SetLastFragment: Remember to add this fragment", Toast.LENGTH_LONG).show();
        }

        global_app.GetPref().edit().putInt(constants.LAST_FRAGMENT, m_current_id).commit();
        UpdateStatus();
    }

    private void GetNextFragment() {
        if (current_frag instanceof communication_frag) {
            current_frag = new listofusers_frag();
        } else if (current_frag instanceof listofusers_frag) {
            current_frag = new start_frag();
        } else if (current_frag instanceof start_frag) {
            current_frag = new question_evaluation_frag();
        } else if (current_frag instanceof question_evaluation_frag) {
            current_frag = new evaluation_frag();
        } else if (current_frag instanceof evaluation_frag) {
            current_frag = new find_location1_frag();
        } else if (current_frag instanceof find_location1_frag) {
            current_frag = new map_frag();
        } else if (current_frag instanceof map_frag) {
            current_frag = new letter_frag();
        } else if (current_frag instanceof letter_frag) {
            current_frag = new finished_game_frag();
        }
        SetFragment();
    }

    private void GetPreviousFragment() {
        if (current_frag instanceof listofusers_frag) {
            current_frag = new communication_frag();
        } else if (current_frag instanceof start_frag) {
            current_frag = new listofusers_frag();
        } else if (current_frag instanceof question_evaluation_frag) {
            current_frag = new start_frag();
        } else if (current_frag instanceof evaluation_frag) {
            current_frag = new question_evaluation_frag();
        } else if (current_frag instanceof find_location1_frag) {
            current_frag = new evaluation_frag();
        } else if (current_frag instanceof map_frag) {
            current_frag = new find_location1_frag();
        } else if (current_frag instanceof letter_frag) {
            current_frag = new map_frag();
        } else if (current_frag instanceof finished_game_frag) {
            current_frag = new letter_frag();
        }
        SetFragment();
    }

    private void SetFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, current_frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        SetLastFragment();
        UpdateStatus();
    }

    public void setResult(String answer) {
        if (current_frag instanceof question_evaluation_frag) {
            if (answer == "yes") {
                current_frag = new evaluation_frag();
                SetFragment();
            } else if (answer == "no") {
                current_frag = new find_location1_frag();
                SetFragment();
            }
        }

        if (current_frag instanceof map_frag) {
            if (answer == "done") {
                current_frag = new letter_frag();
                SetFragment();
            }
        }

        if (current_frag instanceof letter_frag) {
            if (answer == "done") {
                current_frag = new finished_game_frag();
                SetFragment();
            }
        }

        if (current_frag instanceof find_location1_frag) {
            if (answer == "start") {
                current_frag = new map_frag();
                SetFragment();
            }
        }

        if (current_frag instanceof letter_frag) {
            if (answer == "share") {
                current_frag = new communication_frag();
                SetFragment();
            }
        }

        SetLastFragment();
    }

    private void UpdateStatus()
    {
        String t = "Status ";
        for (int i = 0; i <= m_current_id; i++)
        {
            t += "*";
        }

        for (int i = m_current_id + 1; i < 9; i++)
        {
            t += "-";
        }

        m_tv.setText(t);
    }
}