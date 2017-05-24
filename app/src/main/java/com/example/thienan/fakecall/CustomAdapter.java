package com.example.thienan.fakecall;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ThienAn on 5/4/2017.
 */

public class CustomAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private List<Contact> data;

    public CustomAdapter(Context context, List<Contact> objects) {
        super(context, 0, objects);

        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview, parent, false);

        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvNum = (TextView) view.findViewById(R.id.tvNumber);

        Contact item = data.get(position);

        tvName.setText(item.getName());
        tvNum.setText(item.getNumber());

        return view;
    }
}
