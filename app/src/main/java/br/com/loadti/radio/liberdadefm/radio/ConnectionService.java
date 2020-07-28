package br.com.loadti.radio.liberdadefm.radio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by TI on 19/05/2017.
 */

public class ConnectionService extends Service {

    // Constant
    public static String TAG_INTERVAL = "interval";
    public static String TAG_URL_PING = "url_ping";
    public static String TAG_ACTIVITY_NAME = "activity_name";
    private static final String TAG = "ConnectionService";

    private int interval;
    private String url_ping;
    private String activity_name;

    private Timer mTimer = null;

    ConnectionServiceCallback mConnectionServiceCallback;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface ConnectionServiceCallback {
        void hasInternetConnection();

        void hasNoInternetConnection();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        interval = intent.getIntExtra(TAG_INTERVAL, 10);
        url_ping = intent.getStringExtra(TAG_URL_PING);
        activity_name = intent.getStringExtra(TAG_ACTIVITY_NAME);

        try {
            mConnectionServiceCallback = (ConnectionServiceCallback) Class.forName(activity_name).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new CheckForConnection(), 0, interval * 1000);

        return super.onStartCommand(intent, flags, startId);
    }

    class CheckForConnection extends TimerTask {
        @Override
        public void run() {
            isNetworkAvailable();
        }
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    private boolean isNetworkAvailable() {

        int timeoutConnection = 3000;
        HttpURLConnection httpURLConnection = null;
        url_ping = url_ping.replaceFirst("https", "http");
        try {

            httpURLConnection = (HttpURLConnection) new URL(url_ping).openConnection();


            httpURLConnection.setConnectTimeout(timeoutConnection);
            httpURLConnection.setReadTimeout(timeoutConnection);
            int responseCode = httpURLConnection.getResponseCode();


            mConnectionServiceCallback.hasInternetConnection();
            Log.d(TAG, "isNetwordkAvalilable - Conectado com a internet");
            return (200 <= responseCode && responseCode <= 399);


        } catch (Exception e) {

            Log.e(TAG, "isNetwordkAvalilable - Exception" + e.getMessage());
            mConnectionServiceCallback.hasNoInternetConnection();
        } finally {
            if (httpURLConnection != null) {

                httpURLConnection.disconnect();

            }
        }
        mConnectionServiceCallback.hasNoInternetConnection();
        Log.d(TAG, "isNetwordkAvalilable - Sem internet");
        return false;

    }


}
