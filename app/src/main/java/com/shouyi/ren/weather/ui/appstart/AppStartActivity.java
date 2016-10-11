package com.shouyi.ren.weather.ui.appstart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.shouyi.ren.weather.BuildConfig;
import com.shouyi.ren.weather.ui.appmain.MainActivity;
import com.shouyi.ren.weather.R;
import com.shouyi.ren.weather.base.activity.BaseMvpActivity;
import com.shouyi.ren.weather.common.utils.ToastUtils;

import java.lang.ref.WeakReference;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.ui.appstart
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/10 17:34
 */

public class AppStartActivity extends BaseMvpActivity<AppStartModel, AppStartPersenter> implements IAppStartContract.View {
    private static final String TAG = AppStartActivity.class.getSimpleName();
    private SwithHandler mHandler = new SwithHandler(Looper.getMainLooper(), this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_start;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAllPermissions();

        if (isNewVersionFirstStart()) {
            setSecondStart();

            //第一次进入应用的操作
        } else {
            //非第一次进入应用的操作
        }
    }

    @Override
    public void checkAllPermissions() {
        if (PermissionsManager.hasAllPermissions(this, getAllPermissions())) {
            mHandler.sendEmptyMessageDelayed(1, 1000);
            return;
        }

        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }

            @Override
            public void onDenied(String permission) {
                ToastUtils.showToastLong(AppStartActivity.this, permission + "被禁止");
                finish();
            }
        });
    }

    @Override
    public String[] getAllPermissions() {
        try {
            return getPackageManager()
                    .getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_PERMISSIONS)
                    .requestedPermissions;
        } catch (Exception e) {
            return new String[0];
        }
    }

    @Override
    public boolean isNewVersionFirstStart() {
        SharedPreferences spf = getSharedPreferences("info", Context.MODE_PRIVATE);
        int versionCode = spf.getInt("vc", -1);
        return BuildConfig.VERSION_CODE > versionCode;
    }

    @Override
    public void setSecondStart() {
        SharedPreferences spf = getSharedPreferences("info", Context.MODE_PRIVATE);
        spf.edit().putInt("vc", BuildConfig.VERSION_CODE).apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    class SwithHandler extends Handler {
        private WeakReference<AppStartActivity> mWeakPeference;

        public SwithHandler(Looper looper, AppStartActivity activity) {
            super(looper);
            mWeakPeference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
            AppStartActivity.this.startActivity(intent);

            //activity切换的淡入淡出效果
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            AppStartActivity.this.finish();
        }
    }

    @Override
    public void showWaitDialog() {

    }

    @Override
    public void hideWaitDialog() {

    }

    @Override
    public void showToast(String msg) {

    }
}
