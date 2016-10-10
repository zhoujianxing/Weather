package com.shouyi.ren.weather.component;

import com.shouyi.ren.weather.model.bean.VersionAPI;
import com.shouyi.ren.weather.model.bean.WeatherAPI;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.component
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 20:12
 */

public interface ApiInterface {
    String BASE_URL = "https://api.heweather.com/x3/";

    @GET("weather")
    Observable<WeatherAPI> mWeatherAPI(@Query("city") String city, @Query("key") String key);

    //而且在Retrofit 2.0 中我们还可以在@Url里面定义完整的URL：这种情况下Base URL 会被忽略
    @GET("http://api.fir.im/apps/latest/5630e5f1f2fc425c52000006")
    Observable<VersionAPI> mVersionAPI(@Query("api_token") String api_token);
}
