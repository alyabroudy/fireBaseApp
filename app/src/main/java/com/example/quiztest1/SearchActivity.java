package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    String query;
    String TAG_AKWAM = "Akwam";
    String TAG_CIMA4U = "Cima4u";
    String TAG_AFLAM_PRO = "AflamPro";
    String TAG_FASELHD= "FaselHd";
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
                if (isSeriesLink(artist) ){
                    if (artist.getServer().equals(Artist.SERVER_FASELHD)){
                        fetchFaselHdSession(artist);
                    }else{
                        Log.i(TAG, "OnCreate. is series");
                        Intent seriesLinkIntent = new Intent(SearchActivity.this, LinkSeriesActivity.class);
                        seriesLinkIntent.putExtra("ARTIST_URL", artist.getUrl());
                        seriesLinkIntent.putExtra("ARTIST_NAME", artist.getName());
                        seriesLinkIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                        seriesLinkIntent.putExtra("ARTIST_SERVER", artist.getServer());
                        seriesLinkIntent.putExtra("ARTIST_IS_VIDEO", false);
                        //start the activity
                        startActivity(seriesLinkIntent);
                    }

                    //fetchSeriesLinkAkwam(artist);
                }else {
                    Log.i(TAG, "OnCreate. not series");
                    Intent serverListIntent = new Intent(SearchActivity.this, ServersListActivity.class);
                    serverListIntent.putExtra("ARTIST_URL", artist.getUrl());
                    serverListIntent.putExtra("ARTIST_NAME", artist.getName());
                    serverListIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                    serverListIntent.putExtra("ARTIST_SERVER", artist.getServer());
                    if (artist.getServer().equals(Artist.SERVER_CIMA4U)){
                        serverListIntent.putExtra("ARTIST_IS_VIDEO", true);
                    }else{
                        serverListIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());
                    }
                    //start the activity
                    startActivity(serverListIntent);
                    // fetchOneLinkAkwam(artist);
                }
            }
        });

        start();
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        start();
    }


 */
    public void start(){
     //   if (null == SearchActivity.searchResultList || SearchActivity.searchResultList.isEmpty()){
            listViewArtists.setAdapter(adapterSearch);
         //   searchAkwam(query, false);
         //   searchOldAkoamLinks(query, false);
         //   getShahid4uLinks(query, false);
         //   searchCima4u(query, false);
         //   searchAflamPro(query, false);
            searchFaselHd(query, false);
     //   }
    }



    private boolean isSeriesLink(Artist artist){
        String u = artist.getUrl();
        String n = artist.getName();

        boolean isSeriesShahid= artist.getServer().equals(Artist.SERVER_SHAHID4U) &&
                (u.contains("/series") || u.contains("/season") );

        boolean isSeriesAkwam= artist.getServer().equals(Artist.SERVER_AKWAM) &&
                ( u.contains("akwam.co/series") || u.contains("akwam.co/movies"));

        boolean isSeriesCima= artist.getServer().equals(Artist.SERVER_CIMA4U) &&
                ( n.contains("مسلسل") || n.contains("مسلسلات") || n.contains("انمي"));

        boolean isSeriesAflamPro= artist.getServer().equals(Artist.SERVER_AFLAM_PRO) &&
                ( u.contains("/serie") ||  n.contains("مسلسل") );

        boolean isSeriesFaselHd= artist.getServer().equals(Artist.SERVER_FASELHD) &&
                ( u.contains("/seasons") ||  n.contains("مسلسل") );

       // boolean isSeriesOldAkwam= artist.getServer().equals(Artist.SERVER_OLD_AKWAM) &&
       //         ( u.contains("مسلسل") || u.contains("مسلسلات"));
        return isSeriesAkwam || isSeriesShahid || isSeriesCima || isSeriesAflamPro || isSeriesFaselHd;
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
                /*    Connection.Response response = Jsoup.connect(url).timeout(6000).ignoreContentType(true).execute();

                    Log.i("response:", response.body().toString()+"");


                 */
                    Document doc = Jsoup.connect(url).timeout(6000).get();

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
                           /*     runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //if (!isSeries){
                                        adapterSearch.notifyDataSetChanged();

                                        //}
                                        //  listViewArtists.setAdapter(adapterSearch);
                                    }});

                            */
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //if (!isSeries){
                            adapterSearch.notifyDataSetChanged();

                            //}
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
        Log.i("Shahid", "search:"+query);
        final String url = query;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String shahid= "https://shahid4u.one";

               /*     Document doc = Jsoup.connect(shahid).get();
                     doc = Jsoup.connect(url)
                            .userAgent(" Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36")
                          //  .header("Accept", " text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/ //*;q=0.8")
                    /*        .header("Accept-Encoding", "gzip,deflate")
                            .header("Accept-Language", "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2")
                            .header("Connection", "keep-alive")
                            .ignoreContentType(true)
                             .timeout(10000)
                            .ignoreHttpErrors(true)
                            .followRedirects(true)
                            .get();
                            */
                    Connection con = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(6000).ignoreHttpErrors(true);
                    Connection.Response resp = con.execute();
                    Document doc = null;
                  //  if (resp.statusCode() == 200) {
                        doc = con.get();
                  //  Log.i("connection", "ok");
                  //  }


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
                      /*      runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterSearch.notifyDataSetChanged();
                                    //listViewArtists.setAdapter(adapterSearch);
                                }
                            });

                       */
                        }
                    }
                    // adapter.notifyDataSetChanged();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterSearch.notifyDataSetChanged();
                            //listViewArtists.setAdapter(adapterSearch);
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
                       /*     runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Collections.reverse(MainActivity.artistList);
                                    adapterSearch.notifyDataSetChanged();
                                }
                            });

                        */

                            // }});

                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Collections.reverse(MainActivity.artistList);
                            adapterSearch.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage()+"");
                }
            }
        }).start();
    }

    //cima4u
    private void searchCima4u(String query, boolean isSeries) {
        if (!isSeries) {
            query = "https://cima4u.io/?s=" + query;
        }
        final String url = query;
        Log.i(TAG_CIMA4U, "search: "+query);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Document doc = Jsoup.connect(url).timeout(6000).get();
                    //Elements links = doc.select("a[href]");
                    Elements lis = doc.select("li[class]");
                    for (Element li : lis) {
                        Log.i(TAG_CIMA4U, "element found: ");
                        if (li.hasClass("MovieBlock")) {
                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_CIMA4U);
                            String link = li.getElementsByAttribute("href").attr("href");

                            li.getElementsByClass("BoxTitleInfo").remove();

                            String name = li.getElementsByClass("BoxTitle").text();
                            String image = li.getElementsByClass("Half1").attr("style");
                            image = image.substring(image.indexOf('(')+1, image.indexOf(')'));
                            Log.i(TAG_CIMA4U, "Link found: "+link);
                            Log.i(TAG_CIMA4U, "name found: "+name);
                            Log.i(TAG_CIMA4U, "image found: "+image);


                           a.setName(name);
                            a.setUrl(link);
                            a.setImage(image);
                            //Log.i("old image nn ", div.getElementsByTag("a").attr("style")+"");

                            // a.setImage(link.getElementsByAttribute("src").attr("data-src"));
                            SearchActivity.searchResultList.add(a);

                        /*

                         */
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterSearch.notifyDataSetChanged();
                            //listViewArtists.setAdapter(adapterSearch);
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

    //aflamPro
    private void searchAflamPro(String query, boolean isSeries) {
        if (!isSeries) {
            query = "https://aflampro.com/?s=" + query;
        }
        final String url = query;
        Log.i(TAG_AFLAM_PRO, "search: "+query);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    Elements lis = doc.select("li[class]");
                    for (Element li : lis) {
                        Log.i(TAG_AFLAM_PRO, "element found: ");
                        if (li.hasClass("TPostMv")) {
                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_AFLAM_PRO);
                            String link = li.getElementsByAttribute("href").attr("href");

                            String name = li.getElementsByClass("Title").text();
                            String image = li.getElementsByAttribute("src").attr("src");
                            String rate = li.getElementsByClass("Vote AAIco-star").text();

                            Log.i(TAG_AFLAM_PRO, "Link found: "+link);
                            Log.i(TAG_AFLAM_PRO, "name found: "+name);
                            Log.i(TAG_AFLAM_PRO, "image found: "+image);
                            Log.i(TAG_AFLAM_PRO, "rate found: "+rate);


                            a.setName(name);
                            a.setUrl(link);
                            a.setImage(image);
                            a.setRate(rate);
                            //Log.i("old image nn ", div.getElementsByTag("a").attr("style")+"");

                            // a.setImage(link.getElementsByAttribute("src").attr("data-src"));
                            SearchActivity.searchResultList.add(a);

                            /*

                             */
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterSearch.notifyDataSetChanged();
                            //listViewArtists.setAdapter(adapterSearch);
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

    //FaselHd
    private void searchFaselHd(String query, boolean isSeries) {
        if (!isSeries) {
            query = "https://www.faselhd.pro/?s=" + query;
        }
        final String url = query;
        Log.i(TAG_FASELHD, "search: "+query);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    Elements lis = doc.getElementsByClass("postDiv");
                    for (Element li : lis) {
                        Log.i(TAG_FASELHD, "element found: ");

                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_FASELHD);
                            String link = li.getElementsByAttribute("href").attr("href");

                            String name = li.getElementsByAttribute("alt").attr("alt");
                            String image = li.getElementsByAttribute("data-src").attr("data-src");
                        String rate = "";
                        Elements spans = li.getElementsByTag("span");
                        for (Element span : spans) {
                            if (!span.hasAttr("class")){
                                rate = span.text();
                                break;
                            }
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
                            SearchActivity.searchResultList.add(a);

                            /*

                             */
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterSearch.notifyDataSetChanged();
                            //listViewArtists.setAdapter(adapterSearch);
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

   public void fetchFaselHdSession(Artist artist){
       Intent fetchServerIntent = new Intent(SearchActivity.this, FetchServerActivity.class);
       fetchServerIntent.putExtra("PAGE_NUMBER", 9);  //to fetch goo page = 1
       if (artist.getUrl().equals("")){
           fetchServerIntent.putExtra("ARTIST_URL", artist.getUrl());
       }else{
           fetchServerIntent.putExtra("ARTIST_URL", artist.getUrl());
       }
       fetchServerIntent.putExtra("ARTIST_NAME", artist.getName());
       fetchServerIntent.putExtra("ARTIST_IMAGE", artist.getImage());
       fetchServerIntent.putExtra("ARTIST_SERVER", artist.getServer());
       fetchServerIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());

       Log.i(TAG, "Fasel         fetchServerIntent.putExtra(\"ARTIST_NAME\", artist.getName());\n&V url:"+artist.getUrl());
       startActivityForResult(fetchServerIntent, 9);
   }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Returned Result", "method start code:"+requestCode);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode == 9)
        {
            if (resultCode == RESULT_OK){
                String serverUrl=data.getStringExtra("result");
                String serverName = data.getStringExtra("ARTIST_SERVER");
                String image = data.getStringExtra("ARTIST_IMAGE");
                String name = data.getStringExtra("ARTIST_NAME");
                Log.i("Returned Result", serverUrl + "name"+ serverName);

                Log.i("Returned Final", serverUrl + "name"+ serverName);
                Log.i(TAG, "OnCreate. is series");
                Intent seriesLinkIntent = new Intent(SearchActivity.this, LinkSeriesActivity.class);
                seriesLinkIntent.putExtra("ARTIST_URL", serverUrl);
                seriesLinkIntent.putExtra("ARTIST_NAME", name);
                seriesLinkIntent.putExtra("ARTIST_IMAGE", image);
                seriesLinkIntent.putExtra("ARTIST_SERVER", serverName);
                seriesLinkIntent.putExtra("ARTIST_IS_VIDEO", false);
                //start the activity
                startActivity(seriesLinkIntent);
            }
            if (resultCode == RESULT_CANCELED){
                Log.i("Returned Result", "Nothing returned");
            }
            Log.i("Returned Result:"+requestCode, "method end resultcode:"+resultCode);
            //  textView1.setText(message);
        }
    }

    public String getWebName(String item){
        return item.substring(item.indexOf("//")+1, item.indexOf('.'));
    }

}