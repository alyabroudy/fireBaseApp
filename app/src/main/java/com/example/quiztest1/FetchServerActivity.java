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

    Artist artist;
    Intent severListIntent;
    WebView simpleWebView;
    int pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_server);

        simpleWebView = (WebView) findViewById(R.id.webView);
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.getSettings().setBlockNetworkImage(true);
        simpleWebView.getSettings().setAppCacheEnabled(false);
        simpleWebView.getSettings().setLoadsImagesAutomatically(false);
        simpleWebView.setVisibility(View.GONE);
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
        }
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




}