package com.shouyi.ren.weather.component;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.component
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 18:13
 */

public class RxBus {
    private final Subject<Object, Object> bus;

    /**
     * PublishSubject only sends the data from the original observable to the viewer at the point of time after the subscription is sent.
     */
    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        return RxBusHelper.mInstance;
    }

    private static class RxBusHelper {
        private static final RxBus mInstance = new RxBus();
    }

    /**
     * provide a new event
     *
     * @param o
     */
    public void post(Object o) {
        bus.onNext(o);
    }

    /**
     * return a specially been observed by the provide eventType
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
