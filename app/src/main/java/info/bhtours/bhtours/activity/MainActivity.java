package info.bhtours.bhtours.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import info.bhtours.bhtours.R;
import info.bhtours.bhtours.util.DataLoader;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    private String pageid;
    private String pagedesc;
    private String descPageId;
    private String currentlanguage;
    private String newsPageId;
    private String hiddenTabs;
    private String socialPageId;
    private String aboutAppPageId;
    private int currentFragmentId=0;

    private String mailsmtpserver;
    private String mailusername;
    private String mailpassword;
    private String mailrecipient;

    public final static String CONST_PAGE_ID = "bhtours.PageId";
    public final static String CONST_PAGE_DESC = "bhtours.PageDesc";
    public final static String CONST_PAGE_DESC_PAGE_ID = "bhtours.DescPageId";

    private ProgressDialog dialog;
    private DataLoader dataLoader;
    private Locale myLocale;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayLanguage();
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                ) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                    124 );
        }

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.progressDialogMsg));
        dialog.show();

        dataLoader = new DataLoader(this.getApplicationContext(),this);
        dataLoader.callWS("getStartPageIds", "0", "startPage");

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    private void setDisplayLanguage()
    {
        String languageToLoad = "";
        settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if(settings.getString("language","")!="")
        {

            if(settings.getString("language","").equals("BA"))
                languageToLoad  = "bs";
            else
                languageToLoad  = "en";

        }
        else
        {
            Locale current = getResources().getConfiguration().locale;
            String currLang = current.getLanguage();

            if(currLang.equals("hr")||currLang.equals("bs")||currLang.equals("sr")) {
                languageToLoad = "bs";
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
                editor.putString("language", "BA");
                editor.apply();
            }
            else
            {
                languageToLoad = "en";
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
                editor.putString("language", "UK");
                editor.apply();
            }
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void readStartPageIds(String retJSON)
    {
        dialog.dismiss();

//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if(settings.getString("language", "")!="") {
            currentlanguage=settings.getString("language", "");
        }
        else
            currentlanguage="BA";
        /*OVO PRIVREMENO*/
        //currentlanguage="BA";

        try {
            JSONObject jsonResponse = new JSONObject(retJSON);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("startPageIds");
            int lengthJsonArr = jsonMainNode.length();

            for(int i=0; i < lengthJsonArr; i++)
            {
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                /******* Fetch node values **********/
                String language = jsonChildNode.optString("language").toString();
                String _pageid = jsonChildNode.optString("id").toString();
                String _desc = jsonChildNode.optString("desc").toString();
                String _descPageId = jsonChildNode.optString("descPageId").toString();
                String _newsPageId = jsonChildNode.optString("newsPageId").toString();
                String _hiddenTabs = jsonChildNode.optString("hiddenTabs").toString();
                String _socialPageId = jsonChildNode.optString("socialPageId").toString();
                String _aboutAppPageId = jsonChildNode.optString("aboutAppPageId").toString();
                String _languageMenuOrderId = jsonChildNode.optString("menuOrderId").toString();

                if(language.equals(currentlanguage)) {
                    pageid = _pageid;
                    pagedesc = _desc;
                    descPageId = _descPageId;
                    newsPageId = _newsPageId;
                    hiddenTabs = _hiddenTabs;
                    socialPageId = _socialPageId;
                    aboutAppPageId = _aboutAppPageId;

                    drawerFragment.removeItem(Integer.parseInt(_languageMenuOrderId));

                    if(!_hiddenTabs.isEmpty())
                    {
                        String[] array = _hiddenTabs.split(",");
                        for (String s:array) {
                            if(drawerFragment.getData().size()>Integer.parseInt(s))
                                drawerFragment.removeItem(Integer.parseInt(s));
                        }
                    }


                    WebView wv= (WebView)findViewById(R.id.webViewDesc);
                    final String mimeType = "text/html";
                    final String encoding = "UTF-8";
                    wv.loadDataWithBaseURL("", pagedesc, mimeType, encoding, "");
                }
            }

            JSONObject jsonMailSettingsNode = jsonResponse.getJSONObject("mailSettings");
            mailsmtpserver = jsonMailSettingsNode.optString("smtpserver").toString();
            mailusername = jsonMailSettingsNode.optString("username").toString();
            mailpassword = jsonMailSettingsNode.optString("password").toString();
            mailrecipient = jsonMailSettingsNode.optString("recipient").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        if(currentFragmentId!=0)
            displayView(0);
        else
            finish();
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public static String getApplicationName(Context context) {
        return context.getString(R.string.app_name);
    }

    private void displayView(int position) {
        currentFragmentId = position;
        Fragment fragment = null;
        ListFragment fragmentList = null;

        String title = getString(R.string.app_name);
        Bundle bundle=new Bundle();
        bundle.putString("pageId", pageid);
        bundle.putString("descpageId", descPageId);
        bundle.putString("pageDesc", pagedesc);
        bundle.putString("language", currentlanguage);
        bundle.putString("newspageid", newsPageId);
        bundle.putString("socialpageid", socialPageId);
        bundle.putString("aboutapppageid", aboutAppPageId);
        bundle.putString("mailsmtpserver", mailsmtpserver);
        bundle.putString("mailusername", mailusername);
        bundle.putString("mailpassword", mailpassword);
        bundle.putString("mailrecipient", mailrecipient);

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getApplicationName(this.getApplicationContext());
                break;
            case 1:
                fragment = new AboutFragment();
                title = getString(R.string.title_about);
                break;
            case 2:
                fragment = new NewsFragment();
                title = getString(R.string.title_news);
                break;
            case 3:
                fragmentList = new LocationsFragment();
                title = getString(R.string.title_locations);
                break;
            case 4:
                fragment = new MapFragment();
                title = getString(R.string.title_map);
                break;
            case 5:
                fragment = new GalleryFragment();
                title = getString(R.string.title_gallery);
                break;
            case 6:
                fragment = new SocialFragment();
                title = getString(R.string.title_social);
                break;
            case 7:
                fragment = new ContactFragment();
                title = getString(R.string.title_contact);
                break;
            case 8:
                fragment = new AboutAppFragment();
                title = getString(R.string.title_aboutapp);
                break;
            case 9:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
                if(currentlanguage.equals("BA"))
                    editor.putString("language", "UK");
                else
                    editor.putString("language", "BA");
                editor.apply();
                setLocale();
                break;
            /*case 9:
                editor.putString("language", "UK");
                editor.apply();
                setLocale();
                break;*/

            default:
                break;
        }

        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
        else if (fragmentList!=null) {
            fragmentList.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragmentList);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public void setLocale() {
        Intent refresh = new Intent(this, MainActivity.class);
        this.finish();
        startActivity(refresh);
    }
}