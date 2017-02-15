package info.bhtours.bhtours.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import info.bhtours.bhtours.R;
import info.bhtours.bhtours.model.LocationItem;
import info.bhtours.bhtours.util.DataLoader;
import info.bhtours.bhtours.util.MySpinnerDialog;

public class MapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final static String CONST_PAGE_ID = "bhtours.PageId";
    public final static String CONST_PAGE_DESC = "bhtours.PageDesc";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String pageid;
    private String pagedesc;
    private String descPageId;
    private String currentlanguage;

    private GoogleMap mMap;

    ArrayList<LocationItem> locations = new ArrayList<LocationItem>();
    HashMap<String, LocationItem> extraMarkerInfo = new HashMap<String, LocationItem>();

    private MySpinnerDialog myInstance;

    DataLoader dataLoader;

    //private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageid=getArguments().getString("pageId");
            descPageId=getArguments().getString("descpageId");
            pagedesc=getArguments().getString("pageDesc");
            currentlanguage=getArguments().getString("language");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this.getActivity().getApplication().getApplicationContext()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.151492, 17.796811), 7));

        dataLoader = new DataLoader(this.getActivity().getApplication().getApplicationContext(),this);
        dataLoader.callWS("getLocations", pageid, "mapsActivity");
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
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


    public void putPins(String retJSON)
    {
        JSONArray items;
        ListView myList;
        try {
            items = new JSONObject(retJSON).getJSONArray("locations");
            for(int i = 0; i < items.length(); i++){
                JSONObject c = items.getJSONObject(i);
                // Adding value HashMap key => value

                LocationItem li = new LocationItem(c.getString("file_view_listing"),null,c.getString("title"),c.getString("summary"),c.getDouble("lat"),c.getDouble("long"),c.getLong("page_id"));
                locations.add(li);
            }
            for (LocationItem li:locations) {
                LatLng coords = new LatLng(li.latitude, li.longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(coords).title(li.title));
                extraMarkerInfo.put(marker.getId(),li);
            }

            mMap.setOnInfoWindowClickListener(
                    new GoogleMap.OnInfoWindowClickListener() {
                        public void onInfoWindowClick(Marker marker) {
                            // Get extra data with marker ID
                            LocationItem marker_data = extraMarkerInfo.get(marker.getId());

                            Intent intent = new Intent(getActivity(), LocationDetails.class);
                            intent.putExtra(CONST_PAGE_ID, Integer.toString((int) marker_data.getPageId()));
                            startActivity(intent);
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private View view;
        private LayoutInflater inflater;
        private Context context;

        public CustomInfoWindowAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.custom_info_window,null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (marker != null
                    && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            //MainActivity.this.marker = marker;

            LocationItem marker_data = extraMarkerInfo.get(marker.getId());

            String url = null;


            final ImageView image = ((ImageView) view.findViewById(R.id.ivLocationMapImage));

            Picasso.with(context).load(marker_data.getThumbUrl()).into(image, new MarkerCallback(marker));

            final String title = marker_data.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.tvLocationMapName));
            if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }

            /*final String snippet = marker_data.getDesc();
            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.tvLocationMapDescription));
            if (snippet != null) {
                snippetUi.setText(snippet);
            } else {
                snippetUi.setText("");
            }*/

            return view;
        }
    }

    public class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }
}
