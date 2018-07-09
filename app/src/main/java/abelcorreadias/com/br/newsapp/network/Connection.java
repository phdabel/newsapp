package abelcorreadias.com.br.newsapp.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
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

import abelcorreadias.com.br.newsapp.BuildConfig;
import abelcorreadias.com.br.newsapp.R;
import abelcorreadias.com.br.newsapp.models.NewsItem;

public final class Connection {

    public static final String LOG_TAG = Connection.class.getSimpleName();

    private static final Connection instance = new Connection();

    private static final String URL_BASE = "http://content.guardianapis.com/";

    private static final String PATH = "search";

    private static final String QUERY = "video-games AND games AND videogames";

    private static final String CONTRIBUTOR = "contributor";

    private static final int READ_TIMEOUT = 10000;

    private static final int CONNECT_TIMEOUT = 15000;

    private static Context mContext;

    private static SharedPreferences sharedPrefs;

    private Connection(){}

    public static Connection getInstance(Context context){
        mContext = context;
        return instance;
    }

    private static String getPreferences(String key, String value){
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPrefs.getString(key,value);
    }


    /**
     * Fetch news data from the request
     *
     * @param page
     * @return
     */
    public ArrayList<NewsItem> fetchNewsData(int page) {

        URL url = createURL(page);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream.",e);
        }
        return extractNewsFromJSON(jsonResponse);
    }

    /**
     * Receives the page number and creates the URL request.
     *
     * @param page
     * @return
     */
    private static URL createURL(int page) {
        URL url = null;

        Uri baseUri = Uri.parse(URL_BASE);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(PATH)
                .appendQueryParameter("q",QUERY)
                .appendQueryParameter("show-tags",CONTRIBUTOR)
                .appendQueryParameter("page",String.valueOf(page))
                .appendQueryParameter("api-key",BuildConfig.ApiKey);

        String orderBy = getPreferences(
                mContext.getString(R.string.settings_order_by_key),
                mContext.getString(R.string.settings_order_by_default));

        String section = getPreferences(
                mContext.getString(R.string.settings_section_key),
                mContext.getString(R.string.settings_section_default));
        String anySection = mContext.getString(R.string.settings_section_any_value);

        builder.appendQueryParameter("order-by",orderBy);

        if(!section.equals(anySection)) builder.appendQueryParameter("section",section);

        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Receives the URL, performs the request and returns the response data.
     *
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error response code: "+urlConnection.getResponseCode());
            }
        } catch(IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON result.", e);
            e.printStackTrace();

        } finally {
            if(urlConnection != null) urlConnection.disconnect();
            if(inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     *
     * @param inputStream
     * @return
     * @throws IOException
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

    /**
     * Extract news from the json response.
     *
     * @param response
     * @return
     */
    private static ArrayList<NewsItem> extractNewsFromJSON(String response){

        ArrayList<NewsItem> newsItems = new ArrayList<>();

        try{
            JSONObject root = new JSONObject(response);
            JSONArray results = ((JSONObject)root.get("response")).getJSONArray("results");
            for(int i = 0; i < results.length(); i++){
                JSONObject news = results.getJSONObject(i);
                String id = news.getString("id");
                String title = news.getString("webTitle");
                JSONArray tags = news.getJSONArray("tags");
                String author = mContext.getString(R.string.anonymous_author_tag);
                if(tags.length() > 0){
                    author = ((JSONObject)tags.get(0)).getString("webTitle");
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
