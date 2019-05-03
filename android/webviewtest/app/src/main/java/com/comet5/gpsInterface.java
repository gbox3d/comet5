package com.comet5;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.comet5.fileInterface.What_getFileList;


public class gpsInterface extends Handler implements LocationListener {

    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    public final static String TAG = "WIOgps";

    final static int What_Start = 0;
    final static int What_getLastPosition = 1;
    final static int What_getError = 2;

    WebView mWebView;
    LocationManager locationManager;
    Location loc;

    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;


    public gpsInterface(WebView _wv, Activity _activity) {
        mWebView = _wv;


        mWebView.addJavascriptInterface(this,TAG);

        locationManager = (LocationManager) _activity.getSystemService(Context.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            //getLastLocation();
        } else {
            Log.d(TAG, "Connection on");
            // check permissions
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                if (permissionsToRequest.size() > 0) {
//                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
//                            ALL_PERMISSIONS_RESULT);
//                    Log.d(TAG, "Permission requests");
//                    canGetLocation = false;
//                }
//            }

            // get location
            //getLocation();
        }
    }

    @JavascriptInterface
    public void start()
    {
        Log.d(TAG, "start getPosition");
        getLocation();

        this.obtainMessage(What_Start,0,0,loc)
                .sendToTarget();
    }

    @JavascriptInterface
    public void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            loc = locationManager.getLastKnownLocation(provider);
            //Log.d(TAG, provider);
            //Log.d(TAG, location == null ? "NO LastLocation" : location.toString());

            this.obtainMessage(What_getLastPosition,0,0,loc)
                    .sendToTarget();

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch(msg.what) {
            case What_getLastPosition:
            {
                JSONObject _jsonTemp = new JSONObject();
                try {
                    if (msg.arg1 == 0) {
                        _jsonTemp.put("result", "ok");

                        JSONObject _coord = new JSONObject();


                        _coord.put("accuracy", loc.getAccuracy());
                        _coord.put("latitude", loc.getLatitude());
                        _coord.put("longitude", loc.getLongitude());
                        _coord.put("time", loc.getTime());

                        _jsonTemp.put("coords",_coord);

                    } else {
                        _jsonTemp.put("result", "err");

                    }

                    mWebView.loadUrl("javascript:" + TAG + ".OnCallbackGps(" + _jsonTemp.toString() + ");");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    mWebView.loadUrl("javascript:" + TAG + ".OnErrorGps(" + e.toString() + ");");
                }

            }
                break;
            case What_getError:
            {
                JSONObject _jsonTemp = new JSONObject();
                try {
                    _jsonTemp.put("result", "err");

                    mWebView.loadUrl("javascript:" + TAG + ".OnCallbackGps(" + _jsonTemp.toString() + ");");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    mWebView.loadUrl("javascript:" + TAG + ".OnErrorGps(" + e.toString() + ");");
                }

            }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        //updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
        //getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                        {


                            //updateUI(loc);
                        }

                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null) {
                            //updateUI(loc);
                        }
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    //updateUI(loc);
                }
                this.obtainMessage(What_getLastPosition,0,0,loc)
                        .sendToTarget();
            } else {
                Log.d(TAG, "Can't get location");
                this.obtainMessage(What_getError,0,0,null)
                        .sendToTarget();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            this.obtainMessage(What_getError,0,0,e)
                    .sendToTarget();
        }
    }



}
