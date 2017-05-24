package com.example.thienan.fakecall;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends Activity {

    private EditText fakeName;
    private EditText fakeNumber;
    private EditText tvCustom;

    private RadioGroup radioGroup;

    private ImageButton btnCT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fakeName = (EditText) findViewById(R.id.fakename);
        fakeNumber = (EditText) findViewById(R.id.fakenumber);
        tvCustom = (EditText) findViewById(R.id.edCustom);
        tvCustom.setEnabled(false);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(id);
                if(radioButton.getId()==R.id.radio4)
                {
                    tvCustom.setEnabled(true);
                }
                else
                {
                    tvCustom.setEnabled(false);
                }
            }
        });

        final Intent intent = new Intent(this, showContact.class);

        btnCT = (ImageButton) findViewById(R.id.btnDB);
        btnCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 0);
            }
        });

        final Button fakeCallButton = (Button) findViewById(R.id.fakecalls);
        fakeCallButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int radioSelected = radioGroup.getCheckedRadioButtonId();
                int radioTimeSelected = getSelectedAnswer(radioSelected);
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, "You must select a time", Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.SECOND, radioTimeSelected);
                long currentFakeTime = calendar.getTimeInMillis();

                String fakeNameEntered = fakeName.getText().toString();
                fakeNameEntered = fakeNameEntered.trim();
                String fakeNumberEntered = fakeNumber.getText().toString();
                fakeNumberEntered = fakeNumberEntered.trim();
                if (fakeNumberEntered.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Please enter number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setUpAlarm(currentFakeTime, fakeNameEntered, fakeNumberEntered);
                end();
            }
        });


    }

    private void end() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle d = data.getBundleExtra("data2");
        fakeName.setText(d.getString("name"));
        fakeNumber.setText(d.getString("number"));
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

    private int getSelectedAnswer(int radioSelected) {
        int answerSelected = 0;
        if (radioSelected == R.id.radio0) {
            answerSelected = 10;
        }
        if (radioSelected == R.id.radio1) {
            answerSelected = 30;
        }
        if (radioSelected == R.id.radio2) {
            answerSelected = 60;
        }
        if (radioSelected == R.id.radio4) {
            answerSelected = Integer.parseInt(tvCustom.getText().toString());
        }
        return answerSelected;
    }

    public void setUpAlarm(long selectedTimeInMilliseconds, String fakeName, String fakeNumber) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, FakeCallReceiver.class);

        intent.putExtra("FAKENAME", fakeName);
        intent.putExtra("FAKENUMBER", fakeNumber);

        PendingIntent fakePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, selectedTimeInMilliseconds, fakePendingIntent);
        Toast.makeText(getApplicationContext(), "Your fake call time has been set", Toast.LENGTH_SHORT).show();
    }
}
