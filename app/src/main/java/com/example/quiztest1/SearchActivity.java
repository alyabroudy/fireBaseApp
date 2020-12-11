package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    String query;
    String TAG_AKWAM = "Akwam";
    static List<Artist> searchResultList;
    ArtistList adapterSearch;

    ListView listViewArtists;
    String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        query = getIntent().getStringExtra("QUERY");
        searchResultList = new ArrayList<>();
        adapterSearch = new ArtistList(SearchActivity.this, searchResultList);
        listViewArtists = (ListView) findViewById(R.id.listViewArtist);



        //searchAkwam(query, false);




        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = searchResultList.get(position);
                //  artist.setUrl("https://old.goo-2o.com/5dd7c58d8da14");
                Log.i(TAG, "OnCreate. is Akwam");
                if (isSeriesLinkAkwam(artist) || isSeriesLinkShahid(artist)){
                    Intent seriesLinkIntent = new Intent(SearchActivity.this, LinkSeriesActivity.class);
                    seriesLinkIntent.putExtra("ARTIST_URL", artist.getUrl());
                    seriesLinkIntent.putExtra("ARTIST_NAME", artist.getName());
                    seriesLinkIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                    seriesLinkIntent.putExtra("ARTIST_SERVER", artist.getServer());
                    seriesLinkIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());
                    //start the activity
                    startActivity(seriesLinkIntent);

                    //fetchSeriesLinkAkwam(artist);
                }else {
                    Intent serverListIntent = new Intent(SearchActivity.this, ServersListActivity.class);
                    serverListIntent.putExtra("ARTIST_URL", artist.getUrl());
                    serverListIntent.putExtra("ARTIST_NAME", artist.getName());
                    serverListIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                    serverListIntent.putExtra("ARTIST_SERVER", artist.getServer());
                    serverListIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());
                    //start the activity
                    startActivity(serverListIntent);
                    // fetchOneLinkAkwam(artist);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        start();
    }

    public void start(){
        if (null == SearchActivity.searchResultList || SearchActivity.searchResultList.isEmpty()){
            searchAkwam(query, false);
            searchOldAkoamLinks(query, false);
        }
    }


    /////////////////////////////////////////////////////////////////
    ///////////////   Akwam.co    //////////////////////////////////

    /**
     * check if link is series or a movie link
     * @return true if the link is series link
     */
    private boolean isSeriesLinkAkwam(Artist artist){
        Log.i(TAG_AKWAM, "IsSerise End URL ="+artist.getUrl());
        return artist.getUrl().contains("akwam.co/series") || artist.getUrl().contains("akwam.co/movies") ;
    }

    public void searchAkwam(String query, boolean isSeries){
        final String oldAkwamQuery = query;

        if (!isSeries){
            if (query.trim().equals("مسلسلات")) {
                query = "https://akwam.co/series";
                Log.i(TAG_AKWAM, "Series");
            }else if (query.trim().equals("كوميدي")){
                Log.i(TAG_AKWAM, "Comedy");
                query = "https://akwam.co/movies?section=0&category=20&rating=0&year=0&language=0&format=0&quality=0";
            }else if (query.trim().equals("رعب")){
                Log.i(TAG_AKWAM, "Horror");
                query = "https://akwam.co/movies?section=0&category=22&rating=0&year=0&language=0&format=0&quality=0";
            }else {
                query ="https://akwam.co/search?q=" + query;
            }
        }



        final String url = query;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Log.i(TAG_AKWAM, "Search url:"+url);
                    Document doc = Jsoup.connect(url).get();
                    Elements links = doc.select("a");
                    for (Element link : links) {
                        if (link.hasClass("box"))
                        {
                            if (
                                    link.attr("href").contains("akwam.co/movie") ||
                                            link.attr("href").contains("akwam.co/series") ||
                                            link.attr("href").contains("akwam.co/episode")
                            ){
                                Artist a = new Artist();
                                a.setServer(Artist.SERVER_AKWAM);
                                a.setName(link.getElementsByAttribute("src").attr("alt"));
                                a.setUrl(link.attr("href"));
                                a.setImage(link.getElementsByAttribute("src").attr("data-src"));

                                SearchActivity.searchResultList.add(a);
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isSeries){
                                adapterSearch.notifyDataSetChanged();
                                getShahid4uLinks(oldAkwamQuery, false);
                            }
                          //  listViewArtists.setAdapter(adapterSearch);
                        }});
                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i(TAG_AKWAM, "error"+e.getMessage());
                }
            }
        }).start();
        Log.i(TAG_AKWAM, "Search End");
    }


    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    ///////////////   Shahid4u.one    //////////////////////////////////

    //shahid
    private boolean isSeriesLinkShahid(Artist artist){
        Log.i(TAG_AKWAM, "IsSerise End URL ="+artist.getUrl());
        return artist.getUrl().contains("shahid4u.one/series") || artist.getUrl().contains("shahid4u.one/season") ;
    }

    private void getShahid4uLinks(String query, boolean isSeries) {
        if (!isSeries) {
            query = "https://shahid4u.one/search?s=" + query;
        }
        final String url = query;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    Elements divs = doc.select("div[class]");
                    for (Element div : divs) {
                        if (div.hasClass("content-box")) {
                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_SHAHID4U);

                            a.setName(div.getElementsByTag("h3").text());
                            a.setUrl(div.getElementsByClass("image").attr("href"));
                            a.setImage(div.getElementsByClass("image").attr("data-src"));
                            //Log.i("old image nn ", div.getElementsByTag("a").attr("style")+"");

                            // a.setImage(link.getElementsByAttribute("src").attr("data-src"));
                            SearchActivity.searchResultList.add(a);
                        }
                    }
                    // adapter.notifyDataSetChanged();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                             adapterSearch.notifyDataSetChanged();
                             listViewArtists.setAdapter(adapterSearch);
                        }
                    });

                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage() + "");
                }
            }
        }).start();
    }

    //old Akwam
    private void searchOldAkoamLinks(String query, boolean isSeries) {

        if (!isSeries){
            query = "https://old.akwam.co/search/" + query;
        }
        Log.i("old akwam Search", query);
        final String url = query;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();

                    Elements divs = doc.select("div");
                    for (Element div : divs) {
                        if (div.hasClass("tags_box"))
                        {
                            String url = div.getElementsByTag("a").attr("href");
                            if (url.contains("لعبة") || url.contains("كورس")){
                                continue;
                            }

                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_OLD_AKWAM);

                            a.setName(div.getElementsByTag("h1").text());
                           // a.setUrl(div.getElementsByTag("a").attr("href"));
                            a.setUrl(url);

                            String image = div.getElementsByTag("div").attr("style");
                            image = image.substring(image.indexOf('(')+1,image.indexOf(')'));
                            a.setImage(image);
                            //Log.i("old image nn ", div.getElementsByTag("a").attr("style")+"");

                            // a.setImage(link.getElementsByAttribute("src").attr("data-src"));

                            SearchActivity.searchResultList.add(a);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Collections.reverse(MainActivity.artistList);
                                    adapterSearch.notifyDataSetChanged();
                                }
                            });

                            // }});

                        }
                    }
                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage()+"");
                }
            }
        }).start();
    }


}