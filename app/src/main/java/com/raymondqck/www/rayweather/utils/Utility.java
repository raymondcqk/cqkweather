package com.raymondqck.www.rayweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.raymondqck.www.rayweather.db.MyDB;
import com.raymondqck.www.rayweather.model.City;
import com.raymondqck.www.rayweather.model.County;
import com.raymondqck.www.rayweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 陈其康 on 2016/5/21 0021.
 * 由于服务器返回的省市县数据都是“代号|城市,代号|城市”这种格式的
 * 所以我们最好再提供一个工具类来解析和处理这种数据
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据,并存入数据库
     */
    public synchronized static boolean handleProvinceResponse(MyDB myDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出来的数据存到数据库的Province表
                    myDB.saveProvince(province);
                }
                //成功存入数据库，返回true
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据,并存入数据库
     */
    public synchronized static boolean handleCityResponse(MyDB myDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCity = response.split(",");
            if (allCity != null && allCity.length > 0) {
                for (String c : allCity) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    myDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的县级数据,并存入数据库
     */
    public synchronized static boolean handleCountyResponse(MyDB  myDB,String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCounty = response.split(",");
            if (allCounty != null && allCounty.length>0){
                for (String c:allCounty) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    myDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据（天气），并将解析出的数据存储到本地。
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
                    weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     */
    public static void saveWeatherInfo(Context context, String cityName,
                                       String weatherCode, String temp1, String temp2, String weatherDesp,
                                       String publishTime) {
        //设置日期格式 2016年5月22日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);

        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();

        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }
}
