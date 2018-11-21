package com.example.administrator.newapplication.ui;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.adapter.EnvironmentAdapter;
import com.example.administrator.newapplication.entity.EnvironmentData;
import com.example.administrator.newapplication.utils.L;
import com.example.administrator.newapplication.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentActivity extends BaseActivity {
    private TextView cc_environment_all;
    private TextView temperature_range;
    private TextView temperature_cloth;
    private TextView cc_aqi;
    private TextView cc_quality;

    private ListView cc_mListView;
    private List<EnvironmentData> mlist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);

        initView();
    }

    private void initView() {
        cc_environment_all = findViewById(R.id.cc_environment_all);
        temperature_range = findViewById(R.id.temperature_range);
        temperature_cloth = findViewById(R.id.temperature_cloth);
        ya();
        cc_mListView = findViewById(R.id.cc_mListView);
        cc_aqi = findViewById(R.id.cc_aqi);
        cc_quality = findViewById(R.id.cc_quality);
    }

    private void ya() {
        String url = "http://v.juhe.cn/weather/index?format=2&cityname="+"重庆"+"&key="
                + StaticClass.ENVIRON_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(EnvironmentActivity.this,t, Toast.LENGTH_SHORT).show();
                L.i("Json" + t);
                parsingJson(t);
            }
        });
        String url1 = "http://web.juhe.cn:8080/environment/air/cityair?city=重庆&key=" + StaticClass.QUILTY_KEY;
        RxVolley.get(url1, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Toast.makeText(EnvironmentActivity.this,t, Toast.LENGTH_SHORT).show();
                L.i("Json" + t);
                parsingJson1(t);
            }
        });
    }

    private void parsingJson1(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonResult = jsonObject.getJSONArray("result");
            for (int i = 0;i<1;i++){
                JSONObject json = (JSONObject) jsonResult.get(i);
                JSONObject jsonCitynow = json.getJSONObject("citynow");

                String quality = jsonCitynow.getString("quality");
                String api = jsonCitynow.getString("AQI");
                cc_quality.append(quality);
                cc_aqi.append(api);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingJson(String t) {
        try{
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");

            JSONObject jsonsk = jsonResult.getJSONObject("sk");
            String temp = jsonsk.getString("temp");

            JSONObject jsonToday = jsonResult.getJSONObject("today");
            String temperature = jsonToday.getString("temperature");
            String dressing_index = jsonToday.getString("dressing_index");

            cc_environment_all.append(temp);
            temperature_range.append(temperature);
            temperature_cloth.append(dressing_index);

            JSONArray jsonArray = jsonResult.getJSONArray("future");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);

                EnvironmentData data = new EnvironmentData();
                data.setWeek(json.getString("week"));
                data.setRange(json.getString("temperature"));
                mlist.add(data);
            }
            EnvironmentAdapter adapter = new EnvironmentAdapter(this,mlist);
            cc_mListView.setAdapter(adapter);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
