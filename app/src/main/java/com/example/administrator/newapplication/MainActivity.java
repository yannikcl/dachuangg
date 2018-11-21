package com.example.administrator.newapplication;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;

import com.example.administrator.newapplication.fragment.AFragment;
import com.example.administrator.newapplication.fragment.BFragment;
import com.example.administrator.newapplication.fragment.CFragment;
import com.example.administrator.newapplication.fragment.DFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mTitle;
    private List<Fragment> mFragment;

    //悬浮窗
    //private FloatingActionButton FBI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //去掉阴影
        //getSupportActionBar().setElevation(0);

        iniData();
        initView();

    }

    //初始化数据
    private void iniData() {

        mTitle = new ArrayList<>();
        mTitle.add("首页");
        mTitle.add("发现");
        mTitle.add("睡眠");
        mTitle.add("我的");

        mFragment = new ArrayList<>();
        mFragment.add(new AFragment());
        mFragment.add(new BFragment());
        mFragment.add(new CFragment());
        mFragment.add(new DFragment());

    }

    //初始化view
    private void initView() {
        //FBI = findViewById(R.id.FBI);
        //FBI.setOnClickListener(this);
        //默认隐藏
        //FBI.setVisibility(View.VISIBLE);

        mTabLayout = findViewById(R.id.mTabLayout);
        mViewPager = findViewById(R.id.mViewPager);

        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());

        //mViewPager的滑动监听
        /*
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //根据得到的不同的position值去判定小圆球在哪个界面展示出来
                Log.i("TAG","position:" + position);
                if (position == 0){
                    FBI.setVisibility(View.VISIBLE);
                }else {
                    FBI.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        */

        //设置一个适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
