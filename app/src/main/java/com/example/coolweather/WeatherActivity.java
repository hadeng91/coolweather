package com.example.coolweather;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.coolweather.gson.DailyForecast;
import com.example.coolweather.gson.HourForecast;
import com.example.coolweather.gson.Suggestion;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    public String mCity;

    private ImageView bingPicImg;

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

    private LinearLayout hourForecastLayout;

    private LineChart hourLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        /*if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        //badilbs
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(WeatherActivity.this, permissions, 1);
        } else {
            requestLocation();
        }

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        tempText = (TextView) findViewById(R.id.temp_text);
        cityNameText = (TextView) findViewById(R.id.city_text);
        weatherText = (TextView) findViewById(R.id.weather_text);
        updateTimeText = (TextView) findViewById(R.id.updatetime_text);
        windDirectText = (TextView) findViewById(R.id.winddir_text);
        windPowerText = (TextView) findViewById(R.id.windpower_text);
        humidityText = (TextView) findViewById(R.id.humidity_text);
        pressureText = (TextView) findViewById(R.id.pressure_text);
        hourForecastLayout = (LinearLayout) findViewById(R.id.hour_forecast_layout);
        hourLineChart = (LineChart) findViewById(R.id.hour_line_chart);
        dailyForcastLayout = (LinearLayout) findViewById(R.id.daily_forecast_layout);
        suggestionLayout = (LinearLayout) findViewById(R.id.suggestion_layout);

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.handleJDWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            String cityID = "111";
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(cityID);
        }*/

        /*String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }*/
        //loadBingPic();

        
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic)
                                .into(bingPicImg);
                    }
                });
            }
        });
    }

    private void requestWeather(String city) {
        String weatherUrl = "https://way.jd.com/jisuapi/weather?city="+city+
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
        loadBingPic();
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

        LineData mLineData = getLineData(weather.hourForecasts);
        showChart(hourLineChart, mLineData);

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

    private LineData getLineData(List<HourForecast> hourForecasts) {
        ArrayList<String> xValues = new ArrayList<String>();
        //x轴
        for (int i = 0; i < hourForecasts.size(); i++) {
            HourForecast hf = hourForecasts.get(i);
            xValues.add(hf.time);
        }
        //y轴
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < hourForecasts.size(); i++) {
            HourForecast hf = hourForecasts.get(i);
            int value = hf.temp;
            yValues.add(new Entry(value, i));

        }
        LineDataSet lineDataSet = new LineDataSet(yValues, "");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTypeface(null);
        lineDataSet.setFillAlpha(100);
        //lineDataSet.setValueFormatter

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet);
        LineData lineData = new LineData(xValues, lineDataSets);
        lineData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                int res = (int) v;
                return res+"";
            }

        });
        return lineData;
    }

    private void showChart(LineChart lineChart, LineData lineData) {
        lineChart.setData(lineData);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDescription("温度");//设置图表描述的内容位置，字体等等
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setVisibleXRange(1, 6);
        lineChart.moveViewToX(1);
        lineChart.getLegend().setEnabled(false);
        //lineChart.setBackgroundColor(Color.rgb(42,42,36));

    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mCity = bdLocation.getDistrict();
            requestWeather(mCity);
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}
