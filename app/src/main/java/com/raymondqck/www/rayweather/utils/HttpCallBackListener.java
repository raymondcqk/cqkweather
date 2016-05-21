package com.raymondqck.www.rayweather.utils;

/**
 * Created by Administrator on 2016/5/21 0021.
 * 回调http请求结果，用于处理http返回的结果
 */
public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
