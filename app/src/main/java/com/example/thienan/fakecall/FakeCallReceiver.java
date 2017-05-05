package com.example.thienan.fakecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ThienAn on 4/10/2017.
 */

public class FakeCallReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        String getFakeName = intent.getStringExtra("FAKENAME");
        String getFakePhoneNumber = intent.getStringExtra("FAKENUMBER");
        Intent intentObject = new Intent(context.getApplicationContext(), FakeRingingActivity.class);
        intentObject.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentObject.putExtra("myfakename", getFakeName);
        intentObject.putExtra("myfakenumber", getFakePhoneNumber);
        context.startActivity(intentObject);

    }
}
