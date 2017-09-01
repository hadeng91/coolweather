package com.example.coolweather.gson;

/**
 * Created by Machenike on 2017/9/1.
 */

public class HourForecast {
    public int temp;
    public int imgID;
    public String weather;
    public String time;

    public HourForecast() {
    }

    public HourForecast(int imgID, int temp, String time, String weather) {
        this.imgID = imgID;
        this.temp = temp;
        this.time = time;
        this.weather = weather;
    }
}
