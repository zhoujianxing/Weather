package com.shouyi.ren.weather.base.activity;

import android.os.Bundle;

import com.shouyi.ren.weather.common.utils.ClassUtils;
import com.shouyi.ren.weather.mvp.BaseModel;
import com.shouyi.ren.weather.mvp.BasePresenter;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.base
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 18:02
 */

public abstract class BaseToolbarMvpActivity<M extends BaseModel, P extends BasePresenter> extends BaseToolbarActivity {
    public M model;
    public P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        model = ClassUtils.getT(this, 0);
        presenter = ClassUtils.getT(this, 1);
        presenter.attachModelView(model, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != presenter) {
            presenter.detach();
        }
    }
}
