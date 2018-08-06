package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>>{

    public static final String LOG_TAG = MainActivity.class.getName();
    private NewsAdapter mAdapter;

    private static final String GUARDIANAPI_REQUEST_URL =
            "https://content.guardianapis.com/search?show-tags=contributor&q=art&api-key=1bea4277-7799-4259-8c91-0807050f3b81";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView newsListView = findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        View guardianHeader = findViewById(R.id.guardian_header);
        guardianHeader.setVisibility(View.GONE);

        View introText = findViewById(R.id.intro_text);
        introText.setVisibility(View.GONE);

        newsListView.setAdapter(mAdapter);

        TextView emptyTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyTextView);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News currentNews = (News) parent.getItemAtPosition(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent openUrl = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(openUrl);
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected)
            getLoaderManager().initLoader(1,null,this);

        if(!isConnected) {
            ProgressBar loadingSpinner = findViewById(R.id.loading_spinner);
            loadingSpinner.setVisibility(View.GONE);
            newsListView.setEmptyView(emptyTextView);
            emptyTextView.setText(R.string.no_connection);
        }

    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(MainActivity.this, GUARDIANAPI_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
        TextView emptyTextView = findViewById(R.id.empty_view);
        emptyTextView.setText(R.string.no_news);

        ProgressBar loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);

        ImageView logoView = findViewById(R.id.logo);
        logoView.setVisibility(View.GONE);

        View guardianHeader = findViewById(R.id.guardian_header);
        guardianHeader.setVisibility(View.VISIBLE);

        View introText = findViewById(R.id.intro_text);
        introText.setVisibility(View.VISIBLE);

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        mAdapter.clear();
    }
}
