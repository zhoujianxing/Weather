package com.shouyi.ren.weather.base.activity;

import android.os.Bundle;

import com.shouyi.ren.weather.common.utils.ClassUtils;
import com.shouyi.ren.weather.mvp.BaseModel;
import com.shouyi.ren.weather.mvp.BasePresenter;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.base
 * @Description: the base activity of MVP
 * @date 2016/10/7 15:49
 */

public abstract class BaseMvpActivity<M extends BaseModel, P extends BasePresenter> extends BaseActivity {
    public M model;
    public P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initMvp();
        super.onCreate(savedInstanceState);
    }

    protected void initMvp() {
        model = ClassUtils.getT(this, 0);
        presenter = ClassUtils.getT(this, 1);
        presenter.attachModelView(model, this);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.detach();
        }
        super.onDestroy();
    }
}
