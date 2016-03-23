package com.oushangfeng.ounews.utils;

import android.support.annotation.NonNull;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * ClassName: RxBus<p>
 * Author:oubowu<p>
 * Fuction: Rxjava实现的bus<p>
 * CreateDate:2016/2/14 13:15<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class RxBus {

    private volatile static RxBus sInstance;

    private RxBus() {
    }

    public static RxBus get() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) sInstance = new RxBus();
            }
        }
        return sInstance;
    }

    /**
     * 存储某个标签的Subject集合
     */
    private ConcurrentMap<Object, List<Subject>> mSubjectMapper = new ConcurrentHashMap<>();

    /**
     * 注册事件
     *
     * @param tag   标签
     * @param clazz 类
     * @param <T>   类型
     * @return 被观察者
     */
    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            mSubjectMapper.put(tag, subjectList);
        }

        Subject<T, T> subject;
        subjectList.add(subject = PublishSubject.create());
        KLog.e("{register}subjectMapper: " + mSubjectMapper);
        return subject;
    }

    /**
     * 取消注册事件
     *
     * @param tag        标签
     * @param observable 被观察者
     */
    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        final List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null != subjectList) {
            subjectList.remove(observable);
            if (subjectList.isEmpty()) {
                // 集合数据为0的时候移map除掉tag
                mSubjectMapper.remove(tag);
            }
        }
        KLog.e("{unregister}subjectMapper: " + mSubjectMapper);
    }

    /**
     * 发送事件
     *
     * @param tag     标签
     * @param content 发送的内容
     */
    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content) {
        final List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null != subjectList && !subjectList.isEmpty()) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
    }

}
