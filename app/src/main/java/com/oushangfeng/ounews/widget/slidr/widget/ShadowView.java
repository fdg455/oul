package com.oushangfeng.ounews.widget.slidr.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Oubowu on 2016/9/20 1:28.
 */
public class ShadowView extends View {

    private View mCacheView;

    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private RectF mRectF;
    private float mOffset;
    private float mPercent;
    private int[] colors;

    public ShadowView(Context context, View cacheView) {
        super(context);
        mCacheView = cacheView;
        mPaint = new Paint();
    }

    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCacheView != null) {
            mCacheView.draw(canvas);
            // 绘制渐变阴影
            if (mLinearGradient == null) {
                mRectF = new RectF();
                colors = new int[]{Color.parseColor("#0A000000"), Color.parseColor("#66000000"), Color.parseColor("#aa000000")};
                // 我设置着色器开始的位置为（getWidth() * 29 / 30，0），结束位置为（getWidth(), 0）表示我的着色器要给整个View在水平方向上渲染
                mLinearGradient = new LinearGradient(getWidth() * 29 / 30, 0, getWidth(), 0, colors, null, Shader.TileMode.REPEAT);
                mPaint.setShader(mLinearGradient);
                mRectF.set(getWidth() * 29 / 30, 0, getWidth(), getHeight());
            }
            canvas.save();
            mPaint.setAlpha((int) (mPercent * 255));
            canvas.translate(-mOffset, 0);
            canvas.clipRect(mRectF);
            canvas.drawRect(mRectF, mPaint);
            canvas.restore();
        }
    }

    public void setShadowOffset(float offset, float percent) {
        mOffset = offset;
        mPercent = percent;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCacheView = null;
    }
}
