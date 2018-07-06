package abelcorreadias.com.br.newsapp.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import abelcorreadias.com.br.newsapp.models.NewsItem;
import abelcorreadias.com.br.newsapp.network.Connection;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    private int page;

    public NewsLoader(Context context, int page) {
        super(context);
        this.page = page;
    }


    @Override
    public List<NewsItem> loadInBackground() {
        return Connection.fetchNewsData(1);
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }
}
