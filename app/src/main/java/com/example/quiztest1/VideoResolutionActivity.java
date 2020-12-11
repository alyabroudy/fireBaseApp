package com.example.quiztest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VideoResolutionActivity extends AppCompatActivity {

    WebView simpleWebView;
     String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_resolution);
        simpleWebView = (WebView) findViewById(R.id.webView);
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.getSettings().setBlockNetworkImage(true);
        simpleWebView.getSettings().setAppCacheEnabled(false);
        simpleWebView.getSettings().setLoadsImagesAutomatically(false);
        simpleWebView.getSettings().setDomStorageEnabled(true);

        
        getSupportActionBar().hide();

        url = getIntent().getStringExtra("URL");
        simpleWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.i("Redirect", url);
                //view.loadUrl("");
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
               // view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
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
               // view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children["+ServersListActivity.shahidServersIdCounter+"].click()");

                //view.loadUrl("javascript:window.Android.showToast(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                // view.loadUrl("javascript:location.replace(document.getElementsByTagName(\"iframe\")[0].getAttribute(\"src\").toString());");
                //  }

                // view.loadUrl("javascript:window.document.getElementsByClassName(\"servers-list\")[0].children[1].click()");
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        simpleWebView.loadUrl(url);
    }



}