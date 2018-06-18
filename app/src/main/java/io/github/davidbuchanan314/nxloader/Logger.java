package io.github.davidbuchanan314.nxloader;

import android.content.Context;
import android.content.Intent;

public class Logger {
    public static final String ACTION_LOG_UPDATE = "io.github.davidbuchanan314.LOG_UPDATE";

    public static void log(Context ctx, String message) {
        Intent i = new Intent(ACTION_LOG_UPDATE);
        i.putExtra("msg", message);
        ctx.sendBroadcast(i);
    }
}
