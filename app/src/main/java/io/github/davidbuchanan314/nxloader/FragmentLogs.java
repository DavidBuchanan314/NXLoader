package io.github.davidbuchanan314.nxloader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.TextView;
import android.preference.PreferenceManager;


public class FragmentLogs extends Fragment {
    private TextView logText;

    public FragmentLogs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        logText = view.findViewById(R.id.log_text);
        logText.setText(getString(R.string.log_app_started));
        return view;
    }

    public void appendLog(String message) {
        if (logText != null)
            logText.append(message + "\n");
    }
}
