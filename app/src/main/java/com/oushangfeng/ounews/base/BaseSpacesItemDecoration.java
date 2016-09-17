package com.oushangfeng.ounews.base;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * ClassName: SpacesItemDecoration<p>
 * Author: oubowu<p>
 * Fuction: RecyclerView分隔设置<p>
 * CreateDate: 2016/2/18 18:53<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class BaseSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public BaseSpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.top = 0;
        outRect.bottom = mSpace ;

        if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {

            final int index = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();

            if (index % 2 == 0) {
                outRect.right = mSpace / 2 ;
            } else {
                outRect.left = mSpace / 2 ;
            }

            if (position < ((StaggeredGridLayoutManager) parent.getLayoutManager()).getSpanCount()) {
                outRect.top = mSpace;
            }

        } else if (parent.getLayoutManager() instanceof GridLayoutManager) {
            if (position % 2 == 0) {
                outRect.right = mSpace / 2 ;
            } else {
                outRect.left = mSpace / 2 ;
            }
            if (position < ((GridLayoutManager) parent.getLayoutManager()).getSpanCount()) {
                // 保证第一行有相对顶部有高度
                outRect.top = mSpace;
            }
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (position == 0) {
                // 保证第一行有相对顶部有高度
                outRect.top = mSpace;
            }
        }
    }
}