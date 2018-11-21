package com.example.administrator.newapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.entity.EnvironmentData;

import java.util.List;

public class EnvironmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<EnvironmentData> mList;
    //布局加载器
    private LayoutInflater inflater;
    private EnvironmentData data;

    public EnvironmentAdapter(Context mContext, List<EnvironmentData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //如果是第一次加载
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_environment_item,null);
            viewHolder.cc_week = convertView.findViewById(R.id.cc_week);
            viewHolder.cc_range = convertView.findViewById(R.id.cc_range);
            //设置缓存
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (EnvironmentAdapter.ViewHolder) convertView.getTag();
        }
        //设置数据
        data = mList.get(position);
        viewHolder.cc_week.setText(data.getWeek());
        viewHolder.cc_range.setText(data.getRange());
        return convertView;
    }
    class ViewHolder{
        private TextView cc_week;
        private TextView cc_range;
    }
}
