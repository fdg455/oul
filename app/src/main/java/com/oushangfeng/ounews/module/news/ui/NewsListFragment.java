package com.oushangfeng.ounews.module.news.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseFragment;
import com.oushangfeng.ounews.base.BaseRecyclerAdapter;
import com.oushangfeng.ounews.base.BaseRecyclerViewHolder;
import com.oushangfeng.ounews.base.BaseSpacesItemDecoration;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.bean.SinaPhotoDetail;
import com.oushangfeng.ounews.callback.OnItemClickAdapter;
import com.oushangfeng.ounews.common.DataLoadType;
import com.oushangfeng.ounews.module.news.presenter.INewsListPresenter;
import com.oushangfeng.ounews.module.news.presenter.INewsListPresenterImpl;
import com.oushangfeng.ounews.module.news.view.INewsListView;
import com.oushangfeng.ounews.module.photo.ui.PhotoDetailActivity;
import com.oushangfeng.ounews.utils.ClickUtils;
import com.oushangfeng.ounews.utils.MeasureUtil;
import com.oushangfeng.ounews.widget.AutoLoadMoreRecyclerView;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;
import com.oushangfeng.ounews.widget.refresh.RefreshLayout;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: NewsListFragment<p>
 * Author: oubowu<p>
 * Fuction: 新闻列表界面<p>
 * CreateDate: 2016/2/17 20:50<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.fragment_news_list)
public class NewsListFragment extends BaseFragment<INewsListPresenter> implements INewsListView {

    protected static final String NEWS_ID = "news_id";
    protected static final String NEWS_TYPE = "news_type";
    protected static final String POSITION = "position";

    protected String mNewsId;
    protected String mNewsType;
    protected int mPosition;

    private BaseRecyclerAdapter<NeteastNewsSummary> mAdapter;
    private AutoLoadMoreRecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;

    private SinaPhotoDetail mSinaPhotoDetail;

    private ThreePointLoadingView mLoadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsId = getArguments().getString(NEWS_ID);
            mNewsType = getArguments().getString(NEWS_TYPE);
            mPosition = getArguments().getInt(POSITION);
        }
    }

    public static NewsListFragment newInstance(String newsId, String newsType, int position) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_ID, newsId);
        bundle.putString(NEWS_TYPE, newsType);
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

        mPresenter = new INewsListPresenterImpl(this, mNewsId, mNewsType);

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
    public void updateNewsList(final List<NeteastNewsSummary> data, @DataLoadType.DataLoadTypeChecker int type) {
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

    private void initNewsList(final List<NeteastNewsSummary> data) {
        // mAdapter为空肯定为第一次进入状态
        mAdapter = new BaseRecyclerAdapter<NeteastNewsSummary>(getActivity(), data) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_news_summary;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, NeteastNewsSummary item) {
                Glide.with(getActivity()).load(item.imgsrc).asBitmap().animate(R.anim.image_load)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_fail)
                        .into(holder.getImageView(R.id.iv_news_summary_photo));
                holder.getTextView(R.id.tv_news_summary_title).setText(item.title);
                holder.getTextView(R.id.tv_news_summary_digest).setText(item.digest);
                holder.getTextView(R.id.tv_news_summary_ptime).setText(item.ptime);
            }
        };

        mAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {

                if (ClickUtils.isFastDoubleClick()){
                    return;
                }

                // imgextra不为空的话，无新闻内容，直接打开图片浏览
                KLog.e(mAdapter.getData().get(position).title + ";" + mAdapter.getData()
                        .get(position).postid);

                view = view.findViewById(R.id.iv_news_summary_photo);

                if (mAdapter.getData().get(position).postid == null) {
                    toast("此新闻浏览不了哎╮(╯Д╰)╭");
                    return;
                }

                // 跳转到新闻详情
                if (!TextUtils.isEmpty(mAdapter.getData().get(position).digest)) {
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("postid", mAdapter.getData().get(position).postid);
                    intent.putExtra("imgsrc", mAdapter.getData().get(position).imgsrc);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions
                                .makeSceneTransitionAnimation(getActivity(),
                                        view.findViewById(R.id.iv_news_summary_photo), "photos");
                        getActivity().startActivity(intent, options.toBundle());
                    } else {
                        //让新的Activity从一个小的范围扩大到全屏
                        ActivityOptionsCompat options = ActivityOptionsCompat
                                .makeScaleUpAnimation(view, view.getWidth() / 2,
                                        view.getHeight() / 2, 0, 0);
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                    }
                } else {
                    // 以下将数据封装成新浪需要的格式，用于点击跳转到图片浏览
                    mSinaPhotoDetail = new SinaPhotoDetail();
                    mSinaPhotoDetail.data = new SinaPhotoDetail.SinaPhotoDetailDataEntity();
                    mSinaPhotoDetail.data.title = data.get(position).title;
                    mSinaPhotoDetail.data.content = "";
                    mSinaPhotoDetail.data.pics = new ArrayList<>();
                    // 天啊，什么格式都有 --__--
                    if (data.get(position).ads != null) {
                        for (NeteastNewsSummary.AdsEntity entiity : data.get(position).ads) {
                            SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                            sinaPicsEntity.pic = entiity.imgsrc;
                            sinaPicsEntity.alt = entiity.title;
                            sinaPicsEntity.kpic = entiity.imgsrc;
                            mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
                        }
                    } else if (data.get(position).imgextra != null) {
                        for (NeteastNewsSummary.ImgextraEntity entiity : data
                                .get(position).imgextra) {
                            SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                            sinaPicsEntity.pic = entiity.imgsrc;
                            sinaPicsEntity.kpic = entiity.imgsrc;
                            mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
                        }
                    }

                    Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                    intent.putExtra("neteast", mSinaPhotoDetail);
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2,
                                    0, 0);
                    ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                }
            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setAutoLayoutManager(linearLayoutManager).setAutoHasFixedSize(true)
                .addAutoItemDecoration(
                        new BaseSpacesItemDecoration(MeasureUtil.dip2px(getActivity(), 4)))
                .setAutoItemAnimator(new DefaultItemAnimator()).setAutoAdapter(mAdapter);

        mRecyclerView.setOnLoadMoreListener(new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                // 状态停止，并且滑动到最后一位
                mPresenter.loadMoreData();
                // 显示尾部加载
                mAdapter.showFooter();
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
