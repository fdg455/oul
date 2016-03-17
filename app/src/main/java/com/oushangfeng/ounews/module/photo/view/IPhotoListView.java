package com.oushangfeng.ounews.module.photo.view;

import com.oushangfeng.ounews.base.BaseView;
import com.oushangfeng.ounews.bean.SinaPhotoList;
import com.oushangfeng.ounews.common.DataLoadType;

import java.util.List;

/**
 * ClassName: IPhotoListView<p>
 * Author: oubowu<p>
 * Fuction: 图片新闻列表接口<p>
 * CreateDate: 2016/2/21 1:35<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public interface IPhotoListView extends BaseView {

    void updatePhotoList(List<SinaPhotoList.DataEntity.PhotoListEntity> data, @DataLoadType.DataLoadTypeChecker int type);

}
