package info.bhtours.bhtours.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.bhtours.bhtours.R;
import info.bhtours.bhtours.util.DataLoader;
import info.bhtours.bhtours.util.MySpinnerDialog;

/**
 * Created by Ravi on 29/07/15.
 */
public class AboutFragment extends Fragment {

    private String pageid;
    private String pagedesc;
    private String descPageId;
    private String currentlanguage;

    private MySpinnerDialog myInstance;
    DataLoader dataLoader;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = this.getFragmentManager();
        myInstance = new MySpinnerDialog();
        myInstance.show(fm, "some_tag");
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
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataLoader = new DataLoader(this.getActivity().getApplication().getApplicationContext(),this);
        dataLoader.callWS("getPage", descPageId, "about");
    }

    public void loadAbout(String retJSON)
    {
        myInstance.dismiss();
        try {
            JSONObject jsonResponse = new JSONObject(retJSON);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("page");
            int lengthJsonArr = jsonMainNode.length();

            for(int i=0; i < lengthJsonArr; i++)
            {
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                /******* Fetch node values **********/
                String body = jsonChildNode.optString("content_body").toString();

                WebView wv= (WebView)getActivity().findViewById(R.id.webViewAbout);
                final String mimeType = "text/html";
                final String encoding = "UTF-8";
                wv.loadDataWithBaseURL(getString(R.string.base_url), body, mimeType, encoding, "");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
