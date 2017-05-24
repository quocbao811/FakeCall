package com.example.thienan.fakecall;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class showContact extends Activity {

    private ListView listView;
    private ArrayList<Contact> data;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        data = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);
        readContact();
        adapter = new CustomAdapter(this, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Contact value = (Contact)adapter.getItemAtPosition(position);
                String a = value.getName().toString();
                String b = value.getNumber().toString();
                //Toast.makeText(showContact.this, value.getNumber().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                Bundle c = new Bundle();
                c.putString("name",a);
                c.putString("number", b);
                intent.putExtra("data2", c);
                setResult(0,intent);
                end();
            }
        });
    }

    private void end(){
        finish();
    }

    private void readContact() {
        // select name, lookup_key from contact
        Uri address = ContactsContract.Contacts.CONTENT_URI;

        String NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String LOOKUP_KEY = ContactsContract.Contacts.LOOKUP_KEY;

        String[] projection = new String[]{NAME, LOOKUP_KEY};

        Cursor cursor = getContentResolver().query(address, null, null, null, null);

        while (cursor.moveToNext()) {
            String lookupkey = cursor.getString(cursor.getColumnIndex(LOOKUP_KEY));


            Uri addressDetails = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String CONTACT_NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
            String CONTACT_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

            String column = ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY;

            String[] lookup_key = new String[]{lookupkey};

            String[] proj = new String[]{CONTACT_NAME, CONTACT_NUMBER};

            Cursor c = 	getContentResolver().query(addressDetails, proj, column + " = ?", lookup_key, null);
            while(c.moveToNext()) {
                String name = c.getString(c.getColumnIndex(CONTACT_NAME));
                String number = c.getString(c.getColumnIndex(CONTACT_NUMBER));

                data.add(new Contact(name, number));
            }
            c.close();
        }
        cursor.close();
    }
}
