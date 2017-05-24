package com.example.thienan.fakecall;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class FakeRingingActivity extends Activity {

    private String networkCarrier;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_ringing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView fakeName = (TextView) findViewById(R.id.chosenfakename);
        TextView fakeNumber = (TextView) findViewById(R.id.chosenfakenumber);

        final TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        networkCarrier = tm.getNetworkOperatorName();

        TextView titleBar = (TextView) findViewById(R.id.textView1);
        if (networkCarrier != null) {
            titleBar.setText("Incoming call - " + networkCarrier);
        } else {
            titleBar.setText("Incoming call");
        }

        final String callNumber = getContactNumber();
        final String callName = getContactName();

        fakeName.setText(callName);
        fakeNumber.setText(callNumber);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();

        final ImageButton answerCall = (ImageButton) findViewById(R.id.answercall);
        final ImageButton rejectCall = (ImageButton) findViewById(R.id.rejectcall);

        answerCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mp.stop();
                if (callName.equals("")) {
                    insertPlaceholderCall(getContentResolver(), callNumber);
                } else {
                    insertPlaceholderCall(getContentResolver(), callName);
                }
                Intent callIntent = new Intent(FakeRingingActivity.this, IncomingCall.class);
                callIntent.putExtra("FAKENAME", callName);
                callIntent.putExtra("FAKENUMBER", callNumber);
                FakeRingingActivity.this.startActivity(callIntent);
            }
        });
        rejectCall.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                mp.stop();
                if (callName.equals("")) {
                    insertPlaceholderCall(getContentResolver(), callNumber);
                } else {
                    insertPlaceholderCall(getContentResolver(), callName);
                }

                finish();
                System.exit(0);
            }
        });
    }

    public void insertPlaceholderCall(ContentResolver contentResolver, String number) {
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, number);
        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
        values.put(CallLog.Calls.DURATION, 0);
        values.put(CallLog.Calls.TYPE, CallLog.Calls.OUTGOING_TYPE);
        values.put(CallLog.Calls.NEW, 1);
        values.put(CallLog.Calls.CACHED_NAME, "");
        values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
        values.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
        Log.d(TAG, "Inserting call log placeholder for " + number);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        contentResolver.insert(CallLog.Calls.CONTENT_URI, values);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getContactNumber(){
        String contact = null;
        Intent myIntent = getIntent();
        Bundle mIntent = myIntent.getExtras();
        if(mIntent != null){
            contact  = mIntent.getString("myfakenumber");
        }
        return contact;
    }

    private String getContactName(){
        String contactName = null;
        Intent myIntent = getIntent();
        Bundle mIntent = myIntent.getExtras();
        if(mIntent != null){
            contactName  = mIntent.getString("myfakename");
        }
        return contactName;
    }
}
