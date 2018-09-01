package com.example.gbox3d.webviewtest;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.security.NetworkSecurityPolicy;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.comet5.dummyWebInterface;
import com.comet5.fileInterface;

import java.lang.reflect.Method;

//import com.comet5;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 화면을 portrait(세로) 화면으로 고정하고 싶은 경우
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 화면을 landscape(가로) 화면으로 고정하고 싶은 경우
        //출처: http://proletariat.tistory.com/86 [프롤레타리아, IT에 범접하다.]


        //레이아웃 등록
        setContentView(R.layout.activity_main);

        mWebView =  (WebView)findViewById(R.id.id_webview);

        //웹뷰시작
        com.comet5.core.startwebview("http://192.168.0.15:3000/",mWebView,this);

    }
}
