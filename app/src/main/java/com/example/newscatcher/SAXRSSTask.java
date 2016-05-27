package com.example.newscatcher;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.newscatcher.article.Enclosure;
import com.example.newscatcher.article.Item;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by W7 on 07.02.2016.
 */
//public class SAXRSSTask extends AsyncTask<String, Void, List<Item>> {
//    private ListView listView;
//
//    private SAXHandler handler;
//    private Activity mainActivity;
//    private ListViewAdapter adapter;
//
//    @Override
//    protected void onPostExecute(List<Item> itemList) {
//        super.onPostExecute(itemList);
//
//        adapter = new ListViewAdapter(itemList);
//
//        adapter.notifyDataSetChanged();
//
//    }
//
//    public void attachAdapter(ListView listView) {
//        listView.setAdapter(adapter);
//    }
//
//    @Override
//    protected List<Item> doInBackground(String... params) {
//        String feedUrl = params[0];
//
//        try {
//
//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            SAXParser parser = factory.newSAXParser();
//            XMLReader reader = parser.getXMLReader();
//
//            handler = new SAXHandler();
//            reader.setContentHandler(handler);
//            InputSource source = new InputSource(getInputStream(feedUrl));
//            reader.parse(source);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        return handler.getItemList();
//
//    }
//
//    private InputStream getInputStream(String link) throws IOException {
//        URL url = new URL(link);
//        return url.openConnection().getInputStream();
//    }
//
//    class SAXHandler extends DefaultHandler {
//
//        private Item item;
//        private List<Item> itemList;
//        private boolean title;
//        private boolean pubDate;
//        private boolean link;
//        private Enclosure enclosure;
//
//        public List<Item> getItemList() {
//            return itemList;
//        }
//
//        public SAXHandler() {
//            this.item = new Item();
//            this.itemList = new ArrayList<>();
//        }
//
//        @Override
//        public void characters(char[] ch, int start, int length) throws SAXException {
//            super.characters(ch, start, length);
//
//            if(title) {
//                item.setTitle(new String(ch, start, length));
//            }
//
//            if(pubDate) {
//                item.setPudDate(new String(ch, start, length));
//            }
//
//            if(link) {
//                item.setEnclosure(enclosure);
//            }
//        }
//
//        @Override
//        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//            super.startElement(uri, localName, qName, attributes);
//
//            if(localName.equalsIgnoreCase("title")) {
//                title = true;
//            }
//
//            if (localName.equalsIgnoreCase("pubDate")) {
//                pubDate = true;
//            }
//
//            if(localName.equalsIgnoreCase("enclosure")) {
//                enclosure = new Enclosure();
//                enclosure.setType(attributes.getValue("type"));
//                try {
//                    enclosure.setUrl(new URL(attributes.getValue("url")));
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                enclosure.setLength(Integer.parseInt(attributes.getValue("length")));
//
//                link = true;
//            }
//        }
//
//        @Override
//        public void endElement(String uri, String localName, String qName) throws SAXException {
//            super.endElement(uri, localName, qName);
//
//            if(localName.equalsIgnoreCase("item")) {
//                itemList.add(item);
//            }
//        }
//    }
//}
