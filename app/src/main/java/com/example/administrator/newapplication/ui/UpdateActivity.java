package com.example.administrator.newapplication.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.utils.UtilsTools;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends BaseActivity {
    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView();
    }

    private void initView() {
        mListView = findViewById(R.id.mListView);

        mList.add("检查更新");
        mList.add("客服电话：15023507320");
        mList.add("邮箱：550772893@qq.com");
        mList.add("应用名" + getString(R.string.app_name));
        mList.add("版本号" + UtilsTools.getVersion(this));
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        //设置适配器
        mListView.setAdapter(mAdapter);
    }

}
