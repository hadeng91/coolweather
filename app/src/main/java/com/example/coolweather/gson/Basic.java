package com.example.coolweather.gson;

/**
 * Created by Machenike on 2017/9/1.
 */

public class Basic {
    public String cityName;
    public int cityID;
    public String updateTime;
    public int temperature;
    public String windDirect;
    public String windPower;
    public int humidity;
    public int pressure;
    public String weather;

    public Basic() {
    }

    public Basic(int cityID, String cityName, int humidity,
                 int pressure, int temperature, String updateTime,
                 String windDirect, String windPower, String weather) {
        this.cityID = cityID;
        this.cityName = cityName;
        this.humidity = humidity;
        this.pressure = pressure;
        this.temperature = temperature;
        this.updateTime = updateTime;
        this.windDirect = windDirect;
        this.windPower = windPower;
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Basic{" +
                "cityID=" + cityID +
                ", cityName='" + cityName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", temperature=" + temperature +
                ", windDirect='" + windDirect + '\'' +
                ", windPower='" + windPower + '\'' +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", weather='" + weather + '\'' +
                '}';
    }
}
