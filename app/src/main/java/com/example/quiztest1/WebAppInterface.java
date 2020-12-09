package com.example.quiztest1;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
    Context mContext;


    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        // Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        Log.i("toast", toast);

        if (MainActivity.serverCounts> 0 && toast.trim().contains("http") && !contain(toast)){
          MainActivity.serverList.add(toast);
            MainActivity.serverCounts--;
        }
        Log.i("serverList size", MainActivity.serverList.size()+"");
        Log.i("serverList item", MainActivity.serverList.get(0)+"");
        Log.i("serverList item", MainActivity.serverList.get(1)+"");
        Log.i("serverList item", MainActivity.serverList.get(2)+"");
        Log.i("serverList item", MainActivity.serverList.get(3)+"");
    }

    public boolean contain(String item){

        for (String i: MainActivity.serverList){
            if (getName(i).equals(getName(item)) ){
                return true;
            }
        }
        Log.i("contains", getName(item));
        return false;
    }

    public String getName(String item){
        return item.substring(item.indexOf("//")+1, item.indexOf('.'));
    }
}


