package com.s23010149.mentorme;

import android.content.Context;
import android.content.Intent;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

public class JitsiMeetActivityLauncher {
    public static void join(Context context, JitsiMeetConferenceOptions options) {
        Intent intent = new Intent(context, JitsiMeetActivity.class);
        intent.setAction("org.jitsi.meet.CONFERENCE");
        intent.putExtra("JitsiMeetConferenceOptions", options);
        context.startActivity(intent);
    }
}