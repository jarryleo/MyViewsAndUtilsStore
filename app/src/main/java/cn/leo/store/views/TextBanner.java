package cn.leo.store.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

/**
 * Created by Leo on 2017/11/23.
 */

public class TextBanner extends TextSwitcher implements ViewSwitcher.ViewFactory, Runnable {
    private List<String> mTexts;
    private int interval = 3000;
    private float textSize = 16f;
    private int textColor = Color.BLACK;
    private Handler mHandler = new Handler();
    private int current; //当前文本序列

    public TextBanner(Context context) {
        this(context, null);
    }

    public TextBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setFactory(this);
        setInAnimation(getInAnim());
        setOutAnimation(getOutAnim());
    }

    @Override
    public void run() {
        mHandler.removeCallbacks(this);
        setText(mTexts.get(current));
        current = current < mTexts.size() - 1 ? ++current : 0;
        if (mTexts.size() > 1)
            mHandler.postDelayed(this, interval);
    }

    public void setTexts(List<String> texts) {
        if (texts == null || texts.size() == 0) return;
        mTexts = texts;
        current = 0;
        run();
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        ((TextView) getCurrentView()).setTextSize(textSize);
        ((TextView) getNextView()).setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        ((TextView) getCurrentView()).setTextColor(textColor);
        ((TextView) getNextView()).setTextColor(textColor);
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(getContext());
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        textView.setSingleLine();
        return textView;
    }

    private Animation getInAnim() {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    private Animation getOutAnim() {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f);
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

}
