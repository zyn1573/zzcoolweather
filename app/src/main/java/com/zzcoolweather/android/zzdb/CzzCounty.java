package com.zzcoolweather.android.zzdb;

import org.litepal.crud.DataSupport;

/**
 * Created by z on 2017-12-01.
 */

public class CzzCounty extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;

    //region geter / seter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    //endregion


}
