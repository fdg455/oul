package com.oushangfeng.ounews.module.photo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseFragment;
import com.oushangfeng.ounews.base.BaseRecyclerAdapter;
import com.oushangfeng.ounews.base.BaseRecyclerViewHolder;
import com.oushangfeng.ounews.base.BaseSpacesItemDecoration;
import com.oushangfeng.ounews.bean.SinaPhotoList;
import com.oushangfeng.ounews.callback.OnItemClickAdapter;
import com.oushangfeng.ounews.common.DataLoadType;
import com.oushangfeng.ounews.module.photo.presenter.IPhotoListPresenter;
import com.oushangfeng.ounews.module.photo.presenter.IPhotoListPresenterImpl;
import com.oushangfeng.ounews.module.photo.view.IPhotoListView;
import com.oushangfeng.ounews.utils.ClickUtils;
import com.oushangfeng.ounews.utils.MeasureUtil;
import com.oushangfeng.ounews.widget.AutoLoadMoreRecyclerView;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;
import com.oushangfeng.ounews.widget.refresh.RefreshLayout;
import com.socks.library.KLog;

import java.util.List;
import java.util.Random;

/**
 * ClassName: PhotoListFragment<p>
 * Author: oubowu<p>
 * Fuction: 图片新闻列表界面<p>
 * CreateDate: 2016/2/21 16:13<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.fragment_photo_list)
public class PhotoListFragment extends BaseFragment<IPhotoListPresenter> implements IPhotoListView {

    protected static final String PHOTO_ID = "photo_id";
    protected static final String POSITION = "position";

    protected String mPhotoId;
    protected int mPosition;

    private BaseRecyclerAdapter<SinaPhotoList.DataEntity.PhotoListEntity> mAdapter;
    private AutoLoadMoreRecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;

    private ThreePointLoadingView mLoadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhotoId = getArguments().getString(PHOTO_ID);
            mPosition = getArguments().getInt(POSITION);
        }
    }

    public static PhotoListFragment newInstance(String newsId, int position) {
        PhotoListFragment fragment = new PhotoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO_ID, newsId);
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initView(View fragmentRootView) {

        mLoadingView = (ThreePointLoadingView) fragmentRootView.findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);

        mRecyclerView = (AutoLoadMoreRecyclerView) fragmentRootView
                .findViewById(R.id.recycler_view);

        mRefreshLayout = (RefreshLayout) fragmentRootView.findViewById(R.id.refresh_layout);

        mPresenter = new IPhotoListPresenterImpl(this, mPhotoId, 1);
    }

    @Override
    public void showProgress() {
        mLoadingView.play();
    }

    @Override
    public void hideProgress() {
        mLoadingView.stop();
    }

    @Override
    public void updatePhotoList(final List<SinaPhotoList.DataEntity.PhotoListEntity> data, @DataLoadType.DataLoadTypeChecker int type) {
        switch (type) {
            case DataLoadType.TYPE_REFRESH_SUCCESS:
                mRefreshLayout.refreshFinish();
                if (mAdapter == null) {
                    initNewsList(data);
                } else {
                    mAdapter.setData(data);
                }
                if (mRecyclerView.isAllLoaded()) {
                    // 之前全部加载完了的话，这里把状态改回来供底部加载用
                    mRecyclerView.notifyMoreLoaded();
                }
                break;
            case DataLoadType.TYPE_REFRESH_FAIL:
                mRefreshLayout.refreshFinish();
                break;
            case DataLoadType.TYPE_LOAD_MORE_SUCCESS:
                // 隐藏尾部加载
                mAdapter.hideFooter();
                if (data == null || data.size() == 0) {
                    mRecyclerView.notifyAllLoaded();
                    toast("全部加载完毕噜(☆＿☆)");
                } else {
                    mAdapter.addMoreData(data);
                    mRecyclerView.notifyMoreLoaded();
                }
                break;
            case DataLoadType.TYPE_LOAD_MORE_FAIL:
                mAdapter.hideFooter();
                mRecyclerView.notifyMoreLoaded();
                break;
        }
    }

    private void initNewsList(List<SinaPhotoList.DataEntity.PhotoListEntity> data) {

        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        mAdapter = new BaseRecyclerAdapter<SinaPhotoList.DataEntity.PhotoListEntity>(getActivity(),
                data, true, layoutManager) {

            Random mRandom = new Random();

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_photo_summary;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, final int position, final SinaPhotoList.DataEntity.PhotoListEntity item) {
                final ImageView imageView = holder.getImageView(R.id.iv_photo_summary);
                final ViewGroup.LayoutParams params = imageView.getLayoutParams();
                // KLog.e("图片网址：" + item.kpic);
                if (item.picWidth == -1 && item.picHeight == -1) {
                    item.picWidth = MeasureUtil.getScreenSize(getActivity()).x / 2 - MeasureUtil
                            .dip2px(getActivity(), 4) * 2 - MeasureUtil.dip2px(getActivity(), 2);
                    item.picHeight = (int) (item.picWidth * (mRandom.nextFloat() / 2 + 1));
                }
                params.width = item.picWidth;
                params.height = item.picHeight;
                imageView.setLayoutParams(params);

                Glide.with(getActivity()).load(item.kpic).asBitmap()
                        .placeholder(R.drawable.ic_loading).error(R.drawable.ic_fail)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

                holder.getTextView(R.id.tv_photo_summary).setText(item.title);
            }
        };

        mAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {

                if (ClickUtils.isFastDoubleClick()){
                    return;
                }

                KLog.e(mAdapter.getData().get(position).title + ";" + mAdapter.getData()
                        .get(position).id);

                view = view.findViewById(R.id.iv_photo_summary);
                Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                intent.putExtra("photoId", mAdapter.getData().get(position).id);
                //让新的Activity从一个小的范围扩大到全屏
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0,
                                0);
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

            }
        });

        mRecyclerView.setAutoLayoutManager(layoutManager).addAutoItemDecoration(
                new BaseSpacesItemDecoration(MeasureUtil.dip2px(getActivity(), 4)))
                .setAutoItemAnimator(new DefaultItemAnimator()).setAutoItemAnimatorDuration(250)
                .setAutoAdapter(mAdapter);

        mRecyclerView.setOnLoadMoreListener(new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                // 状态停止，并且滑动到最后一位
                mPresenter.loadMoreData();
                // 显示尾部加载
                // KLog.e("显示尾部加载前："+mAdapter.getItemCount());
                mAdapter.showFooter();
                // KLog.e("显示尾部加载后："+mAdapter.getItemCount());
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

        mRefreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                mPresenter.refreshData();
            }
        });

    }
}
