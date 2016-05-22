package com.raymondqck.www.rayweather.utils;

import android.text.TextUtils;

import com.raymondqck.www.rayweather.db.MyDB;
import com.raymondqck.www.rayweather.model.City;
import com.raymondqck.www.rayweather.model.County;
import com.raymondqck.www.rayweather.model.Province;

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


}
