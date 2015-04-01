package com.example.DTUApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class listofusers_frag extends Fragment implements AdapterView.OnItemClickListener {
        private ArrayList<String> Users = null;
        private ListView lv = null;
        private ArrayList<QBUser> qbOtherUsers = null;
        ArrayList<String> mUsers = new ArrayList<String>();
    View v = null;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            //super.onCreate(savedInstanceState);

            v = inflater.inflate(R.layout.listofusers_frag, container, false);

            //Intent i = getActivity().getIntent();

            GetAllUsers();
            //Users = i.getStringArrayListExtra(constants.USERNAMES);


/*
            lv = new ListView(getActivity());
            lv.setOnItemClickListener(this);
            lv.setAdapter(new ArrayAdapter(this.getActivity(), R.layout.listofusers_frag, R.id.listelement_description, mUsers));

            getActivity().setContentView(lv);*/

            return v;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent();
            intent.putExtra(constants.INDEX, position);
            getActivity().setResult(constants.LIST_INDEX, intent);
            getActivity().finish(); //finishing activity
        }


    private void GetAllUsers() {
        QBUsers.getUsers(null, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                qbOtherUsers = qbUsers;
                mUsers = new ArrayList<String>();

                for (int i = 0; i < qbUsers.size(); i++) {
                    mUsers.add(qbOtherUsers.get(i).getLogin());
                    Log.d("GVE", "User: " + mUsers);
                }

                lv = new ListView(getActivity());
                //lv.setOnItemClickListener(this);
                lv.setAdapter(new ArrayAdapter(getActivity().getApplicationContext(), R.layout.listofusers_frag, R.id.listelement_description, mUsers));

                getActivity().setContentView(lv);



/*                Intent i = new Intent(getActivity(), listofusers_frag.class);
                i.putStringArrayListExtra(constants.USERNAMES, s);
                startActivity(i);*/
            }

            @Override
            public void onError(List<String> errors) {
                Toast.makeText(getActivity().getApplicationContext(), errors.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    }
