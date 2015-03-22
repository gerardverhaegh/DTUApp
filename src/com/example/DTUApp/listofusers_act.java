package com.example.DTUApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Gerard Verhaegh on 3/21/2015.
 */
public class listofusers_act extends Activity implements AdapterView.OnItemClickListener {
        private ArrayList<String> Users = null;
        private ListView lv = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent i = getIntent();
            Users = i.getStringArrayListExtra(constants.USERNAMES);

            lv = new ListView(this);
            lv.setOnItemClickListener(this);
            lv.setAdapter(new ArrayAdapter(this, R.layout.listofusers_act, R.id.listelement_description, Users));

            setContentView(lv);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent();
            intent.putExtra(constants.INDEX, position);
            setResult(constants.LIST_INDEX, intent);
            finish(); //finishing activity
        }
    }
