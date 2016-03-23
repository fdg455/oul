package com.oushangfeng.ounews.base;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * ClassName: FragmentAdapter<p>
 * Author: oubowu<p>
 * Fuction: 装载Fragment的ViewPager适配器<p>
 * CreateDate: 2016/2/17 20:36<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;
    private List<BaseFragment> mFragments;
    private List<String> mTitles;

    /**
     * 更新频道，前面固定的不更新，后面一律更新
     *
     * @param fragments
     * @param titles
     */
    public void updateFragments(List<BaseFragment> fragments, List<String> titles) {
        for (int i = 0; i < mFragments.size(); i++) {
            final BaseFragment fragment = mFragments.get(i);
            final FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (i > 2) {
                ft.remove(fragment);
                mFragments.remove(i);
                i--;
            }
            ft.commit();
        }
        for (int i = 0; i < fragments.size(); i++) {
            if (i > 2) {
                mFragments.add(fragments.get(i));
            }
        }
        this.mTitles = titles;
        notifyDataSetChanged();
    }

    public BaseFragmentAdapter(FragmentManager fm, List<BaseFragment> fragments, List<String> titles) {
        super(fm);
        mFragmentManager = fm;
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    /* @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //得到缓存的fragment
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        //得到tag，这点很重要
        String fragmentTag = fragment.getTag();

        if (((BaseFragment) fragment).isPostUpdate()) {
            //如果这个fragment需要更新
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            //移除旧的fragment
            ft.remove(fragment);
            //换成新的fragment
            fragment = mFragments.get(position);
            //添加新fragment时必须用前面获得的tag，这点很重要
            ft.add(container.getId(), fragment, fragmentTag);
            ft.attach(fragment);
            ft.commit();

            KLog.e("如果这个fragment需要更新");

            //复位更新标志
                    ((BaseFragment) fragment).setPostUpdate(false);
        }
        return fragment;
        // return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }*/

}
