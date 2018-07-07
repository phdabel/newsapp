package abelcorreadias.com.br.newsapp.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import abelcorreadias.com.br.newsapp.R;
import abelcorreadias.com.br.newsapp.adapters.NewsRecyclerAdapter;
import abelcorreadias.com.br.newsapp.listeners.EndlessRecyclerViewScrollListener;
import abelcorreadias.com.br.newsapp.loaders.NewsLoader;
import abelcorreadias.com.br.newsapp.models.NewsItem;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private NewsRecyclerAdapter adapter;

    private ProgressBar progressBar;

    private static final int NEWS_LOADER_ID = 1;

    private TextView emptyStateTextView;

    private RecyclerView recyclerView;

    private EndlessRecyclerViewScrollListener scrollListener;

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);

        emptyStateTextView.setText(R.string.no_news_found);

        setupRecyclerview();

        setupConnectivity();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, 1);
    }

    @Override
    public void onLoadFinished(final Loader<List<NewsItem>> loader, List<NewsItem> data) {
        progressBar.setVisibility(View.GONE);
        emptyStateTextView.setText(R.string.no_news_found);
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
            adapter.setOnItemClickListener(new NewsRecyclerAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(NewsItem item) {
                    Uri uri = Uri.parse(item.url);
                    Intent siteIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(siteIntent);
                }

            });
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        adapter = new NewsRecyclerAdapter(new ArrayList<NewsItem>());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setupRecyclerview() {
        layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new NewsRecyclerAdapter(new ArrayList<NewsItem>());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //@todo: change this method to increment the page in the query
                // Trocar esse método por uma chamada do loader page+1... com esse reinicia sempre o loader e fica chamando a page 1 sempre.
                getLoaderManager().restartLoader(NEWS_LOADER_ID, null, (MainActivity) view.getContext());
            }
        };

        recyclerView.addOnScrollListener(this.scrollListener);

    }

    private void setupConnectivity() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }
}
