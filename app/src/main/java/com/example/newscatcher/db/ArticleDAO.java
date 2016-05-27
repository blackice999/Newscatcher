package com.example.newscatcher.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.newscatcher.MainActivity;
import com.example.newscatcher.app.ArticleApplication;
import com.example.newscatcher.article.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by W7 on 07.02.2016.
 */
public class ArticleDAO  {
    public static final String TITLE = "title";
    public static final String PUB_DATE = "pubDate";
    public static final String ID = "ID";
    public static int increment = 1;

    public static void add(Item item) {
        RSSArticleDB rssArticleDB = new RSSArticleDB(ArticleApplication.getInstance());
        SQLiteDatabase db = rssArticleDB.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, increment);
        values.put(TITLE, item.getTitle());
        values.put(PUB_DATE, item.getPudDate());

        db.insert("article", null, values);
        Log.i("DB_ADD", "added values");

        db.close();
    }

    public static List<Item> list() {
        List<Item> items = new ArrayList<>();
        RSSArticleDB rssArticleDB = new RSSArticleDB(ArticleApplication.getInstance());
        SQLiteDatabase db = rssArticleDB.getReadableDatabase();

        String execQuery = "SELECT title, pubDate FROM article";
        Cursor cursor = db.rawQuery(execQuery, null);

        while(cursor.moveToNext()) {
            Item item = new Item();
            item.setTitle(cursor.getString(1));
            item.setPudDate(cursor.getString(2));

            items.add(item);
        }

        cursor.close();
        db.close();

        return items;
    }

    public static void clear() {
        RSSArticleDB rssArticleDB = new RSSArticleDB(ArticleApplication.getInstance());
        SQLiteDatabase db = rssArticleDB.getWritableDatabase();

        db.execSQL("DELETE FROM article");
        Log.i("DB_CLEAR", "cleared db");

        db.close();
    }
}
