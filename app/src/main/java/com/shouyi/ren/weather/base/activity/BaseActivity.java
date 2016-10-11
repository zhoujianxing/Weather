package com.shouyi.ren.weather.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.shouyi.ren.weather.R;
import com.shouyi.ren.weather.common.PLog;
import com.shouyi.ren.weather.statusbar.StatusBarHelper;
import com.shouyi.ren.weather.swipeback.SwipeBackActivity;

import butterknife.ButterKnife;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.base
 * @Description:
 * @date 2016/10/7 13:59
 */

public abstract class BaseActivity extends SwipeBackActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private boolean mDoubleClickEnable = false;//是否支持双击，默认为不支持
    private long mLastClickTime;
    private final long MIN_CLICK_DELAY_TIME = 300;//被判断为重复点击的时间间隔

    /**
     * set layout_id
     *
     * @return layout_id
     */
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutId = getLayoutId();
        if (layoutId == 0)
            throw new RuntimeException("can't find layout ,initialise fail !");
        setContentView(layoutId);

        ButterKnife.bind(this);

        initStatusBar();

        if (null != getIntent()) {
            handleIntent(getIntent());
        }
    }

    /**
     * immersive status bar
     */
    private void initStatusBar() {
        StatusBarHelper helper = new StatusBarHelper(this,
                StatusBarHelper.LEVEL_19_TRANSLUCENT,
                StatusBarHelper.LEVEL_21_VIEW);
        helper.setColor(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * handle the intent
     *
     * @param intent
     */
    protected void handleIntent(Intent intent) {

    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(R.anim.page_right_in, R.anim.page_right_out);
    }

    /**
     * finish the activity with only one fragment
     */
    @Override
    public void onBackPressed() {
        handlerBack();
    }

    public boolean handlerBack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0 || count == 1) {
            activityBack();
            return true;
        } else {
            removeFragment();
        }
        return false;
    }

    protected void activityBack() {
        finish();
    }

    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /**
     * filter doubleClick
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (isDoubleClick()) {
                PLog.d(TAG, "重复点击");
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isDoubleClick() {
        if (mDoubleClickEnable)
            return false;
        long time = System.currentTimeMillis();
        if (time - mLastClickTime > MIN_CLICK_DELAY_TIME) {
            mLastClickTime = time;
            return false;
        } else {
            return true;
        }
    }

    /**
     * set support double_click
     *
     * @param isDoubleClickEnable
     */
    protected void setDoubleClickEnable(boolean isDoubleClickEnable) {
        mDoubleClickEnable = isDoubleClickEnable;
    }

    /**
     * the Stack with the fragment is less than one then finish the activity first
     *
     * @return
     */
    @Override
    public boolean swipeBackPriority() {
        return super.swipeBackPriority();
    }
}
