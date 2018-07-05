package abelcorreadias.com.br.newsapp.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import abelcorreadias.com.br.newsapp.models.NewsItem;
import abelcorreadias.com.br.newsapp.utils.QueryUtils;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }


    @Override
    public List<NewsItem> loadInBackground() {
        if(mUrl == null) return null;
        return QueryUtils.fetchNewsData(this.mUrl);
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }
}
