package com.example.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Machenike on 2017/8/31.
 */

public class CityInfo extends DataSupport {
    private int cityCode;
    private String cityName;
    private int cityId;
    private int parentid;

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }
}
