package com.oushangfeng.ounews.utils.ijkplayer.widget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.oushangfeng.ounews.utils.ijkplayer.utils.MeasureHelper;

/**
 * Created by Oubowu on 2016/8/25 0025 17:41.
 */
@Deprecated
public class DropView extends View {

    private Paint mPaint;
    private Path mPath;
    private RectF mRectF;
    private Paint.FontMetrics mPaintFontMetrics;
    private float mStrokeWidth;
    private String mText = "";
    private int mColor;


    public DropView(Context context) {
        this(context, null, 0);
    }


    public DropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public DropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DropView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.parseColor("#00BCD4"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mStrokeWidth = mPaint.getStrokeWidth() / 2;

        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mPaintFontMetrics = mPaint.getFontMetrics();

        mPath = new Path();
        mRectF = new RectF();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int w = MeasureHelper.measureSize(widthMeasureSpec,
            MeasureHelper.dip2px(getContext(), 25));
        final int h = MeasureHelper.measureSize(heightMeasureSpec,
            MeasureHelper.dip2px(getContext(), 25 * 1.5f));
        setMeasuredDimension(w, h);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        final int h = getHeight();

        mPaint.setColor(mColor);

        mPath.reset();
        mPath.moveTo(mStrokeWidth, mStrokeWidth);
        mRectF.set(mStrokeWidth, mStrokeWidth, w - mStrokeWidth, w - mStrokeWidth);
        mPath.addArc(mRectF, 0, 360);
        canvas.drawPath(mPath, mPaint);

        mPath.reset();
        mPath.moveTo((float) (w / 2 + mStrokeWidth - w / Math.sqrt(8)),
            (float) (w / 2 + w / Math.sqrt(8)));
        mPath.quadTo(w / 2, w * 1.25f, w / 2, w * 1.5f);
        mPath.quadTo(w / 2, w * 1.25f, (float) (w / 2 - mStrokeWidth + w / Math.sqrt(8)),
            (float) (w / 2 + w / Math.sqrt(8)));
        mPath.lineTo((float) (w / 2 + mStrokeWidth - w / Math.sqrt(8)),
            (float) (w / 2 + w / Math.sqrt(8)));
        canvas.drawPath(mPath, mPaint);

        mPaint.setTextSize(w / 3.0f);
        mPaint.setColor(Color.WHITE);
        final float measureText = mPaint.measureText(mText);
        canvas.drawText(mText, (w - measureText) / 2,
            w * 1.25f / 2 + (-mPaintFontMetrics.ascent + mPaintFontMetrics.descent) / 2 -
                mPaintFontMetrics.descent, mPaint);

    }


    public void setText(String s) {
        mText = s;
        invalidate();
    }


    public void setColor(int color) {
        mColor = color;
    }

}
