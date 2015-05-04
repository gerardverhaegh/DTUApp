package com.example.DTUApp;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

import java.util.List;
import java.util.Random;

/**
 * Created by Gerard Verhaegh on 5/4/2015.
 */
public class random_word_frag extends base_frag {

    private TextView txtStatus = null;
    private TextView tv2 = null;
    String randomLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZÆÅØ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.random_word_frag, container, false);

        txtStatus = (TextView) v.findViewById(R.id.txtStatus);
        tv2 = (TextView) v.findViewById(R.id.tv2);

        QBCustomObject object = new QBCustomObject();

        // put fields
        object.putString("OneWord", "Star Wars");
/*        object.putString("gvecomment", "Star Wars is an American epic space opera franchise consisting of a film series created by George Lucas.");
        object.putString("gvegenre", "fantasy");*/

        // set the class name
        object.setClassName("Words");

        QBCustomObjects.createObject(object, new QBEntityCallbackImpl<QBCustomObject>() {
            @Override
            public void onSuccess(QBCustomObject createdObject, Bundle params) {
                //AddStatus("Success" + createdObject.toString() + " " + params.toString());
                AddStatus("Success");// + createdObject.toString() + " " + params.toString());

                ShowLetter();
            }

            @Override
            public void onError(List<String> errors) {
                AddStatus("onError: " + errors);
            }
        });

        return v;
    }

    private void ShowLetter() {

        final DecelerateInterpolator sDecelerator = new DecelerateInterpolator();
        //final OvershootInterpolator sOvershooter = new OvershootInterpolator(10f);
        Random rnd = new Random();
        char s = randomLetters.charAt(rnd.nextInt(randomLetters.length()));
        tv2.setText(String.valueOf(s));
        if (Build.VERSION.SDK_INT > 11) {
            tv2.animate().setInterpolator(sDecelerator).rotationX(360).alpha(10).setDuration(5000);
        }
    }

    private void AddStatus(String txt) {
        final String txtCopy = txt;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (txtStatus.getVisibility() == View.VISIBLE) {
                    txtStatus.setText(txtCopy + "\r\n" + txtStatus.getText());
                }
            }
        });
    }

}