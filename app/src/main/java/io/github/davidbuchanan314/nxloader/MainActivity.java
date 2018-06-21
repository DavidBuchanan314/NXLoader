package io.github.davidbuchanan314.nxloader;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.davidbuchanan314.nxloader.utils.PermissionsUtils;
import io.github.davidbuchanan314.nxloader.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;

    private FragmentLogs logFragment;
    BroadcastReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // http://www.gadgetsaint.com/android/create-viewpager-tabs-android/
        ViewPager viewPager = findViewById(R.id.main_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        logFragment = new FragmentLogs();
        adapter.addFragment(logFragment, getString(R.string.logs_title));
        adapter.addFragment(new FragmentConfig(), getString(R.string.config_title));
        adapter.addFragment(new FragmentAbout(), getString(R.string.about_title));
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);

        myReceiver = new ReceiveMessages();
        registerReceiver(myReceiver, new IntentFilter(Constants.LOGGER_ACTION));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    // primary payload selection button
    public void onConfigPrimaryPayloadClick(View view) {
        if (!PermissionsUtils.checkPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionsUtils.showPermissionDialog(this);
        } else {
            getPayloadFromStorage();
        }
    }

    private void getPayloadFromStorage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    // primary payload reset button
    public void onConfigRestoreClick(View view) {
        SharedPreferences prefs = getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constants.PREFERENCES_KEY);
        editor.apply();

        Toast.makeText(this, R.string.config_toast_restored, Toast.LENGTH_SHORT).show();
        Logger.log(this, "[*] Payload reset to default (fusee.bin)");
    }

    // After payload selected
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null) {
            Uri uri = resultData.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Utils.copyFile(inputStream, new File(getFilesDir().getPath() + "/payload.bin"));

                String file_name = Utils.getFileName(this, uri);
                SharedPreferences prefs = getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Constants.PREFERENCES_KEY, file_name);
                editor.apply();

                Toast.makeText(this, R.string.config_toast_new_payload, Toast.LENGTH_SHORT).show();
                Logger.log(this, "[*] New payload file selected: " + file_name);
            } catch (IOException e) {
                Toast.makeText(this, R.string.config_toast_new_error, Toast.LENGTH_SHORT).show();
                Logger.log(this, "[-] Failed to set new payload: " + e.toString());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtils.PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPayloadFromStorage();
                } else {
                    Toast.makeText(this, R.string.permission_storage_error, Toast.LENGTH_SHORT).show();
                }
        }
    }

    // Adapter for the viewpager using FragmentPagerAdapter
    // http://www.gadgetsaint.com/android/create-viewpager-tabs-android/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    // https://stackoverflow.com/a/7276808/4454877
    class ReceiveMessages extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String msg = bundle.getString("msg");
            logFragment.appendLog(msg);

            // switch to foreground
            if (!(context instanceof MainActivity)) {
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
                activityManager.moveTaskToFront(getTaskId(), 0);
            }

        }
    }
}
