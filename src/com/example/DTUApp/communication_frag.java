package com.example.DTUApp;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.LogLevel;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBRequestCanceler;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.Date;
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

        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        iv.setImageResource(R.raw.communication);

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
                //SignIn(qbSession, bundle);
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

/*    private void SignIn(QBSession session, Bundle params) {
        // Register new user
        final QBUser user = new QBUser("gve1", "koek4321");

        //user.setId(session.getUserId());
        //txtStatus.setText("userid: " + session.getUserId());

        QBRequestCanceler qbRequestCanceler = QBUsers.signIn(user, new QBEntityCallbackImpl<QBUser>() {
                @Override
                public void onSuccess(QBUser user, Bundle args) {
                    StartChatService(user, args);
                }

                @Override
                public void onError(List<String> errors) {
                    // error
                    txtStatus.setText("createSession error");
                }
            }
        );
    }*/

/*    private void StartChatService(QBUser user, Bundle args) {
        // success
        try {
            Date expirationDate = BaseService.getBaseService().getTokenExpirationDate();
            txtStatus.setText("expirationDate: " + expirationDate);
        } catch (BaseServiceException e) {
            e.printStackTrace();
        }

        // initialize Chat service
        QBChatService chatService = null;
        if (!QBChatService.isInitialized()) {
            QBChatService.init(getActivity().getApplicationContext());
            chatService = QBChatService.getInstance();
        }

        if (chatService == null) {
            txtStatus.setText("chatService null");
        } else {
            chatService.login(user, new QBEntityCallbackImpl() {
                @Override
                public void onSuccess() {
                    // success
                    txtStatus.setText("chatService login SUCCESS");
                }

                @Override
                public void onError(List errors) {
                    // errors
                    txtStatus.setText("chatService login ERROR");
                }
            });
        }
    }*/
}