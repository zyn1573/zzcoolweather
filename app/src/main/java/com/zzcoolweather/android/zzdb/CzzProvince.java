package com.zzcoolweather.android.zzdb;

import org.litepal.crud.DataSupport;

/**
 * Created by z on 2017-12-01.
 */

public class CzzProvince extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceCode;
    //region geter / seter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
    //endregion


}
