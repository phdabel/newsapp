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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import abelcorreadias.com.br.newsapp.adapters.NewsRecyclerAdapter;
import abelcorreadias.com.br.newsapp.listeners.EndlessRecyclerViewScrollListener;
import abelcorreadias.com.br.newsapp.loaders.NewsLoader;
import abelcorreadias.com.br.newsapp.models.NewsItem;
import abelcorreadias.com.br.newsapp.network.Connection;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private NewsRecyclerAdapter adapter;

    private ProgressBar progressBar;

    private static final int NEWS_LOADER_ID = 1;

    private TextView emptyStateTextView;

    private RecyclerView recyclerView;

    private EndlessRecyclerViewScrollListener scrollListener;

    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
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
        return new NewsLoader(this, 1);
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
                    Uri uri = Uri.parse(item.url);
                    Intent siteIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(siteIntent);
                }

            });
        }
        recyclerView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //List<NewsItem> moreNews = Connection.fetchNewsData(page+1);
                //final int curSize = adapter.getItemCount();
                //adapter.addAll(moreNews);
                //adapter.notifyItemRangeInserted(curSize, adapter.getItemCount()-1);
                /*view.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeInserted(curSize, adapter.getItemCount()-1);
                    }
                });*/

                getLoaderManager().restartLoader(NEWS_LOADER_ID, null, (MainActivity)view.getContext());
            }
        };
        recyclerView.addOnScrollListener(this.scrollListener);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        adapter = new NewsRecyclerAdapter(new ArrayList<NewsItem>());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
