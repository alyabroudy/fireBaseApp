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

public class SessionsActivity extends AppCompatActivity {

    static List<Artist> sessionsList;
    ArtistList adapterSearch;
    Artist artist;
    ListView listViewArtists;
    Intent sessionsListIntent;
    String TAG_FASELHD= "FaselHd";
    String TAG_MYCIMA= "MyCima";
    String TAG = "SessionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        sessionsList = new ArrayList<>();
        adapterSearch = new ArtistList(SessionsActivity.this, sessionsList);
        listViewArtists = (ListView) findViewById(R.id.listViewArtist);

        sessionsListIntent = getIntent();
        artist = new Artist();
        artist.setUrl(sessionsListIntent.getStringExtra("ARTIST_URL"));
        artist.setName(sessionsListIntent.getStringExtra("ARTIST_NAME"));
        artist.setImage(sessionsListIntent.getStringExtra("ARTIST_IMAGE"));
        artist.setServer(sessionsListIntent.getStringExtra("ARTIST_SERVER"));
        artist.setIsVideo(sessionsListIntent.getExtras().getBoolean("ARTIST_IS_VIDEO"));


        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = sessionsList.get(position);
                        Intent seriesLinkIntent = new Intent(SessionsActivity.this, LinkSeriesActivity.class);
                        seriesLinkIntent.putExtra("ARTIST_URL", artist.getUrl());
                        seriesLinkIntent.putExtra("ARTIST_NAME", artist.getName());
                        seriesLinkIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                        seriesLinkIntent.putExtra("ARTIST_SERVER", artist.getServer());
                        seriesLinkIntent.putExtra("ARTIST_IS_VIDEO", false);
                        //start the activity
                        startActivity(seriesLinkIntent);
                    }
        });

        start();
    }

    public void start(){
        if (artist.getServer().equals(Artist.SERVER_FASELHD)){
            searchFaselHd(artist);
        }else {
            fetchSessionsLinkMyCima(artist);
        }
    }

    private void searchFaselHd(Artist artist) {
        String url = artist.getUrl();
        String web = "https://www.faselhd.pro/?p=";
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    Elements lis = doc.getElementsByClass("seasonDiv");
                    for (Element li : lis) {
                        Log.i(TAG_FASELHD, "element found: ");

                        Artist a = new Artist();
                        a.setServer(Artist.SERVER_FASELHD);
                        String link = web+li.attr("data-href");

                        String name = li.getElementsByClass("title").text();
                        String image = li.getElementsByAttribute("data-src").attr("data-src");
                        String rate = "";
                        Elements spans = li.getElementsByClass("seasonMeta");
                        for (Element span : spans) {
                                rate = span.children().text();
                                break;
                        }



                        Log.i(TAG_FASELHD, "Link found: "+link);
                        Log.i(TAG_FASELHD, "name found: "+name);
                        Log.i(TAG_FASELHD, "image found: "+image);
                        Log.i(TAG_FASELHD, "rate found: "+rate);


                        a.setName(name);
                        a.setUrl(link);
                        a.setImage(image);
                        a.setRate(rate);
                        //Log.i("old image nn ", div.getElementsByTag("a").attr("style")+"");

                        // a.setImage(link.getElementsByAttribute("src").attr("data-src"));
                        SessionsActivity.sessionsList.add(a);

                        /*

                         */
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterSearch.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterSearch);
                        }
                    });
                    // adapter.notifyDataSetChanged();


                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage() + "");
                }
            }
        }).start();
    }

    private void fetchSessionsLinkMyCima(final Artist artist){
        Log.i(TAG_MYCIMA, "fetchSeries url:"+artist.getUrl());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_MYCIMA, "ur:"+url);
                    Document doc = Jsoup.connect(url).header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header("User-Agent","Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").timeout(0).get();
                    //Elements links = doc.select("a[href]");

                    //get link of episodes page
                    String desc = doc.getElementsByClass("PostItemContent").text();

                    //fetch session
                    Elements boxs = doc.getElementsByClass("List--Seasons--Episodes");

                    if (boxs.isEmpty()){
                        Log.i(TAG_MYCIMA, "isEmpty");
                        Intent seriesLinkIntent = new Intent(SessionsActivity.this, LinkSeriesActivity.class);
                        seriesLinkIntent.putExtra("ARTIST_URL", artist.getUrl());
                        seriesLinkIntent.putExtra("ARTIST_NAME", artist.getName());
                        seriesLinkIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                        seriesLinkIntent.putExtra("ARTIST_SERVER", artist.getServer());
                        seriesLinkIntent.putExtra("ARTIST_IS_VIDEO", false);
                        //start the activity
                        startActivity(seriesLinkIntent);
                        return;
                    }

                    for (Element box: boxs){

                        Elements lis = box.getElementsByTag("a");


                        Log.i(TAG_MYCIMA, "not empty"+lis.size());
                        Log.i("Count", "boxs:"+boxs.size());
                        for (Element li : lis) {
                            String title = li.text();
                            String link = li.attr("href");

                            Artist a = new Artist();
                            a.setName(title);
                            a.setUrl(link);
                            a.setServer(Artist.SERVER_MyCima);
                            a.setImage(artist.getImage());
                            SessionsActivity.sessionsList.add(a);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Collections.reverse(LinkSeriesActivity.seriesArtistList);
                            adapterSearch.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterSearch);
                            //listViewArtists.setAdapter(adapter);
                        }});
                } catch (IOException e) {
                    Log.i("fail", e.getMessage()+"");
                }

            }
        }).start();
        Log.i(TAG_MYCIMA, "FetchSeriesLink end");
    }
}