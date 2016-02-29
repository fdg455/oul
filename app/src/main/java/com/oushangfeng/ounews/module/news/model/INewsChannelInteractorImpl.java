package com.oushangfeng.ounews.module.news.model;

import com.oushangfeng.ounews.app.App;
import com.oushangfeng.ounews.callback.RequestCallback;
import com.oushangfeng.ounews.greendao.NewsChannelTable;
import com.oushangfeng.ounews.greendao.NewsChannelTableDao;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * ClassName: INewsChannelInteractorImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻管理的model层接口实现类<p>
 * CreateDate: 2016/2/20 14:03<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsChannelInteractorImpl
        implements INewsChannelInteractor<Map<Boolean, List<NewsChannelTable>>> {

    @Override
    public Subscription channelDbOperate(final RequestCallback<Map<Boolean, List<NewsChannelTable>>> callback, final String channelName, final Boolean selectState) {

        return Observable
                .create(new Observable.OnSubscribe<Map<Boolean, List<NewsChannelTable>>>() {
                    @Override
                    public void call(Subscriber<? super Map<Boolean, List<NewsChannelTable>>> subscriber) {

                        final NewsChannelTableDao dao = ((App) App.getContext())
                                .getDaoSession().getNewsChannelTableDao();

                        if (selectState == null) {
                            // 初始化
                            KLog.e("初始化取出选中的频道");
                            HashMap<Boolean, List<NewsChannelTable>> map = new HashMap<>();
                            map.put(true, dao.queryBuilder()
                                    .where(NewsChannelTableDao.Properties.NewsChannelSelect
                                            .eq(true))
                                    .orderAsc(NewsChannelTableDao.Properties.NewsChannelIndex)
                                    .build().list());
                            map.put(false, dao.queryBuilder()
                                    .where(NewsChannelTableDao.Properties.NewsChannelSelect
                                            .eq(false))
                                    .orderAsc(NewsChannelTableDao.Properties.NewsChannelIndex)
                                    .build().list());

                            // 只把数据传回去，不调用complete的原因是让RecyclerView把标题位置往下挪完自行关掉
                            subscriber.onNext(map);
                        } else {

                            // 因为名称是唯一的查到即更新其选中状态即可

                            if (selectState) {
                                KLog.e("做增操作: " + channelName + ";" + selectState);
                                // 找到它的信息
                                final NewsChannelTable table = dao.queryBuilder()
                                        .where(NewsChannelTableDao.Properties.NewsChannelName
                                                .eq(channelName)).unique();

                                // 它原来的位置
                                final int originPos = table.getNewsChannelIndex();

                                // 得到现在应该所处位置
                                final long toPos = dao.queryBuilder()
                                        .where(NewsChannelTableDao.Properties.NewsChannelSelect
                                                .eq(true)).buildCount().count();

                                // gt大于   lt小于   ge大于等于   le 小于等于

                                // 找到比它位置小的没被选中的
                                final List<NewsChannelTable> smallChannelTables = dao.queryBuilder()
                                        .where(NewsChannelTableDao.Properties.NewsChannelIndex
                                                        .lt(originPos),
                                                NewsChannelTableDao.Properties.NewsChannelSelect
                                                        .eq(false)).build().list();
                                for (NewsChannelTable s : smallChannelTables) {
                                    s.setNewsChannelIndex(s.getNewsChannelIndex() + 1);
                                    dao.update(s);
                                }

                                // 更新它
                                table.setNewsChannelSelect(true);
                                table.setNewsChannelIndex((int) toPos);

                                dao.update(table);

                            } else {
                                KLog.e("做删操作: " + channelName + ";" + selectState);

                                // 找到它的信息
                                final NewsChannelTable table = dao.queryBuilder()
                                        .where(NewsChannelTableDao.Properties.NewsChannelName
                                                .eq(channelName)).unique();

                                // 它原来的位置
                                final int originPos = table.getNewsChannelIndex();

                                // 得到现在应该所处位置，就是末尾
                                final int nowPos = dao.loadAll().size() - 1;

                                // 未选中的
                                final List<NewsChannelTable> unSelectChannels = dao.queryBuilder()
                                        .where(NewsChannelTableDao.Properties.NewsChannelSelect
                                                .eq(false)).build().list();
                                // 位置全部减1
                                for (NewsChannelTable s : unSelectChannels) {
                                    s.setNewsChannelIndex(s.getNewsChannelIndex() - 1);
                                    dao.update(s);
                                }

                                // 选中的并且比它位置大的
                                final List<NewsChannelTable> bigChannels = dao.queryBuilder()
                                        .where(NewsChannelTableDao.Properties.NewsChannelSelect
                                                        .eq(true),
                                                NewsChannelTableDao.Properties.NewsChannelIndex
                                                        .gt(originPos)).build().list();
                                // 位置全部减1
                                for (NewsChannelTable b : bigChannels) {
                                    b.setNewsChannelIndex(b.getNewsChannelIndex() - 1);
                                    dao.update(b);
                                }


                                // 更新它
                                table.setNewsChannelSelect(false);
                                table.setNewsChannelIndex(nowPos);

                                dao.update(table);

                            }

                            // 只做数据库操作，不关心结果直接调用完成
                            subscriber.onCompleted();
                        }

                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (selectState == null) {
                            // 只在初始化的时候加载动画
                            callback.beforeRequest();
                        }
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<Boolean, List<NewsChannelTable>>>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getLocalizedMessage() + "\n" + e);
                        callback.requestError(
                                e.getLocalizedMessage() + "\n" + e);
                    }

                    @Override
                    public void onNext(Map<Boolean, List<NewsChannelTable>> tableDaos) {
                        callback.requestSuccess(tableDaos);
                    }
                });
    }

    @Override
    public Subscription channelDbSwap(RequestCallback callback, final int fromPos, final int toPos) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                final NewsChannelTableDao dao = ((App) App.getContext())
                        .getDaoSession().getNewsChannelTableDao();

                // 交换前此位置对应的频道
                final NewsChannelTable fromChannel = dao.queryBuilder()
                        .where(NewsChannelTableDao.Properties.NewsChannelIndex.eq(fromPos))
                        .unique();

                final int fromPosition = fromChannel.getNewsChannelIndex();

                // 交换前此位置将要去的对应的频道
                final NewsChannelTable toChannel = dao.queryBuilder()
                        .where(NewsChannelTableDao.Properties.NewsChannelIndex.eq(toPos)).unique();

                final int toPosition = toChannel.getNewsChannelIndex();

                if (Math.abs(fromPosition - toPosition) == 1) {
                    // 相邻的交换，只需要调整两个位置即可
                    KLog.e("相邻的交换，只需要调整两个位置即可");
                    fromChannel.setNewsChannelIndex(toPosition);
                    toChannel.setNewsChannelIndex(fromPosition);
                    dao.update(fromChannel);
                    dao.update(toChannel);
                } else if (fromPosition - toPosition > 0) {
                    //  开始的位置大于要去的位置,往前移
                    KLog.e("开始的位置大于要去的位置,往前移");
                    final List<NewsChannelTable> moveChannels = dao.queryBuilder()
                            .where(NewsChannelTableDao.Properties.NewsChannelIndex
                                    .between(toPosition, fromPosition - 1)).build().list();
                    // 全部加一
                    for (NewsChannelTable c : moveChannels) {
                        c.setNewsChannelIndex(c.getNewsChannelIndex() + 1);
                        dao.update(c);
                    }
                    fromChannel.setNewsChannelIndex(toPosition);
                    dao.update(fromChannel);
                } else if (fromPosition - toPosition < 0) {
                    //  开始的位置小于要去的位置,往后移
                    KLog.e("开始的位置小于要去的位置,往后移: " + toPosition + ";" + fromPosition);
                    final List<NewsChannelTable> moveChannels = dao.queryBuilder()
                            .where(NewsChannelTableDao.Properties.NewsChannelIndex
                                    .between(fromPosition + 1, toPosition)).build().list();
                    KLog.e(moveChannels.size());
                    // 全部减一
                    for (NewsChannelTable c : moveChannels) {
                        c.setNewsChannelIndex(c.getNewsChannelIndex() - 1);
                        dao.update(c);
                    }
                    fromChannel.setNewsChannelIndex(toPosition);
                    dao.update(fromChannel);
                }

                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
