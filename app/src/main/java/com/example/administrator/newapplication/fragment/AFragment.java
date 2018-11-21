package com.example.administrator.newapplication.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.ui.EnvironmentActivity;
import com.example.administrator.newapplication.ui.HealthyActivity;
import com.example.administrator.newapplication.ui.RecognitionActivity;
import com.example.administrator.newapplication.ui.WelcomeActivity;
import com.example.administrator.newapplication.ui.SearchActivity;
import com.example.administrator.newapplication.ui.WriteActivity;
import com.example.administrator.newapplication.utils.L;
import com.example.administrator.newapplication.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static com.example.administrator.newapplication.utils.L.TAG;

public class AFragment extends Fragment implements View.OnClickListener {

    //搜索
    private Button cc_search;
    //环境
    private LinearLayout LL_environment;
    //定位
    private TextView position_text_view;
    private LocationClient mLocationClient;
    //我的食谱
    private TabLayout mTabLayout_1;
    private ViewPager mViewPager_1;
    private List<String> mTitle;
    private List<Fragment> mFragment;
    //健康管家
    private LinearLayout cc_healthy;
    //签到
    private TextView cc_register;
    //环境界面
    private TextView data_whethe_1;
    //噪音检测
    private LinearLayout cc_noisy;
    //美食识别
    private GifImageView cc_watch;
    //食物推荐
    private TextView food_talk;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "****>>>> onCreateView");
        //我的食谱
        iniData();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        View view = inflater.inflate(R.layout.fragment_a,null);
        findView(view);
        return view;
    }

    private void iniData() {
        mTitle = new ArrayList<>();
        mTitle.add("早餐");
        mTitle.add("中餐");
        mTitle.add("晚餐");

        mFragment = new ArrayList<>();
        mFragment.add(new EFragment());
        mFragment.add(new FFragment());
        mFragment.add(new GFragment());
    }

    private void findView(View view) {
        //食物推荐
        food_talk = view.findViewById(R.id.food_talk);
        food_talk.setOnClickListener(this);
        //美食识别
        cc_watch = view.findViewById(R.id.cc_watch);
        cc_watch.setOnClickListener(this);
        //噪音检测
        cc_noisy = view.findViewById(R.id.cc_noisy);
        cc_noisy.setOnClickListener(this);

        //环境界面
        data_whethe_1 = view.findViewById(R.id.data_whethe_1);

        //解析环境
        String url = "http://v.juhe.cn/weather/index?format=2&cityname="+"重庆"+"&key="
                + StaticClass.ENVIRON_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(getActivity(),t, Toast.LENGTH_SHORT).show();
                L.i("Json" + t);
                parsingJson(t);
            }
        });
        //签到
        cc_register = view.findViewById(R.id.cc_register);
        cc_register.setOnClickListener(this);
        //健康管家
        cc_healthy = view.findViewById(R.id.cc_healthy);
        cc_healthy.setOnClickListener(this);
        //我的食谱
        mTabLayout_1 = view.findViewById(R.id.mTabLayout_1);
        mViewPager_1 = view.findViewById(R.id.mViewPager_1);
        //预加载
        mViewPager_1.setOffscreenPageLimit(mFragment.size());
        //设置一个适配器
        mViewPager_1.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }

        });
        mTabLayout_1.setupWithViewPager(mViewPager_1);

        //搜索功能
        cc_search = view.findViewById(R.id.cc_search);
        cc_search.setOnClickListener(this);
        //身边环境
        LL_environment = view.findViewById(R.id.LL_environment);
        LL_environment.setOnClickListener(this);
        //定位
        position_text_view = view.findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }else {
            requireLocation();
        }
    }

    //环境的解析
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");

            JSONObject jsonToday = jsonResult.getJSONObject("today");
            String date_y = jsonToday.getString("date_y");
            String weather = jsonToday.getString("weather");
            String dressing_advice = jsonToday.getString("dressing_advice");

            data_whethe_1.append(date_y + "\n");
            data_whethe_1.append(weather + "\n");
            data_whethe_1.append(dressing_advice + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requireLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(getActivity(),"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requireLocation();
                }else {
                    Toast.makeText(getActivity(),"发生未知错误",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
                default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cc_search:
                startActivity(new Intent(getActivity(),SearchActivity.class));
                break;
            case R.id.LL_environment:
                startActivity(new Intent(getActivity(), EnvironmentActivity.class));
                break;
            case R.id.cc_healthy:
                startActivity(new Intent(getActivity(), HealthyActivity.class));
                break;
            case R.id.cc_register:
                startActivity(new Intent(getActivity(), WriteActivity.class));
                break;
            case R.id.cc_noisy:
                startActivity(new Intent(getActivity(),WelcomeActivity.class));
                break;
            case R.id.cc_watch:
                startActivity(new Intent(getActivity(),RecognitionActivity.class));
                break;
            case R.id.food_talk:
                Toast.makeText(getActivity(),"人的一天能量吸收量为1500-2000kcal",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currenPosition = new StringBuilder();
            currenPosition.append(bdLocation.getCity());
            position_text_view.setText(currenPosition);
        }
    }
}
