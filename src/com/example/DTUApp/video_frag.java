package com.example.DTUApp;

//import android.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBVideoChatWebRTCSignalingManager;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gve on 18-03-2015.
 */
public class video_frag extends base_frag {

    private QBUser qbMainUser = null;
    private QBUser qbThisUser = null;
    private QBRTCClient vv;
    private TextView txtStatus = null;
    private TextView txtReceive = null;
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
        String username = global_app.GetPref().getString(constants.USERNAME, "NO USERNAME");
        String password = global_app.GetPref().getString(constants.PASSWORD, "NO PASSWORD");

        QBAuth.createSession(username, password, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle bundle) {

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

                        //StartVideo();
                    }

                    @Override
                    public void onError(List errors) {
                        //error

                        AddStatus("errors1 " + errors.toString());

                        //StartVideo();
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


        AddStatus("StartVideo 1");
        // success
        if (!QBRTCClient.isInitiated()) {
            QBRTCClient.init(getActivity());
        }

        AddStatus("StartVideo 2");
        QBVideoChatWebRTCSignalingManager videoChatWebRTCSignalingManager =
                QBChatService.getInstance().getVideoChatWebRTCSignalingManager();


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
    public void onReceiveNewSession(QBRTCSession session) {

        AddStatus("onReceiveNewSession " + session.toString());

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
        session.acceptCall(userInfo);
    }

    /**
     * Calls in case when user didn't answer in timer expiration period
     */
    void onUserNotAnswer(QBRTCSession session, Integer userID) {
        AddStatus("onUserNotAnswer " + session.toString());

    }

    ;

    /**
     * Calls in case when opponent has rejected you call
     */
    void onCallRejectByUser(QBRTCSession session, Integer userID, Map<String, String> userInfo) {

        AddStatus("onCallRejectByUser " + session.toString());
    }

    ;

    /**
     * Called in case when opponent hang up
     */
    void onReceiveHangUpFromUser(QBRTCSession session, Integer userID) {

        AddStatus("onReceiveHangUpFromUser " + session.toString());
    }

    ;

    /**
     * Calls once, when video track was created, while session was initializing by chat initiator
     */
    void onLocalVideoTrackReceive(QBRTCSession session, QBRTCVideoTrack videoTrack) {

        AddStatus("onLocalVideoTrackReceive " + session.toString());
    }

    ;

    /**
     * Calls each time when some of session channels receive remote video.
     */
    void onRemoteVideoTrackReceive(QBRTCSession session, QBRTCVideoTrack videoTrack, Integer userID) {

        AddStatus("onRemoteVideoTrackReceive " + session.toString());
    }

    ;

    /**
     * Called in case when connection state changed
     */
    void onConnectionClosedForUser(QBRTCSession session, Integer userID) {

        AddStatus("onConnectionClosedForUser " + session.toString());
    }

    ;

    /**
     * Calls each time when channel initialization was started for some user
     */
    void onStartConnectToUser(QBRTCSession session, Integer userID) {
        AddStatus("onStartConnectToUser " + session.toString());

    }

    ;

    /**
     * Called in case when connection with opponent is established
     */
    void onConnectedToUser(QBRTCSession session, Integer userID) {

        AddStatus("onConnectedToUser " + session.toString());
    }

    ;

    /**
     * Called in case when opponent disconnected
     */
    void onDisconnectedFromUser(QBRTCSession session, Integer userID) {

        AddStatus("onDisconnectedFromUser " + session.toString());
    }

    ;

    /**
     * Called in case when disconnected by timeout
     */
    void onDisconnectedTimeoutFromUser(QBRTCSession session, Integer userID) {

        AddStatus("onDisconnectedTimeoutFromUser " + session.toString());
    }

    ;

    /**
     * Called in case when connection failed with user
     */
    void onConnectionFailedWithUser(QBRTCSession session, Integer userID) {

        AddStatus("onConnectionFailedWithUser " + session.toString());
    }

    ;

    /**
     * Called in case when session will close
     */
    void onSessionStartClose(QBRTCSession session) {

        AddStatus("onSessionStartClose " + session.toString());
    }

    ;

    /**
     * Calls when session was closed.
     */
    void onSessionClosed(QBRTCSession session) {
        AddStatus("onSessionClosed " + session.toString());

    }

    ;

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
