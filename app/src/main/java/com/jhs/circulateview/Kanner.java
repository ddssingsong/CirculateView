package com.jhs.circulateview;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class Kanner extends FrameLayout {
    private int count;
    //private ImageLoader mImageLoader;
    private List<ImageView> imageViews;
    private Context context;
    private ViewPager vp;
    private boolean isAutoPlay;
    private int currentItem;
    private LinearLayout ll_dot;
    private List<ImageView> iv_dots;
    private Handler handler = new Handler();

    private long delaytime;

    public Kanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        //  initImageLoader(context);//这个放到Application中去
        initData();
    }

    public Kanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Kanner(Context context) {
        this(context, null);
    }

    private void initData() {
        imageViews = new ArrayList<ImageView>();
        iv_dots = new ArrayList<ImageView>();
        delaytime = 5000;
    }

    /**
     * 设置一个ImagesUrl的数组
     *
     * @param imagesUrl
     */
    public void setImagesUrl(String[] imagesUrl) {
        initLayout();
        initImgFromNet(imagesUrl);
        showTime();
    }

    /**
     * 设置一个iamgeRes的数组
     *
     * @param imagesRes
     */
    public void setImagesRes(int[] imagesRes) {
        initLayout();
        initImgFromRes(imagesRes);
        showTime();
    }

    /**
     * 设置轮播切换间隔，默认为5000（5秒）
     * @param time
     */
    public void setDelaytime(long time) {
        this.delaytime = time;
    }


    /**
     * 开始滑动
     */
    public void start(){
        startPlay();
    }

    /**
     * 停止滑动
     */
    public void stop(){
        isAutoPlay = false;
        handler.removeCallbacks(task);
    }


    private void initLayout() {
        imageViews.clear();
        View view = LayoutInflater.from(context).inflate(
                R.layout.kanner_layout, this, true);
        vp = (ViewPager) view.findViewById(R.id.vp);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        ll_dot.removeAllViews();
    }

    private void initImgFromRes(int[] imagesRes) {
        count = imagesRes.length;
        for (int i = 0; i < count; i++) {
            ImageView iv_dot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            iv_dot.setImageResource(R.drawable.shape_dot_blur);
            ll_dot.addView(iv_dot, params);
            iv_dots.add(iv_dot);
        }
        iv_dots.get(0).setImageResource(R.drawable.shape_dot_focus);

        for (int i = 0; i <= count + 1; i++) {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ScaleType.FIT_XY);
            iv.setBackgroundResource(R.drawable.loading);
            if (i == 0) {
                iv.setImageResource(imagesRes[count - 1]);
            } else if (i == count + 1) {
                iv.setImageResource(imagesRes[0]);
            } else {
                iv.setImageResource(imagesRes[i - 1]);
            }
            imageViews.add(iv);
        }
    }

    private void initImgFromNet(String[] imagesUrl) {
        count = imagesUrl.length;
        for (int i = 0; i < count; i++) {
            ImageView iv_dot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            iv_dot.setImageResource(R.drawable.shape_dot_blur);
            ll_dot.addView(iv_dot, params);
            iv_dots.add(iv_dot);
        }
        iv_dots.get(0).setImageResource(R.drawable.shape_dot_focus);

        for (int i = 0; i < count + 2; i++) {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ScaleType.FIT_XY);
            iv.setBackgroundResource(R.drawable.loading);
            if (i == 0) {
                ImageLoader.getInstance().displayImage(imagesUrl[count - 1], iv);
            } else if (i == count + 1) {
                ImageLoader.getInstance().displayImage(imagesUrl[0], iv);
            } else {
                ImageLoader.getInstance().displayImage(imagesUrl[i - 1], iv);
            }
            imageViews.add(iv);
        }
    }

    private void showTime() {
        vp.setAdapter(new KannerPagerAdapter());
        vp.setFocusable(true);
        vp.setCurrentItem(1);
        currentItem = 1;
        vp.addOnPageChangeListener(new MyOnPageChangeListener());
        startPlay();
    }

    private void startPlay() {
        isAutoPlay = true;
        handler.postDelayed(task, delaytime);
    }

    //配置Universal-image-loader
//    public void initImageLoader(Context context) {
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context).threadPriority(Thread.NORM_PRIORITY - 2)
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs().build();
//        ImageLoader.getInstance().init(config);
//        mImageLoader = ImageLoader.getInstance();
//    }

    private final Runnable task = new Runnable() {

        @Override
        public void run() {
            if (isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
                if (currentItem == 1) {
                    vp.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    vp.setCurrentItem(currentItem);
                    handler.postDelayed(task, delaytime);
                }
            } else {
                handler.postDelayed(task, delaytime);
            }
        }
    };

    class KannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = imageViews.get(position);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    kannerItemOnClickLister.OnKannerItemClickListener(position);
                }
            });
            container.addView(imageView);
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

    }

    class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case ViewPager.SCROLL_STATE_DRAGGING: //被拖动的时候进行的方法
                    isAutoPlay = false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING://松开手ViewPager恢复的过程
                    isAutoPlay = false;
                    break;
                case ViewPager.SCROLL_STATE_IDLE://Viewpager处于空闲状态
                    if (vp.getCurrentItem() == 0) {
                        vp.setCurrentItem(count, false);
                    } else if (vp.getCurrentItem() == count + 1) {
                        vp.setCurrentItem(1, false);
                    }
                    currentItem = vp.getCurrentItem();
                    isAutoPlay = true;
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < iv_dots.size(); i++) {
                if (i == arg0 - 1) {
                    iv_dots.get(i).setImageResource(R.drawable.shape_dot_focus);
                } else {
                    iv_dots.get(i).setImageResource(R.drawable.shape_dot_blur);
                }
            }
        }

    }


    private KannerItemOnClickLister kannerItemOnClickLister;

    public void setKannerItemOnClickLister(KannerItemOnClickLister kannerItemOnClickLister) {
        this.kannerItemOnClickLister = kannerItemOnClickLister;
    }

    public interface KannerItemOnClickLister {
        void OnKannerItemClickListener(int position);
    }

}
