package com.example.runninggroup.model;

import com.example.runninggroup.request.PostRequest;

public class DaoDynamic {

    private DaoDynamic(){};


    //插入一条动态
    public static String writeDynamic(String username,String dynamic_msg,long dynamic_time){
        String result = PostRequest.postRequest("http://39.97.66.19:8080/dynamic/writeDynamic","username="+username+"&dynamic_msg="+dynamic_msg+"&dynamic_time="+dynamic_time);
        if (result == null) return "ERROR";
        return result;
    }


}
