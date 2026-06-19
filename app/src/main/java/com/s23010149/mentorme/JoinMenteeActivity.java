package com.s23010149.mentorme;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class JoinMenteeActivity extends AppCompatActivity {

    private EditText etMeetingCode;
    private Button btnJoinNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_mentee);

        etMeetingCode = findViewById(R.id.etMeetingCode);
        btnJoinNow = findViewById(R.id.btnJoinNow);

        // Default server
        try {
            URL serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .setAudioMuted(false)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        btnJoinNow.setOnClickListener(v -> {
            String roomName = etMeetingCode.getText().toString().trim();

            if (TextUtils.isEmpty(roomName)) {
                etMeetingCode.setError("Meeting code is required");
                etMeetingCode.requestFocus();
                return;
            }

            // Remove spaces for safe room id
            roomName = roomName.replace(" ", "");

            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(roomName)
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .build();

            Toast.makeText(this, "Joining: " + roomName, Toast.LENGTH_SHORT).show();
            JitsiMeetActivityLauncher.join(this, options);
        });
    }
}