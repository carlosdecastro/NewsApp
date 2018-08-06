package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewsAdapter extends ArrayAdapter<News> {

    public static final String LOG_TAG = NewsAdapter.class.getName();

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = listItemView.findViewById(R.id.news_title);
        titleView.setText(currentNews.getTitle());
        TextView sectionView = listItemView.findViewById(R.id.news_section);
        sectionView.setText(currentNews.getSection());

        TextView authorView = listItemView.findViewById(R.id.news_author);

        if(currentNews.getAuthor() != null) {
            authorView.setText(currentNews.getAuthor());
        }else{
            TextView splitView = listItemView.findViewById(R.id.news_splitter);
            splitView.setVisibility(View.GONE);
        }

        TextView dateView = listItemView.findViewById(R.id.news_date);
        String date = currentNews.getDate();

        if (date != null){
            int parts = date.indexOf("T");
            String dateTime = date.substring(0, parts);

            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy");

            try {
                Date dt = oldFormat.parse(dateTime);
                String newDate = newFormat.format(dt);
                dateView.setText(newDate);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Error while creating the new format date", e);
            }
        }

        return listItemView;
    }
}

