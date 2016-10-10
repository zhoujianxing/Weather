package com.shouyi.ren.weather.mvp;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.mvp
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 15:50
 */

public class BasePresenter<M, V> implements XPresenter<M, V> {

    public M model;
    public V view;

    @Override
    public void attachModelView(M model, V view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }
}
