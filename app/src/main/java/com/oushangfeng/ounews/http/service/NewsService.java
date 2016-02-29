package com.oushangfeng.ounews.http.service;

import com.oushangfeng.ounews.bean.NeteastNewsDetail;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.bean.NeteastVideoSummary;
import com.oushangfeng.ounews.bean.SinaPhotoDetail;
import com.oushangfeng.ounews.bean.SinaPhotoList;
import com.oushangfeng.ounews.bean.WeatherInfo;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * ClassName: NewsService<p>
 * Author: oubowu<p>
 * Fuction: 请求数据服务<p>
 * CreateDate:2016/2/13 20:34<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public interface NewsService {

    /**
     * 请求新闻列表 例子：http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
     *
     * @param type      新闻类别：headline为头条,local为北京本地,fangchan为房产,list为其他
     * @param id        新闻类别id
     * @param startPage 开始的页码
     * @return 被观察对象
     */
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NeteastNewsSummary>>> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type, @Path("id") String id,
            @Path("startPage") int startPage);

    /**
     * 新闻详情：例子：http://c.m.163.com/nc/article/BFNFMVO800034JAU/full.html
     *
     * @param postId 新闻详情的id
     * @return 被观察对象
     */
    @GET("nc/article/{postId}/full.html")
    Observable<Map<String, NeteastNewsDetail>> getNewsDetail(@Header("Cache-Control") String cacheControl,@Path("postId") String postId);

    /**
     * 新浪图片新闻列表 例子：http://api.sina.cn/sinago/list.json?channel=hdpic_pretty&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&p=1
     *
     * @param page 页码
     * @return 被观察对象
     */
    @GET("list.json")
    Observable<SinaPhotoList> getSinaPhotoList(
            @Header("Cache-Control") String cacheControl,
            @Query("channel") String photoTypeId,
            @Query("adid") String adid,
            @Query("wm") String wm,
            @Query("from") String from,
            @Query("chwm") String chwm,
            @Query("oldchwm") String oldchwm,
            @Query("imei") String imei, @Query("uid") String uid, @Query("p") int page);


    /**
     * 新浪图片详情 例子：http://api.sina.cn/sinago/article.json?postt=hdpic_hdpic_toutiao_4&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&id=20550-66955-hdpic
     *
     * @param id 图片的id
     * @return 被观察者
     */
    @GET("article.json")
    Observable<SinaPhotoDetail> getSinaPhotoDetail(
            @Header("Cache-Control") String cacheControl,
            @Query("postt") String postt,
            @Query("wm") String wm,
            @Query("from") String from,
            @Query("chwm") String chwm,
            @Query("oldchwm") String oldchwm,
            @Query("imei") String imei, @Query("uid") String uid, @Query("id") String id);

    /**
     * 网易视频列表 例子：http://c.m.163.com/nc/video/list/V9LG4B3A0/n/0-10.html
     *
     * @param id        视频类别id
     * @param startPage 开始的页码
     * @return 被观察者
     */
    @GET("nc/video/list/{id}/n/{startPage}-10.html")
    Observable<Map<String, List<NeteastVideoSummary>>> getVideoList(
            @Header("Cache-Control") String cacheControl,@Path("id") String id, @Path("startPage") int startPage);

    /**
     * 天气情况 例子：http://wthrcdn.etouch.cn/weather_mini?city=%E5%8C%97%E4%BA%AC
     *
     * @param city 城市名称
     * @return 被观察者
     */
    @GET("weather_mini")
    Observable<WeatherInfo> getWeatherInfo( @Header("Cache-Control") String cacheControl,@Query("city") String city);

}
