package com.example.administrator.newapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.adapter.WeChatAdapter;
import com.example.administrator.newapplication.entity.WeChatData;
import com.example.administrator.newapplication.ui.WebViewActivity;
import com.example.administrator.newapplication.utils.L;
import com.example.administrator.newapplication.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BFragment extends Fragment {

    private ListView mListView;
    private List<WeChatData> mList = new ArrayList<>();
    //存储标题
    private List<String> mListTitle = new ArrayList<>();
    //存储地址
    private List<String> mListUrl = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, null);
        findView(view);
        return view;
    }

    //初始化View
    private void findView(View view) {
        mListView = view.findViewById(R.id.mListView);

        //解析接口
        //网址是从聚合数据申请的api接口并且申请了一个钥匙key
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY + "&ps=20";
        //使用RxVolley网络请求库
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //测试一下数据是否返回到自己的APP上面
                //Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
                L.i("wechat json:" + t);
                //封装解析方法
                parsingJson(t);
            }
        });

        //每一个ListView点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StaticClass.EVERY_DAY++;
                L.i("position:" + position);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", mListTitle.get(position));
                intent.putExtra("url", mListUrl.get(position));
                startActivity(intent);
            }
        });
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            //根据聚合数据上面的内容进行一步步解析
            JSONObject jsonresult = jsonObject.getJSONObject("result");
            JSONArray jsonList = jsonresult.getJSONArray("list");
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject json = (JSONObject) jsonList.get(i);
                //实体内容
                WeChatData data = new WeChatData();

                //得到相关的解析数据
                String titlr = json.getString("title");
                String url = json.getString("url");

                //设置数据，添加进实体类
                data.setTitle(titlr);
                //得到相关的解析数据并且设置进入实体类
                data.setSource(json.getString("source"));
                data.setImgUrl(json.getString("firstImg"));

                //添加进入存储的列表中
                mList.add(data);
                mListTitle.add(titlr);
                mListUrl.add(url);
            }
            //实例化适配器并且传入这个列表
            WeChatAdapter adapter = new WeChatAdapter(getActivity(), mList);
            //设置适配器
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
