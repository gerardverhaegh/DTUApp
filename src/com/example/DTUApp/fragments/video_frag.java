package com.example.DTUApp.fragments;

//import android.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.DTUApp.R;
import com.example.DTUApp.global.constants;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBVideoChatWebRTCSignalingManager;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientCallback;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import org.webrtc.PeerConnection;

import java.util.*;

/**
 * Created by gve on 18-03-2015.
 */
public class video_frag extends base_frag implements QBRTCClientCallback {

    private QBUser qbMainUser = null;
    private QBUser qbThisUser = null;
    private QBRTCClient vv;
    private TextView txtStatus = null;
    private TextView txtReceive = null;

    private String currentSession = null;

    /*private MyGLSurfaceView mPreview;
*/

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        Log.d("GVE", "VIDEO FRAG");

        View v = inflater.inflate(R.layout.video_frag, container, false);

    /*    mPreview = (android.opengl.GLSurfaceView) v.findViewById(R.id.videoView);*/

        txtStatus = (TextView) v.findViewById(R.id.txtStatus);
        txtReceive = (TextView) v.findViewById(R.id.txtReceive);

        if (savedInstanceState == null) {
            CreateSession();
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void CreateSession() {
        Log.d("GVE", "----CreateSession");
        // String appId, String authKey, String authSecret
        QBSettings.getInstance().fastConfigInit(constants.APP_ID, constants.AUTH_KEY, constants.AUTH_SECRET);

        qbMainUser = new QBUser(constants.USER_LOGIN, constants.USER_PASSWORD);
/*        String username = global_app.GetPref().getString(constants.USERNAME, "NO USERNAME");
        String password = global_app.GetPref().getString(constants.PASSWORD, "NO PASSWORD");

        username = "gve1";
        password = "koek4321";

        Log.d("GVE", "----CreateSession username: " + username + ", password " + password);*/

        QBAuth.createSession(qbMainUser, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle bundle) {

                Log.d("GVE", "----CreateSession onSuccess");
                qbMainUser.setId(session.getUserId());

                // INIT CHAT SERVICE
                if (!QBChatService.isInitialized()) {
                    QBChatService.init(getActivity());
                }

                // LOG IN CHAT SERVICE
                QBChatService.getInstance().login(qbMainUser, new QBEntityCallbackImpl<QBUser>() {

                    @Override
                    public void onSuccess() {
                        AddStatus("Login success");

                        StartVideo();
                    }

                    @Override
                    public void onError(List errors) {
                        //error

                        AddStatus("errors1 " + errors.toString());
                    }
                });
            }

            @Override
            public void onError(List<String> errors) {
                //error
                AddStatus("errors2 " + errors.toString());
            }
        });
    }

    private void StartVideo() {
        // Add activity as callback to RTCClient
        if (QBRTCClient.isInitiated()) {
            QBRTCClient.getInstance().addCallback(this);
        }
        AddStatus("StartVideo 1");
        // success
        if (!QBRTCClient.isInitiated()) {
            QBRTCClient.init(getActivity());
        }




        AddStatus("StartVideo 2");
        QBVideoChatWebRTCSignalingManager videoChatWebRTCSignalingManager =
                QBChatService.getInstance().getVideoChatWebRTCSignalingManager();

        AddStatus("StartVideo 2.5");

        // Set custom ice servers up. Use it in case you want set your own servers instead of defaults
        List<PeerConnection.IceServer> iceServerList = new LinkedList<PeerConnection.IceServer>();
        iceServerList.add(new PeerConnection.IceServer("turn:numb.viagenie.ca", "petrbubnov@grr.la", "petrbubnov@grr.la"));
        iceServerList.add(new PeerConnection.IceServer("turn:numb.viagenie.ca:3478?transport=udp", "petrbubnov@grr.la", "petrbubnov@grr.la"));
        iceServerList.add(new PeerConnection.IceServer("turn:numb.viagenie.ca:3478?transport=tcp", "petrbubnov@grr.la", "petrbubnov@grr.la"));
        QBRTCConfig.setIceServerList(iceServerList);

        AddStatus("StartVideo 3");
        //Add signalling manger
        QBRTCClient.getInstance().setSignalingManager(videoChatWebRTCSignalingManager);

        AddStatus("StartVideo 4");
        //Set conference type
        //There are two types of calls exists:
        // - QB_CONFERENCE_TYPE_VIDEO - for video call;
        // - QB_CONFERENCE_TYPE_AUDIO - for audio call;
        QBRTCTypes.QBConferenceType qbConferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;

        AddStatus("StartVideo 5");
        //Initiate opponents list
        List<Integer> opponents = new ArrayList<Integer>();
        opponents.add(2761528); //12345 - QBUser ID

        //Set user information
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("key", "value");

        AddStatus("StartVideo 6");

        //Init session
        QBRTCSession session =
                QBRTCClient.getInstance().createNewSessionWithOpponents(opponents, qbConferenceType);

        AddStatus("StartVideo 7");

        //Start call
        session.startCall(session.getUserInfo());

        AddStatus("startCall done");
    }

    /**
     * Calls each time when new session was received.
     * Calls in case new conversation, not equals current conversation, proposition was received
     */
    @Override
    public void onReceiveNewSession(QBRTCSession session) {


        if (currentSession == null) {
            Log.d("GVE", "Start new session");
            Log.d("GVE", "Income call");
            QBRTCClient.getInstance().getSessions().put(session.getSessionID(), session);
            setCurrentSession(session);
            //addIncomeCallFragment(session);
        } else {
            Log.d("GVE", "Stop new session. Device now is busy");
            session.rejectCall(null);
        }

/*        AddStatus("onReceiveNewSession " + session.toString());

        // .....
        // ..... your code
        // .....

        // Set userInfo
        // User can set any string key and value in user info
        // Then retrieve this data fom sessions which is returned in callbacks
        // and parse them as he wish
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("Key", "Value");

        // Accept incoming call
        session.acceptCall(userInfo);*/
    }
    public QBRTCSession getCurrentSession() {
        return QBRTCClient.getInstance().getSessions().get(currentSession);
    }

    public void setCurrentSession(QBRTCSession session) {
        if (!QBRTCClient.getInstance().getSessions().containsKey(session.getSessionID())) {
            addSession(session);
        }
        currentSession = session.getSessionID();
    }

    public QBRTCSession getSession(String sessionID) {
        return QBRTCClient.getInstance().getSessions().get(sessionID);
    }

