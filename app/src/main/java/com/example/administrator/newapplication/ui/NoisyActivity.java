package com.example.administrator.newapplication.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.fragment.CurveFragment;

public class NoisyActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.context);
        CurveFragment fragment=new CurveFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.context_root, fragment, "CURVEFRAGMENT").commit();

    }
    private long firstTime;

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - firstTime < 3000) {
            finish();
        } else {
            firstTime = System.currentTimeMillis();
            Toast.makeText(this, "连续按两次退出程序", Toast.LENGTH_LONG).show();
        }
    }

}
