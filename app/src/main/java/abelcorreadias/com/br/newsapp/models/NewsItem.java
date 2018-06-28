package abelcorreadias.com.br.newsapp.models;

import java.io.Serializable;
import java.util.Date;

public class NewsItem implements Serializable {

    private static final String TAG = NewsItem.class.getSimpleName();

    private String title;
    private String author;
    private Date date;
    private String section;
    private String url;

    public NewsItem(){}

    public NewsItem(String title, String author, Date date, String section, String url){
        this.setTitle(title);
        this.setAuthor(author);
        this.setDate(date);
        this.setSection(section);
        this.setUrl(url);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString(){
        return TAG+"{" +
                "title='" + this.title + "'" +
                "author='" + this.author + "'" +
                "date'" + this.date.toString() + "'" +
                "section='" + this.section + "'" +
                "}";
    }

}
