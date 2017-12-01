package com.zzcoolweather.android.zzdb;

import org.litepal.crud.DataSupport;

/**
 * Created by z on 2017-12-01.
 */

public class CzzCity extends DataSupport {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;
    //region geter / seter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
    //endregion

}
