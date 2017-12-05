package com.zzcoolweather.android.zzgson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by z on 2017-12-02.
 */

public class CzzWForecast {
    public String date;

    @SerializedName("tmp")
    public CzzWTemperature temperature;

    @SerializedName("cond")
    public CzzWMore more;

    public class CzzWTemperature {
        public String max;
        public String min;
    }

    public class CzzWMore {
        @SerializedName("txt_d")
        public String info;
    }
}
