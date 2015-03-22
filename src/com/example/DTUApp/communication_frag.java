package com.example.DTUApp;

//import android.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBGroupChat;
import com.quickblox.chat.QBGroupChatManager;
import com.quickblox.chat.QBPrivateChat;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.listeners.QBPrivateChatManagerListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gve on 18-03-2015.
 */
public class communication_frag extends Fragment {

    TextView txtStatus = null;
    TextView txtReceive = null;
    EditText txtSend = null;
    QBChatService chatService = null;
    QBUser qbThisUser = null;
    QBUser qbOtherUser = null;
    ArrayList<QBUser> qbOtherUsers = null;
    QBPrivateChatManagerListener privateChatManagerListener = null;
    QBMessageListener<QBPrivateChat> privateChatMessageListener = null;
    QBGroupChatManager groupChatManager = null;

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
        txtReceive = (TextView) v.findViewById(R.id.txtReceive);
        txtSend = (EditText) v.findViewById(R.id.txtSend);

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

        Button btnGetThisUSer = (Button) v.findViewById(R.id.btnGetThisUSer);
        btnGetThisUSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectThisUser();
            }
        });

        Button btnLoginChat = (Button) v.findViewById(R.id.btnLoginChat);
        btnLoginChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginChat();
            }
        });

        Button btnSendMessage = (Button) v.findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });

        return v;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ChatLogout();
        StopSession();
        chatService = null;
        qbThisUser = null;
        qbOtherUser = null;
        qbOtherUsers = null;
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


        qbThisUser = new QBUser(constants.USER_LOGIN, constants.USER_PASSWORD);
        QBAuth.createSession(qbThisUser, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                txtStatus.setText("createSession success");
                //startMapActivity();
            }

            @Override
            public void onError(List<String> errors) {
                Toast.makeText(getActivity().getApplicationContext(), errors.toString(), Toast.LENGTH_SHORT).show();
                txtStatus.setText("createSession error");
            }
        });
    }

    private void StopSession() {
        txtStatus.setText("session stopped");
/*        try {
            //QBAuth.deleteSession();
            //txtStatus.setText("session stopped");
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

        user.setText(global_app.GetPref().getString(constants.USERNAME, "Gerard"));
        final EditText pass = (EditText) prompt.findViewById(R.id.passwordEditText);
        pass.setText(global_app.GetPref().getString(constants.PASSWORD, "longpassword123"));
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
                global_app.GetPref().edit().putString(constants.USERNAME, username).putString(constants.PASSWORD, password).commit();
                txtStatus.setText("signUpSignInTask success");
            }

            @Override
            public void onError(List<String> errors) {

                Toast.makeText(getActivity().getApplicationContext(), errors.toString(), Toast.LENGTH_SHORT).show();
                txtStatus.setText("signUpSignInTask error");
            }
        });
    }

    private void GetAllUsers() {
        QBUsers.getUsers(null, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                qbOtherUsers = qbUsers;
                ArrayList<String> s = new ArrayList<String>();

                for (int i = 0; i < qbUsers.size(); i++) {
                    s.add(qbOtherUsers.get(i).getLogin());
                }
                Intent i = new Intent(getActivity(), listofusers_act.class);
                i.putStringArrayListExtra(constants.USERNAMES, s);
                startActivityForResult(i, 0);
            }

            @Override
            public void onError(List<String> errors) {
                Toast.makeText(getActivity().getApplicationContext(), errors.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SelectThisUser() {
        QBUsers.getUsers(null, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                qbOtherUsers = qbUsers;
                ArrayList<String> s = new ArrayList<String>();

                for (int i = 0; i < qbUsers.size(); i++) {
                    s.add(qbOtherUsers.get(i).getLogin());
                }
                Intent i = new Intent(getActivity(), listofusers_act.class);
                i.putStringArrayListExtra(constants.USERNAMES, s);
                startActivityForResult(i, 1);
            }

            @Override
            public void onError(List<String> errors) {
                Toast.makeText(getActivity().getApplicationContext(), errors.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == constants.LIST_INDEX) {
                int index = data.getIntExtra(constants.INDEX, -1);
                qbOtherUser = qbOtherUsers.get(index);
                Toast.makeText(getActivity().getApplicationContext(), "Other User: " + qbOtherUser.getLogin() + " " + qbOtherUser.getPassword(), Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 1) {
            if (resultCode == constants.LIST_INDEX) {
                int index = data.getIntExtra(constants.INDEX, -1);
                qbThisUser = qbOtherUsers.get(index);
                qbThisUser.setPassword(global_app.GetPref().getString(constants.PASSWORD, "No password found"));
                Toast.makeText(getActivity().getApplicationContext(), "This User: " + qbThisUser.getLogin() + " " + qbThisUser.getPassword(), Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult

    private void LoginChat() {
        // Initialise Chat service
        if (!QBChatService.isInitialized()) {
            QBChatService.init(getActivity().getApplicationContext());
            chatService = QBChatService.getInstance();
        }

/*        try {
            QBChatService.getInstance().startAutoSendPresence(60);
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        }*/

        //AddConnectionListener();

        QBAuth.createSession(qbThisUser, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success, login to chat

                qbThisUser.setId(session.getUserId());
                txtStatus.setText("createSession success: " + session.getUserId());

                loginToChat(qbThisUser);

/*                chatService.login(qbThisUser, new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {
                        // success
                        txtStatus.setText("chatService.login success");
                        SendChatMessage();
                    }

                    @Override
                    public void onError(List errors) {
                        // error
                        txtStatus.setText("chatService.login error: " + errors.toString());
                    }
                });*/
            }

            @Override
            public void onError(List<String> errors) {
                // errors
                txtStatus.setText("createSession error: " + errors.toString());
            }
        });
    }

    private void loginToChat(final QBUser user) {

        boolean isLoggedIn = chatService.isLoggedIn();
        if (!isLoggedIn) {
            chatService.login(user, new QBEntityCallbackImpl() {
                @Override
                public void onSuccess() {

                    Log.d("GVE", "chatService.login success");

                    //SendChatMessage();


                    //txtStatus.setText("chatService.login success");

                    // Start sending presences
                    //
                    try {
                        chatService.startAutoSendPresence(60);
                    } catch (SmackException.NotLoggedInException e) {
                        e.printStackTrace();
                    }

                    AddConnectionListener();
                    StartListeningForPrivateChats();
                    StartListeningForGroupChats();
/*
                Intent intent = new Intent(getActivity(), DialogsActivity.class);
                startActivity(intent);*/
/*                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            RequestChatDialogs();

                            StartListeningForPrivateChats();
                            StartListeningForGroupChats();
                        }
                    });*/

/*                // go to Dialogs screen
                //
                Intent intent = new Intent(SplashActivity.this, DialogsActivity.class);
                startActivity(intent);
                finish();*/
                }

                @Override
                public void onError(List errors) {
                    Log.d("GVE", "chatService.login error: " + errors);
                    //txtStatus.setText("chatService.login error");

/*                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                dialog.setMessage("chat login errors: " + errors).create().show();*/
                }
            });
        }
        else
        {
/*            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    RequestChatDialogs();
                }
            });*/

            Log.d("GVE", "chatService.login was alrday logged in: ");
            AddConnectionListener();
            StartListeningForPrivateChats();
            StartListeningForGroupChats();
        }
    }

    private void RequestChatDialogs() {
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setPagesLimit(100);

        QBChatService.getChatDialogs(null, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle args) {
                Log.d("GVE", "getChatDialogs onSuccess: ");
                NewGroupDialog();
            }

            @Override
            public void onError(List<String> errors) {
                Log.d("GVE", "getChatDialogs onSuccess: " + errors);
            }
        });
    }

    private void StartListeningForPrivateChats() {
        privateChatMessageListener = new QBMessageListener<QBPrivateChat>() {
            @Override
            public void processMessage(QBPrivateChat privateChat, final QBChatMessage chatMessage) {

                Log.d("GVE", "---------RECEIVING: processMessage: " + chatMessage.getBody() + " from " + chatMessage.getSenderId());

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        txtReceive.setText(chatMessage.getBody());
                    }
                });
            }

            @Override
            public void processError(QBPrivateChat privateChat, QBChatException error, QBChatMessage originMessage) {

                Log.d("GVE", "processMessage: " + error.getMessage());
            }

            @Override
            public void processMessageDelivered(QBPrivateChat privateChat, String messageID) {

                Log.d("GVE", "processMessageDelivered: " + messageID);
            }

            @Override
            public void processMessageRead(QBPrivateChat privateChat, String messageID) {
                Log.d("GVE", "processMessageRead: " + messageID);
            }
        };


        privateChatManagerListener = new QBPrivateChatManagerListener() {
            @Override
            public void chatCreated(final QBPrivateChat privateChat, final boolean createdLocally) {
                if (!createdLocally) {
                    privateChat.addMessageListener(privateChatMessageListener);
                }
            }
        };


        QBChatService.getInstance().getPrivateChatManager().addPrivateChatManagerListener(privateChatManagerListener);

        Integer opponentId = qbOtherUser.getId();

        try {
            QBChatMessage chatMessage = new QBChatMessage();
            chatMessage.setBody("Hi there!");
            chatMessage.setProperty("save_to_history", "1"); // Save a message to history

            QBPrivateChat privateChat = QBChatService.getInstance().getPrivateChatManager().getChat(opponentId);
            if (privateChat == null) {
                privateChat = QBChatService.getInstance().getPrivateChatManager().createChat(opponentId, privateChatMessageListener);
            }
            privateChat.sendMessage(chatMessage);
        } catch (XMPPException e) {

        } catch (SmackException.NotConnectedException e) {

        }
    }

    private void StartListeningForGroupChats() {
        QBMessageListener<QBGroupChat> groupChatQBMessageListener = new QBMessageListener<QBGroupChat>() {
            @Override
            public void processMessage(final QBGroupChat groupChat, final QBChatMessage chatMessage) {

                Log.d("GVE", "processMessage: " + chatMessage.getBody());
            }

            @Override
            public void processError(final QBGroupChat groupChat, QBChatException error, QBChatMessage originMessage) {

                Log.d("GVE", "processError: " + error.getMessage());
            }

            @Override
            public void processMessageDelivered(QBGroupChat groupChat, String messageID) {
                // never be called, works only for 1-1 chat
                Log.d("GVE", "processMessageDelivered: " + messageID);
            }

            @Override
            public void processMessageRead(QBGroupChat groupChat, String messageID) {
                // never be called, works only for 1-1 chat
                Log.d("GVE", "processMessageRead: " + messageID);
            }
        };

        DiscussionHistory history = new DiscussionHistory();
        history.setMaxStanzas(0);

        groupChatManager = QBChatService.getInstance().getGroupChatManager();

/*        QBGroupChat currentChatRoom = groupChatManager.createGroupChat(groupDialog.getRoomJid());
        currentChatRoom.join(history, new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {
                // add listeners
                currentChatRoom.addMessageListener(groupChatQBMessageListener);
            }

            @Override
            public void onError(final List list) {

            }
        });*/
    }

    private void SendMessage() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                RequestChatDialogs();
            }
        });
    }

    private void NewGroupDialog() {
        ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();
        occupantIdsList.add(qbOtherUser.getId());

        QBDialog dialog = new QBDialog();
        dialog.setName("GVE's first chat");
        //dialog.setPhoto("1786");
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);

        QBGroupChatManager groupChatManager = QBChatService.getInstance().getGroupChatManager();
        groupChatManager.createDialog(dialog, new QBEntityCallbackImpl<QBDialog>() {
            @Override
            public void onSuccess(QBDialog dialog, Bundle args) {
                Log.d("GVE", "createDialog onSuccess: ");
                NotifyOthers(dialog);
            }

            @Override
            public void onError(List<String> errors) {
                Log.d("GVE", "createDialog onSuccess: " + errors);

            }
        });
    }

    private void NotifyOthers(QBDialog dialog) {
        Log.d("GVE", "NotifyOthers");
        for (Integer userID : dialog.getOccupants()) {

            Log.d("GVE", "NotifyOthers: " + userID);
            QBChatMessage chatMessage = createChatNotificationForGroupChatCreation(dialog);

            //long time = DateUtils.getCurrentTime();
            String time = "NOWWWWW";
            chatMessage.setProperty("date_sent", time + "");
            chatMessage.setBody(txtSend.getText() + " (a message from: " + qbThisUser.getLogin() + ")");

            Log.d("GVE", "------------SENDING: processMessage: " + chatMessage.getBody() + " from " + qbThisUser.getLogin() + " to " + userID);

            QBPrivateChat chat = QBChatService.getInstance().getPrivateChatManager().getChat(userID);
            if (chat == null) {
                Log.d("GVE", "chat == null: " + userID);
                chat = chatService.getPrivateChatManager().createChat(userID, null);
            }

            try {
                Log.d("GVE", "chat.sendMessage: " + userID);
                chat.sendMessage(chatMessage);
            } catch (Exception e) {
                // error
            }
        }
    }

    public static QBChatMessage createChatNotificationForGroupChatCreation(QBDialog dialog) {
        String dialogId = String.valueOf(dialog.getDialogId());
        String roomJid = dialog.getRoomJid();
        String occupantsIds = TextUtils.join(",", dialog.getOccupants());
        String dialogName = dialog.getName();
        String dialogTypeCode = String.valueOf(dialog.getType().ordinal());

        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody("optional text");

        // Add notification_type=1 to extra params when you created a group chat
        //
        chatMessage.setProperty("notification_type", "1");

        chatMessage.setProperty("_id", dialogId);
        if (!TextUtils.isEmpty(roomJid)) {
            chatMessage.setProperty("room_jid", roomJid);
        }
        chatMessage.setProperty("occupants_ids", occupantsIds);
        if (!TextUtils.isEmpty(dialogName)) {
            chatMessage.setProperty("name", dialogName);
        }
        chatMessage.setProperty("type", dialogTypeCode);

        return chatMessage;
    }

