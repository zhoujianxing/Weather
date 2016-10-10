package com.shouyi.ren.weather.component;

import com.litesuits.orm.LiteOrm;
import com.shouyi.ren.weather.base.BaseApplication;
import com.shouyi.ren.weather.BuildConfig;
import com.shouyi.ren.weather.common.PLog;
import com.shouyi.ren.weather.model.bean.C;
import com.shouyi.ren.weather.common.utils.RxUtils;
import com.shouyi.ren.weather.common.utils.SimpleSubscriber;
import com.shouyi.ren.weather.model.domain.CityORM;

import rx.Observable;

/**
 * Created by HugoXie on 16/7/24.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
public class OrmLite {


    static LiteOrm liteOrm;

    public static LiteOrm getInstance() {
        getOrmHolder();
        return liteOrm;
    }

    private static OrmLite getOrmHolder() {
        return OrmHolder.sInstance;
    }

    private OrmLite() {
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(BaseApplication.getAppContext(), C.ORM_NAME);

        }
        liteOrm.setDebugged(BuildConfig.DEBUG);
    }

    private static class OrmHolder {
        private static final OrmLite sInstance = new OrmLite();
    }

    public static <T> void OrmTest(Class<T> t) {
        Observable.from(getInstance().query(t))
            .compose(RxUtils.rxSchedulerHelper())
            .subscribe(new SimpleSubscriber<T>() {
                @Override
                public void onNext(T t) {
                    if (t instanceof CityORM) {
                        PLog.w(((CityORM) t).getName());
                    }
                }
            });
    }
}
