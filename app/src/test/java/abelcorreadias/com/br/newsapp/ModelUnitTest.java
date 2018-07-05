package abelcorreadias.com.br.newsapp;

import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import abelcorreadias.com.br.newsapp.models.NewsItem;

import static org.junit.Assert.*;

public class ModelUnitTest {

    @Test
    public void test_toString(){
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("26/11/1983");
            NewsItem news = new NewsItem("id", "titulo", "autor", date, "secao", "url");
            assertEquals(news.toString(),"NewsItem={\nid='id'\ntitle='titulo'\nauthor='autor'\ndate='Sat Nov 26 00:00:00 BRT 1983'\nsection='secao'\nurl='url'\n}");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
