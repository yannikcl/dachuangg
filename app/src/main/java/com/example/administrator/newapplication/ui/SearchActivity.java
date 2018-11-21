package com.example.administrator.newapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.adapter.MyAdapter;
import com.example.administrator.newapplication.inteface.FilterListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView cc_back;
    private Button food_da_quan;

    private EditText et_ss;
    private ListView lsv_ss;
    private List<String> list = new ArrayList<String>();
    boolean isFilter;
    private MyAdapter adapter = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        setData();// 给listView设置adapter
        setListeners();// 设置监听
    }

    private void initView() {
        cc_back = findViewById(R.id.cc_back);
        cc_back.setOnClickListener(this);
        food_da_quan = findViewById(R.id.food_da_quan);
        food_da_quan.setOnClickListener(this);

        et_ss = (EditText) findViewById(R.id.et_ss);// EditText控件
        lsv_ss = (ListView)findViewById(R.id.lsv_ss);// ListView控件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cc_back:
                finish();
                break;
            case R.id.food_da_quan:
                startActivity(new Intent(this,FoodActivity.class));
                break;
        }
    }

    private void setData() {
        initData();// 初始化数据

        // 这里创建adapter的时候，构造方法参数传了一个接口对象，这很关键，回调接口中的方法来实现对过滤后的数据的获取
        adapter = new MyAdapter(list, this, new FilterListener() {
            // 回调方法获取过滤后的数据
            public void getFilterData(List<String> list) {
                // 这里可以拿到过滤后数据，所以在这里可以对搜索后的数据进行操作
                Log.e("TAG", "接口回调成功");
                Log.e("TAG", list.toString());
                setItemClick(list);
            }
        });
        lsv_ss.setAdapter(adapter);
    }

    /**
     * 给listView添加item的单击事件
     * @param filter_lists  过滤后的数据集
     */
    protected void setItemClick(final List<String> filter_lists) {
        lsv_ss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 点击对应的item时，弹出toast提示所点击的内容
                Toast.makeText(SearchActivity.this, filter_lists.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 简单的list集合添加一些测试数据
     */
    private void initData() {
        list.add("看着飞舞的尘埃   掉下来");
        list.add("没人发现它存在");
        list.add("多自由自在");
        list.add("可世界都爱热热闹闹");
        list.add("容不下   我百无聊赖");
        list.add("不应该   一个人 发呆");
        list.add("只有我   守着安静的沙漠");
        list.add("等待着花开");
        list.add("只有我   看着别人的快乐");
    }

    private void setListeners() {
        // 没有进行搜索的时候，也要添加对listView的item单击监听
        setItemClick(list);

        /**
         * 对编辑框添加文本改变监听，搜索的具体功能在这里实现
         * 很简单，文本该变的时候进行搜索。关键方法是重写的onTextChanged（）方法。
         */
        et_ss.addTextChangedListener(new TextWatcher() {

            /**
             *
             * 编辑框内容改变的时候会执行该方法
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 如果adapter不为空的话就根据编辑框中的内容来过滤数据
                if(adapter != null){
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 控件初始化
     */
    private void setViews() {
        et_ss = (EditText) findViewById(R.id.et_ss);// EditText控件
        lsv_ss = (ListView)findViewById(R.id.lsv_ss);// ListView控件
    }

}

