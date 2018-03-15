package com.shejimoshi.www.androidyuanmashejimoshejiexiyushizhan.A_MianXiangDuiXiangLiuDaYuanZe.A_DanYiZhiZe;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Administrator on 2017/12/24 0024.
 * 图片缓存
 */

public class ImageCache {
    LruCache<String,Bitmap> mImageChche;

    public ImageCache(){
        initImageCache();
    }

    private void initImageCache() {
        final int maxMemory= (int) ((Runtime.getRuntime().maxMemory())/1024);
        final int cacheSize=maxMemory/8;
        mImageChche=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount()*value.getHeight();
            }
        };

    }

    //存
    public void put(String url,Bitmap bitmap){
        mImageChche.put(url,bitmap);
    }

    //取
    public Bitmap get(String url){
        return mImageChche.get(url);
    }
}
