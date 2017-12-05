package com.zzcoolweather.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.zzcoolweather.android.zzgson.CzzWeather;
import com.zzcoolweather.android.zzutil.CzzHttpUtil;
import com.zzcoolweather.android.zzutil.CzzUtility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SzzAutoUpdate extends Service {
    private static final String TAG = "http://SzzAutoUpdate";

    public SzzAutoUpdate() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUpdateWeather();
        mUpdateBingPic();

        AlarmManager m = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1 * 60 * 60 * 1000;
        long nextTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, SzzAutoUpdate.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        m.cancel(pi);
        m.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    private void mUpdateWeather() {
        Log.d(TAG, "mUpdateWeather: ");

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        String s = p.getString("weather", null);
        if (!TextUtils.isEmpty(s)) {
            CzzWeather w = CzzUtility.zzHandleWeatherResonse(s);
            String wid = w.basic.weatherId;
            String url = "http://guolin.tech/api/weather/?cityid="
                    + wid
                    + "&key=d664e054215e45f0ab97ac4692664e27";
            CzzHttpUtil.zzGetAsyn(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    CzzWeather w = CzzUtility.zzHandleWeatherResonse(s);
                    if (w != null && "ok".equals(w.status)) {
                        SharedPreferences.Editor edt = PreferenceManager.getDefaultSharedPreferences(SzzAutoUpdate.this).edit();
                        edt.putString("weather", s);
                        edt.apply();
                    }
                }
            });
        }
    }

    private void mUpdateBingPic() {
        String url = "http://guolin.tech/api/bing_pic";
        CzzHttpUtil.zzGetAsyn(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPicUrl = response.body().string();
                SharedPreferences.Editor edt = PreferenceManager.getDefaultSharedPreferences(SzzAutoUpdate.this).edit();
                edt.putString("bing_pic", bingPicUrl);
                edt.apply();
            }
        });
    }
}
