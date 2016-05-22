package com.raymondqck.www.rayweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.raymondqck.www.rayweather.R;
import com.raymondqck.www.rayweather.db.MyDB;
import com.raymondqck.www.rayweather.model.City;
import com.raymondqck.www.rayweather.model.County;
import com.raymondqck.www.rayweather.model.Province;
import com.raymondqck.www.rayweather.utils.HttpCallBackListener;
import com.raymondqck.www.rayweather.utils.HttpUtil;
import com.raymondqck.www.rayweather.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈其康 raymondchan on 2016/5/22 0022.
 * 编写用于遍历省市县数据的活动
 */
public class ChooseAreaActivity extends Activity {
    public final static int LEVEL_PROVINCE = 0;
    public final static int LEVEL_CITY = 1;
    public final static int LEVEL_COUNTY = 2;

    private ProgressDialog mProgressDialog;
    private TextView mTitleText;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private MyDB mMyDB;
    private List<String> dataList = new ArrayList<String>();

    /**
     * 省列表
     */
    private List<Province> mProvinceList;
    /**
     * 市列表
     */
    private List<City> mCityList;
    /**
     * 县列表
     */
    private List<County> mCountyList;

    /**
     * 选中的省份
     */
    private Province mSelectedProvince;
    /**
     * 选中的城市
     */
    private City mSelectedCity;
    /**
     * 选中的县（书中没有）
     */
    private County mSelectedCounty;

    /**
     * 当前选中的级别
     */
    private int mCurrentLevel;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //加载地区选择布局
        setContentView(R.layout.choose_area);
        init();

    }

    private void init() {
        //UI
        mListView = (ListView) findViewById(R.id.list_view);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        mListView.setAdapter(mAdapter);

        //DB
        mMyDB = MyDB.getInstance(this);

        //更新点击操作
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                if (mCurrentLevel == LEVEL_PROVINCE) {
                    mSelectedProvince = mProvinceList.get(position);
                    //根据所选省份查询城市列表
                    // 加载市级数据
                    queryCity();
                } else if (mCurrentLevel == LEVEL_CITY) {
                    mSelectedCity = mCityList.get(position);
                    // 加载县级数据
                    queryCounty();
                }
            }
        });
        // 加载省级数据
        queryProvince();
    }

    /**
     * 查询全国所有省，优先从数据库查询，若无则到服务器查询
     */
    private void queryProvince() {
        mProvinceList = mMyDB.loadProvinces();
        if (mProvinceList.size() > 0) {    //若数据库读到有数据，则使用数据库数据
            //listView数据源清空
            dataList.clear();
            for (Province province : mProvinceList) {
                dataList.add(province.getProvinceName());
            }
            //通知ListView更新UI
            mAdapter.notifyDataSetChanged();
            //默认选中第一个
            mListView.setSelection(0);
            mTitleText.setText("中国");
            mCurrentLevel = LEVEL_PROVINCE;
        } else {    //若数据没数据，则通过http请求数据
            //从网络获取省份信息
            queryFromServer(null,"province");
        }
    }
    /**
     * 查询选中的省内所有的城市，优先从数据库查询，若无则到服务器查询
     */
    private void queryCity() {
        mCityList = mMyDB.lodaCity(mSelectedProvince.getId());
        if (mCityList.size()>0){
            dataList.clear();
            for (City city:mCityList) {
            dataList.add(city.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText(mSelectedProvince.getProvinceName());
            mCurrentLevel = LEVEL_CITY;
        }else {
            queryFromServer(mSelectedProvince.getProvinceCode(),"city");
        }


    }

    /**
     * 查询选中的城市内的所有县，优先从数据库查询，若无则到服务器查询
     */
    private void queryCounty() {
        mCountyList = mMyDB.loadCounty(mSelectedCity.getId());
        if (mCountyList.size()>0){
            dataList.clear();
            for (County county:mCountyList){
                dataList.add(county.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText(mSelectedCity.getCityName());
            mCurrentLevel = LEVEL_COUNTY;
        }else {
            queryFromServer(mSelectedCity.getCityCode(),"county");
        }
    }


    /**
     * 根据传入的代号和类型从服务器查询省/市/县数据
     * @param code
     * @param type
     */
    private void queryFromServer(final String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        //建立http连接并获取数据
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)){   //字符串匹配 用 equals（）
                    result = Utility.handleProvinceResponse(mMyDB,response);
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponse(mMyDB,response,mSelectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(mMyDB,response,mSelectedCity.getId());
                }
                if (result){
                    //通过runOnUiThread()返回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvince();
                            }else if ("city".equals(type)){
                                queryCity();
                            }else if ("county".equals(type)){
                                queryCounty();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                //通过runUiThread（）返回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载ing...");
            //设置点击空白区不可关闭进度对话框
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        //若mProgressDialog已创建过，直接显示
        mProgressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }

    /**
     * 捕获Back按键，根据当前等级判断，此时应返回市、省列表还是直接退出
     */
    @Override
    public void onBackPressed() {
       if (mCurrentLevel == LEVEL_COUNTY){
           queryCity();
       }else if (mCurrentLevel == LEVEL_CITY){
           queryProvince();
       }else if (mCurrentLevel == LEVEL_PROVINCE){
           finish();
       }

    }
}
