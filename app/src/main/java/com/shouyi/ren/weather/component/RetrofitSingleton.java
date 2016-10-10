package com.shouyi.ren.weather.component;

import com.litesuits.orm.db.assit.WhereBuilder;
import com.shouyi.ren.weather.BuildConfig;
import com.shouyi.ren.weather.base.BaseApplication;
import com.shouyi.ren.weather.common.PLog;
import com.shouyi.ren.weather.common.utils.RxUtils;
import com.shouyi.ren.weather.common.utils.ToastUtils;
import com.shouyi.ren.weather.common.utils.Util;
import com.shouyi.ren.weather.model.bean.C;
import com.shouyi.ren.weather.model.bean.VersionAPI;
import com.shouyi.ren.weather.model.bean.Weather;
import com.shouyi.ren.weather.model.bean.WeatherAPI;
import com.shouyi.ren.weather.model.domain.CityORM;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.component
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 20:11
 */

public class RetrofitSingleton {
    private static ApiInterface apiInterface = null;
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    private RetrofitSingleton() {
        init();
    }

    private void init() {
        initOkHttp();
        initRetrofit();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    private static class SingletonHelper {
        private static final RetrofitSingleton mInstance = new RetrofitSingleton();
    }

    public static RetrofitSingleton getInstance() {
        return SingletonHelper.mInstance;
    }

    /**
     * 初始化 OkHttp
     */
    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        //
        File cacheFile = new File(BaseApplication.cacheDir, "/NetCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        Interceptor cacheIntercepter = chain -> {
            Request request = chain.request();
            if (!Util.isNetworkConnected(BaseApplication.getAppContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (Util.isNetworkConnected(BaseApplication.getAppContext())) {
                int maxAge = 0;
                //有网络时 设置缓存时间为0 即不缓存
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                //无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        };

        builder.cache(cache).addInterceptor(cacheIntercepter);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);

        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static void disposeFailureInfo(Throwable t) {
        if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") || t.toString().contains("UnknownHostException")) {
            ToastUtils.showToastShort(BaseApplication.getAppContext(), "网络问题");
        } else if (t.toString().contains("API没有")) {
            OrmLite.getInstance().delete(new WhereBuilder(CityORM.class).where("name=?", Util.replaceInfo(t.getMessage())));
            PLog.w(Util.replaceInfo(t.getMessage()));
            ToastUtils.showToastShort(BaseApplication.getAppContext(), "错误" + t.getMessage());
        }

        PLog.w(t.getMessage());
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }

    public Observable<Weather> fetchWeather(String city) {
        return apiInterface.mWeatherAPI(city, C.KEY)
                .flatMap(weatherAPI -> {
                    String status = weatherAPI.mHeWeatherDataService30s.get(0).status;
                    if ("no more requests".equals(status)) {
                        return Observable.error(new RuntimeException("/~~~/~~~,Api免费次数已用完"));
                    } else if ("unknown city".equals(status)) {
                        return Observable.error(new RuntimeException(String.format("Api没有 %s", city)));
                    }
                    return Observable.just(weatherAPI);
                })
                .map(weatherAPI -> weatherAPI.mHeWeatherDataService30s.get(0))
                .compose(RxUtils.rxSchedulerHelper());
    }

    public Observable<VersionAPI> fetchVersion() {
        return apiInterface.mVersionAPI(C.API_TOKEN).compose(RxUtils.rxSchedulerHelper());
    }
}
