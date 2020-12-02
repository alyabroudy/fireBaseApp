package com.example.quiztest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    EditText editTextName;
    Button buttonSearch;
    ListView listViewArtists;
    TextView textViewResponse;
    ImageView imageView;
    ArtistList adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //databaseArtist = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        listViewArtists = (ListView) findViewById(R.id.listViewArtist);
        textViewResponse = (TextView) findViewById(R.id.textView_response);
        imageView = (ImageView) findViewById(R.id.imageView);

        //initialize artist list
        artistList = new ArrayList<>();
        adapter = new ArtistList(MainActivity.this, artistList);

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistList.get(position);

                if (isSeriesLink(artist.getUrl()))
                {
                    //generateAkwamSeriesLink(artist);
                    Log.i("Click", "is Series Link");
                   generateAkwamSeriesLink(artist);
                }else {
                    Log.i("Click", "not Series Link");
                    generateAkwamLink(artist);
                }
            }
        });
        //click listener for the button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //artistList.clear();
        String movies = "https://akwam.co/movies";
        getLinks(movies, true);
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

    private boolean isSeriesLink(String url){
        //TODO: check if movie or series
        Log.i("isSeries", url+"");
        return url.contains("akwam.co/series");
    }

    private void generateAkwamSeriesLink(final Artist artist){
        Log.i("akwam series links", "start");
        final String url = artist.getUrl();
        MainActivity.artistList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Artist> linksList= new ArrayList<>();
                    Log.i("akwam series", "try before connect");
                    Log.i("akwam series url", url+"s");
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    Log.i("akwam series", "try after connect");

                    Elements links = doc.select("a");
                    for (Element link : links) {

                            if (
                                   link.attr("href").contains("akwam.co/episode") &&
                                           link.getElementsByAttribute("src").hasAttr("alt")
                            ){
                                Log.i("imageFound", link.attr("href") + "s");
                                Log.i("imageFound", link.getElementsByAttribute("src").attr("src") + "s");
                                Log.i("imageFound", link.getElementsByAttribute("src").attr("alt") + "s");

                                Artist a = new Artist("","","","","","");
                                Log.i("link found", link.attr("href")+"");
                                a.setName(link.getElementsByAttribute("src").attr("alt"));
                                a.setUrl(link.attr("href"));
                                a.setImage(link.getElementsByAttribute("src").attr("src"));

                                MainActivity.artistList.add(a);
                                Log.i("akwam list size In", MainActivity.artistList.size() + "s");
                                Log.i("artis in", a.getUrl());
                            }
                            // }});


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

    private void generateAkwamLink(final Artist artist) {
        Log.i("akwam fetchAkwamPageOne", "start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i("akwam fetchl1", url);
                    // page2 links
                    String p2Caption = "http://goo-2o.com/link/";
                    Document doc = Jsoup.connect(url).get();
                    Elements links = doc.select("a[href]");
                    for (Element link : links) {
                        //  builder.append("\n").append("Link : ").append(link.attr("href"))
                        // builder.append("\n").append("Link : ")
                        //       .append("\n").append("Text : ").append(link.text());
                        if (link.attr("href").contains(p2Caption)) {
                            //databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                            //artist.setUrl(link.attr("href"));
                            url = link.attr("href");
                            break;
                        }
                    }

                    Log.i("akwam fetchl2", url);
                    doc = Jsoup.connect(url).get();
                    Elements links2 = doc.select("a[href]");

                    // page3 links fetch akwam link from goo- page
                    String p3Caption = "akwam.co";
                    Log.i("akwam p3Links", "s" + links.size());
                    for (Element link : links2) {
                        if (link.attr("href").contains(p3Caption)) {
                            //  databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                            //artist.setUrl(link.attr("href"));
                            Log.i("akwam url3", link.attr("href"));
                            url = link.attr("href");
                            break;
                        }
                    }
                    Log.i("akwam fetchl3", url);
                    doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    //page 3 links download page
                    String videoCaption = "akwam.download";
                    Elements links3 = doc.select("a[href]");
                    for (Element link : links3) {
                        if (link.attr("href").contains(videoCaption)) {
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
                    Log.i("akwam fetchl4", url);
                            String type = "video/*"; // It works for all video application
                            Uri uri = Uri.parse(url);
                            Intent in1 = new Intent(Intent.ACTION_VIEW, uri);
                            in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //  in1.setPackage("org.videolan.vlc");
                            in1.setDataAndType(uri, type);
                            Log.i("video started", uri.toString());
                            startActivity(in1);
                            Log.i("akwam method", "artikel in method");
                            Log.i("akwam Url", artist.getUrl());
                  //      }
                //    });
                } catch (IOException e) {
                    Log.i("akwam Error", e.getMessage().toString());
                }
            }
            }).start();
        Log.i("akwam getLinks", "end");
        Log.i("akwam fetchAkwamPageOne", artist.getUrl());
        Log.i("akwam fetchAkwamPageOne", "end");
    }

    /**
     * search artikle in akwam
     */
    private void search() {
        String name = editTextName.getText().toString();

        if (!TextUtils.isEmpty(name)){
            MainActivity.artistList.clear();
            getLinks(name, false);
        }
            Toast.makeText(this, " Searching for " + name , Toast.LENGTH_LONG).show();
    }

    private List<Artist> getLinks(String query, final boolean isSeries) {
        Log.i("akwam getLinks", "start");
        final String oldAkwamQuery = query;

        if (!isSeries){
            query = "https://akwam.co/search?q=" + query + "&section=0&year=0&rating=0&formats=0&quality=0";
        }
        final String url = query;
       final List<Artist> linksList = new ArrayList<>();
        new Thread(new Runnable() {
        @Override
        public void run() {

        final StringBuilder builder = new StringBuilder();
        try {
            Log.i("akwam getLinks", "try before connect");
            Log.i("akwam links url", url+"s");
            Document doc = Jsoup.connect(url).get();
            //Elements links = doc.select("a[href]");
            Log.i("akwam getLinks", "try after connect");

           /* final Artist a = new Artist(
                    "1", "", "comedy",
                    "",
                    "", "01");
*/
            Elements links = doc.select("a");
            for (Element link : links) {
                if (link.hasClass("box"))
                {

                    Log.i("imageFound", link.attr("href") + "s");
                    Log.i("imageFound", link.getElementsByAttribute("src").attr("data-src") + "s");
                    Log.i("imageFound", link.getElementsByAttribute("src").attr("alt") + "s");


                    if (
                            link.attr("href").contains("akwam.co/movie") ||
                            link.attr("href").contains("akwam.co/series") ||
                            link.attr("href").contains("akwam.co/episode")
                    ){
                        Artist a = new Artist("","","","","","");
                        Log.i("link found", link.attr("href")+"");
                        a.setName(link.getElementsByAttribute("src").attr("alt"));
                        a.setUrl(link.attr("href"));
                        a.setImage(link.getElementsByAttribute("src").attr("data-src"));

                        MainActivity.artistList.add(a);
                        Log.i("akwam list size In", MainActivity.artistList.size() + "s");
                        Log.i("artis in", a.getUrl());
                    }
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
                  /*  if (!isSeries){
                        getOldAkoamLinks(oldAkwamQuery, false);
                        adapter.notifyDataSetChanged();
                    }*/

                    listViewArtists.setAdapter(adapter);
                }});
        } catch (IOException e) {
            //builder.append("Error : ").append(e.getMessage()).append("\n");
            Log.i("fail", e.getMessage()+"");
        }

}
        }).start();
        Log.i("akwam getLinks", "end");
        return linksList;
    }

    private void getOldAkoamLinks(String query, boolean isSeries) {
        Log.i("akwam getLinks", "start");

        if (!isSeries){
            query = "https://old.akwam.co/search/" + query;
        }
        final String url = query;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("old akwam getLinks", "try before connect");
                    Log.i("old akwam links url", url+"s");
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    Log.i("old akwam getLinks", "try after connect");

           /* final Artist a = new Artist(
                    "1", "", "comedy",
                    "",
                    "", "01");
*/
                    Elements divs = doc.select("div");
                    for (Element div : divs) {
                        Log.i("old akwam div", "found div no class");
                        if (div.hasClass("tags_box"))
                        {
                            Log.i("old akwam div", "found div with class");
                            Artist a = new Artist("","","","","","");

                                a.setName(div.getElementsByTag("h1").text());
                                Log.i("old name", div.getElementsByTag("h1").text().toString() + "");
                                a.setUrl(div.getElementsByTag("a").attr("href"));
                                Log.i("old link nn ", div.getElementsByTag("a").attr("href")+"");
                                Log.i("old image nn ", div.getElementsByTag("a").attr("style")+"");
                                Log.i("old link nn ", div.getElementsByTag("a").attr("href")+"");
                                Log.i("old link nn ", div.getElementsByTag("a").attr("href")+"");
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
                            Log.i("old akwam list size O", MainActivity.artistList.size() + "s");
                            //adapter.notifyDataSetChanged();
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
}