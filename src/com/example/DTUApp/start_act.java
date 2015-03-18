package com.example.DTUApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class start_act extends Activity implements Runnable {

    Handler handler = new Handler();
    static start_act currentAct = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start_act);

        // set splash screen logo
        ImageView iv = (ImageView)findViewById(R.id.iv);
        iv.setImageResource(R.raw.splash);

        if (savedInstanceState == null) {
            handler.postDelayed(this, 1000);
        }
        currentAct = this;
    }

    public void run() {
        startActivity(new Intent(this, main_act.class));
        currentAct.finish();
        currentAct = null;
    }

    @Override
    public void finish() {
        super.finish();
        handler.removeCallbacks(this);
    }
}
