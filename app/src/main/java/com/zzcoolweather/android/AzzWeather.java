package com.zzcoolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zzcoolweather.android.zzgson.CzzWForecast;
import com.zzcoolweather.android.zzgson.CzzWeather;
import com.zzcoolweather.android.zzutil.CzzHttpUtil;
import com.zzcoolweather.android.zzutil.CzzUtility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AzzWeather extends AppCompatActivity {

    public DrawerLayout zzlo_drawer;
    public SwipeRefreshLayout zzlo_swipe_refresh;
    private ScrollView zzscr_weather_all;
    private ImageView zzimg_bing_pic;
    private Button zzbtn_nav;
    private TextView zztxt_title_city;
    private TextView zztxt_title_update_time;
    private TextView zztxt_degree;
    private TextView zztxt_weather_info;
    private LinearLayout zzlo_forecast_list;
    private TextView zztxt_aqi;
    private TextView zztxt_pm25;
    private TextView zztxt_comfort;
    private TextView zztxt_car_wash;
    private TextView zztxt_sport;

    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.zzlayout_azz_weather);

        zzlo_drawer=(DrawerLayout) findViewById(R.id.zzlo_drawer);
        zzlo_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.zzlo_swipe_refresh);
        zzscr_weather_all = (ScrollView) findViewById(R.id.zzscr_weather_all);
        zzimg_bing_pic = (ImageView) findViewById(R.id.zzimg_bing_pic);
        zzbtn_nav = (Button) findViewById(R.id.zzbtn_nav);
        zztxt_title_city = (TextView) findViewById(R.id.zztxt_title_city);
        zztxt_title_update_time = (TextView) findViewById(R.id.zztxt_title_update_time);
        zztxt_degree = (TextView) findViewById(R.id.zztxt_degree);
        zztxt_weather_info = (TextView) findViewById(R.id.zztxt_weather_info);
        zzlo_forecast_list = (LinearLayout) findViewById(R.id.zzlo_forecast_list);
        zztxt_aqi = (TextView) findViewById(R.id.zztxt_aqi);
        zztxt_pm25 = (TextView) findViewById(R.id.zztxt_pm25);
        zztxt_comfort = (TextView) findViewById(R.id.zztxt_comfort);
        zztxt_car_wash = (TextView) findViewById(R.id.zztxt_car_wash);
        zztxt_sport = (TextView) findViewById(R.id.zztxt_sport);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String ws = prefs.getString("weather", null);
        if (!TextUtils.isEmpty(ws)) {
            CzzWeather w = CzzUtility.zzHandleWeatherResonse(ws);
            mWeatherId = w.basic.weatherId;
            mShow(w);
        } else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            zzscr_weather_all.setVisibility(View.INVISIBLE);
            zzReq(mWeatherId);
        }

        String bingPicUrl = prefs.getString("bing_pic", null);
        if (!TextUtils.isEmpty(bingPicUrl)) {
            Glide.with(this).load(bingPicUrl).into(zzimg_bing_pic);
        } else {
            zzLoadBingPic();
        }

        zzlo_swipe_refresh.setColorSchemeResources(R.color.colorPrimary);
        zzlo_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                zzReq(mWeatherId);
                zzLoadBingPic();
            }
        });

        zzbtn_nav.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                zzlo_drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    public void zzReq(final String wid) {
        String url = "http://guolin.tech/api/weather/?cityid="
                + wid
                + "&key=d664e054215e45f0ab97ac4692664e27";
        CzzHttpUtil.zzGetAsyn(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String text = response.body().string();
                final CzzWeather w = CzzUtility.zzHandleWeatherResonse(text);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (w != null && "ok".equals(w.status)) {
                            SharedPreferences.Editor edt = PreferenceManager.getDefaultSharedPreferences(AzzWeather.this).edit();
                            edt.putString("weather", text);
                            edt.apply();
                            mWeatherId=w.basic.weatherId;
                            mShow(w);
                        } else {
                            Toast.makeText(AzzWeather.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                        }
                        zzlo_swipe_refresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AzzWeather.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                        zzlo_swipe_refresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    public void zzLoadBingPic() {
        String url = "http://guolin.tech/api/bing_pic";
        CzzHttpUtil.zzGetAsyn(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPicUrl = response.body().string();
                SharedPreferences.Editor edt = PreferenceManager.getDefaultSharedPreferences(AzzWeather.this).edit();
                edt.putString("bing_pic", bingPicUrl);
                edt.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(AzzWeather.this).load(bingPicUrl).into(zzimg_bing_pic);
                    }
                });
            }
        });
    }

    private void mShow(CzzWeather w) {
        String cityName = w.basic.cityName;
        String updateTime = w.basic.update.updateTime.split(" ")[1];
        String degree = w.now.temperature + "℃";
        String weatherInfo = w.now.more.info;
        zztxt_title_city.setText(cityName);
        zztxt_title_update_time.setText(updateTime);
        zztxt_degree.setText(degree);
        zztxt_weather_info.setText(weatherInfo);

        zzlo_forecast_list.removeAllViews();
        for (CzzWForecast forecast : w.forecastList) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.zzlayout_azz_weather_forecast_item,
                    zzlo_forecast_list, false);
            TextView zztxt_date = (TextView) view.findViewById(R.id.zztxt_date);
            TextView zztxt_info = (TextView) view.findViewById(R.id.zztxt_info);
            TextView zztxt_max = (TextView) view.findViewById(R.id.zztxt_max);
            TextView zztxt_min = (TextView) view.findViewById(R.id.zztxt_min);
            zztxt_date.setText(forecast.date);
            zztxt_info.setText(forecast.more.info);
            zztxt_max.setText(forecast.temperature.max);
            zztxt_min.setText(forecast.temperature.min);
            zzlo_forecast_list.addView(view);
        }
        if (w.aqi != null) {
            zztxt_aqi.setText(w.aqi.city.aqi);
            zztxt_pm25.setText(w.aqi.city.pm25);
        }

        String comfort = "舒适度：" + w.suggestion.comfort.info;
        String carWash = "洗车指数：" + w.suggestion.carWash.info;
        String sport = "运动建议：" + w.suggestion.sport.info;
        zztxt_comfort.setText(comfort);
        zztxt_car_wash.setText(carWash);
        zztxt_sport.setText(sport);

        zzscr_weather_all.setVisibility(View.VISIBLE);

        Intent i=new Intent(this,SzzAutoUpdate.class);
        startService(i);
    }
}
