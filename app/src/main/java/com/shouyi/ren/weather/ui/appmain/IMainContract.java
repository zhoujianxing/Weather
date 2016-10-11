package com.shouyi.ren.weather.ui.appmain;

import com.shouyi.ren.weather.mvp.BaseModel;
import com.shouyi.ren.weather.mvp.BasePresenter;
import com.shouyi.ren.weather.mvp.BaseView;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.ui.appmain
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/11 11:48
 */

public interface IMainContract {

    interface Model extends BaseModel {
        void initIcon();
    }

    interface View extends BaseView {
        /**
         * 初始化基础View
         */
        void initViewPager();

        /**
         * 初始化 抽屉
         */
        void initDrawer();

        /**
         * ViewPager 到 ChoiceCityActivity 页
         */
        void selectToChoiceCity();

        /**
         * ViewPager 到 主页面
         */
        void selectToMain();

        /**
         * 显示 Dialog
         */
        void showFabDialog();

    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void setIcon();
    }
}
