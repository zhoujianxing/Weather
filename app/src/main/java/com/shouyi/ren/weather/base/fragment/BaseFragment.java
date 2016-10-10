package com.shouyi.ren.weather.base.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouyi.ren.weather.base.activity.BaseActivity;
import com.shouyi.ren.weather.common.utils.Dialoghelper;
import com.shouyi.ren.weather.common.utils.ToastUtils;
import com.shouyi.ren.weather.mvp.BaseView;
import com.shouyi.ren.weather.swipeback.SwipeBackFragment;

import butterknife.ButterKnife;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.base
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 16:04
 */

public abstract class BaseFragment extends SwipeBackFragment implements BaseView {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected BaseActivity mActivity;
    protected ProgressDialog mDialog;
    private View mFragmentView = null;

    @Override
    public void showWaitDialog() {
        if (mDialog == null) {
            mDialog = Dialoghelper.getWaitDialog(mActivity);
            mDialog.setMessage("正在加载数据...");
        }
        mDialog.show();
    }

    @Override
    public void hideWaitDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        if (mActivity != null) {
            ToastUtils.showToastShort(mActivity, msg);
        }
    }

    /**
     * set layout_id
     *
     * @return layout_id
     */
    protected abstract int getLayoutId();

    /**
     * after layout created
     *
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void onViewCreatedFinish(Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        if (layoutId == 0)
            throw new RuntimeException("can't find layout ,initialise fail !");

        mFragmentView = inflater.inflate(layoutId, container, false);
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null)
            parent.removeView(mFragmentView);
        ButterKnife.bind(this, mFragmentView);
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewCreatedFinish(savedInstanceState);
    }

    /**
     * go back with fragment
     */
    protected void fragmentBack() {
        mActivity.handlerBack();
    }

    /**
     * set the title of actionBar
     *
     * @param title
     */
    protected void safeSetTitle(String title) {
        ActionBar appBarLayout = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }
    }
}
