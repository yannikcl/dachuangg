package com.example.administrator.newapplication.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.utils.ZXingUtils;

public class QrCodeActivity extends BaseActivity implements View.OnClickListener{

    private EditText tv_qrCode_content;//用来生成二维码图片内包含的内容

    private TextView tv_click;//按钮

    private ImageView iv_qr_code;//显示二维码的ImageView

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        findView();
        inIt();
    }

    private void findView() {
        tv_qrCode_content= (EditText) findViewById(R.id.tv_qrCode_content);
        tv_click= (TextView) findViewById(R.id.tv_click);
        iv_qr_code= (ImageView) findViewById(R.id.iv_qr_code);
    }

    private void inIt() {
        tv_click.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_click:
                String content = tv_qrCode_content.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(this,"请先输入需要生成二维码的内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                Bitmap bitmap = ZXingUtils.createQRImage(content, iv_qr_code.getWidth(), iv_qr_code.getHeight());
                iv_qr_code.setImageBitmap(bitmap);
                break;
        }
    }
}
