package abelcorreadias.com.br.newsapp.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import abelcorreadias.com.br.newsapp.models.NewsItem;

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){}

    public static ArrayList<NewsItem> fetchNewsData(String requestUrl){

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error closing input stream.", e);
        }

        return extractNewsFromJSON(jsonResponse);

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }finally {
            if(urlConnection != null) urlConnection.disconnect();
            if(inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<NewsItem> extractNewsFromJSON(String response){

        ArrayList<NewsItem> newsItems = new ArrayList<>();

        try{
            JSONObject root = new JSONObject(response);
            JSONArray results = ((JSONObject)root.get("response")).getJSONArray("results");
            for(int i = 0; i < results.length(); i++){
                JSONObject news = results.getJSONObject(i);
                String id = news.getString("id");
                String titleAuthorProperty = news.getString("webTitle");
                String title;
                String author = null;
                if(titleAuthorProperty.contains("|")){
                    String[] titleAuthor = titleAuthorProperty.split("[|]");
                    title = titleAuthor[0].trim();
                    author = titleAuthor[1].trim();
                }else{
                    title = titleAuthorProperty;
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date date = format.parse(news.getString("webPublicationDate"));
                String section = news.getString("sectionName");
                String url = news.getString("webUrl");
                newsItems.add(new NewsItem(id, title, author, date, section, url));
            }

        }catch(JSONException e){
            Log.e(LOG_TAG, "Problem parsing the news item JSON results.", e);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Problem parsing date from news item JSON results.", e);
        }

        return newsItems;
    }

}
