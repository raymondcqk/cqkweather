package com.raymondqck.www.rayweather.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/21 0021.
 */
public class HttpUtil {

    /**
     * http请求：httpclient
     */
    public static void sendHttpRequest(final String address, final HttpCallBackListener listener) {
        //启动一个线程处理网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener!=null){
                        //回调onFinis（）
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                        //回调onError（）
                    if (listener!=null){
                        //回调onFinis（）
                        listener.onError(e);
                    }
                } finally {
                    //关闭请求连接
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
