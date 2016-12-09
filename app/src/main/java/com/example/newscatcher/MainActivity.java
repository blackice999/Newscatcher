package com.example.newscatcher;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newscatcher.article.Channel;
import com.example.newscatcher.article.Enclosure;
import com.example.newscatcher.article.Item;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogArticleFragment.DialogClickListener {

    public static final String REALITATEA_URL = "http://rss.realitatea.net/sport.xml";
    public static final String EUROSPORT_FR_URL = "http://www.eurosport.fr/rss.xml";
    public static final String FOX_SPORTS_URL = "http://api.foxsports.com/v1/rss?partnerKey=zBaFxRyGKCfxBagJG9b8pqLyndmvo7UU&tag=nba";
    private List<String> articleLinks = new ArrayList<>();
    private ListViewAdapter adapter;
    public int type;
    public boolean wifi_switch;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SAXRSSTask saxrssTask;
    public ProgressDialog dialog;

    @Override
    protected void onResume() {
        super.onResume();

        if(adapter == null) {
//            getArticles(articleLinks);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(dialog != null) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.rv_article);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(saxrssTask.getItems().get(position).getLink()));
                startActivity(intent);
            }
        });

        //Initialize ProgressDialog here to avoid ANR when refreshing
        dialog = new ProgressDialog(MainActivity.this);

        adapter = new ListViewAdapter();
        //Store the links
        articleLinks.add(REALITATEA_URL);
        articleLinks.add(EUROSPORT_FR_URL);

        saxrssTask = new SAXRSSTask(articleLinks);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Fetch only if an internet connection is available
                if(!isNetworkAvailable()) {
                    Snackbar.make(findViewById(android.R.id.content), "No connection", Snackbar.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    saxrssTask = null;
                    saxrssTask = new SAXRSSTask(articleLinks);
                    saxrssTask.execute();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "Fetched new articles", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        wifi_switch = pref.getBoolean("wifi_switch", true);

        //Get connection info
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(!isNetworkAvailable()) {
            Snackbar.make(findViewById(android.R.id.content), "No connection", Snackbar.LENGTH_LONG).show();
        } else {
            saxrssTask.execute();

            type = networkInfo.getType();
            //Verify if WiFi is turned on
            if(type == ConnectivityManager.TYPE_WIFI) {
                //If on WiFi and wifi toggle on, fetch images
                Toast.makeText(MainActivity.this, "On wifi", Toast.LENGTH_SHORT).show();
            } else if(type == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(MainActivity.this, "on mobile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    private synchronized void getArticles(SAXRSSTask saxrssTask) {
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_links) {
            FragmentTransaction manager = getFragmentManager().beginTransaction();
            DialogFragment dialogFragment = new DialogArticleFragment().newInstance();

            dialogFragment.show(manager, "dialog");

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);

            //Fix to add newly added links to settings activity's fragment
            SettingsActivity.GeneralPreferenceFragment generalPreferenceFragment = new SettingsActivity.GeneralPreferenceFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("links", (ArrayList<String>) articleLinks);
            bundle.putString("test", "fish");
            generalPreferenceFragment.setArguments(bundle);

            startActivity(intent);

        } else if (id == R.id.nav_share) {
            //Add real link here
            String link = "https://www.google.com";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            startActivity(intent);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPositiveClick(String articleURL) {

        if(articleURL.length() > 0) {

            articleLinks.add(articleURL);
            Toast.makeText(MainActivity.this, "Link added", Toast.LENGTH_SHORT).show();
//            recreate();
        } else {
            Toast.makeText(MainActivity.this, "Please add a valid link", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNegativeClick() {
        Toast.makeText(MainActivity.this, "Link not saved", Toast.LENGTH_LONG).show();
    }

    public class SAXRSSTask extends AsyncTask<Void, Void, Void>{

        private SAXHandler handler;
        public volatile List<Item> items;
        private List<String> feedUrl;
//        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        public List<Item> getItems() {
            return items;
        }

        public SAXRSSTask(List<String> feedUrl) {
            this.feedUrl = feedUrl;
            this.items = new ArrayList<>();
        }

        private InputStream getInputStream(String link) throws IOException {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getString(R.string.articleLoading));
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(dialog.isShowing()) {
                dialog.dismiss();
            }

//            adapter = new ListViewAdapter();
            adapter.setItemList(items);
            listView.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader reader = parser.getXMLReader();

                handler = new SAXHandler();
                reader.setContentHandler(handler);

                for(String link : feedUrl) {

                    InputSource source = new InputSource(getInputStream(link));
                    reader.parse(source);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        class SAXHandler extends DefaultHandler implements ArticleListener{

            private Item item;
            private Enclosure enclosure;
            private boolean inItem;
            private StringBuilder content;
            private Channel channel;
            private boolean inChannel;
            private Bitmap mIcon;

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                super.characters(ch, start, length);
                content.append(ch, start,length);
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                super.startElement(uri, localName, qName, attributes);
                content = new StringBuilder();

                if (localName.equalsIgnoreCase("enclosure")) {
                    enclosure = new Enclosure();
                    enclosure.setType(attributes.getType("type"));
                    enclosure.setUrl(attributes.getValue("url"));

                    if(attributes.getValue("length") != null) {

                        enclosure.setLength(Integer.parseInt(attributes.getValue("length")));
                    }

                    item.setEnclosure(enclosure);

                    //The user is on Wifi connection or he disabled the Wifi-switch, so he's on mobile data
                    //so download images
                    if (type == ConnectivityManager.TYPE_WIFI || !wifi_switch) {
                        item.setBitmap(downloadImage(attributes.getValue("url")));
                    }
                }

                if(localName.equalsIgnoreCase("item")) {
                    item = new Item();
                    inItem = true;
                }

                if(localName.equalsIgnoreCase("channel")) {
                    channel = new Channel();
                    inChannel = true;
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                super.endElement(uri, localName, qName);

                if (localName.equalsIgnoreCase("item")) {

                    item.setChannel(channel);
                    onArticle(item);
                }

                if (localName.equalsIgnoreCase("title")) {
                    if(inItem) {
                        item.setTitle(content.toString());

                    } else if (inChannel) {
                        String[] channelTitle = content.toString().split(" ");

                        String title = channelTitle[0].substring(0,1).toUpperCase() + channelTitle[0].substring(1).toLowerCase();
                        channel.setTitle(title);
                    }
                }

                if(localName.equalsIgnoreCase("pubDate")) {
                    if(inItem) {
                        item.setPudDate(content.toString());
                    }
                }

                if(localName.equalsIgnoreCase("link")) {
                    if(inItem) {
                        item.setLink(content.toString());
                    }
                }
            }

            @Override
            public synchronized void onArticle(final Item item) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        items.add(item);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            private Bitmap downloadImage(String url) {
                InputStream inputStream;
                try {

                    inputStream = new URL(url).openStream();
                    mIcon = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return mIcon;
            }
        }
    }
}
