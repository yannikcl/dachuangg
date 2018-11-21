package com.example.administrator.newapplication.ui;

import android.os.Bundle;
import android.widget.ListView;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.adapter.FoodAdapter;
import com.example.administrator.newapplication.entity.FoodData;
import com.example.administrator.newapplication.utils.L;
import com.example.administrator.newapplication.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends BaseActivity {

    private ListView mListView_food;
    private List<FoodData> mList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        initView();
    }

    private void initView() {
        mListView_food = findViewById(R.id.mListView_food);

        //解析接口
        String url = "http://apis.juhe.cn/cook/query?key=" + StaticClass.FOOD_KEY + "&menu=%E8%A5%BF%E7%BA%A2%E6%9F%BF&rn=30&pn=2";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //测试一下数据是否返回到自己的APP上面
                //Toast.makeText(FoodActivity.this, t, Toast.LENGTH_SHORT).show();
                L.i("wechat json:" + t);
                //封装解析方法
                parsingJson(t);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonResult.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);

                FoodData data = new FoodData();

                data.setTitle(json.getString("title"));
                data.setTags(json.getString("tags"));
                data.setImtro(json.getString("imtro"));
                data.setAlbums(json.getString("albums"));

                mList.add(data);
            }
            FoodAdapter adapter = new FoodAdapter(this, mList);
            mListView_food.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
