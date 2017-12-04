package com.zzcoolweather.android.zzgson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by z on 2017-12-02.
 */

public class CzzWSuggestion {
    @SerializedName("comf")
    public CzzWComfort comfort;

    @SerializedName("cw")
    public CzzWCarWash carWash;

    public CzzWSport sport;

    public class CzzWComfort{
        @SerializedName("txt")
        public String info;
    }
    public class CzzWCarWash{
        @SerializedName("txt")
        public String info;
    }
    public class CzzWSport{
        @SerializedName("txt")
        public String info;
    }
}
