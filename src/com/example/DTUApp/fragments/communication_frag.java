package com.example.DTUApp.fragments;

//import android.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.DTUApp.R;
import com.example.DTUApp.activities.communication_viewpager_act;
import com.example.DTUApp.global.constants;
import com.example.DTUApp.global.global_app;
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
public class communication_frag extends base_frag {

    private TextView txtStatus = null;
    private TextView txtReceive = null;
    private EditText txtSendMessage = null;
    private TextView txtLogin = null;
    private QBUser qbMainUser = null;
    private QBUser qbThisUser = null;
    private QBUser qbOtherUser = null;
    private ArrayList<QBUser> qbOtherUsers = null;
    private QBPrivateChatManagerListener privateChatManagerListener = null;
    private QBMessageListener<QBPrivateChat> privateChatMessageListener = null;
    private QBGroupChatManager groupChatManager = null;
    private QBDialog qbGroupDialog = null;

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

   /*     if (QBChatService.getInstance() != null && !QBChatService.getInstance().isLoggedIn()) {*/
            CreateSession();
/*        } else {
            GetAllUsers(txtLogin.getText().toString(), true);
        }*/

        return v;
    }

    private void StopSession() {
        ChatLogout();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // do not log out
/*        ChatLogout();
        qbThisUser = null;
        qbOtherUser = null;
        qbOtherUsers = null;
        chatService = null;
        privateChatManagerListener = null;
        privateChatMessageListener = null;
        groupChatManager = null;*/
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
        QBUsers.signOut(new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                if (qbUser == qbThisUser) {
                    qbThisUser = null;
                }

                if (qbUser == qbMainUser) {
                    qbThisUser = null;
                }

                AddStatus("signOut success");
            }

            @Override
            public void onError(List<String> errors) {
                qbThisUser = null;
                AddStatus("signOut error" + errors);
            }
        });
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

