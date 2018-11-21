package com.example.administrator.newapplication.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.entity.FoodData;
import com.example.administrator.newapplication.utils.L;
import com.example.administrator.newapplication.utils.PicassoUtils;

import java.util.List;

public class FoodAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<FoodData> mList;
    private FoodData data;

    private int width,height;
    private WindowManager wm;

    public FoodAdapter(Context mContext, List<FoodData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        L.i("Width:" + width + "Height:" + height);
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
        //第一次加载
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.food_item, null);
            viewHolder.tv_title_food = convertView.findViewById(R.id.tv_title_food);
            viewHolder.tv_tags_food = convertView.findViewById(R.id.tv_tags_food);
            viewHolder.tv_imtro_food =  convertView.findViewById(R.id.tv_imtro_food);
            viewHolder.iv_img_food = convertView.findViewById(R.id.iv_img_food);
            //设置缓存
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        data = mList.get(position);

        viewHolder.tv_title_food.setText(data.getTitle());
        viewHolder.tv_tags_food.setText(data.getTags());
        viewHolder.tv_imtro_food.setText(data.getImtro());
        //图片的解析
        if(!TextUtils.isEmpty(data.getAlbums())){
            //加载图片
            PicassoUtils.loadImageViewSize(mContext, data.getAlbums(), width/3, 250, viewHolder.iv_img_food);
        }
        return convertView;
    }
    class ViewHolder {
        private TextView tv_title_food;
        private TextView tv_tags_food;
        private TextView tv_imtro_food;
        private ImageView iv_img_food;
    }
}

