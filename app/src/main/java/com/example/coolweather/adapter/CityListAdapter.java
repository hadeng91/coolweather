package com.example.coolweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.coolweather.R;

import java.util.List;

/**
 * Created by Machenike on 2017/9/9.
 */

public class CityListAdapter extends ArrayAdapter<String> {
    public CityListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.city_list_item, null);
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.city_textview);
        textView.setText(getItem(position));
        return view;
    }
}
