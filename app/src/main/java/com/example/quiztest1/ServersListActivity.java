package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
                else {
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
            }
        }
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
                        listViewArtists.setAdapter(adapterServersList);
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

    public String getWebName(String item){
        return item.substring(item.indexOf("//")+1, item.indexOf('.'));
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