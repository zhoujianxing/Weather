package com.shouyi.ren.weather.mvp;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.mvp
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 15:51
 */

public interface BaseView {
    void showWaitDialog();

    void hideWaitDialog();

    void showToast(String msg);
}
