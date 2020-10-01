package com.example.runninggroup.controller;

import android.graphics.drawable.Drawable;

import com.example.runninggroup.cache.UserCache;
import com.example.runninggroup.dao.FileDao;
import com.example.runninggroup.dao.UserDao;
import com.example.runninggroup.model.DaoUser;
import com.example.runninggroup.pojo.User;
import com.example.runninggroup.request.ImgUpload;
import com.example.runninggroup.util.ImgNameUtil;

import java.io.File;
import java.util.List;

public class UserController {
    private UserControllerInterface mUserControllerInterface;
    public UserController (UserControllerInterface userControllerInterface) {
        mUserControllerInterface = userControllerInterface;
    }

    //登陆验证
    public void isLoad (String registerNum, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setRegisterNum(registerNum);
                user.setPassword(password);
                List<User> users = UserDao.getUser(user);
                if (users.size() == 0) mUserControllerInterface.isLoadBack(null);
                else mUserControllerInterface.isLoadBack(users.get(0));
            }
        }).start();
    }

    //注册用户
    public void register (String registerNum, String password, String sex, String username) {
        User user = new User();
        user.setRegisterNum(registerNum);
        user.setPassword(password);
        user.setUsername(username);
        user.setSex(sex.equals("男") ? 1 : 2);
        mUserControllerInterface.registerBack(UserDao.addUser(user));

    }
    //更换头像
    public void changeHeadImg (File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int id = UserCache.user.getId();
                String imgName = ImgNameUtil.getUserHeadImgName(id);

                User user = new User();
                user.setId(id);
                user.setHeadImg(imgName);
                boolean res1 = UserDao.updateUser(user);
                boolean res2 = FileDao.upload(file, imgName);
                if (res1 && res2) {
                    UserCache.user.setHeadImg(imgName);
                    mUserControllerInterface.changeHeadImgBack(true);
                }else mUserControllerInterface.changeHeadImgBack(false);
            }
        }).start();


    }

    //获得头像
    public void getHeadImg () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = FileDao.getImg(UserCache.user.getHeadImg());
                mUserControllerInterface.getHeadImg(drawable);
            }
        }).start();
    }

    //修改昵称
    public void changeName (String newName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setId(UserCache.user.getId());
                user.setUsername(newName);
                if (UserDao.updateUser(user)) {
                    UserCache.user.setUsername(newName);
                    mUserControllerInterface.changeNameBack(true);
                }else mUserControllerInterface.changeNameBack(false);

            }
        }).start();
    }
    //修改昵称
    public void changeSex () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setId(UserCache.user.getId());
                user.setSex(UserCache.user.getSex() == 1 ? 2 : 1);
                if (UserDao.updateUser(user)) {
                    UserCache.user.setSex(UserCache.user.getSex() == 1 ? 2 : 1);
                    mUserControllerInterface.changeSexBack(true);
                }else mUserControllerInterface.changeSexBack(false);

            }
        }).start();
    }
    //修改密码
    public void changePwd (String newPwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setId(UserCache.user.getId());
                user.setPassword(newPwd);
                if (UserDao.updateUser(user)) {
                    UserCache.user.setPassword(newPwd);
                    mUserControllerInterface.changePwdBack(true);
                }else mUserControllerInterface.changePwdBack(false);

            }
        }).start();
    }



    public interface UserControllerInterface {
        //用户登录查询回调函数
        default void isLoadBack (User user){}
        //注册用户
        default void registerBack (boolean res){}

        //更换头像
        default void changeHeadImgBack (boolean res){}
        //获得头像
        default void getHeadImg (Drawable drawable) {}
        //修改昵称
        default void changeNameBack (boolean res) {}
        //修改性别
        default void changeSexBack (boolean res) {}
        //修改密码
        default void changePwdBack(boolean res) {};
    }
}