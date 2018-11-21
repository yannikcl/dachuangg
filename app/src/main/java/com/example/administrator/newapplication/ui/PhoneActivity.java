package com.example.administrator.newapplication.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.utils.L;
import com.example.administrator.newapplication.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_number;
    private ImageView iv_company;
    private TextView tv_result;
    private Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_0,btn_del,btn_find;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        initView();
    }

    private void initView() {
        et_number = findViewById(R.id.et_number);
        iv_company = findViewById(R.id.iv_company);
        tv_result = findViewById(R.id.tv_result);

        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_0 = findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_del = findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_find = findViewById(R.id.btn_find);
        btn_find .setOnClickListener(this);

        //长按就删除
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_number.setText("");
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        //获取输入框的内容
        String str = et_number.getText().toString();
        switch (v.getId()){
            /**
             * 逻辑
             * 1.获取输入框的内容
             * 2.判断是否为空
             * 3.网络请求
             * 4.解析Json
             * 5.结果显示
             *
             * ------
             * 键盘逻辑
             */

            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                //每次结尾添加一
                et_number.setText(str + ((Button)v).getText());
                //移动光标
                et_number.setSelection(str.length() + 1);
                break;
            case R.id.btn_del:
                if (!TextUtils.isEmpty(str) && str.length() > 0){
                    //每次结尾减去一
                    et_number.setText(str.substring(0,str.length()-1));
                    //移动光标
                    et_number.setSelection(str.length() - 1);
                }
                break;
            case R.id.btn_find:
                getPhone(str);
                break;
        }
    }

    private void getPhone(String str) {
        String url = "http://apis.juhe.cn/mobile/get?phone="+ str +"&key="+ StaticClass.PHONE_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(PhoneActivity.this,"结果",Toast.LENGTH_SHORT).show();
                L.i("phone" + t);
                //解析
                parsingJson(t);
            }

            private void parsingJson(String t) {
                try{
                    JSONObject jsonObject = new JSONObject(t);
                    JSONObject jsonResult = jsonObject.getJSONObject("result");
                    //解析数据并且得到数据的详细内容
                    String province = jsonResult.getString("province");
                    String city = jsonResult.getString("city");
                    String areacode = jsonResult.getString("areacode");
                    String zip = jsonResult.getString("zip");
                    String company = jsonResult.getString("company");

                    //设置数据在文本中显示出来
                    tv_result.append("归属地:" + province + city + "\n");
                    tv_result.append("区号:" + areacode + "\n");
                    tv_result.append("邮编:" + zip + "\n");
                    tv_result.append("运营商:" + company + "\n");
                    //图片显示
                    switch (company){
                        case "移动":
                            iv_company.setBackgroundResource(R.drawable.move);
                            break;
                        case "联通":
                            iv_company.setBackgroundResource(R.drawable.unicom);
                            break;
                        case "电信":
                            iv_company.setBackgroundResource(R.drawable.telecom);
                            break;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
