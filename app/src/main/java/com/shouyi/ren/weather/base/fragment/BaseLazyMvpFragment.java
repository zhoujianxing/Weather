package com.shouyi.ren.weather.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.shouyi.ren.weather.common.utils.ClassUtils;
import com.shouyi.ren.weather.mvp.BaseModel;
import com.shouyi.ren.weather.mvp.BasePresenter;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.base
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 16:58
 */

public abstract class BaseLazyMvpFragment<M extends BaseModel, P extends BasePresenter> extends BaseLazyFragment {
    public M model;
    public P presenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        model = ClassUtils.getT(this, 0);
        presenter = ClassUtils.getT(this, 1);
        presenter.attachModelView(model, this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != presenter) {
            presenter.detach();
        }
    }
}
