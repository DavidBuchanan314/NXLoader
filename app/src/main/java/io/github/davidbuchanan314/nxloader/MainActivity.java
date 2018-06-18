package io.github.davidbuchanan314.nxloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.*;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private FragmentLogs logFragment;

    public static final String PREFERENCE_PAYLOAD_NAME = "io.github.davidbuchanan314.PREFERENCE_PAYLOAD_NAME";

    BroadcastReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // http://www.gadgetsaint.com/android/create-viewpager-tabs-android/
        ViewPager viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        logFragment = new FragmentLogs();
        adapter.addFragment(logFragment, getString(R.string.tab_logs));
        adapter.addFragment(new FragmentConfig(), getString(R.string.tag_config));
        adapter.addFragment(new FragmentAbout(), getString(R.string.tab_about));
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        myReceiver = new ReceiveMessages();
        registerReceiver(myReceiver, new IntentFilter(Logger.ACTION_LOG_UPDATE));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    // primary payload selection button
    public void primarySelect(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    // primary payload reset button
    @SuppressLint("ApplySharedPref")
    public void primaryReset(View view) {
        SharedPreferences prefs = getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREFERENCE_PAYLOAD_NAME);
        editor.commit();
        Logger.log(this, getString(R.string.log_payload_reset));
    }

    // After payload selected
    @SuppressLint("ApplySharedPref")
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null && resultData.getData() != null) {
            Uri uri = resultData.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                if (inputStream == null){
                    Logger.log(this, getString(R.string.log_payload_select_fail, "unable to open stream"));
                    return;
                }

                Utils.copyFile(inputStream, new File(getFilesDir().getPath() + "/payload.bin"));

                String file_name = Utils.getFileName(this, uri);
                SharedPreferences prefs = getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PREFERENCE_PAYLOAD_NAME, file_name);
                editor.commit();

                Logger.log(this, getString(R.string.log_payload_selected, file_name));
            } catch (IOException e) {
                Logger.log(this, getString(R.string.log_payload_select_fail, e.toString()));
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
    class ReceiveMessages extends BroadcastReceiver{
        @Override
        public void onReceive(Context ctx, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle == null) {
                return;
            }

            String msg = bundle.getString("msg");
            logFragment.appendLog(msg);

            // switch to foreground
            if (!(ctx instanceof MainActivity)) {
                ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Service.ACTIVITY_SERVICE);

                if (activityManager != null)
                    activityManager.moveTaskToFront(getTaskId(), 0);
            }

        }
    }
}
