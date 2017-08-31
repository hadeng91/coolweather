package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.CityInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Machenike on 2017/8/30.
 */

public class Utility {

    public static boolean handleJDCityInfoResponse(String responseContent) {
        if (!TextUtils.isEmpty(responseContent)) {
            try {
                //JSONArray allCityInfos = new JSONArray(responseContent).get;
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
}
