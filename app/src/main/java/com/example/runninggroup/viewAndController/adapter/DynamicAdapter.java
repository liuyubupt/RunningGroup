package com.example.runninggroup.viewAndController.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.runninggroup.R;
import com.example.runninggroup.model.DaoUser;
import com.example.runninggroup.viewAndController.helper.DynamicHelper;
import com.example.runninggroup.viewAndController.helper.GroupTaskHelper;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class DynamicAdapter extends BaseAdapter {
    public LayoutInflater mInflater;
    public List<DynamicHelper> mList;
    HashMap<Integer,Drawable> mDrawable;
    String name;
    public DynamicAdapter(LayoutInflater inflater, List<DynamicHelper> list,String name) {
        mInflater = inflater;
        mList = list;
        this.name = name;
        mDrawable = new HashMap<>(list.size());
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<list.size();i++){
                    Drawable drawable = DaoUser.getImg(DaoUser.getDynamicImgName(name, mList.get(i).getDynamic_time()));
                    if(drawable!=null) mDrawable.put(i,drawable);
                }

            }
        }).start();
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
        //ViewHolder内部类
        class ViewHolder{
            public ImageView img;
            public TextView msg;
            public TextView time;

        }
        //判断converView是否为空
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.helper_dynamic,null);
            viewHolder=new ViewHolder();
            viewHolder.img=convertView.findViewById(R.id.dynamic_img);
            viewHolder.msg=convertView.findViewById(R.id.dynamic_msg);
            viewHolder.time=convertView.findViewById(R.id.dynamic_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mDrawable.get(position) == null){
                    Drawable drawable = DaoUser.getImg(DaoUser.getDynamicImgName(name,mList.get(position).getDynamic_time()));
                    if(drawable != null){
                        mDrawable.put(position,DaoUser.getImg(DaoUser.getDynamicImgName(name,mList.get(position).getDynamic_time())));
                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(mDrawable.get(position) != null){viewHolder.img.setImageDrawable(mDrawable.get(position));}
        //赋值
        viewHolder.msg.setText(mList.get(position).getDynamic_msg());
        viewHolder.time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mList.get(position).getDynamic_time()));







        return convertView;

    }
}
