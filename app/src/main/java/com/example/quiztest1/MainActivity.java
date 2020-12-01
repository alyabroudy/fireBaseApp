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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseArtist;

    //list contains retrieved artists
    List<Artist> artistList;

    OkHttpClient client;

    List<String> responseContent;

    EditText editTextName;
    EditText editTextUrl;
    Button buttonAddArtist;
    Spinner spinnerGenres;
    ListView listViewArtists;
    TextView textViewResponse;
    ImageView imageView;
    TextView textViewRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseContent = new ArrayList<>();
        databaseArtist = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUrl = (EditText) findViewById(R.id.editTextUrl);
        buttonAddArtist = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        listViewArtists = (ListView) findViewById(R.id.listViewArtist);
        textViewResponse = (TextView) findViewById(R.id.textView_response);
        imageView = (ImageView) findViewById(R.id.imageView);
        textViewRate = (TextView) findViewById(R.id.textView_rate);

        //initialize artist list
        artistList = new ArrayList<>();

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistList.get(position);
               String url = artist.getUrl();
                String type = "video/*"; // It works for all video application
                Uri uri = Uri.parse(url);
                Intent in1 = new Intent(Intent.ACTION_VIEW, uri);
                in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              //  in1.setPackage("org.videolan.vlc");
                in1.setDataAndType(uri, type);
                Log.i("video started", uri.toString());
                startActivity(in1);
            }
        });

        //click listener for the button
        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistList.clear();
                for (DataSnapshot artistSnapshot: dataSnapshot.getChildren()){
                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artistList.add(artist);
                }
                Collections.reverse(artistList);
                ArtistList adapter = new ArtistList(MainActivity.this, artistList);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error:"+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void generateAkwamLink(Artist artist){

        Log.i("generate Method", "hello 1");
        //artist.setUrl("https://akwam.co/movie/2719/sky-sharks");
        //check which page is it
        String page1 = "akwam.co/movie";
        String page1Part2 = "akwam.co/episode";
        String page1Part3 = "akwam.co/show";

        String page2 = "goo-";
        String page3 = "akwam.co/download";

        if (artist.getUrl().contains(page1) || artist.getUrl().contains(page1Part2) || artist.getUrl().contains(page1Part3))
        {
            Log.i("generate Method", "hello page one");
            //get Second page url
            fetchAkwamPageOne(artist);
            Log.i("generate name", artist.getName());
        }else if(artist.getUrl().contains(page2) )
        {
            fetchAkwamPageTwo(artist);
            Log.i("generate Method", "hello page two");
        }else if(artist.getUrl().contains(page3) )
        {
            Log.i("generate Method", "hello fetch video link");
            fetchAkwamVideoLink(artist);
        }
    }

    private void fetchAkwamPageOne(final Artist artist){
        Log.i("akwam fetchAkwamPageOne","start");
        final String url = artist.getUrl();
        final StringBuilder builder = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    Elements names = doc.select("h1");
                    //get movie name
                    for (Element name : names) {
                      //  builder.append("\n").append("Link : ").append(link.attr("href"))
                       // builder.append("\n").append("Link : ")
                         //       .append("\n").append("Text : ").append(link.text());
                        builder.append(name.text());
                        break;
                    }

                    databaseArtist.child(artist.getId()).child("name").setValue(builder.toString());

                    //page1 image
                    String imageCaption = "https://img.akwam.co/thumb/260x380/";
                    Elements images = doc.select("img[src]");
                    for (Element image : images) {
                        //  builder.append("\n").append("Link : ").append(link.attr("href"))
                        // builder.append("\n").append("Link : ")
                        //       .append("\n").append("Text : ").append(link.text());
                        if (image.attr("src").contains(imageCaption))
                        {
                            databaseArtist.child(artist.getId()).child("image").setValue(image.attr("src"));
                            artist.setImage(image.attr("src").toString());
                            Log.i("image found", image.attr("src").toString());
                            break;
                        }
                    }

                    //page1 rate
                    String rateCaption = "mx-2";
                    Elements spans = doc.select("span");
                    for (Element span : spans) {
                        //  builder.append("\n").append("Link : ").append(link.attr("href"))
                        // builder.append("\n").append("Link : ")
                        //       .append("\n").append("Text : ").append(link.text());
                        if (span.hasClass(rateCaption))
                        {
                            databaseArtist.child(artist.getId()).child("rate").setValue(span.text());
                            artist.setRate(span.text());
                            Log.i("rate found", span.text());
                            break;
                        }
                    }


                    // page2 links
                    String p2Caption = "http://goo-2o.com/link/";
                    Elements links = doc.select("a[href]");
                    for (Element link : links) {
                        //  builder.append("\n").append("Link : ").append(link.attr("href"))
                        // builder.append("\n").append("Link : ")
                        //       .append("\n").append("Text : ").append(link.text());
                        if (link.attr("href").contains(p2Caption))
                        {
                            databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                            artist.setUrl(link.attr("href"));
                            fetchAkwamPageTwo(artist);
                            break;
                        }
                    }

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // textViewResponse.setText(builder.toString());
                        Log.i("akwam method","artikel in method");
                        Log.i("name",builder.toString());
                        Log.i("akwam Url",artist.getUrl());
                    }
                });

            }
        }).start();
        Log.i("akwam fetchAkwamPageOne",artist.getUrl());
        Log.i("akwam fetchAkwamPageOne","end");
    }

    private void fetchAkwamPageTwo(final Artist artist){
        Log.i("akwam fetchAkwamPageTwo","start");
        final String url = artist.getUrl();
        final StringBuilder builder = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");

                    // page3 links fetch akwam link from goo- page
                    String p3Caption = "akwam.co";
                    Elements links = doc.select("a[href]");
                    for (Element link : links) {
                        if (link.attr("href").contains(p3Caption))
                        {
                            databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                            artist.setUrl(link.attr("href"));
                            fetchAkwamVideoLink(artist);
                            break;
                        }
                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // textViewResponse.setText(builder.toString());
                        Log.i("akwam method","artikel in method");
                        Log.i("name",builder.toString());
                        Log.i("akwam Url",artist.getUrl());

                    }
                });

            }
        }).start();
        Log.i("akwam fetchAkwamPageTwo","end");
    }

    private void fetchAkwamVideoLink(final Artist artist){
        Log.i("akwam fetchAkwamVideoLink","start");
        final String url = artist.getUrl();
        final StringBuilder builder = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");
                    //page 3 links download page
                    String p3Caption = "akwam.download";
                    Elements links = doc.select("a[href]");
                    for (Element link : links) {
                        if (link.attr("href").contains(p3Caption))
                        {
                            //TODO: later not to save the video link permanently
                            databaseArtist.child(artist.getId()).child("url").setValue(link.attr("href"));
                            artist.setUrl(link.attr("href"));
                            break;
                        }
                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // textViewResponse.setText(builder.toString());
                        Log.i("akwam method","artikel in method");
                        Log.i("name",builder.toString());
                        Log.i("akwam Url",artist.getUrl());

                    }
                });

            }
        }).start();
        Log.i("akwam fetchAkwamVideoLink","end");
    }

    /**
     * add Artist to database
     */
    private void addArtist(){
        String name = editTextName.getText().toString();
        String url = editTextUrl.getText().toString();
        String genre = spinnerGenres.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name)){

            //to generate a unique id in the database
            String id = databaseArtist.push().getKey();

            //name = generateAkwamLink(name);
            //create an Artist
            Artist artist = new Artist(id, name, genre, url, "", "");

            databaseArtist.child(id).setValue(artist);
           // if (artist.getGenre().equals("Akwam")){
                generateAkwamLink(artist);
                Log.i("akwam method","artikel after method");
           // }
            Toast.makeText(this, " Artist "+name+" added!", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"You need to enter a name", Toast.LENGTH_LONG).show();
        }
    }

}