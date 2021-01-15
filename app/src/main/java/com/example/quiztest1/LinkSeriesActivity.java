package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LinkSeriesActivity extends AppCompatActivity {


    ListView listViewArtists;
    Artist artist;
    String TAG_AKWAM = "Akwam";
    String TAG = "LinkSeries";
    String TAG_CIMA4U = "Cima4u";
    String TAG_AFLAM_PRO = "AflamPro";
    String TAG_FASELHD = "FaselHd";
    String TAG_MYCIMA = "MyCima";
    ArtistList adapterSeries;
    static List<Artist> seriesArtistList;
   // ImageView imageView;
    TextView textViewDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_series);

        artist = new Artist();
        artist.setUrl(getIntent().getStringExtra("ARTIST_URL"));
        artist.setName(getIntent().getStringExtra("ARTIST_NAME"));
        artist.setImage(getIntent().getStringExtra("ARTIST_IMAGE"));
        artist.setServer(getIntent().getStringExtra("ARTIST_SERVER"));
        artist.setIsVideo(getIntent().getExtras().getBoolean("ARTIST_IS_VIDEO"));

        textViewDesc = (TextView)  findViewById(R.id.textViewDesc);
        textViewDesc.setMovementMethod(new ScrollingMovementMethod());

        seriesArtistList = new ArrayList<>();

        adapterSeries = new ArtistList(LinkSeriesActivity.this, seriesArtistList);

        listViewArtists = (ListView) findViewById(R.id.listViewArtist);

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = seriesArtistList.get(position);
                        Intent serversListIntent = new Intent(LinkSeriesActivity.this, ServersListActivity.class);
                        serversListIntent.putExtra("ARTIST_URL", artist.getUrl());
                        serversListIntent.putExtra("ARTIST_NAME", artist.getName());
                        serversListIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                        serversListIntent.putExtra("ARTIST_SERVER", artist.getServer());
                        serversListIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());
                        //fetchSeriesLinkAkwam(artist);

                        //fetchOneLinkAkwam(artist);
                //start the activity
                startActivity(serversListIntent);
            }
        });

     /*   if (artist.getServer().equals(Artist.SERVER_AKWAM)){
            fetchSeriesLinkAkwam(artist);
        }else if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){
            fetchSeriesLinkShahid4u(artist);
        }

      */
    }

    @Override
    protected void onStart() {
        super.onStart();
        start();
    }

    public void start(){
        if (null == LinkSeriesActivity.seriesArtistList || LinkSeriesActivity.seriesArtistList.isEmpty()){
            if (artist.getServer().equals(Artist.SERVER_AKWAM)){
                Log.i(TAG, "start akwam series");
                fetchSeriesLinkAkwam(artist);
            }else if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){
                Log.i(TAG, "start shahid series");
                fetchSeriesLinkShahid4u(artist);
            }else if (artist.getServer().equals(Artist.SERVER_CIMA4U)){
                Log.i(TAG, "start cima series");
                fetchSeriesLinkCima4u(artist);
            }else if (artist.getServer().equals(Artist.SERVER_AFLAM_PRO)){
                Log.i(TAG, "start aflamPro series");
                fetchSeriesLinkAflamPro(artist);
            }
            else if (artist.getServer().equals(Artist.SERVER_FASELHD)){
                Log.i(TAG, "start FaselHd series");
                fetchSeriesLinkFaselHd(artist);
            }
            else if (artist.getServer().equals(Artist.SERVER_MyCima)){
                Log.i(TAG, "start MyCima series");
                fetchSeriesLinkMyCima(artist);
            }
        }
    }

    //akwam
    /**
     * Fetch episode links from a series link
     * @param artist
     */
    private void fetchSeriesLinkAkwam(Artist artist){
        final String url = artist.getUrl();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG_AKWAM, "FetchSeriesLink url:"+url);

                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    //description
                    Elements decDivs = doc.select("h2");


                    for (Element div: decDivs){
                        final String description= div.getElementsByTag("p").html();
                        Log.i("description", "found:"+description);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewDesc.setText(description);
                            }
                        });
                        if (null != description && !description.equals("")){break;}
                    }



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

                            LinkSeriesActivity.seriesArtistList.add(a);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Collections.reverse(LinkSeriesActivity.seriesArtistList);
                            adapterSeries.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterSeries);
                        }});
                } catch (IOException e) {
                    Log.i(TAG_AKWAM, e.getMessage()+"");
                }
            }
        }).start();
    }


    //shahid
    private void fetchSeriesLinkShahid4u(final Artist artist){
        Log.i("akwam series links", "start");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    if (url.contains("/series/")){
                        url = url.replace("/series/", "/season/");
                    }

                    if (url.contains("/season/")){
                        url = url.concat("/episodes");
                    }

                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    //Elements links = doc.select("a[href]");

                    Elements divs = doc.select("div[class]");
                    for (Element div : divs) {

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

                            LinkSeriesActivity.seriesArtistList.add(a);

                            // }});

                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Collections.reverse(LinkSeriesActivity.seriesArtistList);
                            adapterSeries.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterSeries);
                            //listViewArtists.setAdapter(adapter);
                        }});
                } catch (IOException e) {
                    Log.i("fail", e.getMessage()+"");
                }

            }
        }).start();
        Log.i("akwam getLinks", "end");
    }


    //cima4u
    //cima4u
    private void fetchSeriesLinkCima4u(final Artist artist){
        Log.i(TAG_CIMA4U, "fetchSeries url:"+artist.getUrl());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_CIMA4U, "ur:"+url);
                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    //Elements links = doc.select("a[href]");

                    //get link of episodes page
                    Elements divs = doc.select("div[class]");
                    for (Element div : divs) {
                        if (div.hasClass("SingleContentSide"))
                        {
                            Log.i(TAG_CIMA4U, "elemet fount a");
                            boolean found=false;
                            Elements links = div.getElementsByAttribute("href");
                            for (Element link : links) {
                                Log.i(TAG_CIMA4U, "elemet a:"+link.attr("href"));
                                if (link.attr("href").contains("cima4u.io")){
                                    url = link.attr("href");
                                    found=true;
                                    break;
                                }

                            }
                            if (found){break;}
                        }
                        // LinkSeriesActivity.seriesArtistList.add(a);
                    }
                     doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    Log.i(TAG_CIMA4U, "Second url:"+url);

                    Elements lis = doc.select("li");

                    for (Element li : lis) {

                        if (li.hasClass("EpisodeItem"))
                        {
                            String episodeUrl = li.getElementsByAttribute("href").attr("href");
                            String episodeName = li.getElementsByAttribute("href").html().replaceAll("\\<.*?\\>", "");
                            Artist a = new Artist();
                            a.setName(episodeName);
                            a.setUrl(episodeUrl);
                            Log.i("Episode", episodeUrl);
                            Log.i("Episode n", episodeName);
                            a.setServer(Artist.SERVER_CIMA4U);
                            a.setImage(artist.getImage());
                            LinkSeriesActivity.seriesArtistList.add(a);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Collections.reverse(LinkSeriesActivity.seriesArtistList);
                            adapterSeries.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterSeries);
                            //listViewArtists.setAdapter(adapter);
                        }});
                } catch (IOException e) {
                    Log.i("fail", e.getMessage()+"");
                }

            }
        }).start();
        Log.i("akwam getLinks", "end");
    }

    //AflamPro
    private void fetchSeriesLinkAflamPro(final Artist artist){
        Log.i(TAG_AFLAM_PRO, "fetchSeries url:"+artist.getUrl());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_AFLAM_PRO, "ur:"+url);
                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    //Elements links = doc.select("a[href]");


                    //fetch description
                    doc.getElementsByClass("Description").select("h4").remove();
                    doc.getElementsByClass("Description").select("h4").remove();

                   String   description = doc.getElementsByClass("Description").html().replaceAll("\\<.*?\\>", "");



                    //fetch session
                    Elements boxs = doc.getElementsByClass("Wdgt AABox");
                    Log.i("Count", "boxs:"+boxs.size());
                    int sessionCounter= 1;
                    for (Element box : boxs) {
                        String title = "الموسم " + sessionCounter;
                       // String title = box.getElementsByClass("Title AA-Season On").html();
                        //get link of episodes page
                        Elements divs = box.getElementsByTag("tr");
                        for (Element div : divs) {

                            String link = div.getElementsByAttribute("href").attr("href");
                            Log.i(TAG_AFLAM_PRO, "Episode url found:"+link);

                            String image = div.getElementsByAttribute("src").attr("src");

                            if (image.equals("")){
                                String   image2 = div.getElementsByClass("cnv cnv2").html();
                                Log.i(TAG_AFLAM_PRO, "Episode image2 found:"+image2);
                                image2 = image2.substring(image2.indexOf("https"));
                                image = image2.substring(0, image2.indexOf('"'));

                            }
                            Log.i(TAG_AFLAM_PRO, "Episode image found:"+image);

                            String name = artist.getName();
                            Elements namesDivs = div.getElementsByClass("MvTbTtl");
                            for (Element nameDiv : namesDivs) {
                                name = nameDiv.getElementsByAttribute("href").text() + " - "+ title;
                                break;
                            }
                            Log.i(TAG_AFLAM_PRO, "Episode name found:"+name);
                            Artist a = new Artist();
                            a.setName(name);
                            a.setUrl(link);
                            a.setServer(Artist.SERVER_AFLAM_PRO);
                            a.setImage(image);
                            LinkSeriesActivity.seriesArtistList.add(a);
                        }
                        sessionCounter++;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Collections.reverse(LinkSeriesActivity.seriesArtistList);
                            textViewDesc.setText(description);
                            adapterSeries.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterSeries);
                            //listViewArtists.setAdapter(adapter);
                        }});
                } catch (IOException e) {
                    Log.i("fail", e.getMessage()+"");
                }

            }
        }).start();
        Log.i(TAG_AFLAM_PRO, "FetchSeriesLink end");
    }

    //FaselHd
    private void fetchSeriesLinkFaselHd(final Artist artist){
        Log.i(TAG_FASELHD, "fetchSeries url:"+artist.getUrl());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_FASELHD, "ur:"+url);
                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    //Elements links = doc.select("a[href]");


                    //fetch description

                   String   description = doc.getElementsByClass("singleDesc").html().replaceAll("\\<.*?\\>", "");



                    //fetch session
                    Elements boxs = doc.getElementsByClass("seasonDiv active");
                    Log.i("Count", "boxs:"+boxs.size());
                    for (Element box : boxs) {
                        String title = box.getElementsByClass("title").text();
                        String image = box.getElementsByAttribute("data-src").attr("data-src");
                        // String title = box.getElementsByClass("Title AA-Season On").html();
                        //get link of episodes page
                        Elements divs = doc.getElementById("epAll").getElementsByAttribute("href");
                        for (Element div : divs) {

                            String link = div.attr("href");
                            Log.i(TAG_FASELHD, "Episode url found:"+link);


                            String name = div.text();
                            Log.i(TAG_FASELHD, "Episode name found:"+name);
                            Artist a = new Artist();
                            a.setName(name);
                            a.setUrl(link);
                            a.setServer(Artist.SERVER_FASELHD);
                            a.setImage(image);
                            LinkSeriesActivity.seriesArtistList.add(a);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Collections.reverse(LinkSeriesActivity.seriesArtistList);
                            textViewDesc.setText(description);
                            adapterSeries.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterSeries);
                            //listViewArtists.setAdapter(adapter);
                        }});
                } catch (IOException e) {
                    Log.i("fail", e.getMessage()+"");
                }

            }
        }).start();
        Log.i(TAG_FASELHD, "FetchSeriesLink end");
    }

    //MyCima
    private void fetchSeriesLinkMyCima(final Artist artist){
        Log.i(TAG_MYCIMA, "fetchSeries url:"+artist.getUrl());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_MYCIMA, "ur:"+url);
                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    //Elements links = doc.select("a[href]");

                    //get link of episodes page
                    String desc = doc.getElementsByClass("PostItemContent").text();

                    //fetch session
                    Elements boxs = doc.getElementsByClass("Episodes--Seasons--Episodes");
                    for (Element box: boxs){

                        Elements lis = box.getElementsByTag("a");

                        Log.i("Count", "boxs:"+boxs.size());
                        for (Element li : lis) {
                            String title = li.text();
                            String link = li.attr("href");

                            Artist a = new Artist();
                            a.setName(title);
                            a.setUrl(link);
                            a.setServer(Artist.SERVER_MyCima);
                            a.setImage(artist.getImage());
                            LinkSeriesActivity.seriesArtistList.add(a);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Collections.reverse(LinkSeriesActivity.seriesArtistList);
                            textViewDesc.setText(desc);
                            adapterSeries.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterSeries);
                            //listViewArtists.setAdapter(adapter);
                        }});
                } catch (IOException e) {
                    Log.i("fail", e.getMessage()+"");
                }

            }
        }).start();
        Log.i(TAG_FASELHD, "FetchSeriesLink end");
    }

}