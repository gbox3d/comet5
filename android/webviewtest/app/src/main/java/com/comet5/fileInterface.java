package com.comet5;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;

public class fileInterface extends Handler {

    private String TAG = "WIOFile";

    WebView mWebView;

    final static int What_Version = 0;
    final static int What_getFileList = 1;
    final static int What_writeFile = 2;
    final static int What_readFile = 3;


    public fileInterface(WebView _wv) {
        mWebView = _wv;

        mWebView.addJavascriptInterface(this,TAG);
    }

    @JavascriptInterface
    public void Version() {

        //Log.d(TAG, "msg :" + arg1);

        this.obtainMessage(0,0,0,"100")
                .sendToTarget();

    }

    @JavascriptInterface
    public  void getFileList(String szPath) {

        /*File directory = new File(szPath);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }
        */
        JSONObject _obj = OnGetFileList(szPath);

        this.obtainMessage(What_getFileList,0,0,_obj)
                .sendToTarget();
    }

    private JSONObject OnGetFileList(String FILE_PATH) {

        File files = new File(FILE_PATH);

        File[] file_list = files.listFiles(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return true;
                    }
                }
        );

        JSONObject json_obj = new JSONObject();
        JSONArray json_ary = new JSONArray();

        Log.d(TAG,"file num :" + file_list.length);

        for(File file : file_list )
        {
            JSONObject _obj = new JSONObject();
            try {
                _obj.put("name",file.getName());
                _obj.put("isFile",file.isFile());
                _obj.put("length",file.length());

                json_ary.put(_obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //json_ary.put(file.getName());
            //Log.d(TAG,file.getName());
        }

        try {
            json_obj.put("file_list",json_ary);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json_obj;

    }

    @JavascriptInterface
    public void write( String filename, String data) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(filename);
            output.write(data.getBytes(), 0, data.length());

            output.flush();
            output.close();

            this.obtainMessage(What_writeFile,0,0,null)
                    .sendToTarget();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.obtainMessage(What_writeFile,1,0,e)
                    .sendToTarget();
        } catch (IOException e) {
            this.obtainMessage(What_writeFile,2,0,e)
                    .sendToTarget();
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void read(String filename) {
        try {

            //file->object
            FileInputStream fis = new FileInputStream(filename);
            byte[] data = new byte[fis.available()];

            while (fis.read(data) != -1) {}

            Log.d(TAG, new String(data));
            this.obtainMessage(What_readFile,0,0,data)
                    .sendToTarget();

        } catch (FileNotFoundException e) {
            this.obtainMessage(What_readFile,1,0,e)
                    .sendToTarget();
            e.printStackTrace();
        } catch (IOException e) {
            this.obtainMessage(What_readFile,2,0,e)
                    .sendToTarget();
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Message msg) {

        switch(msg.what)
        {
            case What_Version:
            {
//                Log.d(TAG,"javascript:"+ TAG +".OnCallback("+ (String) msg.obj +");");
                mWebView.loadUrl("javascript:"+ TAG +".OnCallbackVersion(" + (String) msg.obj + ");");
            }
            break;
            case What_getFileList:
            {
                JSONObject _jsonTemp = new JSONObject();
                mWebView.loadUrl("javascript:"+ TAG +".OnCallbackGetFileList(" + msg.obj.toString() + ");");

            }
            break;
            case What_writeFile:
            {
                JSONObject _jsonTemp = new JSONObject();
                try {
                    if (msg.arg1 == 0) {
                        _jsonTemp.put("result", "ok");
                    } else {
                        _jsonTemp.put("result", "err");
                        _jsonTemp.put("err", msg.obj.toString());

                    }

                    mWebView.loadUrl("javascript:" + TAG + ".OnCallbackWriteFile(" + _jsonTemp.toString() + ");");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            break;
            case What_readFile:
            {
                JSONObject _jsonTemp = new JSONObject();
                try {
                    if (msg.arg1 == 0) {
                        _jsonTemp.put("result", "ok");
                        _jsonTemp.put("data", new String( (byte [])msg.obj ) );
                    } else {
                        _jsonTemp.put("result", "err");
                        _jsonTemp.put("err", msg.obj.toString());

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                mWebView.loadUrl("javascript:" + TAG + ".OnCallbackReadFile(" + _jsonTemp.toString() + ");");


            }
            break;


        }

        super.handleMessage(msg);

    }
}
