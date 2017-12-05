package com.zzcoolweather.android.zzgson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by z on 2017-12-02.
 */

public class CzzWNow {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public CzzWMore more;

    public class CzzWMore {
        @SerializedName("txt")
        public String info;
    }
}
