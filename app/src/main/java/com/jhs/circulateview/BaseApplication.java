package com.jhs.circulateview;

import android.app.Application;

import com.jhs.circulateview.utils.ImageLoaderUtil;

/**
 * Created by dds on 2016/10/10.
 *
 * @TODO
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderUtil.getInstance().init(this);
    }
}
