package com.shouyi.ren.weather.model.bean.db;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.model.bean.db
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 20:37
 */

public class ChangeCityEvent {
    private String city;
    private boolean isSetting;

    public ChangeCityEvent() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isSetting() {
        return isSetting;
    }

    public void setSetting(boolean setting) {
        isSetting = setting;
    }
}
