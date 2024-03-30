package com.apck.proyectfx.actyvity_vc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.apck.proyectfx.R;
import com.apck.proyectfx.model.User_VC;
import com.apck.proyectfx.network.ApiClient;
import com.apck.proyectfx.network.ApiService;
import com.apck.proyectfx.utilities.Constants;
import com.apck.proyectfx.utilities.PreferenceManager;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoinginvitationActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private String inviterToken = null;
    String meetingRoom = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoinginvitation);

        preferenceManager = new PreferenceManager(getApplicationContext());


        ImageView imageMeetingtype = findViewById(R.id.imageMeetingType);
        String meetingType = getIntent().getStringExtra("type");

        if(meetingType != null){
            if(meetingType.equals("video")){
                imageMeetingtype.setImageResource(R.drawable.ic_baseline_video_call);
            }
        }

        TextView textusername = findViewById(R.id.username);
        TextView textmail = findViewById(R.id.usermail);

        User_VC user_vc = (User_VC) getIntent().getSerializableExtra("user");
        if(user_vc != null){
            textusername.setText(user_vc.user_name);
            textmail.setText(user_vc.email);
        }

        ImageView stopinvitation = findViewById(R.id.stopinvitation);
        stopinvitation.setOnClickListener(view -> {
            if(user_vc != null){
                cancelInvitation(user_vc.token);
            }
        });



        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                inviterToken = task.getResult();

                if(meetingType != null && user_vc != null){
                    initiateMeeting(meetingType, user_vc.token);
                    Log.d("mytag", "1");
                }

            }

        });


    }

    private void initiateMeeting(String meetingType, String receiverToken){
        Log.d("mytag", "receiverToken: "+receiverToken);
        Log.d("mytag", "inviterToken: "+ inviterToken);
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION);
            data.put(Constants.REMOTE_MSG_MEETING_TYPE, meetingType);
            data.put(Constants.KEY_USER_NAME, preferenceManager.getString(Constants.KEY_USER_NAME));
            data.put(Constants.KEY_USER_MAIL, preferenceManager.getString(Constants.KEY_USER_MAIL));
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, inviterToken);

            meetingRoom = preferenceManager.getString(Constants.KEY_USER_ID)+ "_" +
                    UUID.randomUUID().toString().substring(0, 5);
            data.put(Constants.REMOTE_MSG_MEETING_ROOM, meetingRoom);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION);
            Log.d("mytag", "2");
            Log.d("mytag", body.toString()+Constants.REMOTE_MSG_INVITATION);


        }catch (Exception exception){
            Toast.makeText(OutgoinginvitationActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void sendRemoteMessage(String remoteMessageBody, String type){
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                Constants.getRemoteMessageHeader(), remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    if(type.equals(Constants.REMOTE_MSG_INVITATION)){
                        //Toast.makeText(OutgoinginvitationActivity.this, "invitation send succesfully", Toast.LENGTH_SHORT).show();
                    }else if(type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)){
                        //Toast.makeText(OutgoinginvitationActivity.this, "invitation canceled", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Toast.makeText(OutgoinginvitationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(OutgoinginvitationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("mytag", t.getMessage());
                finish();
            }
        });
    }

    private void cancelInvitation(String receiverToken){
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, Constants.REMOTE_MSG_INVITATION_CANCELLED);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION_RESPONSE);

        }catch (Exception exception){
            Toast.makeText(OutgoinginvitationActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if(type != null){
                if(type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)){
                    try {
                        URL serverURL = new URL("https://meet.jit.si");
                        JitsiMeetConferenceOptions conferenceOptions = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(serverURL)
                                .setWelcomePageEnabled(false)
                                .setRoom(meetingRoom)
                                .build();
                        JitsiMeetActivity.launch(OutgoinginvitationActivity.this, conferenceOptions);
                        finish();

                    }catch (Exception exception){
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else if(type.equals(Constants.REMOTE_MSG_INVITATION_REJECTED)){
                    //Toast.makeText(context, "invitation Rejected", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }
}