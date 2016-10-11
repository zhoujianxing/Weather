package com.shouyi.ren.weather.common.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.shouyi.ren.weather.R;
import com.shouyi.ren.weather.common.utils.SharedPreferenceUtil;
import com.shouyi.ren.weather.common.utils.Util;
import com.shouyi.ren.weather.component.RetrofitSingleton;
import com.shouyi.ren.weather.model.bean.Weather;
import com.shouyi.ren.weather.ui.appmain.MainActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.common.service
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/11 11:51
 */

public class AutoUpdateService extends Service {
    private static final String TAG = AutoUpdateService.class.getSimpleName();
    private SharedPreferenceUtil mSharedPreferenceUtil;

    // http://blog.csdn.net/lzyzsd/article/details/45033611
    // 在生命周期的某个时刻取消订阅。一个很常见的模式就是使用CompositeSubscription来持有所有的Subscriptions，然后在onDestroy()或者onDestroyView()里取消所有的订阅
    private CompositeSubscription mCompotionSubscription;
    private Subscription mNetSubcription;
    private boolean isUnsubcribed = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        mCompotionSubscription = new CompositeSubscription();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            unSubscribed();
            if (isUnsubcribed) {
                unSubscribed();
                if (mSharedPreferenceUtil.getAutoUpdate() != 0) {
                    mNetSubcription = Observable.interval(mSharedPreferenceUtil.getAutoUpdate(), TimeUnit.HOURS)
                            .subscribe(aLong -> {
                                isUnsubcribed = false;
                                fetchDataByNetWork();
                            });
                    mCompotionSubscription.add(mNetSubcription);
                }
            }
        }

        return START_REDELIVER_INTENT;
    }

    /**
     * 网络中获取 该城市相关的信息
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void fetchDataByNetWork() {
        String cityName = mSharedPreferenceUtil.getCityName();
        if (cityName != null) {
            cityName = Util.replaceCity(cityName);
        }

        RetrofitSingleton.getInstance().fetchWeather(cityName)
                .subscribe(weather -> {
                    normalStyleNotification(weather);
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void normalStyleNotification(Weather weather) {
        Intent intent = new Intent(AutoUpdateService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(AutoUpdateService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(AutoUpdateService.this);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setContentTitle(weather.basic.city)
                .setContentText(String.format("%s 当前温度: %s℃ ", weather.now.cond.txt, weather.now.tmp))
                // 这里部分 ROM 无法成功
                .setSmallIcon(mSharedPreferenceUtil.getInt(weather.now.cond.txt, R.mipmap.none))
                .build();

        notification.flags = mSharedPreferenceUtil.getNotificationModel();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // tag 和 id 都是可以拿来区分不同的通知的
        manager.notify(1, notification);
    }

    /**
     * 移除 注册
     */
    private void unSubscribed() {
        isUnsubcribed = true;
        mCompotionSubscription.remove(mNetSubcription);
    }
}
