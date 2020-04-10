package com.liqi.roulettedemo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.liqi.roulettedemo.R;

import androidx.annotation.Nullable;

/**
 * Created by lqf on 3/31/20
 *
 * 自定义控件里面的尺寸距离，一定要用比例，如果写了固定的dp,那么控件在当前尺寸下可用，换容器尺寸可能就不不成比例了。
 */
public class CoreControlView extends View {

    private static final String TAG = CoreControlView.class.getSimpleName();


    private int mWidth;       // 控件宽
    private int mHeight;      // 控件高
    private int mDialRadius;    //刻度盘半径

    private int mRadius; //view半径;

    private int mArcRadius;  //圆弧半径
    private Paint dialPaint; //刻度盘的画笔
    private Paint bitmapPaint; //背景绘图
    private Paint ballPaint; //指示器的绘笔;

    private Context mContext;
    private int scaleHeight = Density.dp2px(getContext(), 7f);

    private int maxValue = 100; //默认最大;
    private int minValue = 0; //默认最小数值

    private int currentValue  = 0; //当前的数值

    private int sumScales = 60; //总共的刻度线为61格，60个间距;

    private float unitDegree = 4.5f;//每刻度旋转的角度;
    private int sumDegree = (int) (60 * 4.5f); //总共的旋转角度为270;
    private int resId;//大背景资源;
    private int resIndicator; //指示器文件;

    private int resContent; // 下面的指示图内容;


    // 当前的角度
    private float currentAngle;
    // 当前按钮旋转的角度
    private float rotateAngle;

    private Rect mRectSrc, mRectDst; //bitmap被绘制的区域，bitmap要绘制到view中的区域;


    public CoreControlView(Context context) {
        this(context, null);
    }

