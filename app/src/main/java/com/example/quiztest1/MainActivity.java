package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   // DatabaseReference databaseArtist;

    //list contains retrieved artists
    static List<Artist> artistList;
    static List<Artist> resultList;
    static List<Artist> resolutionsList;

    //variable for back button confirmation
    private long backPressedTime;

    String TAG_AKWAM = "Akwam";
    String TAG_CIMA4U = "Cima4u";
    String TAG = "MainActivity";

    EditText editTextName;
    Button buttonSearch;
    ListView listViewArtists;

    ImageView imageView;
    ArtistList adapter;
    ArtistList adapterResult;
    ArtistList adapterResolution;

    static List<String> serverList;
    static int serverCounts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //databaseArtist = FirebaseDatabase.getInstance().getReference("artists");
        //initialise server Controllers
        serverList = new ArrayList<>();
        serverCounts = 4;

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        listViewArtists = (ListView) findViewById(R.id.listViewArtist);
       // textViewResponse = (TextView) findViewById(R.id.textView_response);
        imageView = (ImageView) findViewById(R.id.imageView);




        //initialize artist list
        artistList = new ArrayList<>();
        resultList = new ArrayList<>();
        resolutionsList = new ArrayList<>();
        adapter = new ArtistList(MainActivity.this, artistList);
        adapterResult = new ArtistList(MainActivity.this, resultList);
        adapterResolution = new ArtistList(MainActivity.this, resolutionsList);

        listViewArtists.setAdapter(adapter);
        String movies = "https://akwam.co/movies";
        String movies2 = "https://akwam.co/movies?section=0&category=0&rating=0&year=0&language=1&formats=0&quality=0";
        String oldAkwam1 = "https://old.akwam.co/cat/156/%D8%A7%D9%84%D8%A3%D9%81%D9%84%D8%A7%D9%85-%D8%A7%D9%84%D8%A7%D8%AC%D9%86%D8%A8%D9%8A%D8%A9";
     //   String oldAkwam2 = "https://old.akwam.co/cat/155/%D8%A7%D9%84%D8%A3%D9%81%D9%84%D8%A7%D9%85-%D8%A7%D9%84%D8%B9%D8%B1%D8%A8%D9%8A%D8%A9";

  /*      searchAkwam(movies, true);
        searchAkwam(movies2, true);
        String series = "https://akwam.co/series";
        searchAkwam(series, true);

   */
       // searchOldAkoamLinks(oldAkwam1, true);
       // searchOldAkoamLinks(oldAkwam2, true);

       // searchCima4u(netflix, true);


        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistList.get(position);
              //  artist.setUrl("https://old.goo-2o.com/5dd7c58d8da14");
            //    if (artist.getServer().equals(Artist.SERVER_AKWAM))
              //  {
                    Log.i(TAG, "OnCreate. is Akwam");

                  //  if (artist.getIsVideo()){
                   //     fetchLinkVideoAkwam(artist);
                   // }else {
                        if (isSeriesLink(artist) ){
                            Intent seriesLinkIntent = new Intent(MainActivity.this, LinkSeriesActivity.class);
                            seriesLinkIntent.putExtra("ARTIST_URL", artist.getUrl());
                            seriesLinkIntent.putExtra("ARTIST_NAME", artist.getName());
                            seriesLinkIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                            seriesLinkIntent.putExtra("ARTIST_SERVER", artist.getServer());
                            seriesLinkIntent.putExtra("ARTIST_IS_VIDEO", false);
                            //start the activity
                            startActivity(seriesLinkIntent);

                            //fetchSeriesLinkAkwam(artist);
                        }else {
                            Intent serverListIntent = new Intent(MainActivity.this, ServersListActivity.class);
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
                  //  }

                /*

/*



                }else {
                    Log.i(TAG, "OnCreate. is shaihd");
                        Intent shahidLinkIntent = new Intent(MainActivity.this, LinkSeriesActivity.class);
                    shahidLinkIntent.putExtra("ARTIST_URL", artist.getUrl());
                    shahidLinkIntent.putExtra("ARTIST_NAME", artist.getName());
                    shahidLinkIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                        //start the activity
                        startActivity(shahidLinkIntent);
                    /*
                    if (artist.getIsVideo()){
                        generateShahid4uLink(artist);
                    }else {
                        if (isSeriesLinkShahid(artist)) {
                            generateShahid4uSeriesLink(artist);
                        } else {
                            getServersShahid4u(artist);
                        }
                    }


                }

                 */
            }
        });
        //click listener for the button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editTextName.getText().toString();
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                searchIntent.putExtra("QUERY", query);
                //start the activity
                startActivity(searchIntent);

             /*   search();
                Artist artist = new Artist();
                artist.setUrl("http://old.goo-2o.com/5dd7c58d8da14");
               // artist.setUrl(getOldAkwamGooPage(artist));
                getOldAkwamVideoLink("https://old.akwam.co/download/3c472024a3f1/High-Seas-S02-Ep01-720p-WEB-DL-akoam-net-mkv");
                Log.i("artist goo", artist.getUrl()+"");
              //  artist.setUrl(getOldAkwamVideoLink(artist));
                //Log.i("artist video", artist.getUrl()+"");

              */
            }
        });


    }

    public void start(){
        if (MainActivity.artistList.isEmpty()){

            new Thread(new Runnable() {
                @Override
                public void run() {

            listViewArtists.setAdapter(adapter);
            String movies = "https://akwam.co/movies";
            String movies2 = "https://akwam.co/movies?section=0&category=0&rating=0&year=0&language=1&formats=0&quality=0";
            String oldAkwam1 = "https://old.akwam.co/cat/156/%D8%A7%D9%84%D8%A3%D9%81%D9%84%D8%A7%D9%85-%D8%A7%D9%84%D8%A7%D8%AC%D9%86%D8%A8%D9%8A%D8%A9";
            //   String oldAkwam2 = "https://old.akwam.co/cat/155/%D8%A7%D9%84%D8%A3%D9%81%D9%84%D8%A7%D9%85-%D8%A7%D9%84%D8%B9%D8%B1%D8%A8%D9%8A%D8%A9";

            searchAkwam(movies, true);

            searchAkwam(movies2, true);


            String series = "https://akwam.co/series";
            searchAkwam(series, true);
            //searchOldAkoamLinks(oldAkwam1, true);

                }}).start();
        }
    }

    //confirmation if the user click the back button to exit the app
    @Override
    public void onBackPressed() {
        //check if waiting time between the second click of back button is greater less than 2 seconds so we finish the app
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finish();
        }else {
            /*
            if (adapter.getArtistList() == MainActivity.resolutionsList){
                listViewArtists.setAdapter(adapterResult);
            }else if (adapter.getArtistList() == MainActivity.resultList){
                listViewArtists.setAdapter(adapter);
            }else {
                Toast.makeText(this, "Press back 2 time to exit", Toast.LENGTH_SHORT).show();
            }
            */
            Toast.makeText(this, "Press back 2 time to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
        start();
    }

    private boolean isSeriesLink(Artist artist){
        String u = artist.getUrl();
        String n = artist.getName();

        boolean isSeriesShahid= artist.getServer().equals(Artist.SERVER_SHAHID4U) &&
                (u.contains("shahid4u.one/series") || u.contains("shahid4u.one/season") );

        boolean isSeriesAkwam= artist.getServer().equals(Artist.SERVER_AKWAM) &&
                ( u.contains("akwam.co/series") || u.contains("akwam.co/movies"));

        boolean isSeriesCima= artist.getServer().equals(Artist.SERVER_CIMA4U) &&
                ( n.contains("مسلسل") || n.contains("مسلسلات") || n.contains("انمي"));

      //  boolean isSeriesOldAkwam= artist.getServer().equals(Artist.SERVER_OLD_AKWAM) &&
      //          ( u.contains("مسلسل") || u.contains("مسلسلات"));
        return isSeriesAkwam || isSeriesShahid || isSeriesCima;
    }

    /////////////////////////////////////////////////////////////////
    ///////////////   Akwam.co    //////////////////////////////////

    public void searchAkwam(String query, boolean isSeries){
        Log.i(TAG_AKWAM, "Search Start");
        final String oldAkwamQuery = query;

        if (!isSeries){
            query ="https://akwam.co/search?q=" + query;
        }


        Log.i(TAG_AKWAM, "Query="+query);
        final String url = query;


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
                                Log.i("link found", link.attr("href")+"");
                                a.setName(link.getElementsByAttribute("src").attr("alt"));
                                a.setUrl(link.attr("href"));
                                a.setImage(link.getElementsByAttribute("src").attr("data-src"));

                                MainActivity.artistList.add(a);
                            /*    runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("akwam list size O", MainActivity.artistList.size() + "s");
                                        adapter.notifyDataSetChanged();
                                        // Collections.shuffle(MainActivity.artistList);
                                       // listViewArtists.setAdapter(adapter);
                                    }});

                             */
                            }
                        }
                    }
                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i(TAG_AKWAM, "error"+e.getMessage());
                }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                // Collections.shuffle(MainActivity.artistList);
                // listViewArtists.setAdapter(adapter);
            }});

        Log.i(TAG_AKWAM, "Search End");
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
                            MainActivity.artistList.add(a);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                   // listViewArtists.setAdapter(adapter);
                                }
                            });

                        }
                    }
                    // adapter.notifyDataSetChanged();
           /*         runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          //  adapter.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapter);
                        }
                    });

            */

                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage() + "");
                }
            }
        }).start();
    }

    //////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    ///////////////   Old.Akwam.co   //////////////////////////////////

    //old Akwam 000 different then the only search
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
                    Document doc = Jsoup.connect(url).timeout(6000).get();

                    Elements divs = doc.select("div");

                    for (Element div : divs) {
                        //if (div.hasClass("tags_box"))
                        if (div.hasClass("subject_box shape"))
                        {
                            String url = div.getElementsByTag("a").attr("href");
                            if (url.contains("لعبة") || url.contains("كورس")){
                                continue;
                            }

                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_OLD_AKWAM);

                            // a.setName(div.getElementsByTag("h1").text());
                            String name = div.getElementsByTag("h3").text();
                            if (name.contains("توضيح هام لمتابعي")){
                                continue;
                            }
                            a.setName(name);
                            // a.setUrl(div.getElementsByTag("a").attr("href"));
                            a.setUrl(url);

                            String image = div.getElementsByTag("img").attr("src");
                            // String image = div.getElementsByTag("div").attr("style");
                            // image = image.substring(image.indexOf('(')+1,image.indexOf(')'));
                            a.setImage(image);
                            //Log.i("old image nn ", div.getElementsByTag("a").attr("style")+"");

                            // a.setImage(link.getElementsByAttribute("src").attr("data-src"));

                            MainActivity.artistList.add(a);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Collections.reverse(MainActivity.artistList);
                                    adapter.notifyDataSetChanged();
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

    //////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    ///////////////   shahid4u.one    //////////////////////////////////
    private void getShahid4uLinks(String query, boolean isSeries) {
        Log.i("akwam getLinks", "start");

        if (!isSeries) {
            query = "https://shahid4u.one/search?s=" + query;
        }
        final String url = query;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Log.i("old akwam getLinks", "try before connect");
                    Log.i("old akwam links url", url + "s");
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    Log.i("old akwam getLinks", "try after connect");

                    Elements divs = doc.select("div[class]");
                    for (Element div : divs) {
                        Log.i("old akwam div", "found div no class");
                        if (div.hasClass("content-box")) {
                            Log.i("old akwam div", "found div with class");
                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_SHAHID4U);

                            a.setName(div.getElementsByTag("h3").text());
                            Log.i("old name", div.getElementsByTag("h3").text().toString() + "");

                            a.setUrl(div.getElementsByClass("image").attr("href"));
                            a.setImage(div.getElementsByClass("image").attr("data-src"));
                            //Log.i("old image nn ", div.getElementsByTag("a").attr("style")+"");

                            // a.setImage(link.getElementsByAttribute("src").attr("data-src"));

                            MainActivity.artistList.add(a);
                            Log.i("akwam list size In", MainActivity.artistList.size() + "s");
                            Log.i("artis in", a.getUrl());

                            // }});

                        }
                    }
                    // adapter.notifyDataSetChanged();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // ArtistList adapter = new ArtistList(MainActivity.this, linksList);
                            //  listViewArtists.setAdapter(adapter);
                            // MainActivity.artistList.add(a);
                            //Log.i("artis O", a.getUrl());
                            Log.i("old akwam list size O", MainActivity.artistList.size() + "s");
                            adapter.notifyDataSetChanged();
                            //listViewArtists.setAdapter(adapter);
                        }
                    });

                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage() + "");
                }
            }
        }).start();
        Log.i("akwam getLinks", "end");
    }



    //////////////////////////////////////////////////////////////
}