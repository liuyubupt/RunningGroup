package com.example.runninggroup.viewAndController;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.runninggroup.R;
import com.example.runninggroup.model.DaoUser;
import com.example.runninggroup.request.ImgUpload;
import com.example.runninggroup.util.BitmapUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PersonalSetting extends AppCompatActivity {
    String username;
    private String img_src;
    private final int SELECT_PHOTO = 2;
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    ListView mListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalsetting);
        initView();
        initEvent();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                switch (resultCode) {
                    case RESULT_OK:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Uri uri = data.getData();
                                img_src = uri.getPath();//这是本机的图片路径
                                Log.v("本机路径：",img_src);
                                ContentResolver cr = getContentResolver();
                                try {
                                    InputStream inputStream = cr.openInputStream(uri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    inputStream.close();
                                    /* 将Bitmap设定到ImageView */
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PersonalSetting.this);
                                            final View view1=getLayoutInflater().inflate(R.layout.helper_changehead,null);
                                            ImageView img = view1.findViewById(R.id.head_img);
                                            img.setImageBitmap(bitmap);

                                            builder.setMessage("确定更换头像？")
                                                    .setView(view1)
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    String path = BitmapUtil.saveMyBitmap(PersonalSetting.this,bitmap,DaoUser.getUserHeadImgName(username));
                                                                    File file = new File(path);
                                                                    String result = ImgUpload.uploadFile(file, DaoUser.getUserHeadImgName(username));
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Toast.makeText(PersonalSetting.this, result, Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(PersonalSetting.this,MainInterface.class);
                                                                            intent.putExtra("username",username);
                                                                            intent.putExtra("id",0);
                                                                            startActivity(intent);
                                                                        }
                                                                    });
                                                                }
                                                            }).start();
                                                        }
                                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                            builder.show();
//                                            mIma_chosePic.setImageBitmap(bitmap);
                                        }
                                    });


                                    String[] proj = {MediaStore.Images.Media.DATA};
                                    CursorLoader loader = new CursorLoader(PersonalSetting.this, uri, proj, null, null, null);
                                    Cursor cursor = loader.loadInBackground();
                                    if (cursor != null) {
                                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                        cursor.moveToFirst();

                                        img_src = cursor.getString(column_index);//图片实际路径
                                        Log.v("实际路径：",img_src);

                                    }
                                    cursor.close();

                                } catch (FileNotFoundException e) {
                                    Log.e("Exception", e.getMessage(), e);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();

                        break;
                }
                break;
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:

                break;

        }

    }
    private void initView() {
        username = getIntent().getStringExtra("username");
        mListView = findViewById(R.id.settingList);
    }
    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSetting.this);
                        builder.setTitle("请选择头像：");
                        builder.setMessage("可以通过相机或者相册选取头像。");
                        builder.setPositiveButton("相册选取", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent, SELECT_PHOTO);
                            }
                        });
                        builder.create();
                        builder.show();
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent = new Intent(PersonalSetting.this,ChangePassword.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        break;
                    case 3:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PersonalSetting.this);
                        builder1.setMessage("确定要更改性别吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (DaoUser.changeSex(username)) {
                                                    makeToast("修改成功！");
                                                }
                                                else makeToast("修改失败！");
                                            }
                                        }).start();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        break;
                }
            }
        });
    }
    public void makeToast(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PersonalSetting.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
