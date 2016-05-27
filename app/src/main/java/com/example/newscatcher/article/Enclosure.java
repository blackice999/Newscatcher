package com.example.newscatcher.article;

import java.net.URL;

/**
 * Created by W7 on 10.01.2016.
 */
public class Enclosure {
    private String type;
    private String url;
    private Integer length;

    public Enclosure() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
