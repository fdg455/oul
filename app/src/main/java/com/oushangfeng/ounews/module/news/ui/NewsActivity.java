package com.oushangfeng.ounews.module.news.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.app.AppManager;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.base.BaseFragment;
import com.oushangfeng.ounews.base.BaseFragmentAdapter;
import com.oushangfeng.ounews.greendao.NewsChannelTable;
import com.oushangfeng.ounews.module.news.presenter.INewsPresenter;
import com.oushangfeng.ounews.module.news.presenter.INewsPresenterImpl;
import com.oushangfeng.ounews.module.news.view.INewsView;
import com.oushangfeng.ounews.utils.RxBus;
import com.oushangfeng.ounews.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * ClassName: NewsActivity<p>
 * Author: oubowu<p>
 * Fuction: 新闻界面<p>
 * CreateDate: 2016/2/20 2:12<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_news,
        menuId = R.menu.menu_news,
        hasNavigationView = true,
        toolbarTitle = R.string.news,
        toolbarIndicator = R.drawable.ic_list_white,
        menuDefaultCheckedItem = R.id.action_news)
public class NewsActivity extends BaseActivity<INewsPresenter> implements INewsView {

    private Observable<Boolean> mChannelObservable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("channelChange", mChannelObservable);
    }

    @Override
    protected void initView() {

        // 设了默认的windowBackground使得冷启动没那么突兀，这里再设置为空减少过度绘制
        getWindow().setBackgroundDrawable(null);
        ViewUtil.quitFullScreen(this);

        AppManager.getAppManager().orderNavActivity(getClass().getName(), false);

        mPresenter = new INewsPresenterImpl(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_channel_manage) {
            //  跳转到频道选择界面
            showActivity(this, new Intent(this, NewsChannelActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initViewPager(List<NewsChannelTable> newsChannels) {

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        List<BaseFragment> fragments = new ArrayList<>();
        final List<String> title = new ArrayList<>();

        if (newsChannels != null) {
            // 有除了固定的其他频道被选中，添加
            for (NewsChannelTable news : newsChannels) {
                final NewsListFragment fragment = NewsListFragment
                        .newInstance(news.getNewsChannelId(), news.getNewsChannelType(),
                                news.getNewsChannelIndex());

                fragments.add(fragment);
                title.add(news.getNewsChannelName());
            }

            if (viewPager.getAdapter() == null) {
                // 初始化ViewPager
                BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(),
                        fragments, title);
                viewPager.setAdapter(adapter);
            } else {
                final BaseFragmentAdapter adapter = (BaseFragmentAdapter) viewPager.getAdapter();
                adapter.updateFragments(fragments, title);
            }
            viewPager.setCurrentItem(0, false);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setScrollPosition(0, 0, true);
            // 根据Tab的长度动态设置TabLayout的模式
            ViewUtil.dynamicSetTabLayoutMode(tabLayout);
        } else {
            toast("数据异常");
        }

    }

    @Override
    public void initRxBusEvent() {
        mChannelObservable = RxBus.get().register("channelChange", Boolean.class);
        mChannelObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean change) {
                if (change) {
                    mPresenter.operateChannelDb();
                }
            }
        });
    }


}
