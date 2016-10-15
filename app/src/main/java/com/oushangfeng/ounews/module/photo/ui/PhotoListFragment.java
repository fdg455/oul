package com.oushangfeng.ounews.module.photo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseFragment;
import com.oushangfeng.ounews.base.BaseRecyclerAdapter;
import com.oushangfeng.ounews.base.BaseRecyclerViewHolder;
import com.oushangfeng.ounews.base.BaseSpacesItemDecoration;
import com.oushangfeng.ounews.bean.SinaPhotoList;
import com.oushangfeng.ounews.callback.OnEmptyClickListener;
import com.oushangfeng.ounews.callback.OnItemClickAdapter;
import com.oushangfeng.ounews.callback.OnLoadMoreListener;
import com.oushangfeng.ounews.common.DataLoadType;
import com.oushangfeng.ounews.module.photo.presenter.IPhotoListPresenter;
import com.oushangfeng.ounews.module.photo.presenter.IPhotoListPresenterImpl;
import com.oushangfeng.ounews.module.photo.view.IPhotoListView;
import com.oushangfeng.ounews.utils.ClickUtils;
import com.oushangfeng.ounews.utils.GlideUtils;
import com.oushangfeng.ounews.utils.MeasureUtil;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;
import com.oushangfeng.ounews.widget.refresh.RefreshLayout;
import com.socks.library.KLog;

import java.util.List;

/**
 * ClassName: PhotoListFragment<p>
 * Author: oubowu<p>
 * Fuction: 图片新闻列表界面<p>
 * CreateDate: 2016/2/21 16:13<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.fragment_photo_list,
        handleRefreshLayout = true)
public class PhotoListFragment extends BaseFragment<IPhotoListPresenter> implements IPhotoListView {

    protected static final String PHOTO_ID = "photo_id";
    protected static final String POSITION = "position";

    protected String mPhotoId;

    private BaseRecyclerAdapter<SinaPhotoList.DataEntity.PhotoListEntity> mAdapter;
    private RecyclerView mRecyclerView;
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

        mRecyclerView = (RecyclerView) fragmentRootView.findViewById(R.id.recycler_view);

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
    public void updatePhotoList(final List<SinaPhotoList.DataEntity.PhotoListEntity> data, String errorMsg, @DataLoadType.DataLoadTypeChecker int type) {

        if (mAdapter == null) {
            initNewsList(data);
        }

        switch (type) {
            case DataLoadType.TYPE_REFRESH_SUCCESS:
                mRefreshLayout.refreshFinish();
                mAdapter.enableLoadMore(true);
                mAdapter.setData(data);
                break;
            case DataLoadType.TYPE_REFRESH_FAIL:
                mRefreshLayout.refreshFinish();
                mAdapter.enableLoadMore(false);
                mAdapter.showEmptyView(true, errorMsg);
                mAdapter.notifyDataSetChanged();
                break;
            case DataLoadType.TYPE_LOAD_MORE_SUCCESS:
                mAdapter.loadMoreSuccess();
                if (data == null || data.size() == 0) {
                    mAdapter.enableLoadMore(null);
                    toast("全部加载完毕");
                    return;
                }
                mAdapter.addMoreData(data);
                break;
            case DataLoadType.TYPE_LOAD_MORE_FAIL:
                mAdapter.loadMoreFailed(errorMsg);
                break;
        }
    }

    private void initNewsList(List<SinaPhotoList.DataEntity.PhotoListEntity> data) {

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        mAdapter = new BaseRecyclerAdapter<SinaPhotoList.DataEntity.PhotoListEntity>(getActivity(), data, layoutManager) {

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_photo_summary;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, final int position, final SinaPhotoList.DataEntity.PhotoListEntity item) {

                GlideUtils.loadDefault(item.kpic, holder.getImageView(R.id.iv_photo_summary), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
                //                Glide.with(getActivity()).load(item.kpic).asBitmap().animate(R.anim.image_load).placeholder(R.drawable.ic_loading).error(R.drawable.ic_fail).format(DecodeFormat.PREFER_ARGB_8888)
                //                        .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.getImageView(R.id.iv_photo_summary));

                holder.getTextView(R.id.tv_photo_summary).setText(item.title);
            }
        };

        mAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {

                if (ClickUtils.isFastDoubleClick()) {
                    return;
                }

                KLog.e(mAdapter.getData().get(position).title + ";" + mAdapter.getData().get(position).id);

                view = view.findViewById(R.id.iv_photo_summary);
                Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                intent.putExtra("photoId", mAdapter.getData().get(position).id);
                //让新的Activity从一个小的范围扩大到全屏
                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

            }
        });

        mAdapter.setOnEmptyClickListener(new OnEmptyClickListener() {
            @Override
            public void onEmptyClick() {
                showProgress();
                mPresenter.refreshData();
            }
        });

        mAdapter.setOnLoadMoreListener(10, new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mPresenter.loadMoreData();
                // mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        });

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new BaseSpacesItemDecoration(MeasureUtil.dip2px(getActivity(), 4)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(250);
        mRecyclerView.getItemAnimator().setMoveDuration(250);
        mRecyclerView.getItemAnimator().setChangeDuration(250);
        mRecyclerView.getItemAnimator().setRemoveDuration(250);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                mPresenter.refreshData();
            }
        });

    }
}
