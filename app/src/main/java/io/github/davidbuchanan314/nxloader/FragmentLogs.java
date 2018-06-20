package io.github.davidbuchanan314.nxloader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        logText = view.findViewById(R.id.logs_actions);
        logText.setText("\n[*] App started\n");
        return view;
    }

    public void appendLog(String message) {
        if (logText != null)
            logText.append(message + "\n");
    }
}
