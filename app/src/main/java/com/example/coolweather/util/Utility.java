package com.example.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.coolweather.db.CityInfo;
import com.example.coolweather.gson.Basic;
import com.example.coolweather.gson.DailyForecast;
import com.example.coolweather.gson.HourForecast;
import com.example.coolweather.gson.Suggestion;
import com.example.coolweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Machenike on 2017/8/30.
 */

public class Utility {

    public static String TAG = "Utility";

    public static boolean handleJDCityInfoResponse(String responseContent) {
        if (!TextUtils.isEmpty(responseContent)) {
            try {
                JSONArray allCityInfos = new JSONObject(responseContent).
                        getJSONObject("result").getJSONArray("result");
                for (int i = 0; i < allCityInfos.length(); i++) {
                    JSONObject cityInfoObj = allCityInfos.getJSONObject(i);
                    CityInfo cityInfo = new CityInfo();
                    cityInfo.setCityId(cityInfoObj.getInt("cityid"));
                    cityInfo.setCityName(cityInfoObj.getString("city"));
                    cityInfo.setParentid(cityInfoObj.getInt("parentid"));
                    cityInfo.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleJDWeatherResponse(String responseContent) {
        if (!TextUtils.isEmpty(responseContent)) {
            try {
                //JSONArray weatherResult = new J
                Weather weather = new Weather();
                JSONObject weahterJson = new JSONObject(responseContent).
                        getJSONObject("result").getJSONObject("result");
                Basic basic = getBasic(weahterJson);
                List<DailyForecast> dailyForecasts = getDailyForecasts(weahterJson);
                List<HourForecast> hourForecasts = getHourForecasts(weahterJson);
                List<Suggestion> suggestions = getSuggestions(weahterJson);
                weather.basic = basic;
                weather.dailyForecasts = dailyForecasts;
                weather.hourForecasts = hourForecasts;
                weather.suggestions = suggestions;
                return weather;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private static List<Suggestion> getSuggestions(JSONObject weahterJson) throws JSONException {
        List<Suggestion> suggestions = new ArrayList<>();
        JSONArray indexs = weahterJson.getJSONArray("index");
        for (int i = 0; i < indexs.length(); i++) {
            JSONObject index = indexs.getJSONObject(i);
            Suggestion suggestion = new Suggestion();
            String iname = index.getString("iname");
            String detail = index.getString("detail");
            String ivalue = index.getString("ivalue");
            suggestion.iname = iname;
            suggestion.detail = detail;
            suggestion.ivalue = ivalue;
            suggestions.add(suggestion);

        }
        //Log.i(TAG, suggestions.size()+"");
        return suggestions;
    }

    private static List<HourForecast> getHourForecasts(JSONObject weahterJson) throws JSONException {
        List<HourForecast> hourForecasts = new ArrayList<>();
        JSONArray hours = weahterJson.getJSONArray("hourly");
        for (int i = 0; i < hours.length(); i++) {
            JSONObject hour = hours.getJSONObject(i);
            HourForecast hourForecast = new HourForecast();
            int temp = hour.getInt("temp");
            String weather = hour.getString("weather");
            String time = hour.getString("time");
            hourForecast.temp = temp;
            hourForecast.weather = weather;
            hourForecast.time = time;
            hourForecasts.add(hourForecast);
        }
        //Log.i(TAG, hourForecasts.size()+"");
        return hourForecasts;
    }

    private static List<DailyForecast> getDailyForecasts(JSONObject weahterJson) throws JSONException {
        List<DailyForecast> dailyForecasts = new ArrayList<>();
        JSONArray dailys = weahterJson.getJSONArray("daily");
        for (int i = 0; i < dailys.length(); i++) {
            DailyForecast dailyForecast = new DailyForecast();
            JSONObject daily = dailys.getJSONObject(i);
            JSONObject night = daily.getJSONObject("night");
            JSONObject day = daily.getJSONObject("day");
            String date = daily.getString("date");
            String nightWeather = night.getString("weather");
            String dayWeather = day.getString("weather");
            int tempHigh = day.getInt("temphigh");
            int tempLow = night.getInt("templow");
            dailyForecast.date = date;
            dailyForecast.dayWeather = dayWeather;
            dailyForecast.nigtWeather = nightWeather;
            dailyForecast.tempHigh = tempHigh;
            dailyForecast.tempLow = tempLow;
            dailyForecasts.add(dailyForecast);

        }
        //Log.i(TAG, dailyForecasts.size()+"");
        return dailyForecasts;
    }


    private static Basic getBasic(JSONObject weahterJson) throws JSONException {
        Basic basic = new Basic();
        String cityName = weahterJson.getString("city");
        int cityID = weahterJson.getInt("cityid");
        String updateTime = weahterJson.getString("updatetime");
        int temp = weahterJson.getInt("temp");
        String windDirect = weahterJson.getString("winddirect");
        String windPower = weahterJson.getString("windpower");
        int humidity = weahterJson.getInt("humidity");
        int pressure = weahterJson.getInt("pressure");
        String weather = weahterJson.getString("weather");
        basic.cityID = cityID;
        basic.cityName = cityName;
        basic.updateTime = updateTime;
        basic.temperature = temp;
        basic.windDirect = windDirect;
        basic.windPower = windPower;
        basic.humidity = humidity;
        basic.pressure = pressure;
        basic.weather = weather;
        //Log.i(TAG, basic.toString());
        return basic;
    }
}
