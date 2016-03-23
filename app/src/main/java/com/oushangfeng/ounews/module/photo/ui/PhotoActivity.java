package com.oushangfeng.ounews.module.photo.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.base.BaseFragment;
import com.oushangfeng.ounews.base.BaseFragmentAdapter;
import com.oushangfeng.ounews.http.Api;
import com.oushangfeng.ounews.module.photo.presenter.IPhotoPresenter;
import com.oushangfeng.ounews.module.photo.presenter.IPhotoPresenterImpl;
import com.oushangfeng.ounews.module.photo.view.IPhotoView;
import com.oushangfeng.ounews.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ClassName: PhotoActivity<p>
 * Author: oubowu<p>
 * Fuction: 图片新闻界面<p>
 * CreateDate: 2016/2/21 16:13<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_photo,
        menuId = R.menu.menu_photo,
        hasNavigationView = true,
        toolbarTitle = R.string.photo,
        toolbarIndicator = R.drawable.ic_list_white,
        menuDefaultCheckedItem = R.id.action_photo)
public class PhotoActivity extends BaseActivity<IPhotoPresenter> implements IPhotoView {

    @Override
    protected void initView() {

        mPresenter = new IPhotoPresenterImpl(this);

    }

    @Override
    public void initViewPager() {
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        List<BaseFragment> fragments = new ArrayList<>();
        final List<String> title = Arrays.asList("精选", "趣图", "美图", "故事");

        fragments.add(PhotoListFragment.newInstance(Api.SINA_PHOTO_CHOICE_ID, 0));
        fragments.add(PhotoListFragment.newInstance(Api.SINAD_PHOTO_FUN_ID, 1));
        fragments.add(PhotoListFragment.newInstance(Api.SINAD_PHOTO_PRETTY_ID, 2));
        fragments.add(PhotoListFragment.newInstance(Api.SINA_PHOTO_STORY_ID, 3));

        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments,
                title);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        ViewUtil.dynamicSetTabLayoutMode(tabLayout);

    }

}
