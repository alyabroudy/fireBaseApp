package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    static String oldAkwamGooLink="";
    static String oldAkwamVideoLink="";

    //variable for back button confirmation
    private long backPressedTime;

    String TAG_AKWAM = "Akwam";
    String TAG = "MainActivity";

    EditText editTextName;
    Button buttonSearch;
    ListView listViewArtists;

    ImageView imageView;
    ArtistList adapter;
    ArtistList adapterResult;
    ArtistList adapterResolution;

    WebView simpleWebView;
    WebView simpleWebView2;
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
        //simpleWebView = (WebView) findViewById(R.id.webView);
        //simpleWebView2 = (WebView) findViewById(R.id.webView2);
      //  simpleWebView.getSettings().setJavaScriptEnabled(true);
        //simpleWebView.addJavascriptInterface(new WebAppInterface(this), "Android");


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


        String movies = "https://akwam.co/movies";
        //String movies2 = "https://old.akwam.co/cat/156/%D8%A7%D9%84%D8%A3%D9%81%D9%84%D8%A7%D9%85-%D8%A7%D9%84%D8%A7%D8%AC%D9%86%D8%A8%D9%8A%D8%A9";
  //      searchAkwam(movies, true);
      //  searchOldAkoamLinks(movies2, true);
       // String series = "https://akwam.co/series";
        //searchAkwam(series, true);
       // Collections.shuffle(MainActivity.artistList);
       // listViewArtists.setAdapter(adapter);



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
                        if (isSeriesLinkAkwam(artist) || isSeriesLinkShahid(artist)){
                            Intent seriesLinkIntent = new Intent(MainActivity.this, LinkSeriesActivity.class);
                            seriesLinkIntent.putExtra("ARTIST_URL", artist.getUrl());
                            seriesLinkIntent.putExtra("ARTIST_NAME", artist.getName());
                            seriesLinkIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                            seriesLinkIntent.putExtra("ARTIST_SERVER", artist.getServer());
                            seriesLinkIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());
                            //start the activity
                            startActivity(seriesLinkIntent);

                            //fetchSeriesLinkAkwam(artist);
                        }else {
                            Intent serverListIntent = new Intent(MainActivity.this, ServersListActivity.class);
                            serverListIntent.putExtra("ARTIST_URL", artist.getUrl());
                            serverListIntent.putExtra("ARTIST_NAME", artist.getName());
                            serverListIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                            serverListIntent.putExtra("ARTIST_SERVER", artist.getServer());
                            serverListIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());
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
         //    String movies = "https://akwam.co/movies";
          //   searchAkwam(movies, true);
        //    String series = "https://akwam.co/series";
          //  searchAkwam(series, true);
           // Collections.shuffle(MainActivity.artistList);
           // listViewArtists.setAdapter(adapter);
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

        /*
        //artistList.clear();
        if (artistList.isEmpty()){

          /*  String movies = "https://old.akwam.co/download/3c472024a3f1/High-Seas-S02-Ep01-720p-WEB-DL-akoam-net-mkv";
            Artist artist = new Artist();
            artist.setUrl(movies);
            generateOldAkwamLink(artist);

           */
      //  }



        //artist.setServer(Artist.SERVER_AKWAM);
        //akwamController.fetchLink(artist, MainActivity.artistList);
        //adapter.notifyDataSetChanged();
        //adapter.setArtistList(MainActivity.artistList);
        //getLinks(movies, true);
