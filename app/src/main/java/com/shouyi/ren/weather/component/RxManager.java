package com.shouyi.ren.weather.component;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.component
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 19:13
 */

public class RxManager {
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public void add(Subscription s) {
        compositeSubscription.add(s);
    }

    public void clear() {
        compositeSubscription.unsubscribe();
    }
}
