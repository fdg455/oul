package com.oushangfeng.ounews.widget.slidr.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.socks.library.KLog;

/**
 * Created by Oubowu on 2016/9/20 0020 11:19.
 */
public class FakeView extends View {

    private View mCacheView;

    public FakeView(Context context) {
        super(context);
    }

    public void drawCacheView(View cacheView) {
        mCacheView = cacheView;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCacheView != null) {
            mCacheView.draw(canvas);
            KLog.e("绘制前一个Activity的图");
            mCacheView = null;
        }
    }

    /*@Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KLog.e("onDetachedFromWindow");
        mCacheView = null;
    }*/
}
