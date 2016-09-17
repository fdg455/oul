package com.oushangfeng.ounews.utils.ijkplayer.widget.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.utils.ijkplayer.utils.MeasureHelper;

import java.text.DecimalFormat;

/**
 * Created by Oubowu on 2016/8/26 0026 12:24.
 */
public class ProgressView extends View {

    private Paint mPaint;
    private RectF mRectF;
    private float mStrokeWidth;
    private Path mPath;

    //    private DropView mDropView;
    private float mMaxProgress;

    private float mSaveProgress;
    private float mProgress;
    private float mLastX;

    private float mLastY;

    private float mTotalDistance;

    private ViewGroup.MarginLayoutParams mlp;
    private String mProgressText;

    private boolean mIsAnimate;

    private int mProgressbarColor;

    @Orientation
    private int mOrientation;

    private ValueAnimator mAnimator;
    private float mOffset;
    private float mWaveOffset;
    private int mAnimatorRepeatCount;
    private DecimalFormat mDecimalFormat;

    private boolean mActionDown;
    private boolean mActionMove;

    private int mFrameColor;
    private OnProgressChangeListener mOnProgressChangeListener;
    private boolean mWave;
    private int mSaveProgressbarColor;


    public ProgressView(Context context) {
        this(context, null, 0);
    }


    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    public float getMaxProgress() {
        return mMaxProgress;
    }


    public void setMaxProgress(float maxProgress) {
        mMaxProgress = maxProgress;
    }


    public float getProgress() {
        return mProgress;
    }

    public float getSaveProgress() {
        return mSaveProgress;
    }

    public void setSaveProgress(float saveProgress) {
        mSaveProgress = saveProgress;
    }


