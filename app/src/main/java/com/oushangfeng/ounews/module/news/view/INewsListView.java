package com.oushangfeng.ounews.module.news.view;

import com.oushangfeng.ounews.base.BaseView;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.common.DataLoadType;

import java.util.List;

/**
 * ClassName: INewsListView<p>
 * Author: oubowu<p>
 * Fuction: 新闻列表视图接口<p>
 * CreateDate: 2016/2/18 14:42<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface INewsListView extends BaseView {

    void updateNewsList(List<NeteastNewsSummary> data,@DataLoadType.DataLoadTypeChecker int type);

}
