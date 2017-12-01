package com.zzcoolweather.android.zzutil;

import android.text.TextUtils;

import com.zzcoolweather.android.zzdb.CzzCity;
import com.zzcoolweather.android.zzdb.CzzCounty;
import com.zzcoolweather.android.zzdb.CzzProvince;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by z on 2017-12-01.
 */

public class CzzUtility {
    public static boolean zzHandleProvinceResponse(String response) {
        if (TextUtils.isEmpty(response)) return false;
        try {
            JSONArray js = new JSONArray(response);
            for (int i = 0; i < js.length(); i++) {
                JSONObject j = js.getJSONObject(i);
                CzzProvince p = new CzzProvince();
                p.setProvinceCode(j.getInt("id"));
                p.setProvinceName(j.getString("name"));
                p.save();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean zzHandleCityResponse( String response,int pid) {
        if (TextUtils.isEmpty(response)) return false;
        try {
            JSONArray js = new JSONArray(response);
            for (int i = 0; i < js.length(); i++) {
                JSONObject j = js.getJSONObject(i);
                CzzCity c = new CzzCity();
                c.setCityCode(j.getInt("id"));
                c.setCityName(j.getString("name"));
                c.setProvinceId(pid);
                c.save();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean zzHandleCountyResponse( String response,int cid) {
        if (TextUtils.isEmpty(response)) return false;
        try {
            JSONArray js = new JSONArray(response);
            for (int i = 0; i < js.length(); i++) {
                JSONObject j = js.getJSONObject(i);
                CzzCounty y = new CzzCounty();
                y.setCountyName(j.getString("name"));
                y.setWeatherId(j.getString("weather_id"));
                y.setId(cid);
                y.save();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
