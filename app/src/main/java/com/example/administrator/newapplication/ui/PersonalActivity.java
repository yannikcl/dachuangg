package com.example.administrator.newapplication.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newapplication.R;
import com.example.administrator.newapplication.entity.MyUser;
import com.example.administrator.newapplication.utils.L;
import com.example.administrator.newapplication.utils.ShareUtils;
import com.example.administrator.newapplication.view.CustomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;

    private Button btn_update_ok;
    private TextView edit_user;
    //圆形头像
    private CircleImageView profile_image;
    private CustomDialog dialog;
    //dialog
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pensonal_information);

        init();
    }

    private void init() {
        profile_image = findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        //拿到String
        String imageString = ShareUtils.getString(this, "image_title", "");
        if (!imageString.equals("")) {
            //利用Base64将我们String转换
            byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            //生成Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
            profile_image.setImageBitmap(bitmap);
        }
        //初始化dialog
        dialog = new CustomDialog
                (PersonalActivity.this, 0, 0,
                        R.layout.dialog_photo, R.style.pop_anim_style,
                        Gravity.BOTTOM, 0);
        //屏幕外点击无效
        dialog.setCancelable(false);
        btn_camera = dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        et_username = findViewById(R.id.et_username);
        et_sex = findViewById(R.id.et_sex);
        et_age = findViewById(R.id.et_age);
        et_desc = findViewById(R.id.et_desc);
        //默认是不可点击的
        setEnabled(false);
        //设置具体的数值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getUsername());
        //age是int值所以需要加一个字符串
        et_age.setText(userInfo.getAge() + "");
        et_sex.setText(userInfo.isSex() ? "男" : "女");
        et_desc.setText(userInfo.getDesc());

        btn_update_ok = findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);
        edit_user = findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
    }

    private void setEnabled(boolean is) {
        //默认是不可点击的
        et_username.setEnabled(is);
        et_sex.setEnabled(is);
        et_age.setEnabled(is);
        et_desc.setEnabled(is);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //确认修改
            case R.id.btn_update_ok:
                //那到输入框的值
                String uername = et_username.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String sex = et_sex.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(uername) & !TextUtils.isEmpty(age) & !TextUtils.isEmpty(sex)) {
                    //更新属性
                    MyUser user = new MyUser();
                    user.setUsername(uername);
                    user.setAge(Integer.parseInt(age));
                    //性别
                    if (sex.equals("男")) {
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }
                    //简介
                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc("这个人很懒，什么都没有留下");
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //修改成功
                                setEnabled(false);
                                btn_update_ok.setVisibility(View.GONE);
                                Toast.makeText(PersonalActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PersonalActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            //编辑资料
            case R.id.edit_user:
                setEnabled(true);
                btn_update_ok.setVisibility(View.VISIBLE);
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_camera:
                //跳转相机
                toCamera();
                break;
            case R.id.btn_picture:
                //跳转相册
                toPicture();
                break;
            case R.id.btn_cancel:
                //取消
                dialog.dismiss();
                break;
        }

    }

    //定义一个常量
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int Result_REQUEST_CODE = 102;
    private File tempFile;

    //可能会出现一些兼容性问题
    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存是否可用，用的化就直接存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                //拍照得到的文件
                Uri.fromFile(new File(Environment.
                        //得到一个内存的文件
                                getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        //图片
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    //在里面拿到返回值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PersonalActivity.this.RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    //相册数据
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    //相机数据
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case Result_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经拿到了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            //判断是否为空，为空就直接返回
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置类型
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高,一比一相当于正方形裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Result_REQUEST_CODE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存
        BitmapDrawable bitmapDrawable = (BitmapDrawable) profile_image.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        //第一步将bitmap压缩成字节输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步利用base64将字节数组输出流转换为String
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String imgString = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        //第三步保存String
        ShareUtils.putString(this, "image_title", imgString);
    }
}
