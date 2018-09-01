package com.comet5;

import android.app.Activity;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.gbox3d.webviewtest.R;

public class core {
    public static dummyWebInterface m_dummyWIO;
    public static fileInterface m_WIOFile;
    public static gpsInterface m_WIOgps;

    public static void startwebview(String appUrl, WebView mWebView, Activity _activity)
    {

        m_dummyWIO = new dummyWebInterface(mWebView);
        m_WIOFile = new fileInterface(mWebView);
        m_WIOgps = new gpsInterface(mWebView,_activity);


        WebSettings set = mWebView.getSettings();
        set.setJavaScriptEnabled(true); // javascript를 실행할 수 있도록 설정

        //크로스도메인허용
        set.setPluginState(WebSettings.PluginState.ON);
        set.setAllowFileAccess(true);
        set.setAllowContentAccess(true);
        set.setAllowFileAccessFromFileURLs(true);
        set.setAllowUniversalAccessFromFileURLs(true);

        set.setJavaScriptCanOpenWindowsAutomatically(true);// javascript가 window.open()을 사용할 수 있도록 설정
        set.setBuiltInZoomControls(true); // 안드로이드에서 제공하는 줌 아이콘을 사용할 수 있도록 설정
        //set.setPluginState(WebSettings.PluginState.ON_DEMAND); // 플러그인을 사용할 수 있도록 설정
        //set.setSupportMultipleWindows(true); // 여러개의 윈도우를 사용할 수 있도록 설정
        set.setSupportZoom(true); // 확대,축소 기능을 사용할 수 있도록 설정
        set.setBlockNetworkImage(false); // 네트워크의 이미지의 리소스를 로드하지않음
        set.setLoadsImagesAutomatically(true); // 웹뷰가 앱에 등록되어 있는 이미지 리소스를 자동으로 로드하도록 설정
        set.setUseWideViewPort(true); // wide viewport를 사용하도록 설정
        set.setCacheMode(WebSettings.LOAD_NO_CACHE); // 웹뷰가 캐시를 사용하지 않도록 설정

        set.setDomStorageEnabled(true); //로컬스토리지 사용허용

        //줌인기능 제거
        set.setBuiltInZoomControls(true);
        set.setSupportZoom(false);
        //set.setDisplayZoomControls(false);

        //원격디버깅하도록...

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //if ( 0 != ( getApplcationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) ) {
            WebView.setWebContentsDebuggingEnabled(true);
            //}
        }

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false; //내부브라우져로 열기
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.clearHistory();
        mWebView.clearFormData();
        mWebView.clearCache(true);
        //mWebView.loadUrl("http://192.168.0.15:3000/");
        mWebView.loadUrl(appUrl);
    }
}
