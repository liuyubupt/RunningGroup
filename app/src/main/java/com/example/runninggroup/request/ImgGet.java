package com.example.runninggroup.request;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.Log;

import com.example.runninggroup.util.ConstantUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgGet {
//    private static String urls = "http://39.97.66.19:8080/user/getImg";
    private static String urls = ConstantUtil.URL + ConstantUtil.GET_IMG;
    public static Drawable getImg(String imgName) {
        try {
            String params = "imgName="+imgName;
            // 1. 获取访问地址URL
            URL url = new URL(urls);
            // 2. 创建HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            /* 3. 设置请求参数等 */
            // 请求方式
            connection.setRequestMethod("POST");
            // 超时时间
            connection.setConnectTimeout(100);
            // 设置是否输出
            connection.setDoOutput(true);
            // 设置是否读入
            connection.setDoInput(true);
            // 设置是否使用缓存
            connection.setUseCaches(true);
            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
            connection.setInstanceFollowRedirects(true);
            // 设置使用标准编码格式编码参数的名-值对
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 连接(有问题)
//            connection.connect();
            /* 4. 处理输入输出 */
            // 写入参数到请求中
            OutputStream out = connection.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            out.close();
            // 从连接中读取响应信息
            InputStream inputStream = connection.getInputStream();
            String msg = "";
            int code = connection.getResponseCode();
            if (code == 200) {
               return loadImageFromNetwork(inputStream);

            }
            // 5. 断开连接
            connection.disconnect();

            // 处理结果
            System.out.println(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    private static Drawable loadImageFromNetwork(InputStream inputStream) {

        Drawable drawable = Drawable.createFromStream(inputStream, null);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;

    }
}
