package com.example.newscatcher.article;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by W7 on 10.01.2016.
 */
public class Item implements Comparable<Item>{
    private String title;
    private String description;
    private String pudDate;
    private Enclosure enclosure;
    private Channel channel;
    private String link;
    private Bitmap bitmap;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Item() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPudDate() {
        return pudDate;
    }

    public void setPudDate(String pudDate) {
        this.pudDate = pudDate;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int compareTo(@NonNull Item another) {

        if(getPudDate() == null || another.getPudDate() == null) {
            return 0;
        }

        return getPudDate().compareTo(another.getPudDate());
    }
}
