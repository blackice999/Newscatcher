package com.example.newscatcher;

/**
 * Created by W7 on 03.01.2016.
 */
public class Article {
    private String title;
    private String from;
    private String datePublished;
    private int imageID;

    public Article(String title, String from, String datePublished, int imageID) {
        this.title = title;
        this.from = from;
        this.datePublished = datePublished;
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public String getFrom() {
        return from;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public int getImageID() {
        return imageID;
    }

    //Create in MainActivity a list of the articles per NetTuts tutorial
}