    public CoreControlView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoreControlView(Context context, @Nullable AttributeSet attrs,
                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context);
    }

    private void init(Context context) {

        dialPaint = new Paint();
        dialPaint.setAntiAlias(true);

        dialPaint.setStrokeWidth(Density.dp2px(context,1.0f));
        dialPaint.setStyle(Paint.Style.STROKE);//描边

        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);

        ballPaint = new Paint();
        ballPaint.setAntiAlias(true);
        ballPaint.setColor(getResources().getColor(R.color.theme_color));

        mRectSrc = new Rect();
        mRectDst = new Rect();

    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * 设置当前的数值，同时你要计算旋转的角度;
     * @param value
     */
    public void setCurrentValue(int value) {
        this.currentValue = value;
        rotateAngle = (value - minValue) * getUnit() * 4.5f;
        invalidate();
    }

    public int getCurrentValue() {
        return currentValue;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        mWidth = w;
        mHeight = h;
        //半径取短边的长度，并且排除掉padding数;
        mRadius = Math.min(mWidth, mHeight)/2 - getPaddingTop();
        mDialRadius = (int) (mRadius - 0.16 * mRadius) ;
        scaleHeight = (int) (0.09 * mRadius);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackDrawable(canvas);
        drawDialScale(canvas);
        drawIndicator(canvas);
        drawContent(canvas);
    }


    /**
     * 绘制跟随手势的指示点;
     */
    private void drawIndicator(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth/2, mHeight/2);
        canvas.rotate(-(90 + 45 + 6) );//逆时针先旋转到第三象限的45度 + 圆弧的角度ｕ调整;
        canvas.rotate(rotateAngle);//旋转角度
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_core_indicator);

        mRectSrc.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mRectDst.set((int) (- 0.05f * mRadius), -(int)(mRadius - 0.32 * mRadius), (int) (0.05f * mRadius),
                -(int)(mRadius - 0.32 * mRadius - 0.1f * mRadius));
        canvas.drawBitmap(bitmap, mRectSrc, mRectDst, ballPaint);
        canvas.restore();
    }

    /**
     * 绘制背景资源; 适配宽高，不同的尺寸;
     */
    private void drawBackDrawable(Canvas canvas) {
        Bitmap bitmap;
        if(resId != 0) {
            bitmap = BitmapFactory.decodeResource(getResources(), resId);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_core_view);
        }
        //平铺绘制背景资源;
        mRectSrc.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        //保证控件不是正放行能正确地显示;
        mRectDst.set(mWidth/2 - mRadius, mHeight/2 - mRadius,  mWidth/2 + mRadius , mHeight/2 + mRadius);
        canvas.drawBitmap(bitmap, mRectSrc, mRectDst, bitmapPaint);
    }
    /**
     * 绘制轮盘的刻度线
     */
    private void drawDialScale(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth/2, mHeight/2);
        canvas.rotate(-(90 + 45));//逆时针先旋转到第三的45度
        dialPaint.setColor(getResources().getColor(R.color.default_dial_bg));
        for(int i = 0; i <= sumScales; i++) {
            canvas.drawLine(0, - mDialRadius, 0, -mDialRadius + scaleHeight, dialPaint);
            if(i != sumScales -1) {//最后一格不旋转
                canvas.rotate(unitDegree);
            }
        }
        canvas.rotate(90);//回到最初点;
        dialPaint.setColor(getResources().getColor(R.color.theme_color));

        int scales = (int) ((getCurrentValue() - minValue)* getUnit());//需要绘制的刻度线;
        if(scales >= sumScales){
            scales = sumScales;
        }
        for (int i = 0; i < scales; i++) {
            canvas.drawLine(0, -mDialRadius, 0, - mDialRadius + scaleHeight, dialPaint);
            canvas.rotate(unitDegree);
        }
        canvas.restore();
    }

    private void drawContent(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth/2, mHeight/2);
        Bitmap bitmap;
        if(resContent == 0) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_time_core);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), resContent);
        }
        mRectSrc.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mRectDst.set((int)(-0.15f * mRadius), (int) (0.3*mRadius), (int)( 0.15f * mRadius), (int) (0.3*mRadius
                + 0.30f * mRadius));
        canvas.drawBitmap(bitmap, mRectSrc, mRectDst, bitmapPaint);

        canvas.restore();
    }


    /**
     *  每一数值有多少刻度, 总的刻度距／总的数值
     */
    private float getUnit() {
        return sumScales/(1.0f*(maxValue - minValue));
    }

    /**
     * 每一角度代表多少数值;
     */
    private float getUnitDegree() {
        return (maxValue - minValue)*1.0f/sumDegree;
    }

    public void setResId(int resId) {
        this.resId = resId;
        invalidate();
    }

    public void setResContent(int resContent) {
        this.resContent = resContent;
        invalidate();
    }

    public void setResIndicator(int resIndicator) {
        this.resIndicator = resIndicator;
        invalidate();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                currentAngle = calcAngle(downX, downY);
                if(listener != null) {
                    listener.onDown();
                }
                break;

            case MotionEvent.ACTION_MOVE:

                float targetX = event.getX();
                float targetY = event.getY();
                float angle = calcAngle(targetX, targetY);
                float angleIncreased = currentAngle - angle;
                //防止越界;因为在越过0度时候会转向到360;这里设定100,也可以是其他较大的数值;
                if(angleIncreased > 100) {
                    angleIncreased = angleIncreased - 360;
                } else if(angleIncreased < -100) {
                    angleIncreased = angleIncreased + 360;
                }
                increaseAngle(angleIncreased);
                currentAngle = angle;
                if(listener != null) {
                    listener.onChange(currentValue);
                }
                invalidate();
                break;


            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(listener != null) {
                    listener.onUp();
                }
                //修正角度，抬起的时候让滑动按钮旋转角度和刻度线保持一致：
                rotateAngle = (currentValue - minValue) * getUnit() * unitDegree;
                invalidate();
                break;

        }
        return true;

    }

    /**
     * 累计角度的变化,并计算对应的数值;
     * @param angle 变化的角度
     */
    private void increaseAngle(float angle) {
        rotateAngle += angle;
        if (rotateAngle < 0) {
            rotateAngle = 0;
        } else if (rotateAngle >= sumDegree) {
            rotateAngle = sumDegree;
        }
        currentValue = (int) (minValue + rotateAngle * getUnitDegree() + 0.5f);
    }

    public interface OnControlChangeListener {
        void onChange(int currentValue);

        void onDown();

        void onUp();
    }

    private OnControlChangeListener listener;

    public void setOnControlChangeListener(OnControlChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 计算触摸点和横原点之间的连线和坐标轴之间的角度;
     */
    private float calcAngle(float targetX, float targetY) {

        float x = targetX - mWidth / 2f;
        float y = targetY - mHeight / 2f;
        double radian;
        if(x != 0) {
            float tan = Math.abs(y/x);
            if(x > 0) {
                if(y >= 0) {
                    radian = 2 * Math.PI - Math.atan(tan);
                } else {
                    radian = Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    radian = Math.PI + Math.atan(tan);
                } else {
                    radian = Math.PI - Math.atan(tan);
                }
            }
        } else {
            if (y > 0) {
                radian = -Math.PI / 2;
            } else {
                radian = Math.PI / 2;
            }
        }
        return (float) (radian * 180 / Math.PI);
    }

}
