package com.example.administrator.newapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.utils.StaticClass;

public class HealthyActivity extends BaseActivity implements View.OnClickListener {
    private TextView cc_rule;
    //好友阅读篇数
    private TextView cc_read;
    //达标天数
    private TextView cc_day;
    //所得棵树
    private TextView cc_tree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heaithy);

        initView();
    }

    private void initView() {
        cc_rule = findViewById(R.id.cc_rule);
        cc_rule.setOnClickListener(this);

        cc_read = findViewById(R.id.cc_read);
        cc_read.setText("" + StaticClass.EVERY_DAY+"篇");
        cc_day = findViewById(R.id.cc_day);
        cc_day.setText("" + StaticClass.QIAN_DAO +"天");
        cc_tree = findViewById(R.id.cc_tree);
        cc_tree.setText("" + StaticClass.TREE_KEY +"棵");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cc_rule:
                startActivity(new Intent(this,TreeActivity.class));
                break;
        }
    }
}
