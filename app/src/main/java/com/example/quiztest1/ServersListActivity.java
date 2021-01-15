package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServersListActivity extends AppCompatActivity {


    ListView listViewArtists;
    Artist artist;
    String TAG_AKWAM = "Akwam";
    String TAG_SHAHID4U = "Shahid4u";
    String TAG_CIMA4U = "Cima4u";
    String TAG = "ServersList";
    ArtistList adapterServersList;
   // ImageView imageView;
    TextView textViewDesc;
    static List<Artist> serversArtistList;

    static int shahidServersIdCounter=5;
    static int shahidServersId=1;

    WebView simpleWebView;
    WebView simpleWebView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers_list);

      //  imageView = (ImageView)  findViewById(R.id.imageView);
        textViewDesc = (TextView)  findViewById(R.id.textViewDesc);
        textViewDesc.setMovementMethod(new ScrollingMovementMethod());

        artist = new Artist();
        artist.setUrl(getIntent().getStringExtra("ARTIST_URL"));
        artist.setName(getIntent().getStringExtra("ARTIST_NAME"));
        artist.setImage(getIntent().getStringExtra("ARTIST_IMAGE"));
        artist.setServer(getIntent().getStringExtra("ARTIST_SERVER"));
        artist.setIsVideo(getIntent().getExtras().getBoolean("ARTIST_IS_VIDEO"));

        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // view.loadUrl(url);
                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);
                if (!url.contains("openload") && !url.contains("estream") && url.length() > 7 && !url.contains("onmarshtompor")){
                    Artist a = new Artist();
                    a.setImage(artist.getImage());
                    a.setName(artist.getName());
                    a.setServer(artist.getServer());
                    a.setIsVideo(true);
                    a.setUrl(url);
                    a.setGenre(getWebName(url));
                    ServersListActivity.serversArtistList.add(a);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterServersList.notifyDataSetChanged();
                        }});
                }
                ServersListActivity.shahidServersIdCounter--;
                view.loadUrl(" ");

                //String url = url.trim().replace(" ", "");


                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WEBCLIENT", "onPageFinished");
                view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                //view.loadUrl("javascript:location.replace(document.getElementsByClassName(\"download_button\")[0].getAttribute(\"href\").toString());");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+ServersListActivity.shahidServersIdCounter+"].click()");
                //view.loadUrl("javascript:window.document.getElementsByClassName(\"download_button\")[0].click()");
                Log.d("WEBCLIENT","onLoadResource");
            }
        };



        // initiate a web view
        simpleWebView = (WebView) findViewById(R.id.webView);
        simpleWebView.getSettings().setJavaScriptEnabled(true);
     /*   simpleWebView.getSettings().setBlockNetworkImage(true);
        simpleWebView.getSettings().setAppCacheEnabled(false);
        simpleWebView.getSettings().setLoadsImagesAutomatically(false);
        simpleWebView.setVisibility(View.GONE);
        simpleWebView.getSettings().setDomStorageEnabled(true);
        simpleWebView.setWebViewClient(client);

      */

        simpleWebView2 = (WebView) findViewById(R.id.webView2);
        simpleWebView2.getSettings().setJavaScriptEnabled(true);
        simpleWebView2.getSettings().setBlockNetworkImage(true);
        simpleWebView2.getSettings().setAppCacheEnabled(false);
        simpleWebView2.getSettings().setLoadsImagesAutomatically(false);
        simpleWebView2.setVisibility(View.GONE);
        simpleWebView2.getSettings().setDomStorageEnabled(true);
        simpleWebView2.setWebViewClient(client);

        serversArtistList = new ArrayList<>();

        adapterServersList = new ArtistList(ServersListActivity.this, serversArtistList);

        listViewArtists = (ListView) findViewById(R.id.listViewArtist);

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = serversArtistList.get(position);

                if (artist.getServer().equals(Artist.SERVER_AKWAM)){
                    if (artist.getName().equals("playNow")){
                        String type = "video/mp4"; // It works for all video application
                        // }

                     //   String url = artist.getUrl().trim().replace(" ", "");
                        //  url = url.replace("/video.mp4", "");
                        Uri uri = Uri.parse(artist.getUrl() + "");
                        Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                        videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  in1.setPackage("org.videolan.vlc");
                        videoIntent.setDataAndType(uri, type);
                        Log.i("video started", uri.toString() + "");
                        startActivity(videoIntent);
                    }else{
                        fetchLinkVideoAkwam(artist);
                    }
                }
                else if (artist.getServer().equals(Artist.SERVER_CIMA4U) ) {

                    /*
                    Intent webViewIntent = new Intent(ServersListActivity.this, VideoResolutionActivity.class);
                    webViewIntent.putExtra("URL", artist.getUrl());
                    startActivity(webViewIntent);
                    */
                    //  String type = "video/*";
                    // if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){
                    String type = "text/html"; // It works for all video application
                    // }

                    String url = artist.getUrl().trim().replace(" ", "");
                    //  url = url.replace("/video.mp4", "");
                    Uri uri = Uri.parse(url + "");
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                    videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  in1.setPackage("org.videolan.vlc");
                    videoIntent.setDataAndType(uri, type);
                    Log.i("video started", uri.toString() + "");
                    startActivity(videoIntent);

                }else if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){
                    if (artist.getUrl().contains("vidhd") && artist.getIsVideo()){
                        //fetchVideoLindVidHd(artist);
                          String type = "video/*";
                        // if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){
                    //    String type = "text/html"; // It works for all video application
                        // }

                        String url = artist.getUrl().trim().replace(" ", "");
                        //  url = url.replace("/video.mp4", "");
                        Uri uri = Uri.parse(url + "");
                        Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                        videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  in1.setPackage("org.videolan.vlc");
                        videoIntent.setDataAndType(uri, type);
                        Log.i("video started", uri.toString() + "");
                        startActivity(videoIntent);
                    }
                    else{
                        String type = "text/html"; // It works for all video application
                        // }

                        String url = artist.getUrl().trim().replace(" ", "");
                        //  url = url.replace("/video.mp4", "");
                        Uri uri = Uri.parse(url + "");
                        Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                        videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  in1.setPackage("org.videolan.vlc");
                        videoIntent.setDataAndType(uri, type);
                        Log.i("video started", uri.toString() + "");
                        startActivity(videoIntent);
                    }
                }else if (artist.getServer().equals(Artist.SERVER_AFLAM_PRO)){
                    String type = "video/*"; // It works for all video application
                    // }

                    String url = artist.getUrl().trim().replace(" ", "");
                    //  url = url.replace("/video.mp4", "");
                    Uri uri = Uri.parse(url + "");
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                    videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  in1.setPackage("org.videolan.vlc");
                    videoIntent.setDataAndType(uri, type);
                    Log.i("video started", uri.toString() + "");
                    startActivity(videoIntent);
            }else if (artist.getServer().equals(Artist.SERVER_FASELHD) || artist.getServer().equals(Artist.SERVER_MyCima)){
                    String type = "video/*"; // It works for all video application
                    // }

                    String url = artist.getUrl().trim().replace(" ", "");
                    //  url = url.replace("/video.mp4", "");
                    Uri uri = Uri.parse(url + "");
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                    videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  in1.setPackage("org.videolan.vlc");
                    videoIntent.setDataAndType(uri, type);
                    Log.i("video started", uri.toString() + "");
                    startActivity(videoIntent);
                }
                else {
                    if (artist.getUrl().contains("download")){
                        fetchOldAkwamVideo(artist, null);
                    }else {
                        fetchLinkAndVideoOldAkwam(artist);
                    }
                }
            }
        });



        start();

     /*   if (artist.getServer().equals(Artist.SERVER_AKWAM)){
            fetchOneLinkAkwam(artist);
        }else if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){
            fetchOneLinkShahid4u(artist);
        }

      */

    }

    @Override
    protected void onStart() {
        super.onStart();
      //  start();
    }


    public void start(){
        if (null == ServersListActivity.serversArtistList || ServersListActivity.serversArtistList.isEmpty()){
            if (artist.getServer().equals(Artist.SERVER_AKWAM)){
                fetchOneLinkAkwam(artist);
            }else if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){
                fetchOneLinkShahid4u(artist);
              //  fetchOtherServersShahid(artist,2, 5);
            //    fetchOtherServersShahid(artist,3, 6);
            //    fetchOtherServersShahid(artist,3, 6);
             /*   String url2 = artist.getUrl();
                if (url2.contains("shahid4u.one/episode/")){
                    url2= url2.replace("shahid4u.one/episode/", "shahid4u.one/watch/");
                }else if (url2.contains("shahid4u.one/film/")){
                    url2 = url2.replace("shahid4u.one/film/", "shahid4u.one/watch/");
                }
                 String url3 = url2;

                simpleWebView.loadUrl(url3);
                simpleWebView2.loadUrl(url3);

              */

            }else if (artist.getServer().equals(Artist.SERVER_CIMA4U)){
                if (artist.getIsVideo()){
                    fetchOnePageLinkCima4u(artist);
                }else {
                    fetchOneLinkCima4u(artist, "");
                }
            }
        else if (artist.getServer().equals(Artist.SERVER_AFLAM_PRO)){
            if (artist.getIsVideo()){
               // fetchOnePageLinkCima4u(artist);
            }else {
                fetchOneLinkAflamPro(artist, "");
            }
        }
            else if (artist.getServer().equals(Artist.SERVER_FASELHD)){
                    fetchOneLinkFaselHd(artist, "");
            }
            else if (artist.getServer().equals(Artist.SERVER_MyCima)){
                fetchOneLinkMyCima(artist);
            }
            else {
                fetchSeriesLinkOldAkwam(artist);
            }
        }
      /*  Intent intent=new Intent(ServersListActivity.this,VideoResolutionActivity.class);
        intent.putExtra("ARTIST_URL","https://old.goo-2o.com/5dd7c58d8da14");
        startActivityForResult(intent, 2);// Activity is started with requestCode 2

       */
    }


    //akwam
    /**
     * fetch resolution links from a film link
     * @param artist
     */
    private void fetchOneLinkAkwam(Artist artist){
        // MainActivity.resolutionsList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_AKWAM, "FetchOneLink start url:"+url);

                    // page2 fetch goo- links
                    String p2Caption = "/link/";
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



                    //TODO: find better way to fetch links
                    Elements divs = doc.getElementsByClass("tab-content quality");
                    for (Element div : divs) {
                        Artist a = new Artist(artist.getId(), "", artist.getGenre(),"",artist.getImage(),artist.getRate(),artist.getServer(),true ,"");
                        Elements links = div.getElementsByAttribute("href");
                        for (Element link: links){
                            if (link.attr("href").contains(p2Caption)) {
                                a.setUrl(link.attr("href") );
                                a.setName(link.text());
                            }
                        }
                        ServersListActivity.serversArtistList.add(a);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterServersList.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterServersList);
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

    public void fetchLinkVideoAkwam(Artist artist) {
        Log.i(TAG_AKWAM, "FetchVedio Start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_AKWAM, "FetchLinkVedio url:"+url);

                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                                    "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                                            "accept-encoding","gzip, deflate").header(
                                                    "accept-language","en,en-US;q=0.9").header(
                                                            "x-requested-with","pc1"
                    ).timeout(6000).get();


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

                     doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9"
                    ).timeout(6000).get();

                    //check if security caption


                    Elements divs = doc.getElementsByClass("btn-loader");

                    Elements hs = doc.getElementsByTag("h1");




                    boolean isCheck = divs.size() == 0;
                    Log.i("isCheck", "size:"+isCheck);

                    if (!isCheck){
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

                   }else {

                        Intent fetchServerIntent = new Intent(ServersListActivity.this, FetchServerActivity.class);
                        fetchServerIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        fetchServerIntent.putExtra("PAGE_NUMBER", 9);
                        fetchServerIntent.putExtra("ARTIST_URL", url);
                        fetchServerIntent.putExtra("SERVER_ID",  ServersListActivity.shahidServersId);
                        fetchServerIntent.putExtra("ARTIST_NAME", artist.getName());
                        fetchServerIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                        fetchServerIntent.putExtra("ARTIST_SERVER", artist.getServer());
                        fetchServerIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());

                        Log.i(TAG_AKWAM, "Akwam check url:"+url);
                        startActivityForResult(fetchServerIntent, 9);

                    }





                } catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i(TAG_AKWAM, "FetchVideoLink"+e.getMessage()+"");
                }
            }
        }).start();
        Log.i(TAG_AKWAM, "FetchVedio End");
    }

    //shahid
    public void fetchOneLinkShahid4u(Artist artist){
        String url2 = artist.getUrl();
        if (url2.contains("/episode/")){
            url2= url2.replace("/episode/", "/watch/");
        }else if (url2.contains("/film/")){
            url2 = url2.replace("/film/", "/watch/");
        }
        final String url3 = url2;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = url3;
                    Log.i("shaihd fetchl1", url);


                    Log.i("shaihd fetchl 22", url);
                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();

                    //here get servers

                    String ss = url.trim().replace(" ", "");

                    Elements divs = doc.select("iframe");
                    MainActivity.artistList.clear();
                    for (Element div : divs) {
                        // artist.setUrl();
                        Artist a = new Artist();
                        a.setImage(artist.getImage());
                        a.setUrl(div.attr("src"));
                        a.setIsVideo(false);
                        a.setServer(Artist.SERVER_SHAHID4U);
                        a.setName(getWebName(div.attr("src")));

                        if (a.getUrl().contains("vidhd")){
                            fetchVidHdVideo(a);
                        }else {
                            boolean toBePassed = a.getUrl().contains("estream")
                                    || a.getUrl().contains("watchers")
                                    || a.getUrl().contains("vidzi.tv")
                                    || a.getUrl().contains("rapidvideo")
                                    || a.getUrl().contains("thevideo")
                                    || a.getUrl().contains("cloudy")
                                    || a.getUrl().contains("streamin")
                                    || a.getUrl().contains("uptostream")
                                    || a.getUrl().contains("verystream")
                                    || a.getUrl().contains("streamango")
                                    || a.getUrl().contains("cloudvideo")
                                    || a.getUrl().contains("vidoza")
                                    || a.getUrl().contains("thevid")
                                    || a.getUrl().contains("flashx")
                                    || a.getUrl().contains("openload");

                            if (!toBePassed)
                            {
                                ServersListActivity.serversArtistList.add(a);
                            }
                            Elements lists = doc.getElementsByAttribute("data-embedd");
                            boolean isOld = lists.attr("data-embedd").length() > 4;
                            int serverListSize = lists.size();
                            if (isOld){
                                for (Element list : lists) {
                                    String sLink = list.attr("data-embedd");
                                    boolean toBePassed2 =sLink.equals(a.getUrl())
                                            ||sLink.contains("estream")
                                            || sLink.contains("watchers")
                                            || sLink.contains("vidzi.tv")
                                            || sLink.contains("rapidvideo")
                                            || sLink.contains("thevideo")
                                            || sLink.contains("cloudy")
                                            || sLink.contains("streamin")
                                            || sLink.contains("uptostream")
                                            || sLink.contains("vidoza")
                                            || sLink.contains("flashx")
                                            || sLink.contains("thevid")
                                            || sLink.contains("verystream")
                                            || sLink.contains("streamango")
                                            || sLink.contains("cloudvideo")
                                            || sLink.contains("openload");

                                    if (!toBePassed2){
                                        Log.i("servers", "link :" + sLink);
                                        Artist aTemp = new Artist();
                                        aTemp.setImage(artist.getImage());
                                        aTemp.setUrl(sLink);
                                        aTemp.setIsVideo(false);
                                        aTemp.setServer(Artist.SERVER_SHAHID4U);
                                        aTemp.setName(getWebName(sLink));
                                        ServersListActivity.serversArtistList.add(aTemp);
                                    }
                                }
                            }else {
                                ServersListActivity.shahidServersId = serverListSize -1;
                                Log.i("servers size:", "s: "+ServersListActivity.shahidServersId);
                                fetchOtherServersShahid(artist, url3, a.getUrl());//artist to get image and other info, url3 to get the page link, a.url to exclude it from fetch
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapterServersList.notifyDataSetChanged();
                               // listViewArtists.setAdapter(adapterServersList);
                            }});
                        // MainActivity.serverCounts= lists.size();
                        break;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //adapterServersList.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterServersList);
                        }});

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } }).start();
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Returned Result", "method start code:"+requestCode);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode == 1)
        {
            if (resultCode == RESULT_OK){
                String serverUrl=data.getStringExtra("result");
                String serverName = data.getStringExtra("ARTIST_SERVER");
                Log.i("Returned Result", serverUrl + "name"+ serverName);
                Artist a = new Artist();
                a.setName("ol ol ol ol");
                a.setUrl(serverUrl);
                a.setServer(serverName);

                if ( serverName.equals(Artist.SERVER_OLD_AKWAM) ){
                    if (a.getIsVideo()){
                        String type = "video/*";
                        Uri uri = Uri.parse(a.getUrl()+"");
                        Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                        videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  in1.setPackage("org.videolan.vlc");
                        videoIntent.setDataAndType(uri, type);
                        Log.i("video started", uri.toString() + "");
                        startActivity(videoIntent);
                    }else {
                        if (!serverUrl.contains("/download/")){
                            fetchLinkAndVideoOldAkwam(a);
                        }else{
                           // fetchLinkVideoOldAkwam(a);
                            fetchOldAkwamVideo(artist, serverUrl);
                        }
                    }
                }


                Log.i("Returned Final", serverUrl + "name"+ serverName);

               // ServersListActivity.serversArtistList.add(a);
               // adapterServersList.notifyDataSetChanged();
            }
            if (resultCode == RESULT_CANCELED){
                Log.i("Returned Result", "Nothing returned");
            }
            Log.i("Returned Result", "method end resultcode:"+resultCode);
          //  textView1.setText(message);
        }
        else  if(requestCode == 3)
        {
            if (resultCode == RESULT_OK){
                String serverUrl=data.getStringExtra("result");
                String serverName = data.getStringExtra("ARTIST_SERVER");
                String image = data.getStringExtra("ARTIST_IMAGE");
                String aName = getWebName(serverUrl);
                boolean isVideo = data.getBooleanExtra("ARTIST_IS_VIDEO", true);
                Log.i("Returned Result", serverUrl + "name"+ serverName);
                Artist a = new Artist();
                a.setName(aName);
                a.setImage(image);
                a.setIsVideo(isVideo);
                a.setUrl(serverUrl);
                a.setServer(serverName);
                Log.i("Returned Final", serverUrl + "name"+ serverName);
                 ServersListActivity.serversArtistList.add(a);
                 adapterServersList.notifyDataSetChanged();
                 listViewArtists.setAdapter(adapterServersList);
            }
            if (resultCode == RESULT_CANCELED){
                Log.i("Returned Result", "Nothing returned");
            }
            Log.i("Returned Result", "method end resultcode:"+resultCode);
            //  textView1.setText(message);
        }

        else  if(requestCode == 4) //fetch shahid vidhd video link but already overwritten
        {
            if (resultCode == RESULT_OK){
                String type = "video/*"; // It works for all video application
                // }
                String serverUrl=data.getStringExtra("result");
                String serverName = data.getStringExtra("ARTIST_SERVER");
                Log.i("Returned Result", serverUrl + "name"+ serverName);

                Uri uri = Uri.parse(serverUrl + "");
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //  in1.setPackage("org.videolan.vlc");
                videoIntent.setDataAndType(uri, type);
                Log.i("shahid video started", uri.toString() + "");
                startActivity(videoIntent);
            }
            if (resultCode == RESULT_CANCELED){
                Log.i("Returned Result", "Nothing returned");
            }
            Log.i("Returned Result", "method end resultcode:"+resultCode);
            //  textView1.setText(message);
        }

        else  if(requestCode == 5 || requestCode == 6)  //fetch shahid other servers
        {
            if (resultCode == RESULT_OK){
                String serverUrl=data.getStringExtra("result");
                String serverName = data.getStringExtra("ARTIST_SERVER");
                String image = data.getStringExtra("ARTIST_IMAGE");
                String aName = getWebName(serverUrl);
                boolean isVideo = data.getBooleanExtra("ARTIST_IS_VIDEO", true);
                Log.i("Returned Result", serverUrl + "name"+ serverName);
                String pageUrl=data.getStringExtra("PAGE_URL");
                String excluded=data.getStringExtra("EXCLUDED");
                Artist a = new Artist();
                a.setName(aName);
                a.setImage(image);
                a.setIsVideo(isVideo);
                a.setUrl(serverUrl);
                a.setServer(serverName);

                Log.i("Returned Final", serverUrl + "name"+ serverName);

                boolean toBePassed = a.getUrl().contains("estream")
                        || a.getUrl().contains("watchers")
                        || a.getUrl().contains("vidzi.tv")
                        || a.getUrl().contains("rapidvideo")
                        || a.getUrl().contains("thevideo")
                        || a.getUrl().contains("cloudy")
                        || a.getUrl().contains("streamin")
                        || a.getUrl().contains("uptostream")
                        || a.getUrl().contains("verystream")
                        || a.getUrl().contains("streamango")
                        || a.getUrl().contains("cloudvideo")
                        || a.getUrl().contains("vidoza")
                        || a.getUrl().contains("thevid")
                        || a.getUrl().contains("flashx")
                        || a.getUrl().contains("openload");
                if (!toBePassed){
                    ServersListActivity.serversArtistList.add(a);
                }
                adapterServersList.notifyDataSetChanged();
                listViewArtists.setAdapter(adapterServersList);

                ServersListActivity.shahidServersId--;

                if (ServersListActivity.shahidServersId != 0) {
                    fetchOtherServersShahid(a, pageUrl, excluded);
                }

            }
            if (resultCode == RESULT_CANCELED){
                Log.i("Returned Result", "Nothing returned");
            }
            Log.i("Returned Result:"+requestCode, "method end resultcode:"+resultCode);
            //  textView1.setText(message);
        }

        else  if(requestCode == 7)
        {
            if (resultCode == RESULT_OK){
                String serverUrl=data.getStringExtra("result");
                String serverName = data.getStringExtra("ARTIST_SERVER");
                String image = data.getStringExtra("ARTIST_IMAGE");
                String aName = getWebName(serverUrl);
                boolean isVideo = data.getBooleanExtra("ARTIST_IS_VIDEO", true);
                Log.i("Returned Result", serverUrl + "name"+ serverName);
                Artist a = new Artist();
                a.setName(aName);
                a.setImage(image);
                a.setIsVideo(isVideo);
                a.setUrl(serverUrl);
                a.setServer(serverName);
                Log.i("Returned Final", serverUrl + "name"+ serverName);
                ServersListActivity.serversArtistList.add(a);
                adapterServersList.notifyDataSetChanged();
                listViewArtists.setAdapter(adapterServersList);

            }
            if (resultCode == RESULT_CANCELED){
                Log.i("Returned Result", "Nothing returned");
            }
            Log.i("Returned Result:"+requestCode, "method end resultcode:"+resultCode);
            //  textView1.setText(message);
        }

        else  if(requestCode == 8)
        {
            if (resultCode == RESULT_OK){
                String serverUrl=data.getStringExtra("result");
                String serverName = data.getStringExtra("ARTIST_SERVER");
                String image = data.getStringExtra("ARTIST_IMAGE");
                String aName = getWebName(serverUrl);
                boolean isVideo = data.getBooleanExtra("ARTIST_IS_VIDEO", true);
                Log.i("Returned Result", serverUrl + "name"+ serverName);
                Artist a = new Artist();
                a.setName("Auto");
                a.setImage(image);
                a.setIsVideo(isVideo);
                a.setUrl(serverUrl);
                a.setServer(serverName);


                Artist a2 = new Artist();
                a2.setName("1080");
                a2.setImage(image);
                a2.setIsVideo(isVideo);
                a2.setUrl(serverUrl.replaceAll("/master.m3u8", "/index-f1-v1-a1.m3u8"));
                a2.setServer(serverName);

                Artist a3 = new Artist();
                a3.setName("720");
                a3.setImage(image);
                a3.setIsVideo(isVideo);
                a3.setUrl(serverUrl.replaceAll("/master.m3u8", "/index-f2-v1-a1.m3u8"));
                a3.setServer(serverName);

                Artist a4 = new Artist();
                a4.setName("480");
                a4.setImage(image);
                a4.setIsVideo(isVideo);
                a4.setUrl(serverUrl.replaceAll("/master.m3u8", "/index-f3-v1-a1.m3u8"));
                a4.setServer(serverName);

                Artist a5 = new Artist();
                a5.setName("320");
                a5.setImage(image);
                a5.setIsVideo(isVideo);
                a5.setUrl(serverUrl.replaceAll("/master.m3u8", "/index-f4-v1-a1.m3u8"));
                a5.setServer(serverName);



                Log.i("Returned Final", serverUrl + "name"+ serverName);
                ServersListActivity.serversArtistList.add(a);
                ServersListActivity.serversArtistList.add(a2);
                ServersListActivity.serversArtistList.add(a3);
                ServersListActivity.serversArtistList.add(a4);
                ServersListActivity.serversArtistList.add(a5);
                adapterServersList.notifyDataSetChanged();
                listViewArtists.setAdapter(adapterServersList);

            }
            if (resultCode == RESULT_CANCELED){
                Log.i("Returned Result", "Nothing returned");
            }
            Log.i("Returned Result:"+requestCode, "method end resultcode:"+resultCode);
            //  textView1.setText(message);
        }

        else  if(requestCode == 9) // akwam isCheck
        {
            if (resultCode == RESULT_OK){
                String serverUrl=data.getStringExtra("result");
                String serverName = data.getStringExtra("ARTIST_SERVER");
                String image = data.getStringExtra("ARTIST_IMAGE");
                String aName = getWebName(serverUrl);
                boolean isVideo = data.getBooleanExtra("ARTIST_IS_VIDEO", true);
                Log.i("Returned Result", serverUrl + "name"+ serverName);
                Artist a = new Artist();
                a.setName("playNow");
                a.setImage(image);
                a.setIsVideo(isVideo);
                a.setUrl(serverUrl);
                a.setServer(serverName);
                Log.i("Returned Final", serverUrl + "name"+ serverName);
                ServersListActivity.serversArtistList.add(a);
                Collections.reverse(ServersListActivity.serversArtistList);
                adapterServersList.notifyDataSetChanged();
                listViewArtists.setAdapter(adapterServersList);
            }
            if (resultCode == RESULT_CANCELED){
                Log.i("Returned Result", "Nothing returned");
            }
            Log.i("Returned Result", "method end resultcode:"+resultCode);
            //  textView1.setText(message);
        }

    }

    public void fetchOtherServersShahid(Artist artist, String pageUrl,String excluded){

        Intent fetchServerIntent = new Intent(ServersListActivity.this, FetchServerActivity.class);
        fetchServerIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        fetchServerIntent.putExtra("PAGE_NUMBER", 5);
        fetchServerIntent.putExtra("ARTIST_URL", pageUrl);
        fetchServerIntent.putExtra("PAGE_URL", pageUrl);
        fetchServerIntent.putExtra("SERVER_ID",  ServersListActivity.shahidServersId);
        fetchServerIntent.putExtra("EXCLUDED", excluded);
        fetchServerIntent.putExtra("ARTIST_NAME", artist.getName());
        fetchServerIntent.putExtra("ARTIST_IMAGE", artist.getImage());
        fetchServerIntent.putExtra("ARTIST_SERVER", artist.getServer());
        fetchServerIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());

        Log.i(TAG, "Shahid Other servers url:"+pageUrl);
        startActivityForResult(fetchServerIntent, 5);
    }

    public void fetchServersCountShahid4u(Artist artist){
        String url2 = artist.getUrl();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //String url = "https://shahid4u.one/watch/%D8%A7%D9%86%D9%85%D9%8A-dragon-quest-dai-no-daibouken-%D8%A7%D9%84%D8%AD%D9%84%D9%82%D8%A9-10-%D8%A7%D9%84%D8%B9%D8%A7%D8%B4%D8%B1%D8%A9-%D9%85%D8%AA%D8%B1%D8%AC%D9%85%D8%A9";

                    String url = url2;
                    Log.i("shaihd fetchl1", url);
                    Log.i("shaihd fetchl 22", url);
                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    //here get servers
                    String ss = url.trim().replace(" ", "");
                    Elements divs = doc.select("iframe");
                    MainActivity.artistList.clear();
                    for (Element div : divs) {
                        // artist.setUrl();
                        Artist a = artist;
                        a.setUrl(div.attr("src"));
                        a.setIsVideo(true);
                        MainActivity.artistList.add(a);
                        //  Elements lists = div.getElementsByAttribute("data-embedd");
                        // MainActivity.serverCounts= lists.size();
                        break;
                    /*    for (Element list : lists) {
                            Log.i("serverId:", list.attr("data-embedd"));
                            Log.i("serverName:", list.text());
                            MainActivity.idList.add(list.attr("data-embedd"));
                        }

                     */

                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterServersList.notifyDataSetChanged();
                    }});

            } }).start();

        //adapter.notifyDataSetChanged();



        // MainActivity.artistList.clear();
    /*    for (String server: MainActivity.serverList){
            Artist a = artist;
            a.setUrl(server);
            a.setIsVideo(true);
            MainActivity.artistList.add(a);
            adapter.notifyDataSetChanged();
        }
*/


    }

    private void fetchServersShahid4u( Artist artist, String url) {
        Log.i("shahid servers", "url:"+artist.getUrl());
        simpleWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.i("Redirect", url);
                if (!url.contains("openload") && !url.contains("estream") && url.length() > 7){
                    Artist a = new Artist();
                    a.setImage(artist.getImage());
                    a.setName(artist.getName());
                    a.setServer(artist.getServer());
                    a.setIsVideo(true);
                    a.setUrl(url);
                    a.setGenre(getWebName(url));


                    ServersListActivity.serversArtistList.add(a);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterServersList.notifyDataSetChanged();
                        }});
                }
                  view.loadUrl(" ");
                //view.destroy();
                // generateShahid4uLink(artist);
                //view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WEBCLIENT", "onPageFinished");
                view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                // view.loadUrl("javascript:window.Android.showToast('hi sexy')");
                //view.loadUrl("javascript:window.document.getElementsByTagName(\"li\")[23].click()");
                //view.loadUrl("javascript:document.getElementsByTagName(\"iframe\")[0].click();");
                // view.loadUrl("javascript:window.Android.showToast(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                //ok      view.loadUrl("javascript:document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString();");
                //view.loadUrl("javascript:window.alert(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                //  Log.i("yesss", view.getTitle()+"");

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.d("WEBCLIENT","onLoadResource count:"+ServersListActivity.shahidServersIdCounter);
                //  for (int i = 0; i< MainActivity.serverCounts; i++){
                view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+ServersListActivity.shahidServersIdCounter+"].click()");

                //view.loadUrl("javascript:window.Android.showToast(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                // view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                //  }

                // view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children[1].click()");
            }
        });

        //for (int i = 4; i !=0; i--){
            simpleWebView.loadUrl(url);
            ServersListActivity.shahidServersIdCounter--;
            simpleWebView2.loadUrl(url);
      //  }

