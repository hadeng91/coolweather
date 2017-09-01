package com.example.coolweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.gson.DailyForecast;
import com.example.coolweather.gson.Suggestion;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;

    private TextView tempText;

    private TextView cityNameText;

    private TextView weatherText;

    private TextView updateTimeText;

    private TextView windDirectText;

    private TextView windPowerText;

    private TextView humidityText;

    private TextView pressureText;

    private LinearLayout dailyForcastLayout;

    private LinearLayout suggestionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        tempText = (TextView) findViewById(R.id.temp_text);
        cityNameText = (TextView) findViewById(R.id.city_text);
        weatherText = (TextView) findViewById(R.id.weather_text);
        updateTimeText = (TextView) findViewById(R.id.updatetime_text);
        windDirectText = (TextView) findViewById(R.id.winddir_text);
        windPowerText = (TextView) findViewById(R.id.windpower_text);
        humidityText = (TextView) findViewById(R.id.humidity_text);
        pressureText = (TextView) findViewById(R.id.pressure_text);
        dailyForcastLayout = (LinearLayout) findViewById(R.id.daily_forecast_layout);
        suggestionLayout = (LinearLayout) findViewById(R.id.suggestion_layout);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.handleJDWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            String cityID = "111";
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(cityID);
        }
    }

    private void requestWeather(String cityID) {
        String weatherUrl = "https://way.jd.com/jisuapi/weather?cityid="+cityID+
        "&appkey=8c6b950a4d6ba13ecf492be439c6be19";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather= Utility.handleJDWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null) {
                            /*SharedPreferences.Editor editot = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editot.putString("weather", responseText);
                            editot.apply();*/
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this,
                                    "获取信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        int temp = weather.basic.temperature;
        String cityName = weather.basic.cityName;
        String weahterString = weather.basic.weather;
        String updateTime = weather.basic.updateTime.split(" ")[1];
        String windiDir = weather.basic.windDirect;
        String windPower = weather.basic.windPower;
        int humidity = weather.basic.humidity;
        int pressure = weather.basic.pressure;
        tempText.setText(temp+"");
        cityNameText.setText(cityName);
        weatherText.setText(weahterString);
        updateTimeText.setText(updateTime);
        windDirectText.setText(windiDir);
        windPowerText.setText(windPower);
        pressureText.setText(pressure+"");
        humidityText.setText(humidity+"");
        dailyForcastLayout.removeAllViews();
        for (DailyForecast df : weather.dailyForecasts) {
            View view = LayoutInflater.from(this).inflate(R.layout.daily_forecast_item,
                    dailyForcastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);

            dateText.setText(df.date);
            infoText.setText(df.dayWeather);
            maxText.setText(df.tempHigh+"");
            minText.setText(df.tempLow+"");

            dailyForcastLayout.addView(view);
        }
        suggestionLayout.removeAllViews();
        for (Suggestion suggestion : weather.suggestions) {
            View view = LayoutInflater.from(this).inflate(R.layout.suggestion_item,
                    suggestionLayout, false);
            TextView nameText = (TextView) view.findViewById(R.id.suggestion_name);
            TextView detailText = (TextView) view.findViewById(R.id.suggestion_detail);
            nameText.setText(suggestion.iname);
            detailText.setText(suggestion.detail);

            suggestionLayout.addView(view);

        }
        weatherLayout.setVisibility(View.VISIBLE);

    }
}