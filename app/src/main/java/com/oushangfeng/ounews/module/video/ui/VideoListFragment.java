package com.oushangfeng.ounews.module.video.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseFragment;
import com.oushangfeng.ounews.base.BaseRecyclerAdapter;
import com.oushangfeng.ounews.base.BaseRecyclerViewHolder;
import com.oushangfeng.ounews.base.BaseSpacesItemDecoration;
import com.oushangfeng.ounews.bean.NeteastVideoSummary;
import com.oushangfeng.ounews.callback.OnEmptyClickListener;
import com.oushangfeng.ounews.callback.OnItemClickAdapter;
import com.oushangfeng.ounews.callback.OnLoadMoreListener;
import com.oushangfeng.ounews.common.DataLoadType;
import com.oushangfeng.ounews.module.video.presenter.IVideoListPresenter;
import com.oushangfeng.ounews.module.video.presenter.IVideoListPresenterImpl;
import com.oushangfeng.ounews.module.video.view.IVideoListView;
import com.oushangfeng.ounews.utils.ClickUtils;
import com.oushangfeng.ounews.utils.GlideUtils;
import com.oushangfeng.ounews.utils.MeasureUtil;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;
import com.oushangfeng.ounews.widget.refresh.RefreshLayout;

import java.util.List;
import java.util.Random;

/**
 * ClassName: VideoListFragment<p>
 * Author: oubowu<p>
 * Fuction: 视频列表界面<p>
 * CreateDate: 2016/2/23 16:54<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.fragment_video_list,
        handleRefreshLayout = true)
public class VideoListFragment extends BaseFragment<IVideoListPresenter> implements IVideoListView {

    protected static final String VIDEO_ID = "video_id";
    protected static final String POSITION = "position";

    protected String mVideoId;

    private BaseRecyclerAdapter<NeteastVideoSummary> mAdapter;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ThreePointLoadingView mLoadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideoId = getArguments().getString(VIDEO_ID);
            mPosition = getArguments().getInt(POSITION);
        }
    }

    public static VideoListFragment newInstance(String newsId, int position) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_ID, newsId);
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

        mPresenter = new IVideoListPresenterImpl(this, mVideoId, 0);

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
    public void updateVideoList(List<NeteastVideoSummary> data, String errorMsg, @DataLoadType.DataLoadTypeChecker int type) {

        if (mAdapter == null) {
            initVideoList(null);
        }

        mAdapter.showEmptyView(false, "");

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

    private void initVideoList(List<NeteastVideoSummary> data) {

        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mAdapter = new BaseRecyclerAdapter<NeteastVideoSummary>(getActivity(), data, layoutManager) {

            Random mRandom = new Random();

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_video_summary;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, final int position, final NeteastVideoSummary item) {
                final ImageView imageView = holder.getImageView(R.id.iv_video_summary);
                final ViewGroup.LayoutParams params = imageView.getLayoutParams();
                if (item.picWidth == -1 && item.picHeight == -1) {
                    item.picWidth = MeasureUtil.getScreenSize(getActivity()).x / 2 - MeasureUtil.dip2px(getActivity(), 4) * 2 - MeasureUtil.dip2px(getActivity(), 2);
                    item.picHeight = (int) (item.picWidth * (mRandom.nextFloat() / 2 + 0.7));
                }
                params.width = item.picWidth;
                params.height = item.picHeight;
                imageView.setLayoutParams(params);

                GlideUtils.loadDefault(item.cover, imageView, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
                //                Glide.with(getActivity()).load(item.cover).asBitmap().animate(R.anim.image_load).placeholder(R.drawable.ic_loading).error(R.drawable.ic_fail).format(DecodeFormat.PREFER_ARGB_8888)
                //                        .diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);

                holder.getTextView(R.id.tv_video_summary).setText(Html.fromHtml(item.title));
            }
        };

        mAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {

                if (ClickUtils.isFastDoubleClick()) {
                    return;
                }

                final String mp4Url = mAdapter.getData().get(position).mp4Url;
                if (TextUtils.isEmpty(mp4Url)) {
                    toast("此视频无播放网址哎╮(╯Д╰)╭");
                    return;
                }
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra("videoUrl", mp4Url);
                intent.putExtra("videoName", mAdapter.getData().get(position).title);
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
