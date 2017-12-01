package com.zzcoolweather.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zzcoolweather.android.zzdb.CzzCity;
import com.zzcoolweather.android.zzdb.CzzCounty;
import com.zzcoolweather.android.zzdb.CzzProvince;
import com.zzcoolweather.android.zzutil.CzzHttpUtil;
import com.zzcoolweather.android.zzutil.CzzUtility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by z on 2017-12-01.
 */

public class FzzChooseArea extends Fragment {
    public static final int zzLEVEL_PROVINCE = 0;
    public static final int zzLEVEL_CITY = 1;
    public static final int zzLEVEL_COUNTY = 2;

    private ProgressDialog mProgressDialog;

    private TextView zztxt_title;
    private Button zzbtn_back;
    private ListView zzlst;

    private ArrayAdapter<String> mAdapter;
    private List<String> mDataS = new ArrayList<>();
    private List<CzzProvince> mProvinceS;
    private List<CzzCity> mCityS;
    private List<CzzCounty> mCountyS;
    private CzzProvince mSelectedProvince;
    private CzzCity mSelectedCity;
    private CzzCounty mSelectedCounty;
    private int mCurrentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.zzlayout_frag_choose_area, container, false);

        zztxt_title = (TextView) fragView.findViewById(R.id.zztxt_title);
        zzbtn_back = (Button) fragView.findViewById(R.id.zzbtn_back);
        zzlst = (ListView) fragView.findViewById(R.id.zzlst);

        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mDataS);
        zzlst.setAdapter(mAdapter);

        return fragView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        zzlst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentLevel == zzLEVEL_PROVINCE) {
                    mSelectedProvince = mProvinceS.get(position);
                    mQueryCities();
                } else if (mCurrentLevel == zzLEVEL_CITY) {
                    mSelectedCity = mCityS.get(position);
                    mQueryCounties();
                }
            }
        });

        zzbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLevel == zzLEVEL_COUNTY) {
                    mQueryCities();
                } else if (mCurrentLevel == zzLEVEL_CITY) {
                    mQueryProvinces();
                }
            }
        });

        mQueryProvinces();
    }

    private void mQueryProvinces() {
        zztxt_title.setText("中国");
        zzbtn_back.setVisibility(View.GONE);
        mProvinceS = DataSupport.findAll(CzzProvince.class);
        if (mProvinceS.isEmpty()) {
            String address = "http://guolin.tech/api/china";
            mQueryFromServer(address, zzLEVEL_PROVINCE);
        } else {
            mDataS.clear();
            for (CzzProvince p : mProvinceS) {
                mDataS.add(p.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            zzlst.setSelection(0);
            mCurrentLevel = zzLEVEL_PROVINCE;
        }
    }

    private void mQueryCities() {
        zztxt_title.setText(mSelectedProvince.getProvinceName());
        zzbtn_back.setVisibility(View.VISIBLE);
        mCityS = DataSupport.where("provinceId = ?", String.valueOf(mSelectedProvince.getId())).find(CzzCity.class);
        if (mProvinceS.isEmpty()) {
            int pcode = mSelectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + pcode;
            mQueryFromServer(address, zzLEVEL_CITY);
        } else {
            mDataS.clear();
            for (CzzCity c : mCityS) {
                mDataS.add(c.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            zzlst.setSelection(0);
            mCurrentLevel = zzLEVEL_CITY;
        }
    }

    private void mQueryCounties() {
        zztxt_title.setText(mSelectedCity.getCityName());
        zzbtn_back.setVisibility(View.VISIBLE);
        mCountyS = DataSupport.where("cityId = ?", String.valueOf(mSelectedCity.getId())).find(CzzCounty.class);
        if (mCountyS.isEmpty()) {
            int pcode = mSelectedProvince.getProvinceCode();
            int ccode = mSelectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + pcode + "/" + ccode;
            mQueryFromServer(address, zzLEVEL_COUNTY);
        } else {
            mDataS.clear();
            for (CzzCounty y : mCountyS) {
                mDataS.add(y.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            zzlst.setSelection(0);
            mCurrentLevel = zzLEVEL_COUNTY;
        }
    }

    private void mQueryFromServer(String address, final int queryType) {
        mShowPorgressDialog();

        CzzHttpUtil.zzGetAsyn(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mClosePorgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                switch (queryType) {
                    case zzLEVEL_PROVINCE:
                        result = CzzUtility.zzHandleProvinceResponse(responseText);
                        break;
                    case zzLEVEL_CITY:
                        result = CzzUtility.zzHandleCityResponse(responseText, mSelectedCity.getId());
                        break;
                    case zzLEVEL_COUNTY:
                        result = CzzUtility.zzHandleCountyResponse(responseText, mSelectedCity.getId());
                        break;
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClosePorgressDialog();
                            switch (queryType) {
                                case zzLEVEL_PROVINCE:
                                    mQueryProvinces();
                                    break;
                                case zzLEVEL_CITY:
                                    mQueryCities();
                                    break;
                                case zzLEVEL_COUNTY:
                                    mQueryCounties();
                                    break;
                            }
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClosePorgressDialog();
                            Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    void mShowPorgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog=new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载..");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    void mClosePorgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
