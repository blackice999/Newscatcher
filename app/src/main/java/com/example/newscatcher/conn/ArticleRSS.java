package com.example.newscatcher.conn;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by W7 on 06.01.2016.
 */
public class ArticleRSS {

    private String title;
    private String link;
    private String description;
    private XmlPullParserFactory xmlPullParserFactory = null;
    public boolean isParsingComplete = false;
    private String urlString = null;


    public ArticleRSS(String urlString) {
        this.urlString = urlString;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public void parseXMLAndStoreIt(XmlPullParser xmlPullParser) {
        int event;
        String text = null;
        try {
            event = xmlPullParser.getEventType();

            while(event != XmlPullParser.END_DOCUMENT) {
                String name = xmlPullParser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = xmlPullParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        text = xmlPullParser.getText();
                        if (name.equals("title")) {
                            title = text;
                        } else if (name.equals("link")) {
                            link = text;
                        } else if(name.equals("description")) {
                            description = text;
                        } else {

                        }
                        break;
                }
                event = xmlPullParser.next();
                isParsingComplete = true;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchXML() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    //Stars the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    xmlPullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

                    xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    xmlPullParser.setInput(stream, null);

                    parseXMLAndStoreIt(xmlPullParser);
                    stream.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
