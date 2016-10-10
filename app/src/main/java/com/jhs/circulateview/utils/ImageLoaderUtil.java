package com.jhs.circulateview.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by dds on 2016/9/26.
 *
 * @TODO Universal-Image-Loader的封装
 * <p>
 * 1. 初始化方法和option的使用
 */
public class ImageLoaderUtil {
    private static ImageLoaderUtil imageLoaderUtil;
    public static DisplayImageOptions options;
    //圆形头像
    public static DisplayImageOptions roundOptions;

    static {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer()).build();

        roundOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .displayer(new CircleBitmapDisplayer()).build();
    }

    private ImageLoaderUtil() {
    }

    public static ImageLoaderUtil getInstance() {
        if (imageLoaderUtil == null) {
            synchronized (ImageLoaderUtil.class) {
                if (imageLoaderUtil == null) {
                    imageLoaderUtil = new ImageLoaderUtil();
                    return imageLoaderUtil;
                }
            }
        }
        return imageLoaderUtil;
    }

    /**
     * 初始化方法
     *
     * @param context
     */
    public void init(Context context) {
        //  ImageLoaderConfiguration.createDefault(this);
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);// 设置线程的优先级
        // config.threadPoolSize(3);//默认为3
        //config.denyCacheImageMultipleSizesInMemory();// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());//默认为HashCodeFileNameGenerator
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        //config.memoryCache(new LruMemoryCache(2 * 1024 * 1024));//可以通过自己的内存缓存实现
        //config.memoryCacheSize(2 * 1024 * 1024);  // 内存缓存的最大值
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 从SD卡获取图片
     *
     * @param uri
     * @param imageView
     */
    public void displayFromSDCard(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage("file://" + uri, imageView);
    }

    /**
     * 从assets获取图片
     *
     * @param uri
     * @param imageView
     */
    public void dispalyFromAssets(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage("assets://" + uri, imageView);
    }

    /**
     * 从内容提供者获取图片
     *
     * @param uri
     * @param imageView
     */
    public void displayFromContent(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage("content://" + uri, imageView);
    }

}
