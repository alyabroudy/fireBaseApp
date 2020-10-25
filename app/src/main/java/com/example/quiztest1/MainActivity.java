package com.example.quiztest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseArtist;

    //list contains retrieved artists
    List<Artist> artistList;

    EditText editTextName;
    Button buttonAddArtist;
    Spinner spinnerGenres;
    ListView listViewArtists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtist = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAddArtist = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        listViewArtists = (ListView) findViewById(R.id.listViewArtist);

        //initialize artist list
        artistList = new ArrayList<>();

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

                ArtistList adapter = new ArtistList(MainActivity.this, artistList);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error:"+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * add Artist to database
     */
    private void addArtist(){
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenres.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name)){

            //to generate a unique id in the database
            String id = databaseArtist.push().getKey();

            //create an Artist
            Artist artist = new Artist(id, name, genre);

            databaseArtist.child(id).setValue(artist);
            Toast.makeText(this, " Artist "+name+" added!", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"You need to enter a name", Toast.LENGTH_LONG).show();
        }
    }

}