package com.example.runninggroup.controller;

import com.example.runninggroup.cache.Cache;
import com.example.runninggroup.dao.FriendApplicationDao;
import com.example.runninggroup.dao.FriendRelationDao;
import com.example.runninggroup.pojo.FriendApplication;
import com.example.runninggroup.pojo.FriendRelation;
import com.example.runninggroup.pojo.User;

import java.util.List;

public class FriendApplicationController {
    private FriendApplicationControllerInterface mFriendApplicationControllerInterface;
    public FriendApplicationController(FriendApplicationControllerInterface friendApplicationControllerInterface) {
        mFriendApplicationControllerInterface = friendApplicationControllerInterface;
    }


    //发送好友申请
    public void sendFriendApplication (String applicationMsg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FriendApplication friendApplication = new FriendApplication();
                friendApplication.setTo(Cache.friend);
                friendApplication.setFrom(Cache.user);
                friendApplication.setState(1);
                friendApplication.setApplicationMsg(applicationMsg);
                mFriendApplicationControllerInterface.sendFriendApplicationBack(FriendApplicationDao.startFriendApplication(friendApplication));
            }
        }).start();
    }
    //删除好友申请
    public void deleteFriendApplication (FriendApplication friendApplication, int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = FriendApplicationDao.deleteFriendApplication(friendApplication);
                mFriendApplicationControllerInterface.deleteFriendApplicationBack(res, position, friendApplication);
            }
        }).start();
    }
    //删除所有申请
    public void deleteAllFriendApplication () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FriendApplication friendApplication = new FriendApplication();
                User user = new User();
                user.setId(Cache.user.getId());
                friendApplication.setUser(user);
                boolean res = FriendApplicationDao.deleteFriendApplication(friendApplication);
                mFriendApplicationControllerInterface.deleteAllFriendApplicationBack(res);
            }
        }).start();
    }

    //查询所有的申请记录
    public void getApplication () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FriendApplication friendApplication = new FriendApplication();
                friendApplication.setUser(Cache.user);
                List<FriendApplication> friendApplicationList = FriendApplicationDao.getFriendApplication(friendApplication);
                mFriendApplicationControllerInterface.getApplicationBack(friendApplicationList);
            }
        }).start();
    }

    //同意或者拒绝
    public void agreeToRefuse (FriendApplication friendApplication, int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = FriendApplicationDao.agreeToRefuse(friendApplication);
                mFriendApplicationControllerInterface.agreeToRefuseBack(res, position);
            }
        }) .start();
    }





    public interface FriendApplicationControllerInterface {
        //发送申请
        default void sendFriendApplicationBack(boolean res) {}
        //查询所有申请记录
        default void getApplicationBack(List<FriendApplication> friendApplicationList) {}
        default void agreeToRefuseBack (boolean res, int position) {}
        default void deleteAllFriendApplicationBack (boolean res) {}
        default void deleteFriendApplicationBack (boolean res, int position, FriendApplication friendApplication) {}

    }
}
