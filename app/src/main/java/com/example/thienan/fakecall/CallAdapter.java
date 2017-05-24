package com.example.thienan.fakecall;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by quocb on 24/5/2017.
 */

class CallAdapter extends ArrayAdapter<String>{
    private Context context;
    private List<String> data;

    public CallAdapter(Context context, List<String> objects) {
        super(context, 0, objects);

        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_call_item, parent, false);

        TextView tvName = (TextView) view.findViewById(R.id.tvCallItem);
        ImageView ivItem = (ImageView) view.findViewById(R.id.ivItem);

        String item = data.get(position);

        tvName.setText(item);
        if(item.equals("Add call")) {
            ivItem.setImageResource(R.mipmap.ic_plus);
        }else if (item.equals("Bluetooth")) {
            ivItem.setImageResource(R.mipmap.ic_bluetooth);
        }else if (item.equals("Keypad")) {
            ivItem.setImageResource(R.mipmap.ic_keypad);
        }else if (item.equals("Mute")) {
            ivItem.setImageResource(R.mipmap.ic_mute);
        }else if (item.equals("Extra Volume")) {
            ivItem.setImageResource(R.mipmap.ic_volume);
        }else {
            ivItem.setImageResource(R.mipmap.ic_speaker);
        }

        return view;
    }
}
