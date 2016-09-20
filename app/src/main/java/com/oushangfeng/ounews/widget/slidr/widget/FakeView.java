package com.oushangfeng.ounews.widget.slidr.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

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
        super.onDraw(canvas);
        if (mCacheView != null) {
            mCacheView.draw(canvas);
            Log.e("TAG","FakeView-28行-onDraw(): ");
        }
    }

}
