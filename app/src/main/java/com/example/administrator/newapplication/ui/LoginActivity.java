package com.example.administrator.newapplication.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newapplication.MainActivity;
import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.entity.MyUser;
import com.example.administrator.newapplication.utils.ShareUtils;
import com.example.administrator.newapplication.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

//登录
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_registered;
    private EditText et_name;
    private EditText et_password;
    private Button btnLogin;
    private CheckBox keepPassword;
    private TextView tv_forget;
    //dialog图标
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        tv_forget = findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);

        btn_registered = findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);

        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        keepPassword = findViewById(R.id.keepPassword);
        //设置我们选中的状态
        Boolean isChecked = ShareUtils.getBoolean(this,"keeppass",false);
        keepPassword.setChecked(isChecked);
        if (isChecked){
            //设置密码
            et_name.setText(ShareUtils.getString(this,"name",""));
            et_password.setText(ShareUtils.getString(this,"password",""));
        }
        //初始化dialog
        dialog = new CustomDialog
                (this,100,100,
                        R.layout.dialog_loding,R.style.Theme_dialog,
                        Gravity.CENTER,R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_forget:
                startActivity(new Intent(this,ForgetPasswordActivity.class));
                break;
            case R.id.btn_registered:
                startActivity(new Intent(this,RegisteredActivity.class));
                break;
            case R.id.btnLogin:
                //获取输入框的值
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(name)&!TextUtils.isEmpty(password)){
                    //点击登录后展示一个dialog
                    dialog.show();
                    //登录
                    MyUser myUser = new MyUser();
                    myUser.setUsername(name);
                    myUser.setPassword(password);
                    myUser.login(new SaveListener<Object>() {
                        @Override
                        public void done(Object o, BmobException e) {
                            //登录后dialog就消失掉
                            dialog.dismiss();
                            //判断结果
                            if (e == null){
                                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                //判断邮箱是否验证
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //假设我现在输入用户名和密码，但我不点击登录，而是直接退出了
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this,"keeppass",keepPassword.isChecked());

        //是否记住密码
        if (keepPassword.isChecked()){
            //选中了后记住用户名和密码
            ShareUtils.putString(this,"name", et_name.getText().toString().trim());
            ShareUtils.putString(this,"password",et_password.getText().toString().trim());
        }else {
            ShareUtils.deleShar(this,"name");
            ShareUtils.deleShar(this,"password");
        }
    }
}
