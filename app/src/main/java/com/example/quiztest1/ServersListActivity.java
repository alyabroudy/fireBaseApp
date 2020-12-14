package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServersListActivity extends AppCompatActivity {


    ListView listViewArtists;
    Artist artist;
    String TAG_AKWAM = "Akwam";
    String TAG = "ServersList";
    ArtistList adapterServersList;
    static List<Artist> serversArtistList;

    static int shahidServersIdCounter=5;

    WebView simpleWebView;
    WebView simpleWebView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers_list);


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
                    fetchLinkVideoAkwam(artist);
                }
                else if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){

                    /*
                    Intent webViewIntent = new Intent(ServersListActivity.this, VideoResolutionActivity.class);
                    webViewIntent.putExtra("URL", artist.getUrl());
                    startActivity(webViewIntent);
                    */
                    String type = "video/*";
                    if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){
                        type = "text/html"; // It works for all video application
                    }

                    String url = artist.getUrl().trim().replace(" ", "");
                    Uri uri = Uri.parse(url+"");
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
                    videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  in1.setPackage("org.videolan.vlc");
                    videoIntent.setDataAndType(uri, type);
                    Log.i("video started", uri.toString() + "");
                    startActivity(videoIntent);

                }else {
                    fetchLinkAndVideoOldAkwam(artist);
                }
            }
        });

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
        start();
    }


    public void start(){
        if (null == ServersListActivity.serversArtistList || ServersListActivity.serversArtistList.isEmpty()){
            if (artist.getServer().equals(Artist.SERVER_AKWAM)){
                fetchOneLinkAkwam(artist);
            }else if (artist.getServer().equals(Artist.SERVER_SHAHID4U)){
                fetchOneLinkShahid4u(artist);
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

            }else {
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

    //shahid
    public void fetchOneLinkShahid4u(Artist artist){
        String url2 = artist.getUrl();
        if (url2.contains("shahid4u.one/episode/")){
            url2= url2.replace("shahid4u.one/episode/", "shahid4u.one/watch/");
        }else if (url2.contains("shahid4u.one/film/")){
            url2 = url2.replace("shahid4u.one/film/", "shahid4u.one/watch/");
        }
        final String url3 = url2;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //String url = "https://shahid4u.one/watch/%D8%A7%D9%86%D9%85%D9%8A-dragon-quest-dai-no-daibouken-%D8%A7%D9%84%D8%AD%D9%84%D9%82%D8%A9-10-%D8%A7%D9%84%D8%B9%D8%A7%D8%B4%D8%B1%D8%A9-%D9%85%D8%AA%D8%B1%D8%AC%D9%85%D8%A9";

                    String url = url3;
                    Log.i("shaihd fetchl1", url);


                    Log.i("shaihd fetchl 22", url);
                    Document doc = Jsoup.connect(url).get();

                    //here get servers

                    String ss = url.trim().replace(" ", "");

                    Elements divs = doc.select("iframe");
                    MainActivity.artistList.clear();
                    for (Element div : divs) {
                        // artist.setUrl();
                        Artist a = artist;
                        a.setUrl(div.attr("src"));
                        a.setIsVideo(true);
                        a.setGenre(getWebName(div.attr("src")));
                        ServersListActivity.serversArtistList.add(a);
                        //  Elements lists = div.getElementsByAttribute("data-embedd");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapterServersList.notifyDataSetChanged();
                                listViewArtists.setAdapter(adapterServersList);
                            }});
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


            } }).start();



       // fetchServersShahid4u(artist, url3);

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
                    Document doc = Jsoup.connect(url).get();
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
        return item.substring(item.indexOf("//")+1, item.indexOf('.'));
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
                    Document doc = Jsoup.connect(url).get();
                    //Elements links = doc.select("a[href]");sw
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
        simpleWebView.loadUrl(url);
    }


    /*

    private void fetchOneLinkShahid4u( Artist artist) {
        Log.i("video", artist.getUrl()+"");
        simpleWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.i("Redirect", url);
                artist.setUrl(url);
                //view.destroy();
                fetchOneLinkShahid4u(artist);
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
                Log.d("WEBCLIENT","onLoadResource");
                //  for (int i = 0; i< MainActivity.serverCounts; i++){
                view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+2+"].click()");
                Log.i("script", MainActivity.serverCounts+"");
                //view.loadUrl("javascript:window.Android.showToast(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                // view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                //  }

                // view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children[1].click()");
            }
        });

        if (artist.getUrl().contains("openload") || artist.getUrl().contains("estream")){
            Log.i("video", "is open");
            simpleWebView.loadUrl(artist.getOldUrl());
        }else {


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
    }

     */

}