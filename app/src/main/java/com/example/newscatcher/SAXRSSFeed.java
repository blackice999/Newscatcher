package com.example.newscatcher;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.newscatcher.article.Channel;
import com.example.newscatcher.article.Enclosure;
import com.example.newscatcher.article.Item;

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

/**
 * Created by W7 on 10.01.2016.
 */
public class SAXRSSFeed extends AsyncTask<Void, Void, ArrayList<Channel>> {

    private boolean title;
    private boolean link;
    private boolean datePublished;
    private List<String> listTitles = new ArrayList<>();
    private List<Long> listDatePublished = new ArrayList<>();
//    private TextView tvTitle;
    private Activity activity;
    private List<Article> articleList;
    private String cTitle;
    private String cFrom;
    private String cDatePublished;
    private ArrayList<Channel> alChannels = new ArrayList<>();

    public SAXRSSFeed() {
        this.articleList = articleList;
    }

    public ArrayList<Channel> getAlChannels() {
        return alChannels;
    }

    @Override
    protected ArrayList<Channel> doInBackground(Void... params) {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        URL url = null;
        try {
            url = new URL("http://rss.realitatea.net/sport.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        XMLReader xmlReader = null;
        try {
            xmlReader = parser.getXMLReader();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        RSSHandler rssHandler = new RSSHandler();
        xmlReader.setContentHandler(rssHandler);
        try {
            InputSource inputSource = new InputSource(url.openStream());
            xmlReader.parse(inputSource);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


        alChannels = rssHandler.getAlChannels();

        return alChannels;

//        URL rssURL = null;
//        try {
//            rssURL = new URL("http://rss.realitatea.net/sport.xml");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//        SAXParser saxParser = null;
//        try {
//            saxParser = saxParserFactory.newSAXParser();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        }
//
//        XMLReader xmlReader = null;
//        try {
//            xmlReader = saxParser.getXMLReader();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        }
//        RSSHandler rssHandler = new RSSHandler();
//        xmlReader.setContentHandler(rssHandler);
//
//        InputSource inputSource = null;
//        try {
//            inputSource = new InputSource(rssURL.openStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            xmlReader.parse(inputSource);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        }
//
//        return null;
    }

    class RSSHandler extends DefaultHandler {

        private ArrayList<Channel> alChannels = new ArrayList<>();
        private Channel channel;
        private String reading;
        private ArrayList<Item> alItems = new ArrayList<>();
        private Item alItem;
        private Enclosure enclosure;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {


            if (localName.equals("rss")) {
                alChannels = new ArrayList<>();
            }

//            if (localName.equals("channel")) {
//                channel = new Channel(title);
//            }

            if (localName.equals("enclosure")) {
                enclosure = new Enclosure();
                enclosure.setType(attributes.getValue("type"));
                enclosure.setUrl(attributes.getValue("url"));

                enclosure.setLength(Integer.parseInt(attributes.getValue("length")));
            }
            if (localName.equals("link")) {
                //change variables names
                link = true;
            }

            if (localName.equals("item")) {
                alItem = new Item();
            }

//                if (!localName.equals("link") && title) {
//                    rssLink += localName + ": ";
//                }

            if (localName.equals("title")) {
                title = true;
            }

//                if (!localName.equals("title") && title) {
//                    rssTitle += localName + ": ";
//                }

            if (localName.equals("pudDate")) {
                datePublished = true;
            }

//                if (!localName.equals("pubDate") && datePublished) {
//                    rssDatePublished += localName + ": ";
//                }

//                if(localName.equals("image")) {
//                    fromArticle = true;
//
//                    if (localName.equals("url")) {
//                        rssFrom += localName + ": ";
//                    }
//                }
        }

//        @Override
//        public void characters(char[] ch, int start, int length) throws SAXException {
//            super.characters(ch, start, length);
//
//            if (title) {
//                cTitle = new String(ch, start, length);
////                    rssTitle = rssTitle + (cTitle.trim()).replaceAll("\\s", " ");
////                    rssTitle += cTitle;
////                    listTitles.add(cTitle);
////                listTitles.add(cTitle);
//                title = false;
//            }
//
//            if (link) {
//                cFrom = new String(ch, start, length);
////                rssFrom = rssFrom + (cFrom.trim()).replaceAll("\\s", " ") + "\t";
////                    rssFrom = cFrom;
//                link = false;
//            }
//
//            if (datePublished) {
//                cDatePublished = new String(ch, start, length);
////                    rssDatePublished = rssDatePublished + (cDatePublished.trim()).replaceAll("\\s", " ") + "\t";
////                rssDatePublished = cDatePublished;
////                    datePublished = false;
//            }
//
//            Article article = new Article(cTitle, cFrom, cDatePublished, )
//
//            //TODO: CREATE METHOD WHICH HIDES TOOLBAR ON SCROLLING DOWN
//            //TODO: SHOW RSS NODES IN SEPARATE TEXTVIEW
//        }


        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            reading = new String(ch, start, length);
        }

        public ArrayList<Channel> getAlChannels() {
            return alChannels;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            if(localName.equals("channel")) {
//                channel.setAlItems(alItems);
                alChannels.add(channel);
//                alItems = null;
                alItems = new ArrayList<>();
            }

            if (localName.equals("title")) {
                if(alItems == null) {
//                    channel.setTitle(reading);
                } else if (alItem != null) {
                    alItem.setTitle(reading);
                }
            }

            if (localName.equals("item")) {
                if(alItems != null) {
                    alItems.add(alItem);
//                    alItem = null;
                }
            }

            if (localName.equals("description")) {
                if (alItem != null) {
                    alItem.setDescription(reading);
                }
            }

            if(localName.equals("pubDate")) {
                if (alItem != null) {
                    alItem.setPudDate(reading);
                }
            }

            if(localName.equals("enclosure")) {
                if (alItem != null) {
                    alItem.setEnclosure(enclosure);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Channel> aVoid) {


//        tvTitle = (TextView) findViewById(R.id.tv_title_article);
//
//        tvDescription = (TextView) findViewById(R.id.tv_description_article);
//
//        tvFrom = (TextView) findViewById(R.id.tv_from_article);
//        tvDatePublished = (TextView) findViewById(R.id.tv_date_published_article);

//        for(Channel channel : alChannels) {
//            tvTitle.append(channel.getTitle());
//
//            for(Item i : channel.getAlItems()) {
//                tv
//            }


//                tvTitle.setText(articleTitle.getTitle() + "\n");
//            tvTitle.append(listTitles.get(i));
        }
//            tvDescription.setText(listTitles.get(1).getTitle());
//        tvDescription.setText(listTitles.get(2));
//        tvFrom.setText(rssFrom);
//        tvDatePublished.setText(rssDatePublished);
    }
