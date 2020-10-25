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

import java.io.IOException;
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

    String responseContent;

    EditText editTextName;
    EditText editTextUrl;
    Button buttonAddArtist;
    Spinner spinnerGenres;
    ListView listViewArtists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseContent = "";

        //reference for videoView
       /* VideoView videoView = (VideoView)  findViewById(R.id.video_view);
        //String videoPath = "android.resource://"+getPackageName() + "/" + R.raw.video;
        String videoPath = "https://s305d2.akwam.download/download/1603728998/5f95a4e6843ce/Ratched.S01E08.720p.WEBRip.akwam.net.mp4";
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();

        //media controller for the video
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

*/
        databaseArtist = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUrl = (EditText) findViewById(R.id.editTextUrl);
        buttonAddArtist = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        listViewArtists = (ListView) findViewById(R.id.listViewArtist);

        //initialize artist list
        artistList = new ArrayList<>();

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistList.get(position);
                String url = artist.getName();
/*
                if (artist.getGenre().equals("Akwam")){
                    Toast.makeText(MainActivity.this, "Akwam", Toast.LENGTH_LONG).show();
                    url = generateAkwamLink(url);
                    Toast.makeText(MainActivity.this, url, Toast.LENGTH_LONG).show();
                }

 */
                Uri uri = Uri.parse(url);
                Intent in1 = new Intent(Intent.ACTION_VIEW, uri);

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

    private String requestUrl(String url){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                      final String content = response.body().string();
                    Log.i("akwam method", "c c"+content);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseContent = content;
                        }
                    });

                }
            }
        });
        return responseContent;
    }


    private String generateAkwamLink(String url){
        Log.d("akwam method", "url first");
        String responseContent = requestUrl(url);

                    String needCaption = "http://goo-2o.com/link/";
                    String page2Url=null;
        Toast.makeText(MainActivity.this, needCaption, Toast.LENGTH_LONG).show();

                    //if link is not a direct download page
                    if (responseContent.contains(needCaption)){
                        Toast.makeText(MainActivity.this, "indirect link", Toast.LENGTH_LONG).show();
                        //find video link location
                        int page1Pos = responseContent.indexOf( needCaption );
                        //get goo page
                        String gooUrl = responseContent.substring(page1Pos ,28);
                        //make request too goo page to get the v link
                        responseContent = requestUrl(gooUrl);

                        needCaption = "https://akwam.co";

                        int page2Pos = responseContent.indexOf( needCaption );
                        //get the line that contains the video link

                        String page2ContentArea = responseContent.substring(page2Pos ,200);
                        //find the position if beginning of the link
                        int page2ContentStartPos= page2ContentArea.indexOf("ht" );
                        //get the rest of the link body
                        String page2ContentArea2= page2ContentArea.substring(page2ContentStartPos ,200);
                        //find the position of the end of the link
                        int page2ContentEndPos = page2ContentArea2.indexOf( "\"" );
                        //getting the complete link
                        page2Url= page2ContentArea2.substring( 0 ,page2ContentEndPos);
                    }

                    if (null != page2Url){
                       responseContent = requestUrl(page2Url);

                    }
        Log.i("akwam method", "url"+needCaption);
        Log.i("akwam method", "page2url"+page2Url);
        Toast.makeText(MainActivity.this, needCaption, Toast.LENGTH_LONG).show();
                    needCaption = "btn-loader";

                    //find video link location
                    int contentPos = responseContent.indexOf(needCaption);
        Log.i("akwam method", "content "+responseContent);
        Log.i("akwam method", "pos1"+contentPos);
                    //get the line that contains the video link
                    String linkContent1 = responseContent.substring(contentPos ,200);
        Log.i("akwam method", "content1"+linkContent1);
                    //find the position if beginning of the link
                    int contentPos2 = linkContent1.indexOf("ht");
                    //get the rest of the link body
                    String linkContent2= linkContent1.substring(contentPos2 ,200);
                    //find the position of the end of the link
                    int contentPos3 = linkContent2.indexOf("\"" );
                    //getting the complete link
                    String linkContent3= linkContent2.substring(0 ,contentPos3);
        Log.i("akwam method", "url3"+linkContent3);
                    return linkContent3;
                 ////////////
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
            Artist artist = new Artist(id, name, genre, url);

            databaseArtist.child(id).setValue(artist);
            Toast.makeText(this, " Artist "+name+" added!", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"You need to enter a name", Toast.LENGTH_LONG).show();
        }
    }

}