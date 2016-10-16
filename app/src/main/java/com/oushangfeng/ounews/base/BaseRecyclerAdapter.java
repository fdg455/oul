package com.oushangfeng.ounews.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.callback.OnEmptyClickListener;
import com.oushangfeng.ounews.callback.OnItemClickListener;
import com.oushangfeng.ounews.callback.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:
 * Author:oubowu<p>
 * Fuction: RecyclerView通用适配器<p>
 * CreateDate:2016/2/16 22:47<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_MORE = 3;
    public static final int TYPE_EMPTY = 4;
    private static final int TYPE_MORE_FAIL = 5;

    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected OnItemClickListener mClickListener;
    protected boolean mShowLoadMoreView;
    protected boolean mShowEmptyView;

    private RecyclerView.LayoutManager mLayoutManager;

    private int mLastPosition = -1;
    private String mExtraMsg;
    private OnEmptyClickListener mEmptyClickListener;
    private int mMoreItemCount;

    private OnLoadMoreListener mOnLoadMoreListener;

    private Boolean mEnableLoadMore;

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this(context, data, null);
    }

    public BaseRecyclerAdapter(Context context, List<T> data, RecyclerView.LayoutManager layoutManager) {
        mContext = context;
        mLayoutManager = layoutManager;
        mData = data == null ? new ArrayList<T>() : data;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MORE) {
            return new BaseRecyclerViewHolder(mContext, mInflater.inflate(R.layout.item_load_more, parent, false));
        } else if (viewType == TYPE_MORE_FAIL) {
            final BaseRecyclerViewHolder holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(R.layout.item_load_more_failed, parent, false));
            if (mOnLoadMoreListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEnableLoadMore = true;
                        mShowLoadMoreView = true;
                        notifyItemChanged(getItemCount() - 1);
                        holder.itemView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mOnLoadMoreListener.loadMore();
                            }
                        }, 300);
                    }
                });
            }
            return holder;
        } else if (viewType == TYPE_EMPTY) {
            final BaseRecyclerViewHolder holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(R.layout.item_empty_view, parent, false));
            if (mEmptyClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEmptyClickListener.onEmptyClick();
                    }
                });
            }
            return holder;
        } else {
            final BaseRecyclerViewHolder holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(getItemLayoutId(viewType), parent, false));
            if (mClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.getLayoutPosition() != RecyclerView.NO_POSITION) {
                            try {
                                mClickListener.onItemClick(v, holder.getLayoutPosition());
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_MORE) {
            fullSpan(holder, TYPE_MORE);
        } else if (getItemViewType(position) == TYPE_MORE_FAIL) {
            fullSpan(holder, TYPE_MORE_FAIL);
            holder.setText(R.id.tv_failed, mExtraMsg + "请点击重试！");
        } else if (getItemViewType(position) == TYPE_EMPTY) {
            fullSpan(holder, TYPE_EMPTY);
            holder.setText(R.id.tv_error, mExtraMsg);
        } else {
            bindData(holder, position, mData.get(position));
        }

        if (!mShowEmptyView && mOnLoadMoreListener != null && (mEnableLoadMore != null && mEnableLoadMore) && !mShowLoadMoreView && position == getItemCount() - 1 && getItemCount() >= mMoreItemCount) {
            mShowLoadMoreView = true;
            holder.itemView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOnLoadMoreListener.loadMore();
                    notifyItemInserted(getItemCount());
                }
            }, 300);
        }

    }

    private void fullSpan(BaseRecyclerViewHolder holder, final int type) {
        if (mLayoutManager != null) {
            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                if (((StaggeredGridLayoutManager) mLayoutManager).getSpanCount() != 1) {
                    StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                    params.setFullSpan(true);
                }
            } else if (mLayoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;
                final GridLayoutManager.SpanSizeLookup oldSizeLookup = gridLayoutManager.getSpanSizeLookup();
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (getItemViewType(position) == type) {
                            return gridLayoutManager.getSpanCount();
                        }
                        if (oldSizeLookup != null) {
                            return oldSizeLookup.getSpanSize(position);
                        }
                        return 1;
                    }
                });
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(BaseRecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addMoreData(List<T> data) {
        int startPos = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(startPos, data.size());
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if (mShowEmptyView) {
            return TYPE_EMPTY;
        }

        if (mOnLoadMoreListener != null && (mEnableLoadMore != null && mEnableLoadMore) && mShowLoadMoreView && getItemCount() - 1 == position) {
            return TYPE_MORE;
        }

        if (mOnLoadMoreListener != null && !mShowLoadMoreView && (mEnableLoadMore != null && !mEnableLoadMore) && getItemCount() - 1 == position) {
            return TYPE_MORE_FAIL;
        }

        return bindViewType(position);
    }

    @Override
    public int getItemCount() {
        int i = mOnLoadMoreListener == null || mEnableLoadMore == null ? 0 : (mEnableLoadMore && mShowLoadMoreView) || (!mShowLoadMoreView && !mEnableLoadMore) ? 1 : 0;
        return mShowEmptyView ? 1 : mData != null ? mData.size() + i : 0;
    }

    public abstract int getItemLayoutId(int viewType);

    public abstract void bindData(BaseRecyclerViewHolder holder, int position, T item);

    protected int bindViewType(int position) {
        return 0;
    }

    public void showEmptyView(boolean showEmptyView, @NonNull String msg) {
        mShowEmptyView = showEmptyView;
        mExtraMsg = msg;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnEmptyClickListener(OnEmptyClickListener listener) {
        mEmptyClickListener = listener;
    }

    public void setOnLoadMoreListener(int moreItemCount, @NonNull OnLoadMoreListener onLoadMoreListener) {
        mMoreItemCount = moreItemCount;
        mOnLoadMoreListener = onLoadMoreListener;
        mEnableLoadMore = true;
    }

    public void loadMoreSuccess() {
        mEnableLoadMore = true;
        mShowLoadMoreView = false;
        notifyItemRemoved(getItemCount());
    }

    public void loadMoreFailed(String errorMsg) {
        mEnableLoadMore = false;
        mShowLoadMoreView = false;
        mExtraMsg = errorMsg;
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * 设置是否开启底部加载
     *
     * @param enableLoadMore true为开启；false为关闭；null为已经全部加载完毕的关闭
     */
    public void enableLoadMore(Boolean enableLoadMore) {
        mEnableLoadMore = enableLoadMore;
    }


}
