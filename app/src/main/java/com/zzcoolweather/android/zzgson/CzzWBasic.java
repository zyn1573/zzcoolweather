package com.zzcoolweather.android.zzgson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by z on 2017-12-02.
 */

public class CzzWBasic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public CzzWUpdate update;

    public class CzzWUpdate{
        @SerializedName("loc")
        public String updateTime;
    }
}
