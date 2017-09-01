package com.example.coolweather.gson;

import java.util.List;

/**
 * Created by Machenike on 2017/9/1.
 */

public class Weather {
    public Basic basic;
    public List<DailyForecast> dailyForecasts;
    public List<HourForecast> hourForecasts;
    public List<Suggestion> suggestions;

    public Weather() {
    }

    public Weather(Basic basic, List<DailyForecast> dailyForecasts,
                   List<HourForecast> hourForecasts, List<Suggestion> suggestions) {
        this.basic = basic;
        this.dailyForecasts = dailyForecasts;
        this.hourForecasts = hourForecasts;
        this.suggestions = suggestions;
    }
}