    public void setProgress(float progress) {
        mProgress = progress;
        if (!mWave) {
            post(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            });
        }
    }


    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {

            final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);

            mFrameColor = ta.getColor(R.styleable.ProgressView_frame_color, Color.parseColor("#80FFFFFF"));
            mProgressbarColor = ta.getColor(R.styleable.ProgressView_progress_bar_color, Color.parseColor("#FFBA31"));
            mSaveProgressbarColor = ta.getColor(R.styleable.ProgressView_save_progress_bar_color, Color.parseColor("#4DFFFFFF"));
            mOrientation = ta.getInt(R.styleable.ProgressView_progress_orientation, Orientation.ORIENTATION_HORIZONTAL);
            mProgress = ta.getFloat(R.styleable.ProgressView_origin_progress, 0);
            mMaxProgress = ta.getFloat(R.styleable.ProgressView_max_progress, 100);
            mWave = ta.getBoolean(R.styleable.ProgressView_wave, false);

            ta.recycle();

        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(mFrameColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(MeasureHelper.dip2px(context, 1.5f));
        mStrokeWidth = mPaint.getStrokeWidth() / 2;

        mRectF = new RectF();

        mPath = new Path();

        mDecimalFormat = new DecimalFormat("0.0");

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w;
        int h;

        if (mOrientation == Orientation.ORIENTATION_HORIZONTAL) {
            w = MeasureHelper.measureSize(widthMeasureSpec, MeasureHelper.getScreenSize(getContext()).x);
            h = MeasureHelper.measureSize(heightMeasureSpec, MeasureHelper.dip2px(getContext(), 20));
        } else {
            w = MeasureHelper.measureSize(widthMeasureSpec, MeasureHelper.dip2px(getContext(), 20));
            h = MeasureHelper.measureSize(heightMeasureSpec, MeasureHelper.getScreenSize(getContext()).x);
        }

        setMeasuredDimension(w, h);

        if (mlp == null && getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            mlp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        } else if (mlp == null) {
            mlp = new ViewGroup.MarginLayoutParams(0, 0);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(mFrameColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mRectF.set(mStrokeWidth, mStrokeWidth, getWidth() - mStrokeWidth, getHeight() - mStrokeWidth);
        canvas.drawRect(mRectF, mPaint);

        mPaint.setStyle(Paint.Style.FILL);

        if (mOrientation == Orientation.ORIENTATION_HORIZONTAL) {

            // 绘制缓存的进度
            mPaint.setColor(mSaveProgressbarColor);
            mRectF.set(mStrokeWidth * 4, mStrokeWidth * 4, (mSaveProgress / mMaxProgress) * mTotalDistance + mStrokeWidth * 4, getHeight() - mStrokeWidth * 4);
            canvas.drawRect(mRectF, mPaint);

            // 绘制限定绘制区域
            mPaint.setColor(mProgressbarColor);
            mTotalDistance = getWidth() - mStrokeWidth * 4 * 2;
            mRectF.set(mStrokeWidth * 4, mStrokeWidth * 4, mTotalDistance + mStrokeWidth * 4, getHeight() - mStrokeWidth * 4);
            canvas.clipRect(mRectF);

            mPath.reset();
            final float progressWidth = (mProgress / mMaxProgress) * mTotalDistance;
            final float progressHeight = getHeight() - mStrokeWidth * 4 * 2;
            final float quadPointX = progressHeight / 10.0f + mWaveOffset;
            mPath.moveTo(mStrokeWidth * 4, mStrokeWidth * 4 - progressHeight + mOffset);
            mPath.lineTo(progressWidth + mStrokeWidth * 4, mStrokeWidth * 4 - progressHeight + mOffset);
            if (!mWave || mProgress <= 0 || mProgress >= mMaxProgress || mActionDown || mActionMove) {
                mPath.lineTo(progressWidth + mStrokeWidth * 4, getHeight() - mStrokeWidth * 4 + mOffset);
            } else {
                mPath.quadTo(progressWidth + mStrokeWidth * 4 + quadPointX, progressHeight / 4 + mStrokeWidth * 4 - progressHeight + mOffset,
                        progressWidth + mStrokeWidth * 4, progressHeight / 2 + mStrokeWidth * 4 - progressHeight + mOffset);
                mPath.quadTo(progressWidth + mStrokeWidth * 4 - quadPointX, progressHeight * 3 / 4.0f + mStrokeWidth * 4 - progressHeight + mOffset,
                        progressWidth + mStrokeWidth * 4, progressHeight + mStrokeWidth * 4 - progressHeight + mOffset);
                mPath.quadTo(progressWidth + mStrokeWidth * 4 + quadPointX, progressHeight / 4 + mStrokeWidth * 4 + mOffset, progressWidth + mStrokeWidth * 4,
                        progressHeight / 2 + mStrokeWidth * 4 + mOffset);
                mPath.quadTo(progressWidth + mStrokeWidth * 4 - quadPointX, progressHeight * 3 / 4.0f + mStrokeWidth * 4 + mOffset, progressWidth + mStrokeWidth * 4,
                        progressHeight + mStrokeWidth * 4 + mOffset);
            }
            mPath.lineTo(mStrokeWidth * 4, getHeight() - mStrokeWidth * 4 + mOffset);
            mPath.lineTo(mStrokeWidth * 4, mStrokeWidth * 4 - progressHeight + mOffset);
            mPath.close();

            canvas.clipPath(mPath, Region.Op.INTERSECT);
            canvas.drawPath(mPath, mPaint);

            startWave(progressHeight, progressHeight);

        } else {

            // 绘制缓存的进度
            mPaint.setColor(mSaveProgressbarColor);
            mRectF.set(mStrokeWidth * 4, getHeight() - mStrokeWidth * 4, getWidth() - mStrokeWidth * 4,
                    getHeight() - (mSaveProgress / mMaxProgress) * mTotalDistance - mStrokeWidth * 4);
            canvas.drawRect(mRectF, mPaint);

            // 绘制限定绘制区域
            mPaint.setColor(mProgressbarColor);
            mTotalDistance = getHeight() - mStrokeWidth * 4 * 2;
            mRectF.set(mStrokeWidth * 4, mStrokeWidth * 4, getWidth() - mStrokeWidth * 4, getHeight() - mStrokeWidth * 4);
            canvas.clipRect(mRectF);

            mPath.reset();

            final float progressHeight = (mProgress / mMaxProgress) * mTotalDistance;
            final float progressWidth = getWidth() - mStrokeWidth * 4 * 2;
            final float quadPointY = progressWidth / 10.0f + mWaveOffset;

            mPath.moveTo(mStrokeWidth * 4 - progressWidth + mOffset, getHeight() - mStrokeWidth * 4);
            mPath.lineTo(mStrokeWidth * 4 + progressWidth + mOffset, getHeight() - mStrokeWidth * 4);

            mPath.lineTo(mStrokeWidth * 4 + progressWidth + mOffset, getHeight() - mStrokeWidth * 4 - progressHeight);

            if (!mWave || mProgress <= 0 || mProgress >= mMaxProgress || mActionDown || mActionMove) {
                mPath.lineTo(mStrokeWidth * 4 - progressWidth + mOffset, getHeight() - mStrokeWidth * 4 - progressHeight);
            } else {
                mPath.quadTo(progressWidth * 3 / 4 + mStrokeWidth * 4 + mOffset, getHeight() - mStrokeWidth * 4 - progressHeight + quadPointY,
                        progressWidth / 2 + mStrokeWidth * 4 + mOffset, getHeight() - mStrokeWidth * 4 - progressHeight);
                mPath.quadTo(progressWidth / 4 + mStrokeWidth * 4 + mOffset, getHeight() - mStrokeWidth * 4 - progressHeight - quadPointY, mStrokeWidth * 4 + mOffset,
                        getHeight() - mStrokeWidth * 4 - progressHeight);
                mPath.quadTo(progressWidth * 3 / 4 + mStrokeWidth * 4 - progressWidth + mOffset, getHeight() - mStrokeWidth * 4 - progressHeight + quadPointY,
                        progressWidth / 2 + mStrokeWidth * 4 - progressWidth + mOffset, getHeight() - mStrokeWidth * 4 - progressHeight);
                mPath.quadTo(progressWidth / 4 + mStrokeWidth * 4 - progressWidth + mOffset, getHeight() - mStrokeWidth * 4 - progressHeight - quadPointY,
                        mStrokeWidth * 4 - progressWidth + mOffset, getHeight() - mStrokeWidth * 4 - progressHeight);
            }

            mPath.lineTo(mStrokeWidth * 4 - progressWidth + mOffset, getHeight() - mStrokeWidth * 4);
            mPath.close();

            canvas.clipPath(mPath, Region.Op.INTERSECT);
            canvas.drawPath(mPath, mPaint);

            startWave(progressWidth, progressWidth);

        }

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startWave(final float totalDistance, final float waveOffset) {
        if (!mWave) {
            return;
        }
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(totalDistance);
            mAnimator.setDuration(1000);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mOffset = (Float) animation.getAnimatedValue();

                    if (mAnimatorRepeatCount % 2 != 0) {
                        mWaveOffset = waveOffset / 30.0f * (1 - animation.getAnimatedFraction());
                    } else {
                        mWaveOffset = waveOffset / 30.0f * animation.getAnimatedFraction();
                    }

                    invalidate();
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    mAnimatorRepeatCount++;
                    if (mAnimatorRepeatCount == 100) {
                        mAnimatorRepeatCount = 0;
                    }
                }
            });
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setRepeatMode(ValueAnimator.INFINITE);
            mAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) {
            mAnimator.removeAllUpdateListeners();
            mAnimator.removeAllListeners();
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final float x = event.getX();
        final float y = event.getY();

        if (mOrientation == Orientation.ORIENTATION_HORIZONTAL) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (mLastX != x) {
                        mActionDown = true;

                        mProgress = (x - mStrokeWidth * 4) * mMaxProgress / mTotalDistance;

                        mProgress = Math.max(Math.min(mProgress, mMaxProgress), 0);

                        if (mOnProgressChangeListener != null) {
                            mOnProgressChangeListener.onProgressChange(mProgress);
                        }

                        invalidate();

                    }

                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mLastX != x) {

                        mActionMove = true;

                        mProgress = (x - mStrokeWidth * 4) * mMaxProgress / mTotalDistance;

                        mProgress = Math.max(Math.min(mProgress, mMaxProgress), 0);

                        if (mOnProgressChangeListener != null) {
                            mOnProgressChangeListener.onProgressChange(mProgress);
                        }

                        invalidate();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mOnProgressChangeListener != null) {
                        mOnProgressChangeListener.onProgressEnd();
                    }
                    mActionDown = false;
                    mActionMove = false;
                    break;

            }
        } else {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (mLastY != y) {
                        mActionDown = true;

                        mProgress = (getHeight() - y - mStrokeWidth * 4) * mMaxProgress / mTotalDistance;

                        mProgress = Math.max(Math.min(mProgress, mMaxProgress), 0);

                        if (mOnProgressChangeListener != null) {
                            mOnProgressChangeListener.onProgressChange(mProgress);
                        }

                        invalidate();

                    }

                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mLastY != y) {
                        mActionMove = true;

                        mProgress = (getHeight() - y - mStrokeWidth * 4) * mMaxProgress / mTotalDistance;

                        mProgress = Math.max(Math.min(mProgress, mMaxProgress), 0);

                        if (mOnProgressChangeListener != null) {
                            mOnProgressChangeListener.onProgressChange(mProgress);
                        }

                        invalidate();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mOnProgressChangeListener != null) {
                        mOnProgressChangeListener.onProgressEnd();
                    }
                    mActionDown = false;
                    mActionMove = false;
                    break;

            }
        }

        mLastX = x;
        mLastY = y;

        return true;
    }


    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        mOnProgressChangeListener = onProgressChangeListener;
    }


    @IntDef
    public @interface Orientation {
        public static final int ORIENTATION_VERTICAL = 1;
        public static final int ORIENTATION_HORIZONTAL = 2;
    }


    public interface OnProgressChangeListener {
        void onProgressChange(float progress);

        void onProgressEnd();
    }

}
