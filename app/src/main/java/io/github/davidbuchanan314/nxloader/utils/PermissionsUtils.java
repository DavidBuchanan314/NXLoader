package io.github.davidbuchanan314.nxloader.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import io.github.davidbuchanan314.nxloader.R;

public class PermissionsUtils {

    public static final int PERMISSIONS_REQUEST_CODE = 125;

    private static void requestPermissions(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, PERMISSIONS_REQUEST_CODE);
    }

    public static boolean checkPermissionGranted(Activity activity, String permission) {
        int result = ContextCompat.checkSelfPermission(activity, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void showPermissionDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.permission_storage_dialog_title);
        builder.setMessage(R.string.permission_storage_dialog_description);

        AlertDialog storagePermissionDialog = builder.create();
        storagePermissionDialog.setButton(AlertDialog.BUTTON_POSITIVE, activity.getString(R.string.permission_storage_button), (dialogInterface, i) -> {
            storagePermissionDialog.dismiss();
            requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        });

        storagePermissionDialog.show();
    }
}