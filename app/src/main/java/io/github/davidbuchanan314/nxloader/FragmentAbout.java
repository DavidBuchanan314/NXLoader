package io.github.davidbuchanan314.nxloader;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.*;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView aboutText = view.findViewById(R.id.about_text);
        String aboutHtml = getString(R.string.about);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutText.setText(Html.fromHtml(aboutHtml, Html.FROM_HTML_MODE_LEGACY));
        } else {
            aboutText.setText(Html.fromHtml(aboutHtml));
        }
        aboutText.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}
