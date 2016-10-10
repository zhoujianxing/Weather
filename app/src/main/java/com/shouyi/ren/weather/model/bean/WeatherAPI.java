package com.shouyi.ren.weather.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.model.bean
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 20:31
 */

public class WeatherAPI {
    @SerializedName("HeWeather data service 3.0")
    @Expose
    public List<Weather> mHeWeatherDataService30s = new ArrayList<>();
}
