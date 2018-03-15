package com.shejimoshi.www.androidyuanmashejimoshejiexiyushizhan.A_MianXiangDuiXiangLiuDaYuanZe.A_DanYiZhiZe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wyj on 2017/12/24 0024.
 * 没有注意单一职责原则的写法
 */

public class ImageLoader {
    //图片缓存
    LruCache<String,Bitmap> mImageChahe;
    //线程池,线程数量为CPU的数量
    ExecutorService mExecutorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ImageLoader(){
        initImageCache();
    }

    private void initImageCache() {
        //计算可用最大内存，以kb为单位
        final int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024);
        //取四分之一作为缓存使用内存
        final int cacheSize=maxMemory/4;
        mImageChahe=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024; //每个item的大小。/1024表示返回用kb为单位
            }
        };
    }

    //用ImageView展示图片并缓存
    public void cacheBitmap(final String url, final ImageView imageView){
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap=downloadImage(url);
                if (bitmap==null){
                    return;
                }
                if (imageView.getTag().equals(bitmap)){
                    imageView.setImageBitmap(bitmap);
                }
                mImageChahe.put(url,bitmap);
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
