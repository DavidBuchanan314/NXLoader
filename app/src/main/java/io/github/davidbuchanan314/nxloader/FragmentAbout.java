package io.github.davidbuchanan314.nxloader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentAbout extends Fragment {
    public FragmentAbout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView aboutLinksGithub = view.findViewById(R.id.about_links_github);
        TextView aboutLinksTwitter = view.findViewById(R.id.about_links_twitter);

        aboutLinksGithub.setOnClickListener(view1 -> Utils.openBrowser(view.getContext(), Constants.LINK_GITHUB));
        aboutLinksTwitter.setOnClickListener(view1 -> Utils.openBrowser(view.getContext(), Constants.LINK_TWITTER));

        return view;
    }
}
