package com.example.runninggroup.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.example.runninggroup.cache.Cache;
import com.example.runninggroup.viewAndController.MainInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class BitmapUtil {

    private static String PHOTO_FILE_NAME = "PMSManagerPhoto";

    /**
     * 获取图片的旋转角度
     *
     * @param filePath
     * @return
     */
    public static int getRotateAngle(String filePath) {
        int rotate_angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate_angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate_angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate_angle = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate_angle;
    }

    /**
     * 旋转图片角度
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap setRotateAngle(int angle, Bitmap bitmap) {

        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(angle);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;

    }

    //转换为圆形状的bitmap
    public static Bitmap createCircleImage(Bitmap source) {
        int length = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(length / 2, length / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }



    /**
     * 图片压缩-质量压缩
     *
     * @param filePath 源图片路径
     * @return 压缩后的路径
     */

    public static String compressImage(String filePath) {

        //原文件
        File oldFile = new File(filePath);


        //压缩文件路径 照片路径/
        String targetPath = oldFile.getPath();
        int quality = 50;//压缩比例0-100
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片

        int degree = getRotateAngle(filePath);//获取相片拍摄角度

        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = setRotateAngle(degree,bm);
        }
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
//                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            } else {
//                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return filePath;
        }
        return outputFile.getPath();
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /** 创建一个bitmap的副本
     * @param bitmap
     * @return
     */
    public static Bitmap copyBitmap(Bitmap bitmap){
        Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), bitmap.getConfig());// 相当于创建了一张白纸
        Paint paint = new Paint();// 创建一个画笔
        Canvas canvas = new Canvas(copyBitmap); // 创建一个画布，把白纸铺到画布上
        canvas.drawBitmap(bitmap, new Matrix(), paint); // 执行完copyBitmap里面才有内容
        return copyBitmap;
    }

    //保存文件到指定路径
    public static String saveMyBitmap(Context context, Bitmap bitmap,String fileName) {
        FileOutputStream fos = null;
        String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
        File appDir = new File(sdCardDir, "RunningGroup");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File f = new File(appDir, fileName+".jpg");
        try {
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fos != null){
                    fos.flush();
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        context.sendBroadcast(intent);
        String path = sdCardDir+"RunningGroup/"+fileName+".jpg";

        return path;
    }

    public static Bitmap copyFile (String path) {

// 设置参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width= options.outWidth;
//        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
//        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        int min = Math.min(height, width);
        int inSampleSize = min / 500;
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        Bitmap bm = BitmapFactory.decodeFile(path, options); // 解码文件
        return bm;
    }



    public static File copyFile(Activity activity, String path, String imgName) {
        FileOutputStream fs = null;
        InputStream inStream = null;
        try {
            String newPath = activity.getExternalCacheDir().toString() + "/GuanGuan";
            File file = new File(newPath);
            System.out.println(file.getPath());
            if (! file.exists()) { //文件不存在时
                file.mkdir();
            }
            file = new File(newPath, imgName + ".jpg");
            System.out.println(file.length());

            inStream = new FileInputStream(path); //读入原文件
            fs = new FileOutputStream(file);


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
            BitmapFactory.decodeStream(inStream, null, options);
            int height = options.outHeight;
            int width= options.outWidth;
//            int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
//            int minLen = Math.min(height, width); // 原图的最小边长
//            if(minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
//                float ratio = (float)minLen / 100.0f; // 计算像素压缩比例
//                inSampleSize = (int)ratio;
//            }
//            options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
            if (Math.min(height, width) > 480) options.inSampleSize = calculateInSampleSize(options, width / 8, height / 8);
            options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
            options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
            Bitmap bm = BitmapFactory.decodeFile(path, options); // 解码文件

           bm.compress(Bitmap.CompressFormat.JPEG, 100, fs);

            return file;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;

        }finally {
            try {
                if (fs != null) fs.close();
                if (inStream != null)  inStream.close();
            }catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
    public static File copyFile(Activity activity, Bitmap bm, String imgName) {
        FileOutputStream fs = null;
        try {
            String newPath = activity.getExternalCacheDir().toString() + "/GuanGuan";
            File file = new File(newPath);
            System.out.println(file.getPath());
            if (! file.exists()) { //文件不存在时
                file.mkdir();
            }
            file = new File(newPath, imgName + ".jpg");
            System.out.println(file.length());
            fs = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fs);
            return file;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;

        }finally {
            try {
                if (fs != null) fs.close();
            }catch (IOException e) {
                e.printStackTrace();
            }

        }


    }









}

