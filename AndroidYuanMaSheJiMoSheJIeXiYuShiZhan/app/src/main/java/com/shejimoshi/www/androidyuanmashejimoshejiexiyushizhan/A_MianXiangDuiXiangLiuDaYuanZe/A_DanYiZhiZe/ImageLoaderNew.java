package com.shejimoshi.www.androidyuanmashejimoshejiexiyushizhan.A_MianXiangDuiXiangLiuDaYuanZe.A_DanYiZhiZe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wyj on 2017/12/24 0024.
 * 修改ImageLoader类，让她满足单一性原则；
 * 将ImageLoader类中负责处理图片缓存的逻辑分离出来封装成ImageCache类：只负责处理图片缓存逻辑
 * ImageLoaderNew只负责图片加载逻辑；
 * 单一原则：
 *        1：每个人都有自己的想法：根据经验，业务逻辑等而定；
 *        2:单一：尽量将不同逻辑的代码分类；
 */

public class ImageLoaderNew {
    //图片缓存
    ImageCache mImageCache=new ImageCache();
    //线程池
    ExecutorService mE= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //加载图片
    public void dispalyImage(final String url, final ImageView imageView){
        final Bitmap bitmap=mImageCache.get(url);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
           return;
        }
        imageView.setTag(url);
        mE.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1=downloadImage(url);
                if (bitmap==null){
                    return;
                }
                if (imageView.getTag().equals(url)){
                    imageView.setImageBitmap(bitmap);
                }
                mImageCache.put(url,bitmap);
            }
        });

    }

    public Bitmap downloadImage(String imageUrl){
        Bitmap bitmap=null;
        try {
            URL url=new URL(imageUrl);
            final HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            bitmap= BitmapFactory.decodeStream(conn.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}

