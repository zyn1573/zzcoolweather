package com.zzcoolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AzzMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zzlayout_azz_main);

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        if (p.getString("weather", null) != null) {
            Intent intent = new Intent(this, AzzWeather.class);
            startActivity(intent);
            finish();
        }
    }
}
