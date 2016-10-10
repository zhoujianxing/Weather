package com.shouyi.ren.weather.base.fragment;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.base
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 16:31
 */

public abstract class BaseLazyFragment extends BaseFragment {
    /**
     * the Mapping relation of the fragment and Activity
     */
    private boolean isCreatadView = false;

    /**
     * the fragment is visiabled
     */
    private boolean isVisiable = false;

    /**
     * the fragment is first load data
     */
    private boolean isFirstLoading = true;

    private boolean isCanLoaded = isCreatadView && isVisiable;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisiable = true;
            lazyLoadData();
        } else {
            isVisiable = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * lazy fragment
     */
    protected void lazyLoadData() {
        if (!isCreatadView)
            return;
        if (!isVisiable)
            return;

        initData(isFirstLoading);
        isFirstLoading = false;
    }

    /**
     * init Data
     *
     * @param isFirstLoading
     */
    protected abstract void initData(boolean isFirstLoading);
}
