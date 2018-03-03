package com.example.daniel.broadcastservicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by daniel on 03.03.18.
 */

public class MyTestBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getExtras().getString("jaja", "nei"), Toast.LENGTH_LONG).show();
    }
}
