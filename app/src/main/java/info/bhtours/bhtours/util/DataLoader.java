package info.bhtours.bhtours.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.GridView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import info.bhtours.bhtours.activity.AboutAppFragment;
import info.bhtours.bhtours.activity.AboutFragment;
import info.bhtours.bhtours.activity.GalleryFragment;
import info.bhtours.bhtours.activity.ImagePreview;
import info.bhtours.bhtours.activity.LocationDetails;
import info.bhtours.bhtours.activity.LocationsFragment;
import info.bhtours.bhtours.activity.NewsFragment;
import info.bhtours.bhtours.activity.MainActivity;
import info.bhtours.bhtours.activity.MapFragment;
import info.bhtours.bhtours.activity.AboutFragment;
import info.bhtours.bhtours.activity.LocationDetails;
import info.bhtours.bhtours.activity.SocialFragment;

/**
 * Created by mcerkic on 18.1.2016..
 */
public class DataLoader {
    Context _context;
    MainActivity _mainActivity;
    //location_list _locationList;
    LocationDetails _locationDetails;
    ImagePreview _imagePreview;
    MapFragment _mapFragment;
    LocationsFragment _locationsList;
    NewsFragment _newsList;
    AboutFragment _about;
    SocialFragment _social;
    AboutAppFragment _aboutapp;
    GalleryFragment _galleryFragment;
    public static String DataHandlerURL = "http://www.bhtours.info/DataHandler.ashx";

    public DataLoader(Context context, MainActivity mainActivity)
    {
        _context = context;
        _mainActivity = mainActivity;
        //SharedPreferences settings = _context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }

    public DataLoader(Context context, LocationsFragment locationsList)
    {
        _context = context;
        _locationsList = locationsList;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }

    public DataLoader(Context context, NewsFragment newsList)
    {
        _context = context;
        _newsList = newsList;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }

    public DataLoader(Context context, LocationDetails locationDetails)
    {
        _context = context;
        _locationDetails = locationDetails;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }

    public DataLoader(Context context, ImagePreview imagePreview)
    {
        _context = context;
        _imagePreview = imagePreview;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }

    public DataLoader(Context context, AboutFragment about)
    {
        _context = context;
        _about = about;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }

    public DataLoader(Context context, SocialFragment social)
    {
        _context = context;
        _social = social;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }

    public DataLoader(Context context, AboutAppFragment aboutapp)
    {
        _context = context;
        _aboutapp = aboutapp;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }

    public DataLoader(Context context, MapFragment mapFragment)
    {
        _context = context;
        _mapFragment = mapFragment;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }

    public DataLoader(Context context, GalleryFragment galleryFragment)
    {
        _context = context;
        _galleryFragment = galleryFragment;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(_context);
        if(settings.getString("DataHandlerURL", "")!="")
            DataHandlerURL = settings.getString("DataHandlerURL", "");
    }
    public void callWS(String action, String pageid, final String sourceActivity)
    {

        AsyncCallWSGetJSON taskCallWS = new AsyncCallWSGetJSON(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                switch (sourceActivity) {
                    case "startPage":
                        _mainActivity.readStartPageIds((String) output);
                        break;
                    case "listLocations":
                        _locationsList.loadLocations((String) output);
                        break;
                    case "locationDetails":
                        _locationDetails.loadLocationDetails((String) output);
                        break;
                    case "about":
                        _about.loadAbout((String) output);
                        break;
                    case "mapsActivity":
                        _mapFragment.putPins((String) output);
                        break;
                    case "listNews":
                        _newsList.loadNews((String) output);
                        break;
                    case "social":
                        _social.loadSocial((String) output);
                        break;
                    case "aboutapp":
                        _aboutapp.loadAboutApp((String) output);
                        break;
                    case "randomImages":
                        _galleryFragment.loadRandomGalleryImages((String) output);
                        break;
                }
            }
        } );
        taskCallWS.execute(DataHandlerURL, action, pageid);
    }

    public void getImageFromUrl(String url, final GridView gView, final Activity act,final int imgIndex)
    {
        AsyncCallWSGetImage taskGetImage = new AsyncCallWSGetImage(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                        _locationDetails.LoadImageToGridFromUrl((Bitmap) output, gView, act, imgIndex);
            }
        }
        );

        taskGetImage.execute(url);
    }

    /*public void getImageFromUrl(String url)
    {
        AsyncCallWSGetImage taskGetImage = new AsyncCallWSGetImage(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                _imagePreview.LoadImage((Bitmap) output);
            }
        }
        );

        taskGetImage.execute(url);
    }*/

    public interface AsyncResponse {

        void processFinish(Object output);
    }

    private class AsyncCallWSGetJSON extends AsyncTask<String, Void, String> {
        private boolean hasErrors = false;

        public AsyncResponse delegate = null;//Call back interface

        public AsyncCallWSGetJSON(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected String doInBackground(String... params) {
            String URL = params[0];
            String action = params[1];
            String pageid = params[2];

            //readJSON(URL, action, pageid);
            return readJSON(URL, action, pageid);
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        public String readJSON(String URL, String action, String pageid)
        {
            String retVal="";
            try {
                URL url = new URL(addParamsToUrl(URL, action, pageid));

                StringBuilder stringBuilder = new StringBuilder();
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    retVal = readStream(in);
                } catch (IOException e) {
                    Log.d("Greska",e.getMessage());
                }
                finally {
                    urlConnection.disconnect();
                }
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return retVal;
        }

        private String readStream(InputStream is) throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
            for (String line = r.readLine(); line != null; line =r.readLine()){
                sb.append(line);
            }
            is.close();
            return sb.toString();
        }

        protected String addParamsToUrl(String url, String action, String pageid){
            if(!url.endsWith("?"))
                url += "?";

            /*List<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("action", action));
            params.add(new Pair<>("pageid", pageid));*/

            //String paramString = URLEncoder. .encode(params.toString(), "utf-8");

            String uri = Uri.parse(url)
                    .buildUpon()
                    .appendQueryParameter("action", action)
                    .appendQueryParameter("pageid",pageid)
                    .build().toString();


            return uri;
        }
    }

    private class AsyncCallWSGetImage extends AsyncTask<String, Void, Bitmap> {
        private boolean hasErrors = false;

        public AsyncResponse delegate = null;//Call back interface

        public AsyncCallWSGetImage(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String URL = params[0];
            return readJSON(URL);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            delegate.processFinish(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        public Bitmap readJSON(String URL)
        {
            try {
                URL url = new URL(URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}