/*                if (!bUserExist) {
                    Log.d("GVE", "User needed to SignIn");
                    SignIn();
                }*/

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
        }

        QBAuth.createSession(qbMainUser, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success, login to chat
                AddStatus("CreateSession Chat success: " + session.getUserId());
                qbMainUser.setId(session.getUserId());
                LoginToChat(qbMainUser);
            }

            @Override
            public void onSuccess() {
                // success, login to chat
                AddStatus("CreateSession Chat success: ");
            }

            @Override
            public void onError(List<String> errors) {
                AddStatus("CreateSession Chat error: " + errors.toString());
            }
        });
    }

    private void LoginToChat(final QBUser user) {
        if (!QBChatService.getInstance().isLoggedIn()) {
            AddStatus("Login to chatService");
            QBChatService.getInstance().login(user, new QBEntityCallbackImpl() {
                @Override
                public void onSuccess() {
                    AddStatus("ChatService Login success");

                    try {
                        //mChatService.startAutoSendPresence(60);
                        QBChatService.getInstance().startAutoSendPresence(60);
                    } catch (SmackException.NotLoggedInException e) {
                        e.printStackTrace();
                    }

                    AddConnectionListener();
                    //GetChatDialogs();
                 /*   StartListeningForPrivateChats();*/
                  /*  StartListeningForGroupChats();*/
                }

                @Override
                public void onError(List errors) {
                    AddStatus("ChatService Login error: " + errors);
                }
            });
        } else {
            AddStatus("Already logged in to chatService");
            AddConnectionListener();
            StartListeningForPrivateChats();
            StartListeningForGroupChats();
        }
    }

    private void AddConnectionListener() {
        AddStatus("AddConnectionListener: initialised");

        ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {
                AddStatus("AddConnectionListener: connected");
            }

            @Override
            public void authenticated(XMPPConnection connection) {
                AddStatus("AddConnectionListener: authenticated");
            }

            @Override
            public void connectionClosed() {
                AddStatus("AddConnectionListener: connectionClosed");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                // connection closed on error. It will be established soon
                AddStatus("AddConnectionListener: connectionClosedOnError");
            }

            @Override
            public void reconnectingIn(int seconds) {
                AddStatus("AddConnectionListener: reconnectingIn");
            }

            @Override
            public void reconnectionSuccessful() {
                AddStatus("AddConnectionListener: reconnectionSuccessful");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                AddStatus("AddConnectionListener: reconnectionFailed");
            }
        };

        QBChatService.getInstance().addConnectionListener(connectionListener);
    }

    private void GetChatDialogs() {
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
                AddStatus("getChatDialogs error: " + errors);
            }
        });
    }

    private void NewGroupDialog() {
        ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();

        for (int i = 0; i < qbOtherUsers.size(); i++) {
            occupantIdsList.add(qbOtherUsers.get(i).getId());
        }

        if (qbGroupDialog == null) {
            qbGroupDialog = new QBDialog();
        }

        qbGroupDialog.setName(qbThisUser.getLogin() + " initiated chat");
        qbGroupDialog.setType(QBDialogType.GROUP);
        qbGroupDialog.setOccupantsIds(occupantIdsList);

        QBGroupChatManager groupChatManager = QBChatService.getInstance().getGroupChatManager();
        groupChatManager.createDialog(qbGroupDialog, new QBEntityCallbackImpl<QBDialog>() {
            @Override
            public void onSuccess(QBDialog dialog, Bundle args) {
                AddStatus("createDialog onSuccess: ");
                NotifyOthers(dialog);
            }

            @Override
            public void onError(List<String> errors) {
                AddStatus("createDialog onError: " + errors);
            }
        });
    }

    private void StartListeningForPrivateChats() {
        privateChatMessageListener = new QBMessageListener<QBPrivateChat>() {
            @Override
            public void processMessage(QBPrivateChat privateChat, final QBChatMessage chatMessage) {
                Log.d("GVE", "StartListeningForPrivateChats: processMessage: " + chatMessage.getBody() + " from " + chatMessage.getSenderId());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        AddMessage(chatMessage.getBody());
                        //txtReceive.setText(chatMessage.getBody() + "\r\n" + txtReceive.getText());
                    }
                });
            }

            @Override
            public void processError(QBPrivateChat privateChat, QBChatException error, QBChatMessage originMessage) {
                Log.d("GVE", "StartListeningForPrivateChats: processMessage: " + error.getMessage());
            }

            @Override
            public void processMessageDelivered(QBPrivateChat privateChat, String messageID) {
                Log.d("GVE", "StartListeningForPrivateChats: processMessageDelivered: " + messageID);
            }

            @Override
            public void processMessageRead(QBPrivateChat privateChat, String messageID) {
                Log.d("GVE", "StartListeningForPrivateChats: processMessageRead: " + messageID);
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

    private void StartListeningForGroupChats() {
        AddStatus("StartListeningForGroupChats initialised");

        final QBMessageListener<QBGroupChat> groupChatQBMessageListener = new QBMessageListener<QBGroupChat>() {
            @Override
            public void processMessage(final QBGroupChat groupChat, final QBChatMessage chatMessage) {
                Log.d("GVE", "StartListeningForGroupChats: processMessage: " + chatMessage.getBody());
            }

            @Override
            public void processError(final QBGroupChat groupChat, QBChatException error, QBChatMessage originMessage) {
                Log.d("GVE", "StartListeningForGroupChats: processError: " + error.getMessage());
            }

            @Override
            public void processMessageDelivered(QBGroupChat groupChat, String messageID) {
                // never be called, works only for 1-1 chat
                Log.d("GVE", "StartListeningForGroupChats: processMessageDelivered: " + messageID);
            }

            @Override
            public void processMessageRead(QBGroupChat groupChat, String messageID) {
                // never be called, works only for 1-1 chat
                Log.d("GVE", "StartListeningForGroupChats: processMessageRead: " + messageID);
            }
        };

        DiscussionHistory history = new DiscussionHistory();
        history.setMaxStanzas(0);

        Log.d("GVE", "groupChatManager 1");
        groupChatManager = QBChatService.getInstance().getGroupChatManager();

        if (qbGroupDialog == null)
        {
            qbGroupDialog = new QBDialog();
            qbGroupDialog.setName(qbThisUser.getLogin() + " initiated chat");
            qbGroupDialog.setType(QBDialogType.GROUP);
            Log.d("GVE", "groupChatManager 2");
        }

        Log.d("GVE", "groupChatManager 3 " + qbGroupDialog.getRoomJid());
        final QBGroupChat currentChatRoom = groupChatManager.createGroupChat(qbGroupDialog.getRoomJid());

        Log.d("GVE", "groupChatManager 4");

        currentChatRoom.join(history, new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {
                // add listeners
                AddStatus("currentChatRoom join: success");
                currentChatRoom.addMessageListener(groupChatQBMessageListener);
            }

            @Override
            public void onError(final List list) {
                AddStatus("currentChatRoom join: error: " + list.toString());
            }
        });

        Log.d("GVE", "groupChatManager 5");
    }

    private void SendMessage() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                GetChatDialogs();
            }
        });
    }

    private void NotifyOthers(QBDialog dialog) {
        AddStatus("NotifyOthers");
        for (Integer userID : dialog.getOccupants()) {
            Log.d("GVE", "NotifyOthers: " + userID);
            QBChatMessage chatMessage = createChatNotificationForGroupChatCreation(dialog);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateAndTime = sdf.format(new Date());
            chatMessage.setProperty("date_sent", currentDateAndTime);
            if (qbThisUser != null) {
                chatMessage.setBody(getCurrentTimeStamp() + "  " + qbThisUser.getLogin() + ": " + txtSendMessage.getText());
                Log.d("GVE", "------------SENDING: processMessage: " + chatMessage.getBody() + " from " + qbThisUser.getLogin() + " to " + userID);
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
            } catch (Exception e) {
                // error
            }
        }
    }

    private static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(new Date());
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

    private void ChatLogout() {
        Log.d("GVE", "-----ChatLogout");

        if (QBChatService.getInstance().isLoggedIn()) {
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
    }

    private void AddMessage(String txt) {
        final String txtCopy = txt;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtReceive.setText(txtCopy + "\r\n" + txtReceive.getText());
            }
        });
    }

    private void AddStatus(String txt) {
        final String txtCopy = txt;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText(txtCopy + "\r\n" + txtStatus.getText());
            }
        });
    }
}