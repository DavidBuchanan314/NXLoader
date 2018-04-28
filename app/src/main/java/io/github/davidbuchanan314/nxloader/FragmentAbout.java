package io.github.davidbuchanan314.nxloader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;

public class FragmentAbout extends Fragment {
    public FragmentAbout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
