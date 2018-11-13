package cn.leo.store.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;


/**
 * Created by Leo on 2017/5/12.
 * 泡泡数字提示控件
 */

public class BubbleNum extends View {
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private int mNum;
    /**
     * 只显示泡泡不显示数字
     */
    private boolean justBubble = false;
    private int mTextHeight;
    private int mPointSize = 5;

    public BubbleNum(Context context) {
        this(context, null);
    }

    public BubbleNum(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleNum(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始画笔
     */
    private void initPaint() {
        //圆的画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.argb(255, 255, 0, 0));
        //文字画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.argb(255, 255, 255, 255));
        mTextPaint.setTextSize(20);
        //绘制文字中间对齐
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
        //int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //测量高
        //包裹内容的时候
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) v, MeasureSpec.EXACTLY);
            height = (int) (v + 0.5);
        }

        //根据高设置文字大小
        String text = String.valueOf(mNum);
        float textSize = height * 0.8f;
        mTextPaint.setTextSize(textSize);
        //根据文字大小测量宽
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), textBounds);
        mTextHeight = textBounds.bottom - textBounds.top;
        //得到本控件宽
        int width = textBounds.right - textBounds.left + height / 2;
        if (width < height) width = height;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);


        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获得控件自身宽高
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        //获得宽高最小值
        int r = Math.min(measuredHeight, measuredWidth);

        //画数字
        if (!justBubble) {
            //如果只显示泡泡就不画数字
            String text = String.valueOf(mNum);
            if (mNum > 99) {
                text = "99+";
            }
            canvas.drawRoundRect(new RectF(0, 0, measuredWidth, measuredHeight), r / 2, r / 2, mCirclePaint);
            canvas.drawText(text, measuredWidth / 2, (mTextHeight + r) / 2 - 1, mTextPaint);
        } else {
            //画小圆点
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPointSize, displayMetrics);
            canvas.drawCircle(r / 2, r / 2, v, mCirclePaint);
        }
    }

    /**
     * 设置显示的数字
     *
     * @param num
     */
    public void setNum(int num) {
        if (num < 1) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        mNum = num;
        requestLayout();
    }

    /**
     * 设置是否只显示泡泡
     *
     * @param justBubble
     */
    public void setJustBubble(boolean justBubble) {
        this.justBubble = justBubble;
        invalidate();
    }

    /**
     * 小圆点大小
     *
     * @param pointSize
     */
    public void setPointSize(int pointSize) {
        mPointSize = pointSize;
        invalidate();
    }

    /**
     * 设置圆圈颜色
     *
     * @param color
     */
    public void setBackColor(int color) {
        mCirclePaint.setColor(color);
        invalidate();
    }

    /**
     * 设置泡泡文字颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

}
