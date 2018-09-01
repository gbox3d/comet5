package com.comet5;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class dummyWebInterface extends Handler {

    private String TAG = "dummyWIO";

    private WebView mWebView;

    public dummyWebInterface(WebView webView) {

        mWebView = webView;

        mWebView.addJavascriptInterface( this,
                TAG);
    }

    //----------------------------------------------------------------------------------------------
    @JavascriptInterface
    public void Test(String arg1) {

        Log.d(TAG, "msg :" + arg1);

        this.obtainMessage(0,0,0,"msg : \"" + arg1+"\"")
                .sendToTarget();

//        mHandler.obtainMessage(0, 0, 0, "test :" + arg1)
//                .sendToTarget();

    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {

            case 0:
                Log.d(TAG,"javascript:"+ TAG +".OnCallback("+ (String) msg.obj +");");
                mWebView.loadUrl("javascript:"+ TAG +".OnCallback({"+ (String) msg.obj +"});");

                break;
        }
        super.handleMessage(msg);

    }
}