/*
        if (!artistList.isEmpty()){
            //Collections.reverse(artistList);
            listViewArtists.setAdapter(adapter);
        }
/*
        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistList.clear();
                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {
                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artistList.add(artist);
                }
                Collections.reverse(artistList);
                ArtistList adapter = new ArtistList(MainActivity.this, artistList);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

 */
    }

    /**
     * search artikle in akwam
     */
    private void search() {
        String name = editTextName.getText().toString();

        if (!TextUtils.isEmpty(name)){
           // MainActivity.artistList.clear();
           // searchAkwam(name, false);
        }
        Toast.makeText(this, " Searching for " + name , Toast.LENGTH_LONG).show();
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
                                Log.i("link found", link.attr("href")+"");
                                a.setName(link.getElementsByAttribute("src").attr("alt"));
                                a.setUrl(link.attr("href"));
                                a.setImage(link.getElementsByAttribute("src").attr("data-src"));

                                MainActivity.artistList.add(a);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("akwam list size O", MainActivity.artistList.size() + "s");
                                        adapter.notifyDataSetChanged();
                                        // Collections.shuffle(MainActivity.artistList);
                                       // listViewArtists.setAdapter(adapter);
                                    }});
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("akwam list size O", MainActivity.artistList.size() + "s");
                            if (!isSeries){
                               // adapter.notifyDataSetChanged();
                                getShahid4uLinks(oldAkwamQuery, false);
                            }
                           // Collections.shuffle(MainActivity.artistList);
                            listViewArtists.setAdapter(adapter);
                        }});
                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i(TAG_AKWAM, "error"+e.getMessage());
                }

            }
        }).start();
        Log.i(TAG_AKWAM, "Search End");
    }

    public void fetchLinkVideoAkwam(Artist artist) {
        Log.i(TAG_AKWAM, "FetchVedio Start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_AKWAM, "FetchLinkVedio url:"+url);

                    Document doc = Jsoup.connect(url).get();

                    Elements links = doc.select("a");

                    String linkCaption = "akwam.co";
                    for (Element link : links) {
                        if (link.attr("href").contains(linkCaption)) {
                            //  databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                            //artist.setUrl(link.attr("href"));
                            Log.i("akwam url3", link.attr("href"));
                            url= link.attr("href");
                            break;
                        }
                    }

                     doc = Jsoup.connect(url).get();
                    Elements divs = doc.getElementsByClass("btn-loader");

                    String videoCaption = "akwam.download";
                    for (Element div : divs) {
                        Elements links2 =div.getElementsByAttribute("href");
                        for (Element link:links2){
                            if (link.attr("href").contains(videoCaption)) {
                                //  databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                                //artist.setUrl(link.attr("href"));
                                Log.i("akwam url3", link.attr("href"));
                                url= link.attr("href");
                               // artist.setUrl(url);
                            }
                        }
                    }
                    Log.i(TAG_AKWAM, "FetchOneLink url3:"+url);

                    String type = "video/*"; // It works for all video application
                    Uri uri = Uri.parse(url);
                    Intent in1 = new Intent(Intent.ACTION_VIEW, uri);
                    in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  in1.setPackage("org.videolan.vlc");
                    in1.setDataAndType(uri, type);
                    startActivity(in1);

                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i(TAG_AKWAM, "FetchVideoLink"+e.getMessage()+"");
                }
            }
        }).start();
        Log.i(TAG_AKWAM, "FetchVedio End");
    }

    /**
     * check if link is series or a movie link
     * @return true if the link is series link
     */
    private boolean isSeriesLinkAkwam(Artist artist){
        Log.i(TAG_AKWAM, "IsSerise Start");

        Log.i(TAG_AKWAM, "IsSerise End URL ="+artist.getUrl());
        return artist.getUrl().contains("akwam.co/series") || artist.getUrl().contains("akwam.co/movies") ;
    }

    /**
     * fetch resolution links from a film link
     * @param artist
     */
    private void fetchOneLinkAkwam(Artist artist){
       // MainActivity.resolutionsList.clear();
        MainActivity.artistList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_AKWAM, "FetchOneLink start url:"+url);

                    // page2 fetch goo- links
                    String p2Caption = "/link/";
                    Document doc = Jsoup.connect(url).get();
                    //TODO: find better way to fetch links
                    Elements divs = doc.getElementsByClass("tab-content quality");
                    for (Element div : divs) {
                        Artist a = new Artist(artist.getId(), "", artist.getGenre(),"",artist.getImage(),artist.getRate(),artist.getServer(),true );
                        Elements links = div.getElementsByAttribute("href");
                        for (Element link: links){
                            if (link.attr("href").contains(p2Caption)) {
                                a.setUrl(link.attr("href") );
                                a.setName(link.text());
                            }
                        }
                       // MainActivity.resolutionsList.add(a);
                        MainActivity.artistList.add(a);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                                adapter.notifyDataSetChanged();
                           // adapterResolution.notifyDataSetChanged();
                            //    listViewArtists.setAdapter(adapterResolution);

                        }});
                } catch (IOException e) {
                    Log.i(TAG_AKWAM, "error:"+e.getMessage());
                }
            }
        }).start();
        Log.i(TAG_AKWAM, "FetchOneLink end");
    }

    /**
     * Fetch episode links from a series link
     * @param artist
     */
    private void fetchSeriesLinkAkwam(Artist artist){
        final String url = artist.getUrl();
        //MainActivity.resultList.clear();
        MainActivity.artistList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG_AKWAM, "FetchSeriesLink url:"+url);

                    Document doc = Jsoup.connect(url).get();

                    Elements links = doc.select("a");
                    for (Element link : links) {
                        // TODO: find better way to get the link
                        if (
                                link.attr("href").contains("akwam.co/episode") &&
                                        link.getElementsByAttribute("src").hasAttr("alt")
                        ){
                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_AKWAM);
                            Log.i(TAG_AKWAM, "linkFound:"+link.attr("href")+"");

                            a.setName(link.getElementsByAttribute("src").attr("alt"));
                            a.setUrl(link.attr("href"));
                            a.setImage(link.getElementsByAttribute("src").attr("src"));
                            //MainActivity.resultList.add(a);
                            MainActivity.artistList.add(a);
                        }
                    }
                       runOnUiThread(new Runnable() {
                         @Override
                       public void run() {
                    // ArtistList adapter = new ArtistList(MainActivity.this, linksList);
                    //  listViewArtists.setAdapter(adapter);
                    // MainActivity.artistList.add(a);
                    //Log.i("artis O", a.getUrl());
                    //Collections.reverse(MainActivity.resultList);
                    Collections.reverse(MainActivity.artistList);
                     adapter.notifyDataSetChanged();
                         //   adapterResult.notifyDataSetChanged();
                   // listViewArtists.setAdapter(adapterResult);
                       }});
                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i(TAG_AKWAM, e.getMessage()+"");
                }
            }
        }).start();
    }

    //////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    ///////////////   Old.Akwam.co   //////////////////////////////////


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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Collections.reverse(MainActivity.artistList);
                            listViewArtists.setAdapter(adapter);
                        }
                    });
                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage()+"");
                }
            }
        }).start();
    }

    public String getOldAkwamVideoLink(String url){
        Log.i("akwam video", url);
        WebViewClient webViewClient2 = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // view.loadUrl(url);
                //MainActivity.oldAkwamVideoLink = url;
                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);
                view.loadUrl("");
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WEBCLIENT 22", "onPageFinished");
                //ok  sh       view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                view.loadUrl("javascript:location.replace(document.getElementsByClassName(\"download_button\")[0].getAttribute(\"href\").toString());");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                //ok sh        view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+MainActivity.serverCounts+"].click()");
                view.loadUrl("javascript:window.document.getElementsByClassName(\"download_button\")[0].click()");
                Log.d("WEBCLIENT 22","onLoadResource");
            }
        };
        //fetch video link from last page
        simpleWebView.setWebViewClient(webViewClient2);
       // String url2 = "https://old.akwam.co/download/3c472024a3f1/High-Seas-S02-Ep01-720p-WEB-DL-akoam-net-mkv";
        simpleWebView.loadUrl(url);
        return MainActivity.oldAkwamVideoLink;
    }

    //////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    ///////////////   shahid4u.one    //////////////////////////////////
    private boolean isSeriesLinkShahid(Artist artist){
        Log.i(TAG_AKWAM, "IsSerise Start");

        Log.i(TAG_AKWAM, "IsSerise End URL ="+artist.getUrl());
        return artist.getUrl().contains("shahid4u.one/series") || artist.getUrl().contains("shahid4u.one/season") ;
    }

    private void generateShahid4uSeriesLink(final Artist artist){
        Log.i("akwam series links", "start");
        final String url = artist.getUrl();
        MainActivity.artistList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("old akwam getLinks", "try before connect");

                    Log.i("old akwam links url", url+"s");
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    Log.i("old akwam getLinks", "try after connect");

                    Elements divs = doc.select("div[class]");
                    for (Element div : divs) {
                        Log.i("old akwam div", "found div no class");
                        if (div.hasClass("content-box"))
                        {
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // ArtistList adapter = new ArtistList(MainActivity.this, linksList);
                            //  listViewArtists.setAdapter(adapter);
                            // MainActivity.artistList.add(a);
                            //Log.i("artis O", a.getUrl());
                            Log.i("akwam list size O", MainActivity.artistList.size() + "s");
                            Collections.reverse(MainActivity.artistList);
                            adapter.notifyDataSetChanged();
                            //listViewArtists.setAdapter(adapter);
                        }});
                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage()+"");
                }

            }
        }).start();
        Log.i("akwam getLinks", "end");
    }



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

    //shahid


    //////////////////////////////////////////////////////////////
    /*
    private boolean isGooLink(String url){
        //TODO: check if movie or series
        Log.i("isSeries", url+"");
        return url.contains("goo-");
    }



    private void generateOldAkwamLink(final Artist artist) {
        Log.i("akwam fetchAkwamPageOne", "start");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                String url = artist.getUrl();
                Log.i("akwam fetchl1", url);


                        String url2 = "http://old.goo-2o.com/5dd7c58d8da14";

                            Document doc = Jsoup.connect(url).timeout(7000).get();

                            Elements links = doc.getElementsByClass("download_button");


                            for (Element link: links){
                                Log.i("result html", link.html() +"");
                                Log.i("result text", link.text() +"");

                            }
                            Log.i("sorry", "nothing found");

/*
                    Document doc = Jsoup.connect(url).timeout(6000).get();
                Document doc2 = Jsoup.parse(doc.toString());

                Log.i("respnse eee", doc2.text()+"");
              //  Elements divs = doc.select("div.link-view.container.container-pod.shorten-container.ng-scope");
                Elements divs = doc.select("div");

               //Element p = doc.createElement("p").attr("value", "get_link(5dd7c58d8da14)").attr("id","sosa");
                   // Log.i("P element", p.html()+"");

                //    Elements p3 = doc.getElementsByTag("a");
                   // Log.i("P sosa", p3.html()+"");
                  //  Elements p2 = doc.getElementsByAttribute("ng-if");
                for (Element link : divs) {
                    Log.i("div text", link.html()+"");
                    //  link.attr("style", "visibility: visible");
                    //Log.i("div after", link.text()+"");
                }




/*

                    for (Element link : divs) {
                        Log.i("div before", link.html()+"");
                      //  link.attr("style", "visibility: visible");
                        Log.i("div after", link.text()+"");
                    }

*/
                    /*
                    // page3 links fetch akwam link from goo- page
                    String p3Caption = "akwam.co";
                    //String p3Caption = "Click Here";
                    for (Element link : links2) {
                        Log.i("fetch found ", link.attr("href")+"");
                  //      Log.i("fetch found ", link.data()+"");
                    //    Log.i("fetch found ", link.html()+"");
                        Log.i("fetch found ", link.text()+"");

                        if (link.hasAttr("ng-href")) {
                            //  databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                            //artist.setUrl(link.attr("href"));
                            Log.i("akwam url3", "yessssssssssss");
                            Log.i("inner data", link.absUrl("ng-href")+"");
                            Log.i("inner html", link.html(link.html())+"");
                            Log.i("akwam size", link.children().size()+"");
                           // url = link.attr("href");
                            break;
                        }
                    }
*/
                    /*
                    Log.i("akwam fetchl3", url+"");
                    doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    //page 3 links download page
                    Elements links3 = doc.select("a[href]");
                    for (Element link : links3) {
                        if (link.hasClass("download_button")) {
                            //TODO: later not to save the video link permanently
                            // databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                            //artist.setUrl(link.attr("href"));
                            url = link.attr("href");
                            break;
                        }
                    }
                    // runOnUiThread(new Runnable() {
                    //   @Override
                    // public void run() {
                    // textViewResponse.setText(builder.toString());
                    Log.i("akwam fetchl4", url+"");
                    String type = "video/*"; // It works for all video application
                    Uri uri = Uri.parse(url);
                    Intent in1 = new Intent(Intent.ACTION_VIEW, uri);
                    in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  in1.setPackage("org.videolan.vlc");
                    in1.setDataAndType(uri, type);
                    Log.i("video started", uri.toString()+"");
                    startActivity(in1);
                    Log.i("akwam method", "artikel in method");
                    Log.i("akwam Url", artist.getUrl()+"");
                    //      }
                    //    });


               } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage()+"");
                }


            }
        }).start();

    }
/*
    private void generatAkwamLinkRessulotions(Artist artist) {
        MainActivity.artistList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String p2Caption = "/link/";

                    String url = artist.getUrl();
                    Document doc = Jsoup.connect(url).get();
                    Elements divs = doc.select("div");
                    for (Element link : divs) {
                        //  builder.append("\n").append("Link : ").append(link.attr("href"))
                        // builder.append("\n").append("Link : ")
                        //       .append("\n").append("Text : ").append(link.text());
                        if (link.hasClass("tab-content quality")) {
                            //databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                            //artist.setUrl(link.attr("href"));
                            Elements as = link.getElementsByClass("link-btn link-download");
                            Artist artist2 = new Artist(artist.getId(), artist.getName(), artist.getGenre(), "", artist.getImage(), artist.getRate(), artist.getServer(), false);
                            for (Element a : as) {
                                artist2.setUrl(a.attr("href"));
                                artist2.setIsVideo(true);
                                break;
                            }

                            MainActivity.artistList.add(artist2);
                            break;
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // ArtistList adapter = new ArtistList(MainActivity.this, linksList);
                            //  listViewArtists.setAdapter(adapter);
                            // MainActivity.artistList.add(a);
                            //Log.i("artis O", a.getUrl());
                            Log.i("akwam list size O", MainActivity.artistList.size() + "s");

                            adapter.notifyDataSetChanged();


                            listViewArtists.setAdapter(adapter);
                        }
                    });

                } catch (IOException e) {
                Log.i("akwam Error", e.getMessage().toString());

            }
            }
        }).start();
    }

*/

}