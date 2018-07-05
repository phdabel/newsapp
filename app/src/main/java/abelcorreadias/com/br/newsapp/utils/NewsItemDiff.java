package abelcorreadias.com.br.newsapp.utils;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import java.util.List;
import abelcorreadias.com.br.newsapp.models.NewsItem;

public class NewsItemDiff extends DiffUtil.Callback {

    List<NewsItem> oldItems;
    List<NewsItem> newItems;

    public NewsItemDiff(List<NewsItem> newPersons, List<NewsItem> oldPersons){
        this.oldItems = oldPersons;
        this.newItems = newPersons;
    }

    @Override
    public int getOldListSize() {
        return this.oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return this.newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).getId() == newItems.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition){
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
