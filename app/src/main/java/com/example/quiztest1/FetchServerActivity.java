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

public class FetchServerActivity extends AppCompatActivity {

    static int shahidServersCounter = 0;
    Artist artist;
    Intent severListIntent;
    WebView simpleWebView;
    int pageNumber;
    int serverId;

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
        else if (pageNumber == 4){// fetch video link
            Log.i("pageNumer", pageNumber+"");
            fetchVidHdVideo(artist);
           // shahid(artist);
        }
        else if (pageNumber == 5){// fetch video link
            Log.i("pageNumer", pageNumber+"");
            fetchShahidOtherServers(artist);
            // shahid(artist);
        }

    }

    public void fetchShahidOtherServers(Artist artist){
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                FetchServerActivity.shahidServersCounter = 2;
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+serverId+"].click()");
                super.onPageFinished(view, url);
                Log.d("WEBCLIENT", "onPageFinished");
                }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.d("WEBCLIENT","onLoadResource url:"+url);
                if (url.contains("/embed") || url.contains("videoembed")){
                    //   if (url.contains(".mp4")){
                    Log.i("yess", "url:"+url);

                    if (FetchServerActivity.shahidServersCounter == 0) {
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
                                //  simpleWebView.loadUrl(" ");
                                simpleWebView.stopLoading();
                                finish();
                            }
                        });
                        view.stopLoading();
                        finish();
                    }
                    FetchServerActivity.shahidServersCounter--;
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
                // MainActivity.linkTwo = url;
                //kk     view.loadUrl("");


                // Intent data = new Intent();
                String text = "Result to be returned....";
//---set the data to pass back---
                // data.setData(Uri.parse(url));
                // setResult(RESULT_OK, data);
//---close the activity---
                // finish();

                // getIntent().putExtra("URL", url);



                Log.d("WEBCLIENT 22", "OnreDirect url:"+url);
                view.loadUrl(url);
                // if (!url.contains("/download/")){

                //}
                //     view.destroy();
                //MainActivity.serverCounts--;
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


}