package com.oushangfeng.ounews.module.photo.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.bean.SinaPhotoDetail;
import com.oushangfeng.ounews.module.photo.presenter.IPhotoDetailPresenter;
import com.oushangfeng.ounews.module.photo.presenter.IPhotoDetailPresenterImpl;
import com.oushangfeng.ounews.module.photo.ui.adapter.OnPageChangeListenerAdapter;
import com.oushangfeng.ounews.module.photo.ui.adapter.PhotoAdapter;
import com.oushangfeng.ounews.module.photo.view.IPhotoDetailView;
import com.oushangfeng.ounews.utils.MeasureUtil;
import com.oushangfeng.ounews.widget.HackyViewPager;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;
import com.socks.library.KLog;

import io.vov.vitamio.utils.StringUtils;
import zhou.widget.RichText;

/**
 * ClassName: PhotoDetailActivity<p>
 * Author: oubowu<p>
 * Fuction: 图片新闻详情界面<p>
 * CreateDate: 2016/2/21 16:13<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_photo_detail,
        enableSlidr = true,
        menuId = R.menu.menu_settings,
        toolbarTitle = R.string.photo_detail)
public class PhotoDetailActivity extends BaseActivity<IPhotoDetailPresenter>
        implements IPhotoDetailView {

    private ThreePointLoadingView mLoadingView;
    // 捕获安卓系统报的一个bug
    private HackyViewPager mViewPager;
    private TextView mTitleTv;
    private TextView mPageTv;
    private RichText mContentTv;
    private int mTitleTvPaddingTop;
    private int mContentTvPaddingBottom;
    private int mOffset;
    private ValueAnimator mAnimator;
    private int mContentTvWidth;
    private int mPageTvWidth;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.removeAllUpdateListeners();
            mAnimator.cancel();
        }
    }

    @Override
    protected void initView() {

        mLoadingView = (ThreePointLoadingView) findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);

        mViewPager = (HackyViewPager) findViewById(R.id.viewpager);

        mTitleTv = (TextView) findViewById(R.id.tv_photo_detail_title);

        mPageTv = (TextView) findViewById(R.id.tv_photo_detail_page);

        mContentTv = (RichText) findViewById(R.id.tv_photo_detail_content);

        String photoId = getIntent().getStringExtra("photoId");

        // 若收到此数据不为空的话，说明是网易新闻那边封装好跳过来的，交给代理类处理
        SinaPhotoDetail data = (SinaPhotoDetail) getIntent().getSerializableExtra("neteast");

        mPresenter = new IPhotoDetailPresenterImpl(this, photoId, data);

    }

    @Override
    public void initViewPager(final SinaPhotoDetail photoList) {

        mOffset = MeasureUtil.getScreenSize(this).y / 4;

        mTitleTv.setText(photoList.data.title);
        mTitleTv.setTag(true);

        final PhotoAdapter photoAdapter = new PhotoAdapter(this, photoList.data.pics);
        mViewPager.setAdapter(photoAdapter);

        final OnPageChangeListenerAdapter mPageChangeListenerAdapter = new OnPageChangeListenerAdapter() {

            @Override
            public void onPageSelected(int position) {

                if (position == 0 && mSlidrInterface != null) {
                    mSlidrInterface.unlock();
                } else if (mSlidrInterface != null) {
                    mSlidrInterface.lock();
                }

                final String s = getString(R.string.photo_page, position + 1,
                        photoList.data.pics.size());

                mPageTv.setText(s);

                final String alt = photoList.data.pics.get(position).alt;
                if (!TextUtils.isEmpty(alt) && !alt
                        .equals(mContentTv.getText().toString().trim())) {
                    ObjectAnimator.ofFloat(mContentTv, "alpha", 0.5f, 1).setDuration(500).start();
                    mContentTv.setRichText(getString(R.string.photo_detail_content, alt));
                    dynamicSetTextViewGravity();
                }

                // 每次切换回来都要处理一下，因为切换回来当前的图片不会调用OnPhotoExpandListener的onExpand方法
                controlView(photoAdapter.getPics().get(position).showTitle);

            }

        };

        mViewPager.addOnPageChangeListener(mPageChangeListenerAdapter);

        mContentTv.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mPageTvWidth = mPageTv.getMeasuredWidth();
                        mContentTvWidth = mContentTv.getMeasuredWidth();
                        KLog.e("长度：" + mPageTvWidth + ";" + mContentTvWidth);
                        mPageChangeListenerAdapter.onPageSelected(0);
                        mContentTv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        photoAdapter.setOnPhotoExpandListener(new PhotoAdapter.OnPhotoExpandListener() {
            @Override
            public void onExpand(boolean show, int position) {
                // KLog.e("回调的时候: " + show);
                if (mViewPager.getCurrentItem() == position) {
                    // 当前页码才处理
                    controlView(show);
                }
            }
        });

    }

    /**
     * 根据文本的长度动态设置对齐方式
     */
    private void dynamicSetTextViewGravity() {
        if ((mContentTv.getPaint()
                .measureText(mContentTv.getText().toString()) < mContentTvWidth)) {
            mContentTv.setGravity(Gravity.CENTER);
            // 设为中心对齐，去掉前面两个空格
            mContentTv.setRichText(StringUtils.replaceBlank(mContentTv.getText().toString()));
            KLog.e("设为中心对齐，去掉前面两个空格");
        } else {
            mContentTv.setGravity(Gravity.TOP | Gravity.START);
            KLog.e("设为原始对齐");
        }
    }

    /**
     * 图片被拉大时控制页面其他元素隐藏
     *
     * @param show
     */
    private void controlView(boolean show) {
        if (mAnimator != null && mAnimator.isRunning()) {
            return;
        }
        mAnimator = new ValueAnimator();
        if (!show && (boolean) mTitleTv.getTag()) {
            // 隐藏
            mAnimator.setIntValues(0, mOffset);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    mTitleTv.setTag(false);
                    mTitleTv.setPadding(mTitleTv.getPaddingLeft(),
                            -(int) animation.getAnimatedValue(), mTitleTv.getPaddingRight(),
                            mTitleTv.getPaddingBottom());

                    mContentTv.setPadding(mContentTv.getPaddingLeft(), mContentTv.getPaddingTop(),
                            mContentTv.getPaddingRight(), -(int) animation.getAnimatedValue());

                    ViewCompat.setScaleX(mPageTv, 1 - animation.getAnimatedFraction());
                    ViewCompat.setScaleY(mPageTv, 1 - animation.getAnimatedFraction());

                }
            });
        } else if (show && !(boolean) mTitleTv.getTag()) {
            // 显示
            mAnimator = new ValueAnimator();
            mAnimator.setIntValues(mOffset, 0);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    mTitleTv.setTag(true);
                    mTitleTv.setPadding(mTitleTv.getPaddingLeft(),
                            -(int) animation.getAnimatedValue() + mTitleTvPaddingTop,
                            mTitleTv.getPaddingRight(), mTitleTv.getPaddingBottom());

                    mContentTv.setPadding(mContentTv.getPaddingLeft(), mContentTv.getPaddingTop(),
                            mContentTv.getPaddingRight(),
                            -(int) animation.getAnimatedValue() + mContentTvPaddingBottom);

                    ViewCompat.setScaleX(mPageTv, animation.getAnimatedFraction());
                    ViewCompat.setScaleY(mPageTv, animation.getAnimatedFraction());
                }
            });
        } else {
            return;
        }
        mAnimator.setDuration(300);
        mAnimator.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mTitleTvPaddingTop == 0) {
            mTitleTvPaddingTop = mTitleTv.getPaddingTop();
            mContentTvPaddingBottom = mContentTv.getPaddingBottom();
        }
    }

    @Override
    public void showProgress() {
        mLoadingView.play();
    }

    @Override
    public void hideProgress() {
        mLoadingView.stop();
    }

}
