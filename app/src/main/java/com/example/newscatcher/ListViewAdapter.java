package com.example.newscatcher;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newscatcher.article.Item;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by W7 on 04.01.2016.
 */
public class ListViewAdapter extends BaseAdapter {

    public List<Item> itemList;
    public String imageURL;
    public ArticleViewHolder articleViewHolder;

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return (itemList != null) ? itemList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            articleViewHolder = new ArticleViewHolder();

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item, null);

            articleViewHolder.articleTitle = (TextView) convertView.findViewById(R.id.tv_title);
            articleViewHolder.articleDatePublished = (TextView) convertView.findViewById(R.id.tv_date_published);
            articleViewHolder.articleImage = (ImageView) convertView.findViewById(R.id.iv_article);
            articleViewHolder.articleFrom = (TextView) convertView.findViewById(R.id.tv_from);


            convertView.setTag(articleViewHolder);
        } else {
            articleViewHolder = (ArticleViewHolder) convertView.getTag();
        }

        Item item = (Item) getItem(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        String datePublished = simpleDateFormat.format(new Date(item.getPudDate()));

        imageURL = item.getEnclosure().getUrl();

        Bitmap image = item.getBitmap();
        articleViewHolder.articleTitle.setText(item.getTitle());
        articleViewHolder.articleImage.setImageBitmap(image);

        articleViewHolder.articleFrom.setText(item.getChannel().getTitle());
        articleViewHolder.articleDatePublished.setText(datePublished);

        return convertView;
    }

    public static class ArticleViewHolder {
        TextView articleTitle;
        TextView articleFrom;
        TextView articleDatePublished;
        ImageView articleImage;
    }
}
