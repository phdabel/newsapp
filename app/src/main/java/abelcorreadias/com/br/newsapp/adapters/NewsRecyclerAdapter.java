package abelcorreadias.com.br.newsapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import abelcorreadias.com.br.newsapp.R;
import abelcorreadias.com.br.newsapp.models.NewsItem;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    public interface OnItemClickListener {

        void onItemClick(NewsItem item);

    }

    private List<NewsItem> dataset;

    private OnItemClickListener listener;

    public NewsRecyclerAdapter(List<NewsItem> dataset){
        this.dataset = dataset;
    }

    @Override
    public NewsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_view, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public void clear(){
        this.dataset.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<NewsItem> list){
        this.dataset.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(NewsRecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(this.dataset.get(position).getTitle());
        holder.author.setText(this.dataset.get(position).getAuthor());
        holder.date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.dataset.get(position).getDate()));
        holder.section.setText(this.dataset.get(position).getSection());
        holder.bind(dataset.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView author;
        TextView date;
        TextView section;

        public ViewHolder(View itemView){
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.news_title_text_view);
            this.author = (TextView) itemView.findViewById(R.id.news_author_text_view);
            this.date = (TextView) itemView.findViewById(R.id.news_date_text_view);
            this.section = (TextView) itemView.findViewById(R.id.news_section_text_view);
        }

        public void bind(final NewsItem item, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    listener.onItemClick(item);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
