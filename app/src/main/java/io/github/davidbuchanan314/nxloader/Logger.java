package io.github.davidbuchanan314.nxloader;

import android.content.Context;
import android.content.Intent;

public class Logger {
    public static void log(Context ctx, String message) {
        Intent i = new Intent("io.github.davidbuchanan314.LOG_UPDATE");
        i.putExtra("msg", message);
        ctx.sendBroadcast(i);
    }
}
