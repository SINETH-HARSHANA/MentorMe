package com.s23010149.mentorme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import java.net.URL;

public class Appointmentactivity extends AppCompatActivity {

    private Button btnVideoCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_appointment); // Replace with your actual layout file name

        btnVideoCall = findViewById(R.id.btnVideoCall);

        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startJitsiCall();
            }
        });
    }

    private void startJitsiCall() {
        try {
            // You can generate a unique room name or use a mentor-specific room
            String roomName = "mentor_session_" + System.currentTimeMillis();

            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom(roomName)
                    .build();

            JitsiMeetActivity.launch(this, options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}