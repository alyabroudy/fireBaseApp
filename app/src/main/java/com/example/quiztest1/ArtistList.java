package com.example.quiztest1;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistList extends ArrayAdapter<Artist> {

    private Activity context;
    private List<Artist> artistList;

    public ArtistList(Activity context, List<Artist> artistList){
        super(context, R.layout.list_layout, artistList);
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewUrl = (TextView) listViewItem.findViewById(R.id.textViewUrl);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);
        TextView textViewRate = (TextView) listViewItem.findViewById(R.id.textViewRate);
        ImageView imageViewImage = (ImageView) listViewItem.findViewById(R.id.imageView);
       // TextView textViewRate = (TextView) listViewItem.findViewById(R.id.textView_rate);

        Artist artist = artistList.get(position);

        textViewName.setText(artist.getName());
        textViewUrl.setText(artist.getServer());
       // textViewGenre.setText(artist.getGenre());
        if (artist.getImage() != null && !artist.getImage().equals("")){
            Picasso.get().load(artist.getImage()).into(imageViewImage);
            Log.i("image url", artist.getImage());
        }
        if (artist.getRate() != null){
            textViewRate.setText(artist.getRate());
        }
        if (artist.getGenre() != null){
                 textViewGenre.setText(artist.getGenre());
             }

        return listViewItem;
    }

    public void setArtistList(List<Artist> list){
        this.artistList = list;
    }
    public List<Artist> getArtistList(){
        return this.artistList;
    }
}
