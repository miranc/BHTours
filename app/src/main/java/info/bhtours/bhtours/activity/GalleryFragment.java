package info.bhtours.bhtours.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import info.bhtours.bhtours.R;
import info.bhtours.bhtours.model.ImageItem;
import info.bhtours.bhtours.model.LocationItem;
import info.bhtours.bhtours.util.DataLoader;


public class GalleryFragment extends Fragment {
    private static ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static ArrayList<String> imageUrls = new ArrayList<>();
    private static ArrayList<String> imageTitles = new ArrayList<>();
    private String pageid;
    private static GridView gridView;
    private static GridViewAdapter gridAdapter;


    static DataLoader dataLoader;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageid = getArguments().getString("pageId");
        }
        dataLoader = new DataLoader(this.getActivity().getApplication().getApplicationContext(), this);
        dataLoader.callWS("getRandomImages", pageid, "randomImages");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridViewGallery);

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
                intent.putExtra("locationName", item.getTitle());
                //Start details activity
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void loadRandomGalleryImages(String retJSON)
    {
        JSONArray items = null;
        try {
            JSONObject jsonMain= new JSONObject(retJSON);

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
            }

            if(gridAdapter==null) {
                gridAdapter = new GridViewAdapter(this.getActivity(), R.layout.fragment_gallery_item, imageItems);
                gridView.setAdapter(gridAdapter);
            }
            else {
                gridView.setAdapter(gridAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