/*    private void SendChatMessage() {

        Log.d("GVE", "SendChatMessage");
        QBChatMessage chatMessage = new QBChatMessage();

        String time = "NOWWWWWWW";
        chatMessage.setProperty("date_sent", time + "");
        chatMessage.setBody("Hi there!");

        //Toast.makeText(getActivity().getApplicationContext(), "OTHER: " + qbOtherUser.getId(), Toast.LENGTH_SHORT).show();

        QBPrivateChat chat = QBChatService.getInstance().getPrivateChatManager().getChat(qbOtherUser.getId());
        if (chat == null) {
            chat = chatService.getPrivateChatManager().createChat(qbOtherUser.getId(), null);
        }

        try {
            chat.sendMessage(chatMessage);
            Log.d("GVE", "chat.sendMessage");
        } catch (Exception e) {
            // error
            Log.d("GVE", "error chat.sendMessage");
        }
    }*/

    private void AddConnectionListener() {
        ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {
                Log.d("GVE", "connected");
            }

            @Override
            public void authenticated(XMPPConnection connection) {
                Log.d("GVE", "authenticated");
            }

            @Override
            public void connectionClosed() {
                Log.d("GVE", "connectionClosed");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                // connection closed on error. It will be established soon
                Log.d("GVE", "connectionClosedOnError");
            }

            @Override
            public void reconnectingIn(int seconds) {
                Log.d("GVE", "reconnectingIn");
            }

            @Override
            public void reconnectionSuccessful() {
                Log.d("GVE", "reconnectionSuccessful");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                Log.d("GVE", "reconnectionFailed");
            }
        };

        QBChatService.getInstance().addConnectionListener(connectionListener);
    }

    private void ChatLogout() {
        boolean isLoggedIn = chatService.isLoggedIn();
        if (!isLoggedIn) {
            return;
        }

        chatService.logout(new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {
                // success
                chatService.destroy();
            }

            @Override
            public void onError(final List list) {

            }
        });
    }
}