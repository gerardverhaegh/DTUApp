package com.example.DTUApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gerard Verhaegh on 3/21/2015.
 */
public class listofusers_frag extends base_frag implements AdapterView.OnItemClickListener {
    private ArrayList<String> Users = null;
    private ListView lv = null;
    private ArrayList<QBUser> qbOtherUsers = null;
    ArrayList<String> mUsers = new ArrayList<String>();
    View v = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv = new ListView(getActivity());
        lv.setOnItemClickListener(this);
        GetAllUsers();
        return lv;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(constants.INDEX, position);
        getActivity().setResult(constants.LIST_INDEX, intent);
        getActivity().finish(); //finishing activity
    }

    private boolean IsUserOnline(QBUser user) {
        if (user == null) {
            Log.d("GVE", "user == null");
            return false;
        }

        long currentTime = System.currentTimeMillis();

        if (user.getLastRequestAt() == null) {
            Log.d("GVE", "user.getLastRequestAt() == null");
            return false;
        }

        long userLastRequestAtTime = user.getLastRequestAt().getTime();

        Log.d("GVE", "currentTime - userLastRequestAtTime: " + (currentTime - userLastRequestAtTime));

        // if user didn't do anything last 5 minutes (5*60*1000 milliseconds)
        return ((currentTime - userLastRequestAtTime) <= 5 * 60 * 1000);
    }

    private void GetAllUsers() {
        QBUsers.getUsers(null, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                qbOtherUsers = qbUsers;
                mUsers = new ArrayList<String>();

                for (int i = 0; i < qbUsers.size(); i++) {
                    String sOnLine = (IsUserOnline(qbOtherUsers.get(i))) ? " (online)" : " (offline)";
                    mUsers.add(qbOtherUsers.get(i).getLogin() + sOnLine);
                    Log.d("GVE", "User: " + mUsers);
                }

                lv.setAdapter(new ArrayAdapter(getActivity().getApplicationContext(), R.layout.listofusers_frag, R.id.listelement_description, mUsers) {
                    @Override
                    public View getView(int position, View cachedView, ViewGroup parent) {
                        View view = super.getView(position, cachedView, parent);

                        TextView le_description = (TextView) view.findViewById(R.id.listelement_description);
                        //TextView le_text = (TextView) view.findViewById(R.id.listelement_text);
                        //ImageView listeelem_billede = (ImageView) view.findViewById(R.id.listeelem_billede);
                        //listeelem_billede.setImageResource(android.R.drawable.sym_action_call);
                        if (position == 0) {
                            le_description.setText("Jeres FeelGood hold er sat! \n" +
                                    "Mød dine medspillere:\n");

                            //le_description.setTextColor(0xFF295055);
                            //le_text.setText("");
                            //le_text.setTextColor(0xFF295055);
                        } else {
                            le_description.setText("* " + le_description.getText());
                            //le_description.setTextColor(0xFF295055);
                        }
                        return view;
                    }
                });

                communication_viewpager_act act = (communication_viewpager_act) getActivity();
                act.setResult("ok");
            }

            @Override
            public void onError(List<String> errors) {
                Toast.makeText(getActivity().getApplicationContext(), errors.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
