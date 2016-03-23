package com.oushangfeng.ounews.http.manager;


import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.oushangfeng.ounews.app.App;
import com.oushangfeng.ounews.bean.NeteastNewsDetail;
import com.oushangfeng.ounews.bean.NeteastNewsSummary;
import com.oushangfeng.ounews.bean.NeteastVideoSummary;
import com.oushangfeng.ounews.bean.SinaPhotoDetail;
import com.oushangfeng.ounews.bean.SinaPhotoList;
import com.oushangfeng.ounews.bean.WeatherInfo;
import com.oushangfeng.ounews.http.Api;
import com.oushangfeng.ounews.http.HostType;
import com.oushangfeng.ounews.http.service.NewsService;
import com.oushangfeng.ounews.utils.NetUtil;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ClassName: RetrofitManager<p>
 * Author: oubowu<p>
 * Fuction: Retrofit请求管理类<p>
 * CreateDate:2016/2/13 20:34<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class RetrofitManager {

    //设缓存有效期为两天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
    private static final String CACHE_CONTROL_NETWORK = "max-age=0";

    private static volatile OkHttpClient sOkHttpClient;

    private NewsService mNewsService;

    // 管理不同HostType的单例
    private static SparseArray<RetrofitManager> sInstanceManager = new SparseArray<>(
            HostType.TYPE_COUNT);

    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isConnected(App.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                KLog.e("no network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetUtil.isConnected(App.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder().header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached," + CACHE_STALE_SEC)
                        .removeHeader("Pragma").build();
            }
        }
    };

    // 打印返回的json数据拦截器
    private Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            final Request request = chain.request();
            final Response response = chain.proceed(request);

            final ResponseBody responseBody = response.body();
            final long contentLength = responseBody.contentLength();

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    KLog.e("");
                    KLog.e("Couldn't decode the response body; charset is likely malformed.");
                    return response;
                }
            }

            if (contentLength != 0) {
                KLog.v("--------------------------------------------开始打印返回数据----------------------------------------------------");
                KLog.json(buffer.clone().readString(charset));
                KLog.v("--------------------------------------------结束打印返回数据----------------------------------------------------");
            }

            return response;
        }
    };

    private RetrofitManager() {
    }

    /**
     * 获取单例
     *
     * @param hostType host类型
     * @return 实例
     */
    public static RetrofitManager getInstance(int hostType) {
        RetrofitManager instance = sInstanceManager.get(hostType);
        if (instance == null) {
            instance = new RetrofitManager(hostType);
            sInstanceManager.put(hostType, instance);
            return instance;
        } else {
            return instance;
        }
    }

    private RetrofitManager(@HostType.HostTypeChecker int hostType) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.getHost(hostType))
                .client(getOkHttpClient()).addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();

        mNewsService = retrofit.create(NewsService.class);
    }

    // 配置OkHttpClient
    private OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (sOkHttpClient == null) {
                    // OkHttpClient配置是一样的,静态创建一次即可
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(App.getContext().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);

                    sOkHttpClient = new OkHttpClient.Builder().cache(cache)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mLoggingInterceptor).retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS).build();

                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * 根据网络状况获取缓存的策略
     *
     * @return
     */
    @NonNull
    private String getCacheControl() {
        return NetUtil.isConnected(App.getContext()) ? CACHE_CONTROL_NETWORK : CACHE_CONTROL_CACHE;
    }

    /**
     * 网易新闻列表 例子：http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
     * <p/>
     * 对API调用了observeOn(MainThread)之后，线程会跑在主线程上，包括onComplete也是，
     * unsubscribe也在主线程，然后如果这时候调用call.cancel会导致NetworkOnMainThreadException
     * 加一句unsubscribeOn(io)
     *
     * @param type      新闻类别：headline为头条,list为其他
     * @param id        新闻类别id
     * @param startPage 开始的页码
     * @return 被观察对象
     */
    public Observable<Map<String, List<NeteastNewsSummary>>> getNewsListObservable(String type, String id, int startPage) {
        return mNewsService.getNewsList(getCacheControl(), type, id, startPage)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * 网易新闻详情：例子：http://c.m.163.com/nc/article/BG6CGA9M00264N2N/full.html
     *
     * @param postId 新闻详情的id
     * @return 被观察对象
     */
    public Observable<Map<String, NeteastNewsDetail>> getNewsDetailObservable(String postId) {
        return mNewsService.getNewsDetail(getCacheControl(), postId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io());
    }

    /**
     * 新浪图片新闻列表 例子：http://api.sina.cn/sinago/list.json?channel=hdpic_pretty&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&p=1
     *
     * @param page 页码
     * @return 被观察对象
     */
    public Observable<SinaPhotoList> getSinaPhotoListObservable(String photoTypeId, int page) {
        KLog.e("新浪图片新闻列表: " + photoTypeId + ";" + page);
        return mNewsService.getSinaPhotoList(getCacheControl(), photoTypeId,
                "4ad30dabe134695c3b7c3a65977d7e72", "b207", "6042095012", "12050_0001",
                "12050_0001", "867064013906290", "802909da86d9f5fc", page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * 新浪图片详情 例子：http://api.sina.cn/sinago/article.json?postt=hdpic_hdpic_toutiao_4&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&id=20550-66955-hdpic
     *
     * @param id 图片的id
     * @return 被观察者
     */
    public Observable<SinaPhotoDetail> getSinaPhotoDetailObservable(String id) {
        return mNewsService.getSinaPhotoDetail(getCacheControl(), Api.SINA_PHOTO_DETAIL_ID, "b207",
                "6042095012", "12050_0001", "12050_0001", "867064013906290", "802909da86d9f5fc", id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * 网易视频列表 例子：http://c.m.163.com/nc/video/list/V9LG4B3A0/n/0-10.html
     *
     * @param id        视频类别id
     * @param startPage 开始的页码
     * @return 被观察者
     */
    public Observable<Map<String, List<NeteastVideoSummary>>> getVideoListObservable(String id, int startPage) {
        KLog.e("网易视频列表: " + id + ";" + startPage);
        return mNewsService.getVideoList(getCacheControl(), id, startPage)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * 天气情况 例子：http://wthrcdn.etouch.cn/weather_mini?city=%E5%8C%97%E4%BA%AC
     *
     * @param city 城市名称
     * @return 被观察者
     */
    public Observable<WeatherInfo> getWeatherInfoObservable(String city) {
        return mNewsService.getWeatherInfo(getCacheControl(), city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io());
    }

}
