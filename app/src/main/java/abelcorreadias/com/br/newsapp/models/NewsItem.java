package abelcorreadias.com.br.newsapp.models;

import java.util.Date;

public class NewsItem extends Model<String> {

    public String title;
    public String author;
    public Date date;
    public String section;
    public String url;

    public NewsItem(String id, String title, String author, Date date, String section, String url){
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.section = section;
        this.url = url;
    }

}
