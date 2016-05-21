package com.raymondqck.www.rayweather.utils;

import android.text.TextUtils;

import com.raymondqck.www.rayweather.db.MyDB;

/**
 * Created by Administrator on 2016/5/21 0021.
 * 由于服务器返回的省市县数据都是“代号|城市,代号|城市”这种格式的
 * 所以我们最好再提供一个工具类来解析和处理这种数据
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvinceResponse(MyDB myDB,String response){
        if (!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length>0){
                //for (String)
            }
        }
        return true;
    }
}
