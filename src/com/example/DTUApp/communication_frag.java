package com.example.DTUApp;

//import android.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gve on 18-03-2015.
 */
public class communication_frag extends Fragment {

    TextView txtStatus = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v = inflater.inflate(R.layout.communication_frag, container, false);

/*        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        iv.setImageResource(R.raw.communication);*/

        txtStatus = (TextView) v.findViewById(R.id.txtStatus);

        Button btnLogin = (Button) v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSession();
            }
        });

        Button btnLogout = (Button) v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopSession();
            }
        });

        Button btnSignUp = (Button) v.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

        Button btnGetAllUsers = (Button) v.findViewById(R.id.btnGetAllUsers);
        btnGetAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAllUsers();
            }
        });

        return v;
    }

    private void CreateSession() {
        //Toast.makeText(getActivity().getApplicationContext(), "Logging in", Toast.LENGTH_SHORT).show();

        txtStatus.setText("Logging in");

        // String appId, String authKey, String authSecret
        QBSettings.getInstance().fastConfigInit(constants.APP_ID, constants.AUTH_KEY, constants.AUTH_SECRET);

        //String api = QBSettings.getInstance().getApiVersion();
        //QBSettings.getInstance().setLogLevel(LogLevel.DEBUG);
        //String dm = QBSettings.getInstance().getChatServerDomain();

        //Toast.makeText(getActivity().getApplicationContext(), "hallo 2" + api + " " + dm, Toast.LENGTH_SHORT).show();

        String date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

        txtStatus.setText("Date of login: " + date);

        System.out.println("--------------------" + date + "-------------------------");


        QBUser qbUser = new QBUser(constants.USER_LOGIN, constants.USER_PASSWORD);
        QBAuth.createSession(qbUser, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                txtStatus.setText("createSession success");
                //startMapActivity();
            }

            @Override
            public void onError(List<String> errors) {
                txtStatus.setText("createSession error");
            }
        });
    }

    private void StopSession() {
        txtStatus.setText("session stopped");
/*        try {
            //QBAuth.deleteSession();
            txtStatus.setText("session stopped");
        } catch (QBResponseException e) {
            e.printStackTrace();
        }*/
    }

    private void startMapActivity() {
        Intent intent = new Intent(this.getActivity(), map_frag.class);
        startActivity(intent);
    }

    private void SignUp() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View prompt = li.inflate(R.layout.login_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(prompt);
        //final String username = getActivity().getIntent().getStringExtra("USERNAME");
        final EditText user = (EditText) prompt.findViewById(R.id.userNameEditText);

        user.setText(global_app.GetPref().getString("Username", "PickAUserName"));
        final EditText pass = (EditText) prompt.findViewById(R.id.passwordEditText);
        pass.setText(global_app.GetPref().getString("Username", "PickAUserPassword"));
        //user.setText(getActivity().getIntent().getStringExtra("USERNAME"));

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String password = pass.getText().toString();
                        String username = user.getText().toString();
                        SignUpInQB(username, password);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });
        alertDialogBuilder.show();
    }

    private void SignUpInQB(final String username, final String password) {
        txtStatus.setText("SignUpInQB: " + username + " " + password);
        QBUser qbUser = new QBUser();
        qbUser.setLogin(username);
        qbUser.setPassword(password);
        QBUsers.signUpSignInTask(qbUser, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                global_app.GetPref().edit().putString("Username", username).putString("Password", password).commit();
                txtStatus.setText("signUpSignInTask success");
            }

            @Override
            public void onError(List<String> strings) {
                txtStatus.setText("signUpSignInTask error");
            }
        });
    }

    private void GetAllUsers() {
        QBUsers.getUsers(null, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                ArrayList<String> s = new ArrayList<String>();

                for (int i = 0; i < qbUsers.size(); i++) {
                    s.add(qbUsers.get(i).getLogin());
                }
                Intent i = new Intent(getActivity(), listofusers_act.class);
                i.putStringArrayListExtra("Usernames", s);
                startActivityForResult(i, 0);
            }

            @Override
            public void onError(List<String> errors) {

            }
        });
    }
}