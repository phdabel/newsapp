package abelcorreadias.com.br.newsapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import abelcorreadias.com.br.newsapp.models.NewsItem;

import static org.junit.Assert.*;

public class ModelUnitTest {


    static Date date;
    static NewsItem news;

    @BeforeClass
    public static void setUp(){
        try{
            date = new SimpleDateFormat("dd/MM/yyyy").parse("26/11/1983");
            news = new NewsItem("id", "titulo", "autor", date, "secao", "url");
        }catch(ParseException e){
            e.printStackTrace();
        }
    }

    @Test
    public void test_toString(){
        assertEquals(news.toString(),"NewsItem={\"id\":\"id\",\"date\":\"Sat Nov 26 00:00:00 BRT 1983\",\"author\":\"autor\",\"section\":\"secao\",\"title\":\"titulo\",\"url\":\"url\"}");
    }

    @Test
    public void test_toJSON(){
        try {
            JSONObject obj = new JSONObject("{\"date\":\"Sat Nov 26 00:00:00 BRT 1983\",\"author\":\"autor\",\"section\":\"secao\",\"title\":\"titulo\",\"url\":\"url\"}");
            assertEquals(news.toJSON().toString(),obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        };
    }

}
