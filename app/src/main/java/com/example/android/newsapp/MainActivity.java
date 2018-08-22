package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>>{

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * URL for news data from The Guardian API
     */
    private static final String GUARDIANAPI_REQUEST_URL = "https://content.guardianapis.com/search?";

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.list);

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        View guardianHeader = findViewById(R.id.guardian_header);
        guardianHeader.setVisibility(View.GONE);

        View introText = findViewById(R.id.intro_text);
        introText.setVisibility(View.GONE);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
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

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String numNews = sharedPrefs.getString(
                getString(R.string.settings_number_news_key),
                getString(R.string.settings_number_news_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(GUARDIANAPI_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        //The parameters of the query
        uriBuilder.appendQueryParameter("q", "art");
        uriBuilder.appendQueryParameter("api-key", "1bea4277-7799-4259-8c91-0807050f3b81");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", numNews);
        uriBuilder.appendQueryParameter("order-by", orderBy);

        return new NewsLoader(MainActivity.this, uriBuilder.toString());
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