//    public void setVideoViewVisibility(int visibility){
//        videoView.setVisibility(visibility);
//    }

    public void addSession(QBRTCSession session) {
        QBRTCClient.getInstance().getSessions().put(session.getSessionID(), session);
    }

    /**
     * Calls in case when user didn't answer in timer expiration period
     */
    @Override
    public void onUserNotAnswer(QBRTCSession session, Integer userID) {
        AddStatus("onUserNotAnswer " + session.toString());

    }

    /**
     * Calls in case when opponent has rejected you call
     */
    @Override
    public void onCallRejectByUser(QBRTCSession session, Integer userID, Map<String, String> userInfo) {

        AddStatus("onCallRejectByUser " + session.toString());
    }

    /**
     * Called in case when opponent hang up
     */
    @Override
    public void onReceiveHangUpFromUser(QBRTCSession session, Integer userID) {

        AddStatus("onReceiveHangUpFromUser " + session.toString());
    }

    /**
     * Calls once, when video track was created, while session was initializing by chat initiator
     */
    @Override
    public void onLocalVideoTrackReceive(QBRTCSession session, QBRTCVideoTrack videoTrack) {

        AddStatus("onLocalVideoTrackReceive " + session.toString());
    }

    /**
     * Calls each time when some of session channels receive remote video.
     */
    @Override
    public void onRemoteVideoTrackReceive(QBRTCSession session, QBRTCVideoTrack videoTrack, Integer userID) {

        AddStatus("onRemoteVideoTrackReceive " + session.toString());
    }

    /**
     * Called in case when connection state changed
     */
    @Override
    public void onConnectionClosedForUser(QBRTCSession session, Integer userID) {

        AddStatus("onConnectionClosedForUser " + session.toString());
    }

    /**
     * Calls each time when channel initialization was started for some user
     */
    @Override
    public void onStartConnectToUser(QBRTCSession session, Integer userID) {
        AddStatus("onStartConnectToUser " + session.toString());

    }

    ;

    /**
     * Called in case when connection with opponent is established
     */
    @Override
    public void onConnectedToUser(QBRTCSession session, Integer userID) {

        AddStatus("onConnectedToUser " + session.toString());
    }

    /**
     * Called in case when opponent disconnected
     */
    @Override
    public void onDisconnectedFromUser(QBRTCSession session, Integer userID) {

        AddStatus("onDisconnectedFromUser " + session.toString());
    }

    /**
     * Called in case when disconnected by timeout
     */
    @Override
    public  void onDisconnectedTimeoutFromUser(QBRTCSession session, Integer userID) {

        AddStatus("onDisconnectedTimeoutFromUser " + session.toString());
    }

    /**
     * Called in case when connection failed with user
     */
    @Override
    public void onConnectionFailedWithUser(QBRTCSession session, Integer userID) {

        AddStatus("onConnectionFailedWithUser " + session.toString());
    }

    /**
     * Called in case when session will close
     */
    @Override
    public void onSessionStartClose(QBRTCSession session) {

        AddStatus("onSessionStartClose " + session.toString());
    }

    /**
     * Calls when session was closed.
     */
    @Override
    public void onSessionClosed(QBRTCSession session) {
        AddStatus("onSessionClosed " + session.toString());

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
/*

class MyGLSurfaceView extends GLSurfaceView {
    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        //setRenderer(new CustomRenderer());
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        //setRenderer(new CustomRenderer());
    }
}*/
