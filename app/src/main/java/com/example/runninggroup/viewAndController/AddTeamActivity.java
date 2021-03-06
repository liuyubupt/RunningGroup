package com.example.runninggroup.viewAndController;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.runninggroup.R;
import com.example.runninggroup.cache.Cache;
import com.example.runninggroup.controller.FileController;
import com.example.runninggroup.controller.FriendApplicationController;
import com.example.runninggroup.controller.TeamApplicationController;
import com.example.runninggroup.controller.UserController;
import com.example.runninggroup.util.ConstantUtil;
import com.example.runninggroup.util.ImgNameUtil;
import com.example.runninggroup.util.StringUtil;
import com.example.runninggroup.util.WindowsEventUtil;
import com.example.runninggroup.view.KyLoadingBuilder;

public class AddTeamActivity extends AppCompatActivity implements View.OnClickListener, FileController.FileControllerInterface, TeamApplicationController.TeamApplicationControllerInterface {
    private ImageView teamImg;
    private TextView teamNameText,sendText,cancleText, teamRegisterNumText;
    private EditText msgEdt;
    private FileController mFileController = new FileController(this);
    private TeamApplicationController mTeamApplicationController = new TeamApplicationController(this);
    private KyLoadingBuilder mKyLoadingBuilder;
    private long startTime;
    private long endTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        initView();
        initEvent();
    }

    private void initView() {

        teamImg = findViewById(R.id.teamImg);
        teamNameText = findViewById(R.id.teamName);
        teamRegisterNumText = findViewById(R.id.teamRegisterNum);
        sendText = findViewById(R.id.send);
        cancleText = findViewById(R.id.cancel);
        msgEdt = findViewById(R.id.msg);
        mFileController.getImg(ImgNameUtil.getGroupHeadImgName(Cache.team.getId()));
        teamNameText.setText(Cache.team.getTeamName());
        teamRegisterNumText.setText(Cache.team.getRegisterNum());




    }
    private void initEvent() {
        cancleText.setOnClickListener(this);
        sendText.setOnClickListener(this);



    }


    @Override
    public void getImgBack(Drawable drawable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawable != null) teamImg.setImageDrawable(drawable);
            }
        });
    }

    @Override
    public void addTeamApplicationBack(boolean res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    endTime = System.currentTimeMillis();
                    if (endTime - startTime < ConstantUtil.MAX_KYLOADING_WAIT_TIME) Thread.sleep(ConstantUtil.MAX_KYLOADING_WAIT_TIME - (endTime - startTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mKyLoadingBuilder.setText(res ? "发送成功" : "发送失败");
                try {
                    Thread.sleep(ConstantUtil.MAX_KYLOADING_SHOW_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mKyLoadingBuilder.dismiss();
                if (res) {
                    Intent intent = new Intent(AddTeamActivity.this, TeamIntroduction.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                mKyLoadingBuilder = new KyLoadingBuilder(this);
                mKyLoadingBuilder.setText("正在发送中...");
                mKyLoadingBuilder.show();
                startTime = System.currentTimeMillis();
                mTeamApplicationController.addTeamApplication(StringUtil.isStringNull(msgEdt.getText().toString()) ? "请求加入跑团" : msgEdt.getText().toString());
                break;

            case R.id.cancel:
                WindowsEventUtil.systemBack();

            case R.id.setting:
                break;
        }
    }


}
