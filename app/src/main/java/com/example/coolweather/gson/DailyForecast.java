package com.example.coolweather.gson;

/**
 * Created by Machenike on 2017/9/1.
 */

public class DailyForecast {
    public String date;
    public String nigtWeather;
    public String dayWeather;
    public int tempLow;
    public int tempHigh;
    public int imgID;

    public DailyForecast() {
    }

    public DailyForecast(String date, String dayWeather, int imgID,
                         String nigtWeather, int tempHigh, int tempLow) {
        this.date = date;
        this.dayWeather = dayWeather;
        this.imgID = imgID;
        this.nigtWeather = nigtWeather;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
    }

    @Override
    public String toString() {
        return "DailyForecast{" +
                "date='" + date + '\'' +
                ", nigtWeather='" + nigtWeather + '\'' +
                ", dayWeather='" + dayWeather + '\'' +
                ", tempLow=" + tempLow +
                ", tempHigh=" + tempHigh +
                ", imgID=" + imgID +
                '}';
    }
}