/*
            Log.i("shahid gen link", "start");
            String ss = artist.getUrl().trim().replace(" ", "");
            Log.i("result sss", ss + "");
            // runOnUiThread(new Runnable() {
            //   @Override
            // public void run() {
            // textViewResponse.setText(builder.toString());
            //url=url.concat(".html");
            Log.i("akwam fetchl4", ss + "");
            //  String type = "video/*"; // It works for all video application
            String type = "text/html"; // It works for all video application
            Uri uri = Uri.parse(ss);
            Intent in1 = new Intent(Intent.ACTION_VIEW, uri);
            in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //  in1.setPackage("org.videolan.vlc");
            in1.setDataAndType(uri, type);
            Log.i("video started", uri.toString() + "");
            startActivity(in1);
            Log.i("akwam method", "artikel in method");
            Log.i("akwam Url", artist.getUrl() + "");
            //      }
            //    });
            }
 */

    }

    public String getWebName(String item){
        String result= item.substring(item.indexOf("://")+3);
        if (result.contains("ww.")){
            result = result.substring(result.indexOf("ww.")+3);
        }
        result = result.substring(0, result.indexOf("."));

        return result;
    }


    //old akwam
    public void fetchLinkAndVideoOldAkwam(Artist artist){

        Intent fetchServerIntent = new Intent(ServersListActivity.this, FetchServerActivity.class);
        fetchServerIntent.putExtra("PAGE_NUMBER", 1);  //to fetch goo page = 1
        fetchServerIntent.putExtra("ARTIST_URL", artist.getUrl());
        fetchServerIntent.putExtra("ARTIST_NAME", artist.getName());
        fetchServerIntent.putExtra("ARTIST_IMAGE", artist.getImage());
        fetchServerIntent.putExtra("ARTIST_SERVER", artist.getServer());
        fetchServerIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());

        Log.i(TAG, "oldAkwamL&V url:"+artist.getUrl());
        startActivityForResult(fetchServerIntent, 1);


    }
    public void fetchLinkVideoOldAkwam(Artist artist){
        Log.i(TAG, "oldAkwamV url:"+artist.getUrl());

        Intent fetchServerIntent = new Intent(ServersListActivity.this, FetchServerActivity.class);
        fetchServerIntent.putExtra("PAGE_NUMBER", 2);  //to fetch video page = 2
        fetchServerIntent.putExtra("ARTIST_URL", artist.getUrl());
        fetchServerIntent.putExtra("ARTIST_NAME", artist.getName());
        fetchServerIntent.putExtra("ARTIST_IMAGE", artist.getImage());
        fetchServerIntent.putExtra("ARTIST_SERVER", artist.getServer());
        fetchServerIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());
        startActivityForResult(fetchServerIntent, 1);
    }

    private void fetchSeriesLinkOldAkwam(final Artist artist){

        MainActivity.artistList.clear();
        final String url = artist.getUrl();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    //Elements links = doc.select("a[href]");sw

                    //get profile image
                //    String imageLink = doc.getElementsByClass("fancybox-thumbs").attr("href");
                    //get description
                    String description = doc.getElementsByClass("sub_desc").text();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textViewDesc.setText(description);
                        }
                    });


                    //get episodes
                    Elements divs = doc.select("div");
                    for (Element div : divs) {
                        if (div.hasClass("sub_episode_links")) {
                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_OLD_AKWAM);
                            a.setGenre("Old");
                            a.setName(div.getElementsByTag("h2").text());
                            a.setImage(artist.getImage());

                            a.setUrl(div.getElementsByTag("a").attr("href"));
                            Log.i(TAG, "old found:"+div.getElementsByTag("h2").text());

                            ServersListActivity.serversArtistList.add(a);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Collections.reverse(MainActivity.artistList);
                                    adapterServersList.notifyDataSetChanged();
                                    listViewArtists.setAdapter(adapterServersList);
                                }
                            });
                        }else if (div.hasClass("sub_direct_links")) {
                            String name = div.getElementsByClass("sub_file_title").text();
                            String url =div.getElementsByTag("a").attr("href");
                            if (name.contains("للتصميم الجديد")){
                                name= "هذه المادة لا تحتوي علي رابط مباشر، بسبب انتهاء مدة الملف";
                                url = "";
                                //break;
                            }
                            Artist a = new Artist();
                            a.setServer(Artist.SERVER_OLD_AKWAM);
                            a.setGenre("Old");
                            a.setName(name);
                            a.setImage(artist.getImage());

                            a.setUrl(url);
                            Log.i(TAG, "old found:"+a.getName());
                            ServersListActivity.serversArtistList.add(a);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Collections.reverse(MainActivity.artistList);
                                    adapterServersList.notifyDataSetChanged();
                                    listViewArtists.setAdapter(adapterServersList);
                                }
                            });
                        }
                        // }});
                    }

                } catch (IOException e) {
                    Log.i("fail", e.getMessage() + "");
                }
            }  }).start();
    }

    public void fetchOldAkwamVideo(Artist artist, String url){

        if (url == null || url.equals("")){
            url = artist.getUrl();
        }

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // view.loadUrl(url);
                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);
                String type = "video/*";
                Uri uri = Uri.parse(url+"");
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //  in1.setPackage("org.videolan.vlc");
                videoIntent.setDataAndType(uri, type);
                Log.i("video started", uri.toString() + "");
                startActivity(videoIntent);

              /*  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent resultIntent = new Intent();

                        resultIntent.putExtra("result", url);
                        Log.i("Result", url);
                        resultIntent.putExtra("ARTIST_NAME", artist.getName());
                        resultIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                        resultIntent.putExtra("ARTIST_SERVER", artist.getServer());
                        resultIntent.putExtra("ARTIST_IS_VIDEO", true);

                        setResult(RESULT_OK, resultIntent);
                        simpleWebView.removeView(view);
                        simpleWebView.removeAllViews();
                        simpleWebView.stopLoading();
                        finish();
                    }});
                view.stopLoading();
                finish();

               */
                view.stopLoading();

                view.loadUrl("");
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WEBCLIENT", "onPageFinished");
                //ok  sh       view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                view.loadUrl("javascript:location.replace(document.getElementsByClassName(\"download_button\")[0].getAttribute(\"href\").toString());");

                //hh  view.loadUrl("javascript:location.replace(document.getElementsByClassName(\"download_button\")[0].getAttribute(\"href\").toString());");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                view.loadUrl("javascript:window.document.getElementsByClassName(\"download_button\")[0].click()");

                super.onLoadResource(view, url);
                //ok sh        view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+MainActivity.serverCounts+"].click()");
                Log.d("WEBCLIENT","onLoadResource :url"+url);


            }
        };
        //fetch video link from last page
        simpleWebView.setWebViewClient(webViewClient);

        //String url2 = "https://old.akwam.co/download/3c472024a3f1/High-Seas-S02-Ep01-720p-WEB-DL-akoam-net-mkv";
        Log.i("videooo1", url);
        simpleWebView.loadUrl(url+"#timerHolder");
    }


    //cima4u
    public void fetchOneLinkCima4u(Artist artist, String url){
        Intent fetchServerIntent = new Intent(ServersListActivity.this, FetchServerActivity.class);
        fetchServerIntent.putExtra("PAGE_NUMBER", 3);  //to fetch goo page = 1
        if (url.equals("")){
            fetchServerIntent.putExtra("ARTIST_URL", artist.getUrl());
        }else{
            fetchServerIntent.putExtra("ARTIST_URL", url);
        }
        fetchServerIntent.putExtra("ARTIST_NAME", artist.getName());
        fetchServerIntent.putExtra("ARTIST_IMAGE", artist.getImage());
        fetchServerIntent.putExtra("ARTIST_SERVER", artist.getServer());
        fetchServerIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());

        Log.i(TAG, "Cima         fetchServerIntent.putExtra(\"ARTIST_NAME\", artist.getName());\n&V url:"+artist.getUrl());
        startActivityForResult(fetchServerIntent, 3);
    }


    public void fetchOnePageLinkCima4u(Artist artist){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_CIMA4U, "ur:"+url);
                  //  Document doc = Jsoup.connect(url).header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header("User-Agent","Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").timeout(6000).get();

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
                    fetchOneLinkCima4u(artist, url);


             /*       runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }});

              */
                } catch (IOException e) {
                    Log.i("fail", e.getMessage()+"");
                }

            }
        }).start();
    }


    //AflamPro
    public void fetchOneLinkAflamPro(Artist artist, String url){
        Intent fetchServerIntent = new Intent(ServersListActivity.this, FetchServerActivity.class);
        fetchServerIntent.putExtra("PAGE_NUMBER", 7);  //to fetch goo page = 1
        if (url.equals("")){
            fetchServerIntent.putExtra("ARTIST_URL", artist.getUrl());
        }else{
            fetchServerIntent.putExtra("ARTIST_URL", url);
        }
        fetchServerIntent.putExtra("ARTIST_NAME", artist.getName());
        fetchServerIntent.putExtra("ARTIST_IMAGE", artist.getImage());
        fetchServerIntent.putExtra("ARTIST_SERVER", artist.getServer());
        fetchServerIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());

        Log.i(TAG, "AflamPro         fetchServerIntent.putExtra(\"ARTIST_NAME\", artist.getName());\n&V url:"+artist.getUrl());
        startActivityForResult(fetchServerIntent, 7);
        //startActivity(fetchServerIntent);
    }

    //FaselHd
    public void fetchOneLinkFaselHd(Artist artist, String url){
        Intent fetchServerIntent = new Intent(ServersListActivity.this, FetchServerActivity.class);
        fetchServerIntent.putExtra("PAGE_NUMBER", 8);  //to fetch goo page = 1
        if (url.equals("")){
            fetchServerIntent.putExtra("ARTIST_URL", artist.getUrl());
        }else{
            fetchServerIntent.putExtra("ARTIST_URL", url);
        }
        fetchServerIntent.putExtra("ARTIST_NAME", artist.getName());
        fetchServerIntent.putExtra("ARTIST_IMAGE", artist.getImage());
        fetchServerIntent.putExtra("ARTIST_SERVER", artist.getServer());
        fetchServerIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());

        Log.i(TAG, "Fasel         fetchServerIntent.putExtra(\"ARTIST_NAME\", artist.getName());\n&V url:"+artist.getUrl());
        startActivityForResult(fetchServerIntent, 8);
        //startActivity(fetchServerIntent);
    }

    public void fetchVidHdVideo(Artist artist){
        String url = artist.getUrl().trim().replaceAll(" ", "");
        if (!url.contains(".html")){
            url= url+".html";
        }
        String url2 = url;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Connection con = Jsoup.connect(url2).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(6000).ignoreHttpErrors(true);
                    Connection.Response resp = con.execute();

                    String respText = resp.body();
                    respText= respText.substring(respText.indexOf("|player|"), respText.indexOf("split"));
                    String[] resArray = respText.split("\\|");

                    String protocol="https://",serverName="",webName="",webEnd="",resolution1="", resolution2="",extension ="";
                    for (int i=0;i < resArray.length;i++){
                        switch (resArray[i]){
                            case "player":
                                webEnd=resArray[i+1]+"/"; //.net
                                webName=resArray[i+2]+"."; //.vidhd
                                break;
                            case "var":
                                serverName=resArray[i+2]+"."; //.c8  servername
                                break;
                            case "label":
                                extension = "v."+resArray[i+1]; //v.mp4
                                break;
                            case "360p":
                                resolution1= resArray[i+1]+"/";
                                break;
                            case "720p":
                                resolution2= resArray[i+1]+"/";
                                break;
                        }
                    }



                    String res360 = protocol+serverName+webName+webEnd+resolution1+extension;
                    String res720 = protocol+serverName+webName+webEnd+resolution2+extension;

                    if (res360.length() < 45){
                        Log.i("360", "smaller" +"");
                        for (int i=0;i < resArray.length;i++){
                            switch (resArray[i]){
                                case "https":
                                    webEnd=resArray[i-2]+"/"; //.net
                                    webName=resArray[i-1]+"."; //.vidhd
                                    break;
                                case "ready":
                                    serverName=resArray[i-1]+"."; //.c8  servername
                                    break;
                                case "label":
                                    extension = "v."+resArray[i+1]; //v.mp4
                                    break;
                                case "360p":
                                    resolution1= resArray[i+3]+"/";
                                    break;
                                case "720p":
                                    resolution2= resArray[i+3]+"/";
                                    break;
                            }
                        }
                        res360 = protocol+serverName+webName+webEnd+resolution1+extension;
                        res720 = protocol+serverName+webName+webEnd+resolution2+extension;
                    }

                    if (!resolution2.equals("")){
                        Artist a = new Artist();
                        a.setUrl(res720);
                        a.setName("vidhd 720p");
                        a.setServer(Artist.SERVER_SHAHID4U);
                        a.setIsVideo(true);
                        a.setImage(artist.getImage());
                        a.setGenre(artist.getGenre());
                        ServersListActivity.serversArtistList.add(a);
                    }



                    Artist a2 = new Artist();

                    if (res360.length() < 45){
                        res360 = url2;
                        a2.setIsVideo(false);
                    }

                    a2.setUrl(res360);
                    a2.setName("vidhd 360p");
                    a2.setServer(Artist.SERVER_SHAHID4U);
                    a2.setIsVideo(true);
                    a2.setImage(artist.getImage());
                    a2.setGenre(artist.getGenre());


                    ServersListActivity.serversArtistList.add(a2);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterServersList.notifyDataSetChanged();
                        }});


                    Log.i("360", res360 +"");
                    Log.i("720", res720 +"");
                }catch (IOException e) {
                    //builder.append("Error : ").append(e.getMessage()).append("\n");
                    Log.i("fail", e.getMessage() + "");
                }
            }
        }).start();
    }


    public void fetchOneLinkMyCima(Artist artist){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = artist.getUrl();
                    Log.i(TAG_CIMA4U, "ur:"+url);
                   // Document doc = Jsoup.connect(url).header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header("User-Agent","Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").timeout(0).get();

                    Document doc = Jsoup.connect(url).header(
                            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8").header(
                            "User-Agent"," Mozilla/5.0 (Linux; Android 8.1.0; Android SDK built for x86 Build/OSM1.180201.031; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/69.0.3497.100 Mobile Safari/537.36").header(
                            "accept-encoding","gzip, deflate").header(
                            "accept-language","en,en-US;q=0.9").header(
                            "x-requested-with","pc1"
                    ).timeout(6000).get();
                    //Elements links = doc.select("a[href]");

                    //get link of episodes page
                    String desc = doc.getElementsByClass("StoryMovieContent").text();
                    Elements uls = doc.getElementsByClass("List--Download--Mycima--Single");
                    for (Element ul : uls) {
                        Elements lis = ul.getElementsByTag ("li");
                        for (Element li : lis) {

                            String link = li.getElementsByAttribute("href").attr("href");
                            String name = li.getElementsByTag("resolution").text();

                            Artist a = new Artist();
                            a.setName(name);
                            a.setUrl(link);
                            a.setImage(artist.getImage());
                            a.setServer(Artist.SERVER_MyCima);
                            a.setIsVideo(true);
                            ServersListActivity.serversArtistList.add(a);
                        }
                        break;
                    }



                  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewDesc.setText(desc);
                            adapterServersList.notifyDataSetChanged();
                            listViewArtists.setAdapter(adapterServersList);
                        }});


                } catch (IOException e) {
                    Log.i("fail", e.getMessage()+"");
                }

            }
        }).start();
    }

}