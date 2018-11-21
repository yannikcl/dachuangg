package com.example.administrator.newapplication.ui;

import android.os.Bundle;
import android.util.Log;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.entity.SignDate;
import com.example.administrator.newapplication.service.OnSignedSuccess;

//签到页
public class WriteActivity extends BaseActivity {

    private SignDate signDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        signDate = findViewById(R.id.signDate);
        signDate.setOnSignedSuccess(new OnSignedSuccess() {
            @Override
            public void OnSignedSuccess() {
                Log.e("wqf","Success");
            }
        });
    }
}
