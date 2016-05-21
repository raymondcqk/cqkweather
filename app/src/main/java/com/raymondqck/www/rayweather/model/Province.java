package com.raymondqck.www.rayweather.model;

/**
 * Created by Administrator on 2016/5/21 0021.
 */
public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;

//    public Province(int id, String provinceName, String provinceCode) {
//        this.id = id;
//        this.provinceName = provinceName;
//        this.provinceCode = provinceCode;
//    }

    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
