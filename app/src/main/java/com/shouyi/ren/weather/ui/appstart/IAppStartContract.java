package com.shouyi.ren.weather.ui.appstart;

import com.shouyi.ren.weather.mvp.BaseModel;
import com.shouyi.ren.weather.mvp.BasePresenter;
import com.shouyi.ren.weather.mvp.BaseView;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.ui.appstart
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/10 17:27
 */

public interface IAppStartContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {
        void checkAllPermissions();

        String[] getAllPermissions();

        boolean isNewVersionFirstStart();

        void setSecondStart();
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
