package abelcorreadias.com.br.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import abelcorreadias.com.br.newsapp.adapters.NewsRecyclerAdapter;
import abelcorreadias.com.br.newsapp.loaders.NewsLoader;
import abelcorreadias.com.br.newsapp.models.NewsItem;
import abelcorreadias.com.br.newsapp.utils.QueryUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String URL_BASE = "http://content.guardianapis.com/";

    private static final String PATH = "search";

    private static final String API_KEY = "&api-key=6e99811c-4ec5-4011-bb96-bab2c3feccc3";

    private static final String QUERY = "?q=video-games AND games AND videogames";

    // @todo change {p} to the page number
    private static final String PAGE = "&page={p}";

    private static final String NEWS_REQUEST_URL =
            URL_BASE+PATH+QUERY+"&page=22"+API_KEY;


    private NewsRecyclerAdapter adapter;

    private ProgressBar progressBar;

    private static final int NEWS_LOADER_ID = 1;

    private TextView emptyStateTextView;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        emptyStateTextView.setText(R.string.no_news_found);

        adapter = new NewsRecyclerAdapter(new ArrayList<NewsItem>());
        recyclerView.setAdapter(adapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {

        progressBar.setVisibility(View.GONE);
        emptyStateTextView.setText(R.string.no_news_found);
        if(data != null && !data.isEmpty()){
            adapter = new NewsRecyclerAdapter(data);
            adapter.setOnItemClickListener(new NewsRecyclerAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(NewsItem item) {
                    Uri uri = Uri.parse(item.getUrl());
                    Intent siteIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(siteIntent);
                }

            });
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        adapter = new NewsRecyclerAdapter(new ArrayList<NewsItem>());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
