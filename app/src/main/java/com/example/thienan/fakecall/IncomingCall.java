package com.example.thienan.fakecall;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by quocb on 24/5/2017.
 */

public class IncomingCall extends Activity {
    TextView timerTextView;
    long startTime = 0;
    TextView fakeName;
    TextView fakePhoneNumber;
    ImageButton btnEndCall;

    private GridView gridView;
    private ArrayList<String> data;
    private CallAdapter adapter;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_answer);

        gridView = (GridView) findViewById(R.id.gvGrid);
        data = new ArrayList<>();
        data.add("Add call");
        data.add("Extra volume");
        data.add("Bluetooth");
        data.add("Speaker");
        data.add("Keypad");
        data.add("Mute");
        adapter = new CallAdapter(this, data);
        gridView.setAdapter(adapter);


        Intent intent = getIntent();
        final String stringFakeName = intent.getStringExtra("FAKENAME");
        String stringFakePhoneNumber = intent.getStringExtra("FAKENUMBER");

        timerTextView = (TextView) findViewById(R.id.tvTimer);
        fakeName = (TextView) findViewById(R.id.tvAnsName);
        fakePhoneNumber = (TextView) findViewById(R.id.tvAnsNumber);
        btnEndCall = (ImageButton) findViewById(R.id.btnEndCall);

        fakeName.setText(stringFakeName);
        fakePhoneNumber.setText(stringFakePhoneNumber);

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        btnEndCall.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        });

//        Button b = (Button) findViewById(R.id.btnStart);
//        b.setText("start");
//        b.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Button b = (Button) v;
//                if (b.getText().equals("stop")) {
//                    timerHandler.removeCallbacks(timerRunnable);
//                    b.setText("start");
//                } else {
//                    startTime = System.currentTimeMillis();
//                    timerHandler.postDelayed(timerRunnable, 0);
//                    b.setText("stop");
//                }
//            }
//        });
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        timerHandler.removeCallbacks(timerRunnable);
//        Button b = (Button)findViewById(R.id.btnStart);
//        b.setText("start");
//    }
}
