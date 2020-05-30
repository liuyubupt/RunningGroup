package com.example.runninggroup.model;

import android.graphics.drawable.Drawable;

import com.example.runninggroup.request.ImgGet;
import com.example.runninggroup.request.PostRequest;
import com.example.runninggroup.viewAndController.helper.FriendsHelper;

import com.alibaba.fastjson.JSONObject;
import com.example.runninggroup.viewAndController.helper.GroupHelper;
import com.example.runninggroup.viewAndController.helper.User;


import java.util.ArrayList;
import java.util.List;

public class DaoUser {

    private DaoUser(){};
//    private static class Inner{
//        private static DaoUser daoUser = new DaoUser();
//    }
//    public static DaoUser getInstance(){
//        return Inner.daoUser;
//    }
    public static String isLoad(String username,String password){
      String result = PostRequest.postRequest("http://39.97.66.19:8080/user/load","username="+username+"&password="+password);
        if (result == null) return "ERROR";
        return result;
    }
    public static String register(String username,String password,String sex){
        String result =  PostRequest.postRequest("http://39.97.66.19:8080/user/register","username="+username+"&password="+password+"&sex="+sex);
        if (result == null) return "ERROR";
        return result;
    }
    public static List<FriendsHelper> getFriends(String username){
        String json =  PostRequest.postRequest("http://39.97.66.19:8080/user/getFriends","username="+username);
        List<FriendsHelper> list = JSONObject.parseArray(json,FriendsHelper.class);
        if(list != null){
            return list;
        }
        return new ArrayList<FriendsHelper>();
    }
    public static String getMyGroup(String username){
        String group =  PostRequest.postRequest("http://39.97.66.19:8080/user/getMyGroup","username="+username);
        if (group == null) return "ERROR";
        return group;
    }

    public static List<GroupHelper> getMyGroupAll(String username){
        String group =  PostRequest.postRequest("http://39.97.66.19:8080/user/getMyGroupAll","username="+username);
        List<GroupHelper> list = JSONObject.parseArray(group, GroupHelper.class);
        if (list == null) return new ArrayList<GroupHelper>();
        return  list;
    }
    public static String setMyGroup(String username,String groupName,int change,String type){
        String group =  PostRequest.postRequest("http://39.97.66.19:8080/user/setMyGroup","username="+username+"&groupName="+groupName+"&change="+change+"&type="+type);
        if (group == null) return "ERROR";
        return group;
    }

    public static List<User> getAllMember(String groupName){
        String json =  PostRequest.postRequest("http://39.97.66.19:8080/user/getAllMember","groupName="+groupName);
        List<User> list = JSONObject.parseArray(json,User.class);
        if (list == null) return new ArrayList<User>();
        return  list;
    }
    //查询用户性别
    public static String getSex(String username){
        String result = PostRequest.postRequest("http://192.168.0.104:8080/run/user/getSex","username="+username);
        return result;
    }
    //修改用户
    public static boolean changeSex(String username){
        String result = PostRequest.postRequest("http://192.168.0.104:8080/run/user/changeSex","username="+username);
        if("SUCCESS".equals(result)){return true;}
        return false;
    }
    //上传用户头像

    //获取用户头像
   public static Drawable getImg(String imgName){
       return ImgGet.getImg(imgName);
   }
    //获取用户头像的imgName
    public static String getUserHeadImgName(String username){
        return "user_"+username+"_head";
    }
    //获取跑团的头像
    public static String getGroupHeadImgName(String groupName){
        return "group_"+groupName+"_head";
    }
    //获取好友某条动态的图片
    public static String getDynamicImgName(String username,long dynamic_time){
        return "dynamic_"+username+"_"+dynamic_time;
    }
    //获取跑团某条任务的图片
    public static String getTaskImgName(String admin,long task_time){
        return "task_"+admin+"_"+task_time;
    }
    //获取跑团招募的图片
    public static String getCallImgName(String admin,long task_time){
        return "call_"+admin+"_"+task_time;
    }

}
