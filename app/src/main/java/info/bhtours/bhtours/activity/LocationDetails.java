package info.bhtours.bhtours.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import info.bhtours.bhtours.util.MySpinnerDialog;
import info.bhtours.bhtours.R;
import info.bhtours.bhtours.util.DataLoader;
import info.bhtours.bhtours.model.ImageItem;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class LocationDetails extends AppCompatActivity {
    public final static String CONST_PAGE_ID = "bhtours.PageId";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    static DataLoader dataLoader;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static String pageid;
    private static GridView gridView;
    private static GridViewAdapter gridAdapter;
    private static ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static ArrayList<String> imageUrls = new ArrayList<>();
    private static ArrayList<String> imageTitles = new ArrayList<>();
    private static String locationName;
    private static String locationDescritpion;
    private static Double Lat;
    private static Double Long;
    private static WebView wv;
    private MySpinnerDialog myInstance;
    private static GoogleMap mMap;
    private static Menu menu;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        FragmentManager fm = getSupportFragmentManager();
        mSectionsPagerAdapter = new SectionsPagerAdapter(fm);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==1)
                    menu.setGroupVisible(R.id.main_menu_group,true);
                else
                    menu.setGroupVisible(R.id.main_menu_group,false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        myInstance = new MySpinnerDialog();
        myInstance.show(fm, "some_tag");

        Intent intent = getIntent();
        pageid = intent.getStringExtra(CONST_PAGE_ID);

        setTitle(locationName);

        //Reset static fields
        if(gridAdapter!=null)gridAdapter=null;
        locationDescritpion="";
        locationName="";
        Lat=null;
        Long=null;

        dataLoader = new DataLoader(this.getApplicationContext(), this);
        dataLoader.callWS("locationDetails", pageid, "locationDetails");
    }

    public void loadLocationDetails(String JSON)
    {
        myInstance.dismiss();
        JSONArray items = null;//new JSONArray();
        try {
            JSONObject jsonMain= new JSONObject(JSON);
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            JSONObject customFields = jsonMain.getJSONObject("customfields");
            Lat = Double.parseDouble(customFields.getString("lat").replaceAll(",", "."));
            Long = Double.parseDouble(customFields.getString("long").replaceAll(",", "."));
            locationDescritpion = jsonMain.getString("content_body");
            locationName = jsonMain.getString("title");
            setTitle(locationName);

            wv= (WebView)findViewById(R.id.webView);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            wv.loadDataWithBaseURL("", locationDescritpion, mimeType, encoding, "");

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Lat, Long))
                    .title(locationName));
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Long), 15));


            imageItems.clear();
            imageUrls.clear();
            imageTitles.clear();
            items = jsonMain.getJSONArray("images");
            for(int i = 0; i < items.length(); i++) {
                JSONObject c = items.getJSONObject(i);
                ImageItem img = new ImageItem(c.getString("thumb4"),null,c.getString("file_path"), c.getString("file_name"));
                imageItems.add(img);

                imageUrls.add(c.getString("file_path"));
                imageTitles.add(c.getString("file_name"));
                //imageUrls.add(c.getString("thumb4"));
                //dataLoader.getImageFromUrl(c.getString("thumb4"), fa);
                // Adding value HashMap key => value
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void LoadImageFromUrl(GridView gView, Activity act)
    {
        /*for (int i = 0; i < imageItems.size(); i++) {
            dataLoader.getImageFromUrl(imageItems.get(i).thumbUrl, gView, act, i);
        }*/
        if(gridAdapter==null) {
            gridAdapter = new GridViewAdapter(act, R.layout.fragment_location_details_gallery_item, imageItems);
            gridView.setAdapter(gridAdapter);
            /*for (int i = 0; i < imageItems.size(); i++) {
                dataLoader.getImageFromUrl(imageItems.get(i).thumbUrl, gView, act, i);
            }*/
        }
        else {
            gridView.setAdapter(gridAdapter);
        }
    }

    public void LoadImageToGridFromUrl(Bitmap thumb, GridView gView, Activity fa, int imgIndex)
    {
        //imageItems.add(new ImageItem(img, "ime"));
        imageItems.get(imgIndex).thumbImage=thumb;
        try {
            gridAdapter = new GridViewAdapter(fa, R.layout.fragment_location_details_gallery_item, imageItems);
            gridView.setAdapter(gridAdapter);
        }
        catch (Exception e)
        {
            Log.d("Greska", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_map_type:
                // do s.th.
                return true;
            case R.id.mapVector:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.mapHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.mapRelief:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = null;
            switch(getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_location_details, container, false);
                    wv= (WebView)rootView.findViewById(R.id.webView);
                    final String mimeType = "text/html";
                    final String encoding = "UTF-8";
                    wv.loadDataWithBaseURL(getString(R.string.base_url), locationDescritpion, mimeType, encoding, "");
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_location_details_map, container, false);
                    mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_location_details_gallery, container, false);
                    gridView = (GridView) rootView.findViewById(R.id.gridView);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                            //Create intent
                            Intent intent = new Intent(getContext(), ImagePreview.class);
                            intent.putExtra("title", item.getTitle());
                            intent.putExtra("imageUrl", item.getImageUrl());
                            intent.putExtra("imageList", imageUrls);
                            intent.putExtra("imageTitles", imageTitles);
                            intent.putExtra("imageListSelectedIndex", position);
                            intent.putExtra("locationName", locationName);
                            //Start details activity
                            startActivity(intent);
                        }
                    });

                    LoadImageFromUrl(gridView, getActivity());
                    break;
            }
            /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }


    public static class GridViewAdapter extends ArrayAdapter {
        private Context context;
        private int layoutResourceId;
        private ArrayList data = new ArrayList();

        public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                //holder.imageTitle = (TextView) row.findViewById(R.id.imgTitle);
                holder.image = (ImageView) row.findViewById(R.id.imgThumb);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            // Get the image URL for the current position.
            String url = imageItems.get(position).thumbUrl;

            // Trigger the download of the URL asynchronously into the image view.
            Picasso.with(context) //
                    .load(url) //
                    .fit() //
                    .tag(context) //
                    .into(holder.image);

            /*ImageItem item = (ImageItem)data.get(position);
            //holder.imageTitle.setText(item.getTitle());
            holder.image.setImageBitmap(item.getThumbImage());*/
            return row;
        }

        public class ViewHolder {
            //TextView imageTitle;
            ImageView image;
        }
    }
}
