package com.example.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "CityManagerActivity";

    private Button addCity;

    private ListView citylistView;

    private ArrayAdapter<String> adapter;

    private List<String> cityList = new ArrayList<>();

    private HashSet<String> citySet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        citylistView = (ListView) findViewById(R.id.city_list);
        adapter = new ArrayAdapter<String>(CityManagerActivity.this,
                android.R.layout.simple_list_item_1, cityList);
        citylistView.setAdapter(adapter);
        addCity = (Button) findViewById(R.id.add_city);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        citySet = new HashSet<String>(preferences.getStringSet(
                "cities", new HashSet<String>()
        ));
        if (citySet != null) {
            cityList.clear();
            for (Iterator iterator = citySet.iterator(); iterator.hasNext();) {
                try {
                    String city = iterator.next().toString();
                    cityList.add(city);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            adapter.notifyDataSetChanged();
            citylistView.setSelection(0);
            citylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String city = cityList.get(i);
                    Intent intent = new Intent(CityManagerActivity.this, WeatherActivity.class);
                    intent.putExtra("city", city);
                    startActivity(intent);
                    //finish();
                }
            });
        }
        addCity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_city:
                Intent intent = new Intent(CityManagerActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
