package com.zzcoolweather.android.zzgson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by z on 2017-12-02.
 */

public class CzzWeather {
    public String status;
    public CzzWBasic basic;
    public CzzWAqi aqi;
    public CzzWNow now;
    public CzzWSuggestion suggestion;

    @SerializedName("daily_forecast")
    public List<CzzWForecast> forecastList;
}
