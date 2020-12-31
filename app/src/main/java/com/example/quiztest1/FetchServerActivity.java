package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class FetchServerActivity extends AppCompatActivity {

    static int shahidServersCounter = 0;
    Artist artist;
    Intent severListIntent;
    WebView simpleWebView;
    int pageNumber;
    int serverId;
    String shahidExclude;
    String shahidPageUrl;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_server);

        simpleWebView = (WebView) findViewById(R.id.webView);
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.getSettings().setBlockNetworkImage(true);
        simpleWebView.getSettings().setAppCacheEnabled(false);
        simpleWebView.getSettings().setLoadsImagesAutomatically(false);
    //    simpleWebView.setVisibility(View.GONE);
        simpleWebView.getSettings().setDomStorageEnabled(true);


        // simpleWebView.setWebViewClient(client);

        severListIntent= getIntent();
        artist = new Artist();
        shahidExclude=severListIntent.getStringExtra("EXCLUDED");
        shahidPageUrl=severListIntent.getStringExtra("PAGE_URL");
        artist.setUrl(severListIntent.getStringExtra("ARTIST_URL"));
        artist.setName(severListIntent.getStringExtra("ARTIST_NAME"));
        artist.setImage(severListIntent.getStringExtra("ARTIST_IMAGE"));
        artist.setServer(severListIntent.getStringExtra("ARTIST_SERVER"));
        artist.setIsVideo(severListIntent.getExtras().getBoolean("ARTIST_IS_VIDEO"));
        pageNumber = severListIntent.getIntExtra("PAGE_NUMBER",0);
        serverId = severListIntent.getIntExtra("SERVER_ID",0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (pageNumber == 1){ // fetch goo page
            Log.i("pageNumer", pageNumber+"");
            fetchOldAkwamLinkAndVideo(artist);
        }else if (pageNumber == 2){// fetch video link
            Log.i("pageNumer", pageNumber+"");
            fetchOldAkwamVideo(artist);
        }else if (pageNumber == 3){// fetch video link
            Log.i("pageNumer", pageNumber+"");
            fetchVideoLinkCima4u(artist);
        }
        else if (pageNumber == 4){// fetch video link for vidHd but already overwritten
            Log.i("pageNumer", pageNumber+"");
            fetchVidHdVideo(artist);
           // shahid(artist);
        }
        else if (pageNumber == 5){// fetch Shahid other servers
            Log.i("pageNumer", pageNumber+"");
            fetchShahidOtherServers(artist);
            // shahid(artist);
        }
        else if (pageNumber == 7){// fetch video link
            Log.i("pageNumer", pageNumber+"");
            fetchVideoLinkAflamPro(artist);
        }
        else if (pageNumber == 8){// fetch video link
            Log.i("pageNumer", pageNumber+"");
            fetchVideoLinkFaselHd(artist);
        }

    }

    public void fetchShahidOtherServers(Artist artist){
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.evaluateJavascript("(function() { var x = document.getElementsByClassName(\"servers-list\")[0].children;x[1].innerHTML = \"Hello World!\"; x[1].click(); return document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString();})();", new ValueCallback<String>() {
                    //   view.evaluateJavascript("(function() { return document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString();})();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("LogName", s); // Prints the string 'null' NOT Java null
                    }
                });
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.evaluateJavascript("(function() { return document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString();})();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("LogName", s); // Prints the string 'null' NOT Java null
                    }
                });
                //ok      view.evaluateJavascript("(function() { var x = document.getElementsByClassName(\"sever_link\"); x[1].innerHTML = \"Hello World!\"; return document.title;})();", new ValueCallback<String>() {
                Log.d("WEBCLIENT", "onPageFinished");
                view.evaluateJavascript("(function() { var x = document.getElementsByClassName(\"servers-list\")[0].children;x["+serverId+"].innerHTML = \"Hello World!\"; x["+serverId+"].click(); return document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString();})();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("LogName", s); // Prints the string 'null' NOT Java null
                    }
                });
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.d("WEBCLIENT","onLoadResource url:"+url);
                if (url.contains(shahidExclude)){return;}
                if (url.contains("vidbom.com/embed")
                        || url.contains("vidhd.net/embed")
                        || url.contains("ok.ru/videoembed")
                        || url.contains("vidshar.net/embed")
                        || url.contains("watchers.to/embed")
                        || url.contains("doodstream.com/e/")
                        || url.contains("estream.to/embed")
                        || url.contains("streamango.com/embed")
                        || url.contains("verystream.com/e/")
                        || url.contains("openload.co/embed/")
                        || url.contains("uptostream.com/iframe")
                        || url.contains("videowood.tv/embed")
                        || url.contains("uqload.com/embed")
                        || url.contains("flashx.tv/embed")
                        || url.contains("mixdrop.co/e/")
                        || url.contains("videorev.cc/embed")
                        || url.contains("streamin.to/embed")
                        || url.contains("embed.mystream.to/")
                        || url.contains("vidzi.tv/embed")
                        || url.contains("thevideo.me/embed")
                        || url.contains("thevid.tv/e/")
                        || url.contains("vidoza.net/embed")
                        || url.contains("videobin.co/embed")
                        || url.contains("jawcloud.co/embed")
                        || url.contains("cloudvideo.tv/embed")
                        || url.contains("rapidvideo.com/e/")){
                    Log.i("yess", "url:"+url);

                    view.stopLoading();
                    view.removeAllViews();
                    simpleWebView.removeAllViews();
                    simpleWebView.stopLoading();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent resultIntent = new Intent();

                            resultIntent.putExtra("result", url);
                            resultIntent.putExtra("ARTIST_NAME", artist.getName());
                            resultIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                            resultIntent.putExtra("ARTIST_SERVER", artist.getServer());
                            resultIntent.putExtra("PAGE_URL", shahidPageUrl);
                            resultIntent.putExtra("EXCLUDED", shahidExclude);
                            resultIntent.putExtra("ARTIST_SERVER", artist.getServer());
                            resultIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());
                            setResult(RESULT_OK, resultIntent);
                            simpleWebView.removeView(view);
                            simpleWebView.removeAllViews();
                            simpleWebView.stopLoading();
                            finish();
                        }
                    });
                }
            }
        };
        simpleWebView.setWebViewClient(webViewClient);
        simpleWebView.loadUrl(artist.getUrl());
    }

    public void fetchOldAkwamLinkAndVideo(Artist artist){
        WebViewClient webViewClient1= new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // view.loadUrl(url);
                Log.d("WEBCLIENT", "OnreDirect url:"+url);
                //  fetchLinkVideoOldAkwam(artist, url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent resultIntent = new Intent();

                        resultIntent.putExtra("result", url);
                       resultIntent.putExtra("ARTIST_NAME", artist.getName());
                        resultIntent.putExtra("ARTIST_IMAGE", artist.getImage());
                        resultIntent.putExtra("ARTIST_SERVER", artist.getServer());
                        resultIntent.putExtra("ARTIST_IS_VIDEO", artist.getIsVideo());

                        setResult(RESULT_OK, resultIntent);
                        simpleWebView.removeView(view);
                        simpleWebView.removeAllViews();
                        simpleWebView.stopLoading();
                        finish();
                    }});

                //view.loadUrl(" ");

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
                //ok akwam   view.loadUrl("javascript:location.replace(document.getElementsByClassName(\"download_button\")[0].getAttribute(\"href\").toString());");
                view.loadUrl("javascript:location.replace(document.getElementsByClassName('unauth_capsule clearfix')[0].getElementsByTagName('a')[0].getAttribute('ng-href'));");
               //hh view.loadUrl("javascript:document.getElementsByClassName('unauth_capsule clearfix')[0].getElementsByTagName('a')[0].getAttribute('ng-href');");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                //ok sh        view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+MainActivity.serverCounts+"].click()");
               //hh view.loadUrl("javascript:window.document.getElementsByClassName('unauth_capsule clearfix')[0].getElementsByTagName('a')[0].click()");
                Log.d("WEBCLIENT","onLoadResource");
            }
        };

        //old akwam fetch download page from goo-
        simpleWebView.setWebViewClient(webViewClient1);

        //String url = "http://old.goo-2o.com/5dd7c58d8da14";
        simpleWebView.loadUrl(artist.getUrl());
    }

    public void fetchOldAkwamVideo(Artist artist){
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // view.loadUrl(url);
                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);

                runOnUiThread(new Runnable() {
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
        Log.i("videooo1", artist.getUrl());
        simpleWebView.loadUrl(artist.getUrl());
    }

    public void fetchVidHdVideo(Artist artist){
        String url = artist.getUrl().trim().replaceAll(" ", "");
        if (!url.contains(".html")){
           url= url+".html";
        }
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // view.loadUrl(url);
                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.loadUrl("javascript:windowdocument.getElementsByTagName(\"iframe\")[0].click()");
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WEBCLIENT", "onPageFinished");
             //   view.loadUrl("javascript:document.Android.showToast('hello');");
                //ok  sh       view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
          //      view.loadUrl("javascript:location.replace(document.getElementsByClassName(\"download_button\")[0].getAttribute(\"href\").toString());");

                //hh  view.loadUrl("javascript:location.replace(document.getElementsByClassName(\"download_button\")[0].getAttribute(\"href\").toString());");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
           //     view.loadUrl("javascript:window.document.getElementsByClassName(\"download_button\")[0].click()");

                super.onLoadResource(view, url);
                //ok sh        view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+MainActivity.serverCounts+"].click()");
                Log.d("WEBCLIENT","onLoadResource :url"+url);

                if (url.contains(".mp4")) {
                    Log.i("yess", "url:" + url);
                    runOnUiThread(new Runnable() {
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
                        }
                    });
                    view.stopLoading();
                    finish();
                }
            }
        };
        //fetch video link from last page
        simpleWebView.setWebViewClient(webViewClient);

        //String url2 = "https://old.akwam.co/download/3c472024a3f1/High-Seas-S02-Ep01-720p-WEB-DL-akoam-net-mkv";
        Log.i("shahid videooo1", url);
        simpleWebView.loadUrl(url);
    }

    public void fetchVideoLinkCima4u(Artist artist){
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
          //kk      view.loadUrl("javascript:window.document.getElementsByClassName(\"serversWatchSide\")[0].children[2].click()");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WEBCLIENT", "onPageFinished");
                // if (url.equals(webActivity.link)){
             //   view.loadUrl("javascript:document.Android.showToast('yesss');");
                //}

                // view.loadUrl("javascript:document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString();");
                // view.loadUrl("javascript:document.Android.showToast('document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString()');");

                //view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                //view.loadUrl("javascript:location.replace(document.getElementsByClassName(\"download_button\")[0].getAttribute(\"href\").toString());");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                //   view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+1+"].click()");
                //view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+MainActivity.serverCounts+"].click()");
                //view.loadUrl("javascript:window.document.getElementsByClassName(\"download_button\")[0].click()");
                // view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+1+"].click()");
                Log.d("WEBCLIENT","onLoadResource url:"+url);
                if (url.contains("embed")){
              //  if (url.contains(".mp4")){
                    Log.i("yess", "url:"+url);


                    runOnUiThread(new Runnable() {
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
                }
                //view.loadUrl("javascript:document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString();");
                //view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
            }
        };

        simpleWebView.setWebViewClient(webViewClient);

        //String url = "https://shahid4u.one/watch/%D9%85%D8%B3%D9%84%D8%B3%D9%84-%D8%A7%D9%84%D8%AC%D8%A7%D8%B3%D9%88%D8%B3-%D8%A7%D9%84%D8%B0%D9%8A-%D8%A7%D8%AD%D8%A8%D9%86%D9%8A-the-spy-who-loved-me-%D8%A7%D9%84%D8%AD%D9%84%D9%82%D8%A9-14-%D9%85%D8%AA%D8%B1%D8%AC%D9%85%D8%A9";

        simpleWebView.loadUrl(artist.getUrl());
    }


    @Override
    public void onBackPressed() {
        //check if waiting time between the second click of back button is greater less than 2 seconds so we finish the app
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finish();
        }else {
            Toast.makeText(this, "Press back 2 time to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }


    public void fetchVideoLinkAflamPro(Artist artist){
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);
               // view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //kk      view.loadUrl("javascript:window.document.getElementsByClassName(\"serversWatchSide\")[0].children[2].click()");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WEBCLIENT", "onPageFinished");
                view.loadUrl("javascript:document.getElementsByClassName(\"Video\")[0].children[0].click();");
                view.loadUrl("javascript:document.getElementsByClassName(\"Video\")[0].click()");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                //   view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+1+"].click()");
                //view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+MainActivity.serverCounts+"].click()");
                //view.loadUrl("javascript:window.document.getElementsByClassName(\"download_button\")[0].click()");
                // view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+1+"].click()");
                Log.d("WEBCLIENT","onLoadResource url:"+url);
                if (url.contains("token=")){
                    //  if (url.contains(".mp4")){
                    Log.i("yess", "url:"+url);
                    view.stopLoading();
                    view.removeView(view);
                    view.removeAllViews();
                    simpleWebView.stopLoading();
                    simpleWebView.removeAllViews();

               /*     String type = "video/*"; // It works for all video application
                    Uri uri = Uri.parse(url);
                    Intent in1 = new Intent(Intent.ACTION_VIEW, uri);
                    in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  in1.setPackage("org.videolan.vlc");
                    in1.setDataAndType(uri, type);
                    startActivity(in1);

                */

                   // finish();


                  runOnUiThread(new Runnable() {
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
                }
                //view.loadUrl("javascript:document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString();");
                //view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
            }
        };

        simpleWebView.setWebViewClient(webViewClient);

        //String url = "https://shahid4u.one/watch/%D9%85%D8%B3%D9%84%D8%B3%D9%84-%D8%A7%D9%84%D8%AC%D8%A7%D8%B3%D9%88%D8%B3-%D8%A7%D9%84%D8%B0%D9%8A-%D8%A7%D8%AD%D8%A8%D9%86%D9%8A-the-spy-who-loved-me-%D8%A7%D9%84%D8%AD%D9%84%D9%82%D8%A9-14-%D9%85%D8%AA%D8%B1%D8%AC%D9%85%D8%A9";

        simpleWebView.loadUrl(artist.getUrl());
    }

    public void fetchVideoLinkFaselHd(Artist artist){
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);
                // view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //kk      view.loadUrl("javascript:window.document.getElementsByClassName(\"serversWatchSide\")[0].children[2].click()");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WEBCLIENT", "onPageFinished");
                //view.loadUrl("javascript:document.getElementsByClassName(\"Video\")[0].children[0].click()");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                //   view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+1+"].click()");
                //view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+MainActivity.serverCounts+"].click()");
                //view.loadUrl("javascript:window.document.getElementsByClassName(\"download_button\")[0].click()");
                // view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+1+"].click()");
                Log.d("WEBCLIENT","onLoadResource url:"+url);
                view.loadUrl("javascript:document.getElementById(\"vihtml\").click()");
                view.loadUrl("javascript:window.document.getElementsByName(\"player_iframe\")[0].click()");
                view.loadUrl("javascript:window.document.getElementsByName(\"player_iframe\")[0].click()");
                if (url.contains("token=")){
                    //  if (url.contains(".mp4")){
                    Log.i("yess", "url:"+url);
                    view.stopLoading();
                    view.removeView(view);
                    view.removeAllViews();
                    simpleWebView.stopLoading();
                    simpleWebView.removeAllViews();

               /*     String type = "video/*"; // It works for all video application
                    Uri uri = Uri.parse(url);
                    Intent in1 = new Intent(Intent.ACTION_VIEW, uri);
                    in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //  in1.setPackage("org.videolan.vlc");
                    in1.setDataAndType(uri, type);
                    startActivity(in1);

                */

                    // finish();


                    runOnUiThread(new Runnable() {
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
                }
                //view.loadUrl("javascript:document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString();");
                //view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
            }
        };

        simpleWebView.setWebViewClient(webViewClient);

        //String url = "https://shahid4u.one/watch/%D9%85%D8%B3%D9%84%D8%B3%D9%84-%D8%A7%D9%84%D8%AC%D8%A7%D8%B3%D9%88%D8%B3-%D8%A7%D9%84%D8%B0%D9%8A-%D8%A7%D8%AD%D8%A8%D9%86%D9%8A-the-spy-who-loved-me-%D8%A7%D9%84%D8%AD%D9%84%D9%82%D8%A9-14-%D9%85%D8%AA%D8%B1%D8%AC%D9%85%D8%A9";

        simpleWebView.loadUrl(artist.getUrl()+"#vihtml");
    }
}