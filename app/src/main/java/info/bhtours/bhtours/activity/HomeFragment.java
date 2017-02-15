package info.bhtours.bhtours.activity;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import info.bhtours.bhtours.R;


public class HomeFragment extends Fragment {

    private String pageid;
    private String pagedesc;
    private String descPageId;
    private String currentlanguage;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            pageid = getArguments().getString("pageId");
            descPageId = getArguments().getString("descpageId");
            pagedesc = getArguments().getString("pageDesc");
            currentlanguage = getArguments().getString("language");
        }

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        WebView wv= (WebView)rootView.findViewById(R.id.webViewDesc);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        wv.loadDataWithBaseURL(getString(R.string.base_url),pagedesc, mimeType, encoding, "");

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
