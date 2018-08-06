package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchNewsData(mUrl);
    }
}
