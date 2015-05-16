package com.example.DTUApp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.DTUApp.R;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gerard Verhaegh on 3/21/2015.
 */
public class listofusers_frag extends base_frag implements AdapterView.OnItemClickListener {
    private ArrayList<String> Users = null;
    private ListView lv = null;
    private ArrayList<QBUser> qbOtherUsers = null;
    ArrayList<MyObject> mUsers = new ArrayList<MyObject>();
    ArrayList<String> mUsersNames = new ArrayList<String>();
    View v = null;
    private ArrayAdapter madapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv = new ListView(getActivity());
        lv.setOnItemClickListener(this);

        // always refresh
        mUsers.clear();
        mUsersNames.clear();
        GetAllUsers();

        return lv;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*        Intent intent = new Intent();
        intent.putExtra(constants.INDEX, position);
        getActivity().setResult(constants.LIST_INDEX, intent);
        getActivity().finish(); //finishing activity*/
        //view.setSelected(true);
        if (mUsers.get(position).isOnline) {
            mUsers.get(position).isSelected = !mUsers.get(position).isSelected;

            Log.d("GVE", "SelectedItem: " + mUsers.get(position).isSelected);

            if (madapter != null) {
                madapter.notifyDataSetChanged();
            }
        }
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

    public ArrayList<MyObject> getUsers() {
        return mUsers;
    }

    private void GetAllUsers() {
        QBUsers.getUsers(null, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                qbOtherUsers = qbUsers;
                mUsers = new ArrayList<MyObject>();
                mUsersNames.clear();

                for (int i = 0; i < qbUsers.size(); i++) {
                    MyObject obj = new MyObject();
                    obj.name = qbOtherUsers.get(i).getLogin() + " (" + qbOtherUsers.get(i).getId() + ")";
                    obj.isOnline = IsUserOnline(qbOtherUsers.get(i));
                    obj.isSelected = obj.isOnline;
                    obj.id = qbOtherUsers.get(i).getId();

                    if (!qbOtherUsers.get(i).getLogin().equals("gve1")) {
                        mUsers.add(obj);
                    }
                    Log.d("GVE", "User: " + obj.name);
                }

                Collections.sort(mUsers);

                MyObject obj = new MyObject();
                obj.name = "Jeres FeelGood hold er sat! \n" +
                        "\tMød dine medspillere her.\n";

                mUsers.add(0, obj);
                for (int i = 0; i < mUsers.size(); i++) {
                    mUsersNames.add(mUsers.get(i).name);
                }

                madapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.listofusers_frag, R.id.listelement_description, mUsersNames) {
                    @Override
                    public View getView(int position, View cachedView, ViewGroup parent) {
                        View view = super.getView(position, cachedView, parent);

                        TextView le_description = (TextView) view.findViewById(R.id.listelement_description);
                        ImageView le_image = (ImageView) view.findViewById(R.id.listelement_image);
                        ImageView le_image2 = (ImageView) view.findViewById(R.id.listelement_image2);
                        ImageButton le_imagebutton = (ImageButton) view.findViewById(R.id.imagebutton);
                        //TextView le_text = (TextView) view.findViewById(R.id.listelement_text);
                        //ImageView listeelem_billede = (ImageView) view.findViewById(R.id.listeelem_billede);
                        //listeelem_billede.setImageResource(android.R.drawable.sym_action_call);

                        le_description.setText("\t" + le_description.getText() + "\t");
                        if (position == 0) {
                            le_imagebutton.setImageResource(android.R.drawable.ic_popup_sync);
                            le_imagebutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("GVE", "GetAllUsers refresh");
                                    GetAllUsers();
                                }
                            });
                        } else {
                            le_imagebutton.setVisibility(View.INVISIBLE);

                            if (mUsers.get(position).isSelected) {
                                le_image.setImageResource(android.R.drawable.star_big_on);
                            } else {
                                le_image.setImageResource(android.R.drawable.star_big_off);
                            }

                            if (mUsers.get(position).isOnline) {
                                le_image2.setImageResource(android.R.drawable.presence_online);
                            } else {
                                le_image2.setImageResource(android.R.drawable.presence_offline);
                            }
                        }

                        return view;
                    }
                };

                lv.setAdapter(madapter);
                ToNextFragment();
            }

            @Override
            public void onError(List<String> errors) {
                Toast.makeText(getActivity().getApplicationContext(), errors.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

/*    public void refreshGetAllUsers(View v)
    {
        GetAllUsers();
    }*/

    public class MyObject implements Comparable<MyObject> {
        public String name;
        public int id;
        public boolean isOnline;
        public boolean isSelected;

        public int compareTo(MyObject compareMyObject) {
            return (this.name).compareTo(compareMyObject.name);
        }
    }

    ;
}
