package com.example.daniel.broadcastservicetest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;


public class TestService extends Service {

    final String ACTION_TO_ACTIVITY = "com.example.daniel.broadcasting.ACTION_TO_ACTIVITY";
    final String ACTION_TO_SERVICE = "com.example.daniel.broadcasting.ACTION_TO_SERVICE";
    Context context;
    TimerTask timerTask = new TimerTask();
    int currentTime = 0;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        context = this;

        IntentFilter intentFilter = new IntentFilter(ACTION_TO_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(br, intentFilter);

        timerTask.execute(intent.getExtras().getInt("maxTime", currentTime));

        return START_STICKY;
    }

    private class TimerTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... args) {
            int maxTime = args[0];

            while (currentTime <= maxTime && !isCancelled()) {
                Intent i = new Intent(ACTION_TO_ACTIVITY);
                i.putExtra("currentTime", currentTime);
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                currentTime++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Done!";
        }

        @Override
        protected void onPostExecute(String result) {
            Intent i = new Intent(ACTION_TO_ACTIVITY);
            i.putExtra("currentTime", 0);
            i.putExtra("status", "Done");
            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerTask.cancel(true);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            timerTask.cancel(true);
            stopSelf();
        }
    };
}
