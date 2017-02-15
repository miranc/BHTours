package info.bhtours.bhtours.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.bhtours.bhtours.R;
import info.bhtours.bhtours.model.NewsItem;
import info.bhtours.bhtours.util.DataLoader;
import info.bhtours.bhtours.util.MySpinnerDialog;

/**
 * Created by Ravi on 29/07/15.
 */
public class NewsFragment extends ListFragment {

    private MySpinnerDialog myInstance;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ArrayList<NewsItem> news = new ArrayList<NewsItem>();
    DataLoader dataLoader;

    private String pageid;
    private String pagedesc;
    private String descPageId;
    private String currentlanguage;
    private String newsPageId;

    public final static String CONST_PAGE_ID = "bhtours.PageId";

    public NewsFragment() {
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
            newsPageId = getArguments().getString("newspageid");
        }

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setDivider(null);
        getListView().setDividerHeight(0);

        dataLoader = new DataLoader(this.getActivity().getApplication().getApplicationContext(),this);
        dataLoader.callWS("getNews", newsPageId, "listNews");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void loadNews(String JSON){
        myInstance.dismiss();
        JSONArray items;
        ListView myList;
        try {
            items = new JSONObject(JSON).getJSONArray("news");
            for(int i = 0; i < items.length(); i++){
                JSONObject c = items.getJSONObject(i);
                // Adding value HashMap key => value

                NewsItem li = new NewsItem(c.getString("file_view_listing"),null,c.getString("title"),c.getString("summary"),c.getLong("page_id"),c.getString("file_name"));
                news.add(li);
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("title", c.getString("title"));
                map.put("page_url", c.getString("file_name"));

                oslist.add(map);
            }
            myList=getListView();

            ListAdapter adapter = new GiAdapter(this.getActivity().getApplication().getApplicationContext(),news);

            myList.setAdapter(adapter);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //Toast.makeText(location_list.this, "You Clicked at " + oslist.get(+position).get("page_id"), Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(getActivity(),LocationDetails.class);
                    intent.putExtra(CONST_PAGE_ID, oslist.get(+position).get("page_id"));
                    startActivity(intent);*/
                    String url = oslist.get(+position).get("page_url");
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*adapter = new ListViewAdapter(location_list.this, items);
        myList.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
    }

    // the Adapter
    public class ListViewAdapter extends BaseAdapter {

        private Context context = null;
        private List<String> fields = null;

        public ListViewAdapter(Context context, JSONArray arr) {
            this.context = context;
            this.fields = new ArrayList<String>();
            for (int i=0; i<arr.length(); ++i) {
                try {
                    fields.add(arr.getJSONObject(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getCount() {
            return fields.size();
        }

        @Override
        public Object getItem(int position) {
            return fields.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.content_news_list, null);
            /*TextView txt = (TextView) convertView.findViewById(R.id.tvNewsCaption);
            txt.setText(fields.get(position));*/
            return convertView;
        }

    }

    public class GiAdapter extends BaseAdapter {

        private Context mContext;

        private List<NewsItem> mListAppInfo;
        private HashMap<Integer, ImageView> views;
        private HashMap<String,Bitmap> oldPicts = new  HashMap<String,Bitmap>();
        private LayoutInflater mInflater;
        private boolean auto;

        private final String BUNDLE_URL = "url";
        private final String BUNDLE_BM = "bm";
        private final String BUNDLE_POS = "pos";
        private final String BUNDLE_ID = "id";

        public GiAdapter(Context context, List<NewsItem> list) {
            mContext = context;
            mListAppInfo = list;
            views = new HashMap<Integer, ImageView>();
            mInflater = LayoutInflater.from(mContext);

        }

        @Override
        public int getCount() {
            return mListAppInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return mListAppInfo.get(position).getPageId();
        }

        @Override
        public long getItemId(int position) {
            return mListAppInfo.get(position).getPageId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LinearLayout layoutItem;

            // reuse of convertView
            if (convertView == null) {
                layoutItem = (LinearLayout) mInflater.inflate(R.layout.news_list_item, parent, false);
            } else {
                layoutItem = (LinearLayout) convertView;
            }

            // infos for the current element
            NewsItem entry = mListAppInfo.get(position);

            //set some text fields
            TextView name = (TextView) layoutItem.findViewById(R.id.tvNewsCaption);
            //TextView desc = (TextView) layoutItem.findViewById(R.id.tvNewsDesc);

            WebView wv= (WebView)layoutItem.findViewById(R.id.webViewNewsDesc);
            wv.setFocusable(false);
            wv.setClickable(false);


            name.setText(entry.getTitle());
            //desc.setText(entry.getDesc());
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            wv.loadDataWithBaseURL(getString(R.string.base_url), entry.getDesc(), mimeType, encoding, "");


            // get the imageView for the current object
            ImageView v = (ImageView) layoutItem.findViewById(R.id.ivNewsImage);

            // put infos in bundle and send to the LoadImage class
            Bundle b = new Bundle();

            //url of the pict
            b.putString(BUNDLE_URL, entry.getThumbUrl());

            //position in the listView
            b.putInt(BUNDLE_POS, position);

            //id of the current object
            b.putLong(BUNDLE_ID, entry.getPageId());

            //put info in the map in order to display in the onPostExecute
            views.put(position, v);

            // Get the image URL for the current position.
            String url = entry.getThumbUrl();

            // Trigger the download of the URL asynchronously into the image view.
            Picasso.with(mContext) //
                    .load(url) //
                    .fit() //
                    .tag(mContext) //
                    .into(v);

            // thread
            //new LoadImage().execute(b);

            return layoutItem;

        }

        //asyncTackClass for loadingpictures
        private class LoadImage extends AsyncTask<Bundle, Void, Bundle> {

            @Override
            protected Bundle doInBackground(Bundle... b) {

                Bitmap bm =null;

                //cache: for better performance, check if url alredy exists
                if(oldPicts.get(b[0].getString(BUNDLE_URL))==null){
                    try {
                        URL url = new URL(b[0].getString(BUNDLE_URL));
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        bm = BitmapFactory.decodeStream(input);

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }

                    //bm = Utils.getBitMapFromUrl(b[0].getString(BUNDLE_URL));
                    oldPicts.put(b[0].getString(BUNDLE_URL),bm);
                }else{
                    bm = oldPicts.get(b[0].getString(BUNDLE_URL));
                }

                // get info from bundle
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_BM, bm);
                bundle.putInt(BUNDLE_POS, b[0].getInt(BUNDLE_POS));

                return bundle;

            }

            @Override
            protected void onPostExecute(Bundle result) {
                super.onPostExecute(result);

                //get picture saved in the map + set
                ImageView view = views.get(result.getInt(BUNDLE_POS));
                Bitmap bm = (Bitmap) result.getParcelable(BUNDLE_BM);

                if (bm != null){ //if bitmap exists...
                    view.setImageBitmap(bm);
                }else{ //if not picture, display the default ressource
                    view.setImageResource(R.drawable.greyback);
                }

            }

        }

    }
}
