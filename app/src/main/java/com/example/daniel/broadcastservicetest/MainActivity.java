package com.example.daniel.broadcastservicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final String ACTION_TO_ACTIVITY = "com.example.daniel.broadcasting.ACTION_TO_ACTIVITY";
    final String ACTION_TO_SERVICE = "com.example.daniel.broadcasting.ACTION_TO_SERVICE";

    Button b;
    EditText et;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = findViewById(R.id.button);
        et = findViewById(R.id.editText);
        tv = findViewById(R.id.textView);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b.getText().equals("Start Timer")) {
                    if (!et.getText().toString().equals("")) {
                        Intent i = new Intent(MainActivity.this, TestService.class);
                        i.putExtra("maxTime", Integer.parseInt(et.getText().toString()));
                        startService(i);
                        b.setText("Stop Timer");
                        et.setText("");
                        et.setVisibility(EditText.GONE);
                        tv.setVisibility(TextView.VISIBLE);
                    } else {
                        Toast.makeText(MainActivity.this, "Enter Time", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent i = new Intent(ACTION_TO_SERVICE);
                    LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(i);
                    resetFields();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ACTION_TO_ACTIVITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(br, intentFilter);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tv.setText(String.valueOf(intent.getExtras().getInt("currentTime", 0)));

            if (intent.getExtras().getString("status") != null) {
                resetFields();
            }
        }
    };

    private void resetFields() {
        b.setText("Start Timer");
        et.setVisibility(EditText.VISIBLE);
        tv.setVisibility(TextView.INVISIBLE);
    }
}
