package com.example.newscatcher.app;

import android.app.Activity;
import android.app.Application;

/**
 * Created by W7 on 07.02.2016.
 */
public class ArticleApplication extends Application {
    public static ArticleApplication instance;

    public static ArticleApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
