package io.github.davidbuchanan314.nxloader;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;

public class Logger {
    public static void log(Context context, String message) {
        Intent intent = new Intent(Constants.LOGGER_ACTION);
        intent.putExtra("msg", message);
        context.sendBroadcast(intent);
    }
}