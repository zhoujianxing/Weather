package com.shouyi.ren.weather.ui.appmain;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.ui.appmain
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/11 11:48
 */

public class MainPersenter extends IMainContract.Presenter {
    @Override
    public void setIcon() {
        model.initIcon();
    }
}
