package com.oushangfeng.ounews.http.manager;


import android.util.SparseArray;

import com.oushangfeng.ounews.app.App;
import com.oushangfeng.ounews.base.BaseSchedulerTransformer;
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

/**
 * ClassName: RetrofitManager<p>
 * Author: oubowu<p>
 * Fuction: Retrofit请求管理类<p>
 * CreateDate:2016/2/13 20:34<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class RetrofitManager {

    // 设缓存有效期为两天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    // 30秒内直接读缓存
    private static final long CACHE_AGE_SEC = 0;

    private static volatile OkHttpClient sOkHttpClient;
    // 管理不同HostType的单例
    private static SparseArray<RetrofitManager> sInstanceManager = new SparseArray<>(HostType.TYPE_COUNT);
    private NewsService mNewsService;
    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            // 在这里统一配置请求头缓存策略以及响应头缓存策略
            if (NetUtil.isConnected(App.getContext())) {
                // 在有网的情况下CACHE_AGE_SEC秒内读缓存，大于CACHE_AGE_SEC秒后会重新请求数据
                request = request.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control").header("Cache-Control", "public, max-age=" + CACHE_AGE_SEC).build();
                Response response = chain.proceed(request);
                return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control").header("Cache-Control", "public, max-age=" + CACHE_AGE_SEC).build();
            } else {
                // 无网情况下CACHE_STALE_SEC秒内读取缓存，大于CACHE_STALE_SEC秒缓存无效报504
                request = request.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC).build();
                Response response = chain.proceed(request);
                return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC).build();
            }

        }
    };

    // 打印返回的json数据拦截器
    private Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            Request.Builder requestBuilder = request.newBuilder();
            requestBuilder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            request = requestBuilder.build();

            final Response response = chain.proceed(request);

            KLog.e("请求网址: \n" + request.url() + " \n " + "请求头部信息：\n" + request.headers() + "响应头部信息：\n" + response.headers());

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

    private RetrofitManager(@HostType.HostTypeChecker int hostType) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.getHost(hostType)).client(getOkHttpClient()).addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();

        mNewsService = retrofit.create(NewsService.class);
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

    // 配置OkHttpClient
    private OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (sOkHttpClient == null) {
                    // OkHttpClient配置是一样的,静态创建一次即可
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(App.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);

                    sOkHttpClient = new OkHttpClient.Builder().cache(cache).addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mRewriteCacheControlInterceptor).addInterceptor(mLoggingInterceptor).retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS).build();

                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * 网易新闻列表 例子：http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
     * <p>
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
        return mNewsService.getNewsList(type, id, startPage).compose(new BaseSchedulerTransformer<Map<String, List<NeteastNewsSummary>>>());
    }

    /**
     * 网易新闻详情：例子：http://c.m.163.com/nc/article/BG6CGA9M00264N2N/full.html
     *
     * @param postId 新闻详情的id
     * @return 被观察对象
     */
    public Observable<Map<String, NeteastNewsDetail>> getNewsDetailObservable(String postId) {
        return mNewsService.getNewsDetail(postId).compose(new BaseSchedulerTransformer<Map<String, NeteastNewsDetail>>());
    }

    /**
     * 新浪图片新闻列表 例子：http://api.sina.cn/sinago/list.json?channel=hdpic_pretty&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&p=1
     *
     * @param page 页码
     * @return 被观察对象
     */
    public Observable<SinaPhotoList> getSinaPhotoListObservable(String photoTypeId, int page) {
        KLog.e("新浪图片新闻列表: " + photoTypeId + ";" + page);
        return mNewsService.getSinaPhotoList(photoTypeId, "4ad30dabe134695c3b7c3a65977d7e72", "b207", "6042095012", "12050_0001", "12050_0001", "867064013906290",
                "802909da86d9f5fc", page).compose(new BaseSchedulerTransformer<SinaPhotoList>());
    }

    /**
     * 新浪图片详情 例子：http://api.sina.cn/sinago/article.json?postt=hdpic_hdpic_toutiao_4&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&id=20550-66955-hdpic
     *
     * @param id 图片的id
     * @return 被观察者
     */
    public Observable<SinaPhotoDetail> getSinaPhotoDetailObservable(String id) {
        return mNewsService.getSinaPhotoDetail(Api.SINA_PHOTO_DETAIL_ID, "b207", "6042095012", "12050_0001", "12050_0001", "867064013906290", "802909da86d9f5fc", id)
                .compose(new BaseSchedulerTransformer<SinaPhotoDetail>());
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
        return mNewsService.getVideoList(id, startPage).compose(new BaseSchedulerTransformer<Map<String, List<NeteastVideoSummary>>>());
    }

    /**
     * 天气情况 例子：http://wthrcdn.etouch.cn/weather_mini?city=%E5%8C%97%E4%BA%AC
     *
     * @param city 城市名称
     * @return 被观察者
     */
    public Observable<WeatherInfo> getWeatherInfoObservable(String city) {
        return mNewsService.getWeatherInfo(city).compose(new BaseSchedulerTransformer<WeatherInfo>());
    }

}
