package com.example.newscatcher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ArticleActivity extends AppCompatActivity {
    private TextView tvTitle;
    private String rssTitle;
    private TextView tvDescription;
    private TextView tvFrom;
    private String rssFrom;
    private TextView tvDatePublished;
    private String rssDatePublished;
    private String rssDescription;
    private String rssItem = "";
    private boolean item = false;
    private String rssLink;
    private boolean title = false;
    private boolean datePublished = false;
    private boolean fromArticle = false;
    private boolean link = false;

    private List<String> listTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        SAXRSSFeed saxrssFeed = new SAXRSSFeed();
//        saxrssFeed.execute();

        //To fetch from given URL (EXTRA_LINK) the article content (div class='article-ad' ??)
        // and article intro (div class='intro-2')


        String title = getIntent().getExtras().getString(MainActivity.EXTRA_TITLE);
        String link = getIntent().getExtras().getString(MainActivity.EXTRA_LINK);
        tvTitle = (TextView) findViewById(R.id.tv_title_article);

        tvDescription = (TextView) findViewById(R.id.tv_description_article);

        tvFrom = (TextView) findViewById(R.id.tv_from_article);
        tvDatePublished = (TextView) findViewById(R.id.tv_date_published_article);

//        RSSFeedTask rssFeedTask = new RSSFeedTask();
//        rssFeedTask.execute();
//        tvTitle.setText(title);
    }
}