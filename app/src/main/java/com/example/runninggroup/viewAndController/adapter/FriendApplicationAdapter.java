package com.example.runninggroup.viewAndController.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.runninggroup.R;
import com.example.runninggroup.cache.Cache;
import com.example.runninggroup.dao.FileDao;
import com.example.runninggroup.pojo.FriendApplication;
import com.example.runninggroup.pojo.FriendRelation;
import com.example.runninggroup.pojo.Team;
import com.example.runninggroup.pojo.User;
import com.example.runninggroup.request.ImgGet;

import java.util.List;

public class FriendApplicationAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    List<FriendApplication> mList;
    private Activity mActivity;
    private ViewHolder viewHolder1;
    public FriendApplicationAdapter(LayoutInflater inflater, List<FriendApplication> list, Activity activity) {
        mInflater = inflater;
        mList = list;
        mActivity = activity;
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
        int state = mList.get(position).getState();
        User user = mList.get(position).getUser();
        User friend = mList.get(position).getFrom();
        String msg = mList.get(position).getApplicationMsg();
        convertView = mInflater.inflate(R.layout.helper_friend_application, null);
        viewHolder1 = new ViewHolder();
        viewHolder1.mImageView = convertView.findViewById(R.id.img);
        viewHolder1.usernameText = convertView.findViewById(R.id.username);
        viewHolder1.msgText = convertView.findViewById(R.id.msg);
        viewHolder1.stateText = convertView.findViewById(R.id.state);
        viewHolder1.btn = convertView.findViewById(R.id.btn);
        convertView.setTag(viewHolder1);
        //自己申请的
        if (friend.getId() == Cache.user.getId()) {
            viewHolder1.mImageView.setImageResource(friend.getSex() == 1 ? R.drawable.default_head_m : R.drawable.default_head_w);
            setImg(viewHolder1, user.getHeadImg());
            if (state == 1) {
                //以发送申请，等待验证
                viewHolder1.usernameText.setText(user.getUsername());
                viewHolder1.btn.setVisibility(View.INVISIBLE);
                viewHolder1.stateText.setText("等待验证");
                viewHolder1.msgText.setText("已发送验证信息");


            }else if (state == 2) {
                //对方已同意
                viewHolder1.usernameText.setText(user.getUsername());
                viewHolder1.btn.setVisibility(View.INVISIBLE);
                viewHolder1.stateText.setText("对方已同意");
                viewHolder1.msgText.setText("已发送验证信息");

            }else if (state == 3) {
                //对方已拒绝
                viewHolder1.usernameText.setText(user.getUsername());
                viewHolder1.btn.setVisibility(View.INVISIBLE);
                viewHolder1.stateText.setText("对方已拒绝");
                viewHolder1.msgText.setText("已发送验证信息");

            }
        }
        //别人申请的
        if (user.getId() == Cache.user.getId()) {
            viewHolder1.mImageView.setImageResource(user.getSex() == 1 ? R.drawable.default_head_m : R.drawable.default_head_w);
            setImg(viewHolder1, friend.getHeadImg());
            if (state == 1) {
                //有验证消息和同意按钮
                viewHolder1.usernameText.setText(friend.getUsername());
                viewHolder1.btn.setVisibility(View.VISIBLE);
                viewHolder1.stateText.setVisibility(View.INVISIBLE);
                viewHolder1.msgText.setText("对方留言： " + msg);

            }else if (state == 2) {
                //验证消息和已同意
                viewHolder1.usernameText.setText(friend.getUsername());
                viewHolder1.btn.setVisibility(View.INVISIBLE);
                viewHolder1.stateText.setVisibility(View.VISIBLE);
                viewHolder1.stateText.setText("已同意");
                viewHolder1.msgText.setText("对方留言： " + msg);

            }else if (state == 3) {
                //验证消息和已拒绝
                viewHolder1.usernameText.setText(friend.getUsername());
                viewHolder1.btn.setVisibility(View.INVISIBLE);
                viewHolder1.stateText.setVisibility(View.VISIBLE);
                viewHolder1.stateText.setText("已拒绝");
                viewHolder1.msgText.setText("对方留言： " + msg);

            }
        }

        return convertView;
    }
    class ViewHolder {
        ImageView mImageView;
        TextView usernameText, msgText, stateText;
        Button btn;
    }

    public void setImg (ViewHolder viewHolder, String imgName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = FileDao.getImg(imgName);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (drawable != null)
                            viewHolder.mImageView.setImageDrawable(drawable);
                    }
                });

            }
        }).start();
    }



}
