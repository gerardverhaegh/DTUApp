package com.example.DTUApp;

//import android.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gve on 18-03-2015.
 */
public class chat_frag extends base_frag {

    private TextView txtStatus = null;
    private TextView txtReceive = null;
    private EditText txtSendMessage = null;
    private TextView txtLogin = null;
    //private QBChatService mChatService = null;
    private QBUser qbThisUser = null;
    private QBUser qbMainUser = null;
    private QBUser qbOtherUser = null;
    private ArrayList<QBUser> qbOtherUsers = null;
    private QBPrivateChatManagerListener privateChatManagerListener = null;
    private QBMessageListener<QBPrivateChat> privateChatMessageListener = null;
    private QBGroupChatManager groupChatManager = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v = inflater.inflate(R.layout.communication_frag, container, false);

        // keep QB data in memory
        setRetainInstance(true);

        txtStatus = (TextView) v.findViewById(R.id.txtStatus);
        txtReceive = (TextView) v.findViewById(R.id.txtReceive);
        txtSendMessage = (EditText) v.findViewById(R.id.txtSendMessage);
        txtLogin = (TextView) v.findViewById(R.id.txtLogin);

        txtLogin.setText(global_app.GetPref().getString(constants.USERNAME, "NO STRING FOUND"));

        Button btnSendMessage = (Button) v.findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });

        Button btnClear = (Button) v.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("");
                txtReceive.setText("");
            }
        });

        if (global_app.GetPref().getBoolean(constants.DEBUGINFO, false)) {
            LinearLayout linlayout1 = (LinearLayout) v.findViewById(R.id.txtStatusLinearLayout1);
            linlayout1.setVisibility(View.VISIBLE);
            LinearLayout linlayout2 = (LinearLayout) v.findViewById(R.id.txtStatusLinearLayout2);
            linlayout2.setVisibility(View.VISIBLE);
        } else {
            LinearLayout linlayout1 = (LinearLayout) v.findViewById(R.id.txtStatusLinearLayout1);
            linlayout1.setVisibility(View.GONE);
            LinearLayout linlayout2 = (LinearLayout) v.findViewById(R.id.txtStatusLinearLayout2);
            linlayout2.setVisibility(View.GONE);
        }

        if (savedInstanceState == null) {
            Log.d("GVE", "new session");
            CreateSession();
        }

        return v;
    }

    private void StopSession() {
        ChatLogout();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d("GVE", "onDestroyView");
    }

    private void CreateSession() {
        Log.d("GVE", "----CreateSession");
        // String appId, String authKey, String authSecret
        QBSettings.getInstance().fastConfigInit(constants.APP_ID, constants.AUTH_KEY, constants.AUTH_SECRET);

        qbMainUser = new QBUser(constants.USER_LOGIN, constants.USER_PASSWORD);
        QBAuth.createSession(qbMainUser, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                AddStatus("createSession success");
                SignIn();
            }

            @Override
            public void onError(List<String> errors) {
                qbMainUser = null;
                AddStatus("createSession error");
            }
        });
    }

    private void SignIn() {
        String username = global_app.GetPref().getString(constants.USERNAME, "NO USERNAME");
        String password = global_app.GetPref().getString(constants.PASSWORD, "NO PASSWORD");
        SignInQB(username, password);
    }

    private void SignInQB(final String username, final String password) {
        Log.d("GVE", "SignInQB username: " + username + ", password: " + password);

        qbThisUser = new QBUser();
        qbThisUser.setLogin(username);
        qbThisUser.setPassword(password);
        QBUsers.signIn(qbThisUser, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                global_app.GetPref().edit().putString(constants.USERNAME, username).putString(constants.PASSWORD, password).commit();
                AddStatus("SignInQB success");
                GetAllUsers(txtLogin.getText().toString(), false);
            }

            @Override
            public void onError(List<String> errors) {
                AddStatus("SignInQB error" + errors);
                qbThisUser = null;

                // user didn't exist yet
                SignUpSignInQB(username, password);
            }
        });
    }

    private void SignUpSignInQB(final String username, final String password) {
        Log.d("GVE", "SignUpInQB username: " + username + ", password: " + password);

        qbThisUser = new QBUser();
        qbThisUser.setLogin(username);
        qbThisUser.setPassword(password);
        QBUsers.signUpSignInTask(qbThisUser, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                global_app.GetPref().edit().putString(constants.USERNAME, username).putString(constants.PASSWORD, password).commit();
                AddStatus("signUpSignInTask success");
            }

            @Override
            public void onError(List<String> errors) {
                qbThisUser = null;
                AddStatus("signUpSignInTask error" + errors);
            }
        });
    }

    private void SignOut() {
        // not yet implemented
    }

    private void GetAllUsers(final String UserName, final boolean bReinit) {
        QBUsers.getUsers(null, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                qbOtherUsers = qbUsers;
                boolean bUserExist = false;

                Log.d("GVE", "qbMainUser: " + ((qbMainUser != null) ? qbMainUser.getLogin() : "null"));
                Log.d("GVE", "qbThisUser: " + ((qbThisUser != null) ? qbThisUser.getLogin() : "null"));

                for (int i = 0; i < qbUsers.size(); i++) {
                    Log.d("GVE", "user: " + qbOtherUsers.get(i).getLogin());

                    if (qbMainUser != null && qbMainUser.getLogin().equals(qbOtherUsers.get(i).getLogin())) {
                        Log.d("GVE", "qbMainUser: " + qbMainUser.getLogin());
                    } else if (qbThisUser != null && qbThisUser.getLogin().equals(qbOtherUsers.get(i).getLogin())) {
                        Log.d("GVE", "qbThisUser: " + qbThisUser.getLogin());
                        bUserExist = true;
                    }
                }

                if (bReinit) {
             /*       AddConnectionListener();
                    StartListeningForPrivateChats();
                    StartListeningForGroupChats();*/
                }

                StartChat();
            }

            @Override
            public void onError(List<String> errors) {
                qbOtherUsers = null;
                Log.d("GVE", "errors: " + errors.toString());
            }
        });
    }

    private void StartChat() {
        // Initialise Chat service
        if (!QBChatService.isInitialized()) {
            QBChatService.init(getActivity().getApplicationContext());
            //mChatService = QBChatService.getInstance();
        }

        QBAuth.createSession(qbThisUser, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success, login to chat
                qbThisUser.setId(session.getUserId());
                AddStatus("createSession success: " + session.getUserId());

                LoginToChat(qbThisUser);
            }

            @Override
            public void onError(List<String> errors) {
                txtStatus.setText("createSession error: " + errors.toString());
            }
        });
    }

    private void LoginToChat(final QBUser user) {
        boolean isLoggedIn = false;
        if (QBChatService.getInstance() != null) {
            isLoggedIn = QBChatService.getInstance().isLoggedIn();
        }

        if (!isLoggedIn) {
            QBChatService.getInstance().login(user, new QBEntityCallbackImpl() {
                @Override
                public void onSuccess() {
                    AddStatus("chatService.login success");

                    try {
                        QBChatService.getInstance().startAutoSendPresence(60);
                    } catch (SmackException.NotLoggedInException e) {
                        e.printStackTrace();
                    }

                    StartListenersEtc();
                }

                @Override
                public void onError(List errors) {
                    Log.d("GVE", "chatService.login error: " + errors);
                }
            });
        } else {
            AddStatus("chatService.login was already logged in: ");
            StartListenersEtc();
        }
    }

    private void StartListenersEtc() {
        NotifyStartOfListOfUsers();
        AddConnectionListener();
        StartListeningForPrivateChats();
        StartListeningForGroupChats();
    }

    private void NotifyStartOfListOfUsers() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (getActivity() instanceof communication_viewpager_act) {
                    communication_viewpager_act act = (communication_viewpager_act) getActivity();
                    act.setResult("ok");
                }
            }
        });
    }

    private void RequestChatDialogs() {
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setPagesLimit(100);

        QBChatService.getChatDialogs(null, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle args) {
                AddStatus("getChatDialogs onSuccess: ");
                NewGroupDialog();
            }

            @Override
            public void onError(List<String> errors) {
                AddStatus("getChatDialogs onSuccess: " + errors);
            }
        });
    }

    private void StartListeningForPrivateChats() {
        privateChatMessageListener = new QBMessageListener<QBPrivateChat>() {
            @Override
            public void processMessage(QBPrivateChat privateChat, final QBChatMessage chatMessage) {
                AddStatus("private processMessage: " + chatMessage.getBody() + " from " + chatMessage.getSenderId());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        txtReceive.setText(chatMessage.getBody() + "\r\n" + txtReceive.getText());
                    }
                });
            }

            @Override
            public void processError(QBPrivateChat privateChat, QBChatException error, QBChatMessage originMessage) {
                AddStatus("private processMessage: " + error.getMessage());
            }

            @Override
            public void processMessageDelivered(QBPrivateChat privateChat, String messageID) {
                AddStatus("private processMessageDelivered: " + messageID);
            }

            @Override
            public void processMessageRead(QBPrivateChat privateChat, String messageID) {
                AddStatus("private processMessageRead: " + messageID);
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

        if (qbOtherUser != null) {
            Integer opponentId = qbOtherUser.getId();

            try {
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody("Welcome!");

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (getActivity() instanceof communication_viewpager_act) {
                            communication_viewpager_act act = (communication_viewpager_act) getActivity();
                            act.setResult("ok");
                        }
                    }
                });

                QBPrivateChat privateChat = QBChatService.getInstance().getPrivateChatManager().getChat(opponentId);
                if (privateChat == null) {
                    privateChat = QBChatService.getInstance().getPrivateChatManager().createChat(opponentId, privateChatMessageListener);
                }
                privateChat.sendMessage(chatMessage);
            } catch (XMPPException e) {
            } catch (SmackException.NotConnectedException e) {
            }
        }
    }

    private void StartListeningForGroupChats() {
        QBMessageListener<QBGroupChat> groupChatQBMessageListener = new QBMessageListener<QBGroupChat>() {
            @Override
            public void processMessage(final QBGroupChat groupChat, final QBChatMessage chatMessage) {
                AddStatus("group processMessage: " + chatMessage.getBody());
            }

            @Override
            public void processError(final QBGroupChat groupChat, QBChatException error, QBChatMessage originMessage) {
                AddStatus("group processError: " + error.getMessage());
            }

            @Override
            public void processMessageDelivered(QBGroupChat groupChat, String messageID) {
                // never be called, works only for 1-1 chat
                AddStatus("group processMessageDelivered: " + messageID);
            }

            @Override
            public void processMessageRead(QBGroupChat groupChat, String messageID) {
                // never be called, works only for 1-1 chat
                AddStatus("group processMessageRead: " + messageID);
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

        communication_viewpager_act cva = (communication_viewpager_act) getActivity();

        listofusers_frag lou = cva.getListOfUsersFrag();

        if (lou != null) {
            Log.d("GVE", "Getting users from list of users fragment");
            ArrayList<listofusers_frag.MyObject> users = lou.getUsers();

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).isSelected) {
                    occupantIdsList.add(users.get(i).id);
                }
            }
        } else {
            Log.d("GVE", "Getting all users");
            for (int i = 0; i < qbOtherUsers.size(); i++) {
                occupantIdsList.add(qbOtherUsers.get(i).getId());
            }
        }

        QBDialog dialog = new QBDialog();
        dialog.setName(qbThisUser.getLogin().toUpperCase() + "'s chat");
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);

        QBGroupChatManager groupChatManager = QBChatService.getInstance().getGroupChatManager();
        groupChatManager.createDialog(dialog, new QBEntityCallbackImpl<QBDialog>() {
            @Override
            public void onSuccess(QBDialog dialog, Bundle args) {
                AddStatus("createDialog onSuccess: ");
                NotifyOthers(dialog);
            }

            @Override
            public void onError(List<String> errors) {
                Log.d("GVE", "createDialog onError: " + errors);
            }
        });
    }

    private void NotifyOthers(QBDialog dialog) {
        //AddStatus("NotifyOthers");
        for (Integer userID : dialog.getOccupants()) {

            AddStatus("NotifyOthers: " + userID);
            QBChatMessage chatMessage = createChatNotificationForGroupChatCreation(dialog);
            //long time = DateUtils.getCurrentTime();
            String time = "NOWWWWW";
            chatMessage.setProperty("date_sent", time + "");
            if (qbThisUser != null) {
                Log.d("GVE", "-------------qbThisUser.getLogin(): " + qbThisUser.getLogin());
                chatMessage.setBody(getCurrentTimeStamp() + "  " + qbThisUser.getLogin() + ": " + txtSendMessage.getText());// + " (" + qbOtherUser.getLogin() + ")");
                AddStatus("group/notify: processMessage: " + chatMessage.getBody() + " from " + qbThisUser.getLogin() + " to " + userID);
            } else {
                Log.d("GVE", "-------------qbThisUser == null ");
            }
            QBPrivateChat chat = QBChatService.getInstance().getPrivateChatManager().getChat(userID);
            if (chat == null) {
                Log.d("GVE", "chat == null: " + userID);
                chat = QBChatService.getInstance().getPrivateChatManager().createChat(userID, null);
            }

            try {
                Log.d("GVE", "chat.sendMessage: " + userID);
                chat.sendMessage(chatMessage);
                chat.close();
            } catch (Exception e) {
                // error
            }
        }
    }

    private static String getCurrentTimeStamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(new Date()); // Find today's date
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static QBChatMessage createChatNotificationForGroupChatCreation(QBDialog dialog) {
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

    private void AddConnectionListener() {
        ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {
                AddStatus("connected");
            }

            @Override
            public void authenticated(XMPPConnection connection) {
                AddStatus("authenticated");
            }

            @Override
            public void connectionClosed() {
                AddStatus("connectionClosed");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                // connection closed on error. It will be established soon
                Log.d("GVE", "connectionClosedOnError");
            }

            @Override
            public void reconnectingIn(int seconds) {
                AddStatus("reconnectingIn");
            }

            @Override
            public void reconnectionSuccessful() {
                AddStatus("reconnectionSuccessful");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                AddStatus("reconnectionFailed");
            }
        };

        QBChatService.getInstance().addConnectionListener(connectionListener);
    }

    private boolean IsLoggedIn() {
        boolean bLoggedIn = false;

/*        if (QBChatService.getInstance() == null) {
            try {
                mChatService = QBChatService.getInstance();
            } catch (IllegalStateException e) {
                mChatService = null;
            }
        }*/

        if (QBChatService.getInstance() == null) {

            bLoggedIn = false;
        } else {
            bLoggedIn = QBChatService.getInstance().isLoggedIn();
        }

        Log.d("GVE", "IsLoggedIn: " + bLoggedIn);
        return bLoggedIn;
    }

    private void ChatLogout() {
        AddStatus("-----ChatLogout");

        if (!IsLoggedIn()) {
            return;
        }

        QBChatService.getInstance().logout(new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {
                // success
                QBChatService.getInstance().destroy();
            }

            @Override
            public void onError(final List list) {

            }
        });
    }


    private void AddMessage(String txt) {
        final String txtCopy = txt;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (txtStatus.getVisibility() == View.VISIBLE) {
                    txtReceive.setText(txtCopy + "\r\n" + txtReceive.getText());
                }
            }
        });
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

    private void AddLogin(String txt) {
        final String txtCopy = txt;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtLogin.setText(txtLogin.getText() + txtCopy);
            }
        });
    }
}