package com.example.runninggroup.controller;

import android.graphics.drawable.Drawable;
import android.net.IpSecManager;
import android.widget.ImageView;

import com.example.runninggroup.cache.Cache;
import com.example.runninggroup.dao.FileDao;
import com.example.runninggroup.dao.UserDao;
import com.example.runninggroup.pojo.Team;
import com.example.runninggroup.pojo.User;
import com.example.runninggroup.util.ImgNameUtil;
import com.example.runninggroup.util.StringUtil;

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
                if (users == null) mUserControllerInterface.isLoadBack(null, "网络故障");
                else if (users.size() == 0) {
                    mUserControllerInterface.isLoadBack(null, "用户名或密码错误");
                }else {
                    mUserControllerInterface.isLoadBack(users.get(0), "登录成功");
                }
            }
        }).start();
    }
    //查找用户
    public void selectUser (String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user1 = new User();
                user1.setUsername(msg);
                List<User> userList1 = UserDao.getUser(user1);

                User user2 = new User();
                user2.setRegisterNum(msg);
                List<User> userList2 = UserDao.getUser(user2);
               if (userList1 != null && userList2 != null) {
                   userList1.addAll(userList2);
                   mUserControllerInterface.selectUserBack(userList1);
               }else if (userList1 != null) mUserControllerInterface.selectUserBack(userList1);
               else mUserControllerInterface.selectUserBack(userList2);
            }
        }).start();
    }
    //查找用户
    public void  selectUserByUser(User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> users = null;
                for (int i = 0; i < 10; i++) {
                    if (users == null) users = UserDao.getUser(user);
                    else break;
                }
                mUserControllerInterface.selectUserByUserBack(users);

            }
        }).start();
    }
    //查找用户
    public void  selectUserByUserWithType(User user, int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> users = null;
                for (int i = 0; i < 10; i++) {
                    if (users == null) users = UserDao.getUser(user);
                    else break;
                }
                mUserControllerInterface.selectUserByUserWithTypeBack(users, type);

            }
        }).start();
    }


    //注册用户
    public void register (String mail, String password, String sex, String username) {
        User user = new User();
        user.setMail(mail);
        user.setPassword(password);
        user.setUsername(username);
        user.setSex(sex.equals("男") ? 1 : 2);
        for (int i = 0; i < 10; i++) {
            String registerNum = StringUtil.getRegisterNum();
            user.setRegisterNum(registerNum);
            if (UserDao.addUser(user)) {
                mUserControllerInterface.registerBack(true, registerNum);
                return;
            }

        }
        mUserControllerInterface.registerBack(false, "请稍后尝试");


    }
    //更换头像
    public void changeHeadImg (File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int id = Cache.user.getId();
                String imgName = ImgNameUtil.getUserHeadImgName(id);
                boolean res = FileDao.upload(file, imgName);
                mUserControllerInterface.changeHeadImgBack(res);

            }
        }).start();


    }

    //获得头像
    public void getHeadImg (String imgName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = null;
                for (int i = 0; i < 10; i++) {
                    if (drawable != null) break;
                    else  drawable = FileDao.getImg(imgName);
                }
                mUserControllerInterface.getHeadImgBack(drawable);
            }
        }).start();
    }

    //根据registerNum拿到drawable
    public void getImgByRegisterNum (String registerNum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setRegisterNum(registerNum);
                List<User> userList = UserDao.getUser(user);
                if (userList == null || userList.size() == 0) mUserControllerInterface.getImgByRegisterNumBack(null);
                else {
                    Drawable drawable = FileDao.getImg(ImgNameUtil.getUserHeadImgName(userList.get(0).getId()));
                    mUserControllerInterface.getImgByRegisterNumBack(drawable);
                }
            }
        }).start();
    }
    //更新用户信息
    public void updateUser (User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = UserDao.updateUser(user);
                mUserControllerInterface.updateUserBack(res, user);
            }
        }).start();
    }

    //修改密码
    public void changePwd (String newPwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setId(Cache.user.getId());
                user.setPassword(newPwd);
                if (UserDao.updateUser(user)) {
                    Cache.user.setPassword(newPwd);
                    mUserControllerInterface.changePwdBack(true);
                }else mUserControllerInterface.changePwdBack(false);

            }
        }).start();
    }
    //退出跑团
    public void signOutTeam () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = UserDao.signOutTeam(Cache.user);
                if (res) Cache.user.setTeam(null);
                mUserControllerInterface.signOutTeamBack(res);

            }
        }).start();
    }

    //修改用户权限
    public void administrators (User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (user.getTeamLevel() == 2) user.setTeamLevel(1);
                else if (user.getTeamLevel() == 1) user.setTeamLevel(2);
                boolean res = UserDao.administrators(user);
                mUserControllerInterface.administratorsBack(res);
            }
        }).start();
    }



    public interface UserControllerInterface {
        //用户登录查询回调函数
        default void isLoadBack (User user, String msg){}
        default void selectUserBack (List<User> users){}
        default void selectUserByUserBack (List<User> users){}
        //注册用户
        default void registerBack (boolean res, String registerNum){}

        //更换头像
        default void changeHeadImgBack (boolean res){}
        //获得头像
        default void getHeadImgBack (Drawable drawable) {}
       //更新用户信息
        default void updateUserBack (boolean res, User user) {};
        //修改密码
        default void changePwdBack(boolean res) {};
        default void getImgByRegisterNumBack (Drawable drawable){}
        //退出跑团
        default void signOutTeamBack(boolean res){};
        //修改用户权限
        default void administratorsBack(boolean res){};

        default void selectUserByUserWithTypeBack(List<User> users, int type){};
    }
}
