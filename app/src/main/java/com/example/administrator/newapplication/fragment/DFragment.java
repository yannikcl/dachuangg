package com.example.administrator.newapplication.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.entity.MyUser;
import com.example.administrator.newapplication.ui.CourierActivity;
import com.example.administrator.newapplication.ui.LoginActivity;
import com.example.administrator.newapplication.ui.PersonalActivity;
import com.example.administrator.newapplication.ui.PhoneActivity;
import com.example.administrator.newapplication.ui.QrCodeActivity;
import com.example.administrator.newapplication.ui.UpdateActivity;
import com.example.administrator.newapplication.utils.StaticClass;

import cn.bmob.v3.BmobUser;

public class DFragment extends Fragment implements View.OnClickListener {

    private Button btn_exit_user;
    private TextView cc_write;
    private Button btn_camera;
    //快递
    private TextView tv_courier;
    //归属地查询
    private TextView tv_phone;
    //意见反馈
    private TextView feedback;
    //版本更新
    private TextView update;
    //生成二维码
    private LinearLayout ll_qr_code;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_d,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //生成二维码
        ll_qr_code = view.findViewById(R.id.ll_qr_code);
        ll_qr_code.setOnClickListener(this);
        //退出登录
        btn_exit_user = view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        //编辑资料
        cc_write = view.findViewById(R.id.cc_write);
        cc_write.setOnClickListener(this);
        //快递查询
        tv_courier = view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
        //归属地查询
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);
        //意见反馈
        feedback = view.findViewById(R.id.feedback);
        feedback.setOnClickListener(this);
        //版本更新
        update = view.findViewById(R.id.update);
        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit_user:
                //退出登录
                //清除缓存对象
                MyUser.logOut();
                //现在的currentUser是null
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.cc_write:
                //从一个Activity的fragment跳转到另外一个Activity
                //https://www.jianshu.com/p/ab1cb7ddf91f
                startActivity(new Intent(getActivity(), PersonalActivity.class));
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;
            case R.id.feedback:
                //直接跳转到到一个QQ群界面
                joinQQGroup(StaticClass.QQ_KEY);
                break;
            case R.id.update:
                startActivity(new Intent(getActivity(),UpdateActivity.class));
                break;
            case R.id.ll_qr_code:
                startActivity(new Intent(getActivity(),QrCodeActivity.class));
                break;
        }
    }
    //跳转qq群
    //连接https://qun.qq.com/join.html
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=" +
                "http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr" +
                "%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，
        // 则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
}
