package cn.leo.store.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Leo on 2017/7/12.
 */

public class BannnerView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private List<String> mImageList;
    private innerViewPager mViewPager;
    private ImageAdapter mAdapter;
    private boolean mAutoScroll = true;
    private ImageLoader mImageLoader;
    private Handler mHandler;
    private int mScrollInterval = 5000; //自动轮播间隔5秒
    private int mCurrentItem;
    private OnPageClickListener mOnPageClickListener;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private float ratio; //宽高比

    public BannnerView(Context context) {
        this(context, null);
    }

    public BannnerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 必须实现图片加载功能
     *
     * @param imageLoader
     * @return
     */
    public BannnerView initImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
        return this;
    }

    public void setImageList(List<String> imageList) {
        if (imageList == null) return;
        mImageList = imageList;
        if (mViewPager == null) {
            mViewPager = new innerViewPager(getContext());
            this.addView(mViewPager, 0);
            mViewPager.setLayoutParams(new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mAdapter = new ImageAdapter();
            mViewPager.setAdapter(mAdapter);
            if (imageList.size() > 1) {
                mCurrentItem = mImageList.size() * 50000;
                setCurrentItem(mCurrentItem);
            }
            mViewPager.addOnPageChangeListener(this);
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mAdapter.notifyDataSetChanged();
            setCurrentItem(0);
        }
        if (mAutoScroll && mImageList.size() > 1) {
            autoScroll();
        } else {
            closeAutoScroll();
        }
    }

    public void setCurrentItem(int currentItem) {
        if (mViewPager == null) return;
        if (currentItem < 50000 && mImageList.size() > 1) {
            currentItem += 50000;
        }
        mCurrentItem = currentItem;
        mViewPager.setCurrentItem(mCurrentItem);
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (ratio == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        //DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        //int widthPixels = displayMetrics.widthPixels;
        int widthPixels = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpec = MeasureSpec.makeMeasureSpec(widthPixels, MeasureSpec.EXACTLY);
        int height = (int) (widthPixels * ratio + 0.5);
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        mOnPageClickListener = onPageClickListener;
    }

    /**
     * 设置自动轮播间隔时长
     *
     * @param interval 单位 ms
     */
    public void setScrollInterval(int interval) {
        if (interval > 0)
            mScrollInterval = interval;
    }

    /**
     * 开启自动轮播
     */
    public void openAutoScroll() {
        if (mAutoScroll) return;
        mAutoScroll = true;
        autoScroll();
    }

    /**
     * 关闭自动轮播
     */
    public void closeAutoScroll() {
        mAutoScroll = false;
        mHandler.removeCallbacks(autoScrollMission);
    }

    private void autoScroll() {
        mHandler.removeCallbacks(autoScrollMission);
        mHandler.postDelayed(autoScrollMission, mScrollInterval);
    }

    private Runnable autoScrollMission = new Runnable() {

        @Override
        public void run() {
            mCurrentItem++;
            if (mCurrentItem > mImageList.size() * 99999L) {
                mCurrentItem = mImageList.size() * 50000 + 1;
            }
            mViewPager.setCurrentItem(mCurrentItem);
            autoScroll();
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentItem = position;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }


    private class ImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mImageList != null) {
                if (mImageList.size() > 1) {
                    return mImageList.size() * 100000;
                } else {
                    return mImageList.size();

                }
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int i = position % mImageList.size();
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            picLoader(imageView, mImageList.get(i));
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void picLoader(ImageView imageView, String imagePath) {
            if (mImageLoader != null) {
                mImageLoader.loadImage(imageView, imagePath);
            }
        }

    }

    private class innerViewPager extends ViewPager {
        private GestureDetector mGestureDetector =
                new GestureDetector(getContext(),
                        new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public boolean onSingleTapUp(MotionEvent e) {
                                if (mOnPageClickListener == null) return false;
                                int index = mCurrentItem % mImageList.size();
                                mOnPageClickListener.onClick(index, mImageList.get(index));
                                return true;
                            }
                        });

        public innerViewPager(Context context) {
            this(context, null);
        }

        public innerViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if (!mAutoScroll) return super.onTouchEvent(ev);
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    //暂时关闭自动滑动
                    mHandler.removeCallbacks(autoScrollMission);
                    break;
                case MotionEvent.ACTION_UP:
                    //开启自动滑动
                    autoScroll();
                    break;
            }
            mGestureDetector.onTouchEvent(ev);
            return super.onTouchEvent(ev);
        }
    }


    /**
     * 图片加载器用户自己实现
     */
    public interface ImageLoader {
        void loadImage(ImageView imageView, String imagePath);
    }

    public interface OnPageClickListener {
        void onClick(int index, String imagePath);
    }
}
