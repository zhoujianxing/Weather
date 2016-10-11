package com.shouyi.ren.weather.ui.appmain;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.shouyi.ren.weather.R;
import com.shouyi.ren.weather.base.activity.BaseMvpActivity;
import com.shouyi.ren.weather.common.utils.Dialoghelper;
import com.shouyi.ren.weather.common.utils.DoubleClickExit;
import com.shouyi.ren.weather.common.utils.RxDrawer;
import com.shouyi.ren.weather.common.utils.RxUtils;
import com.shouyi.ren.weather.common.utils.SimpleSubscriber;
import com.shouyi.ren.weather.common.utils.ToastUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseMvpActivity<MainModel, MainPersenter> implements IMainContract.View, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        initViewPager();
        initDrawer();
        presenter.setIcon();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        RxDrawer.close(drawerLayout)
                .compose(RxUtils.rxSchedulerHelper(AndroidSchedulers.mainThread()))
                .subscribe(new SimpleSubscriber<Void>() {
                    @Override
                    public void onNext(Void aVoid) {
                        switch (item.getItemId()) {
                            case R.id.nav_set:
                                // TODO: 2016/10/11 到设置页面
                                break;
                            case R.id.nav_about:
                                // TODO: 2016/10/11 关于页面
                                break;
                            case R.id.nav_city:
                                // TODO: 2016/10/11 选择城市页面
                                break;
                            case R.id.nav_multi_cities:
                                viewPager.setCurrentItem(1);
                                break;
                        }
                    }
                });
        return false;
    }

    @Override
    public void initViewPager() {
        setSupportActionBar(toolbar);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        homePagerAdapter.addTab(new Fragment(), "主页面");
        homePagerAdapter.addTab(new Fragment(), "多城市");
        viewPager.setAdapter(homePagerAdapter);
        tabLayout.setupWithViewPager(viewPager, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    selectToMain();
                } else {
                    selectToChoiceCity();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab.setOnClickListener(view -> showFabDialog());
    }

    @Override
    public void initDrawer() {
        if (null != navView) {
            navView.setNavigationItemSelectedListener(this);
            View headerLayout = navView.inflateHeaderView(R.layout.nav_header_main);
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }
    }

    @Override
    public void selectToChoiceCity() {
        fab.setImageResource(R.drawable.ic_add_24dp);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
        fab.setOnClickListener(view -> {
            // TODO: 2016/10/11 跳转到 城市选择页面
        });

        if (!fab.isShown()) {
            fab.show();
        }
    }

    @Override
    public void selectToMain() {
        fab.setImageResource(R.drawable.ic_favorite);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
        fab.setOnClickListener(view -> showFabDialog());
    }

    @Override
    public void showFabDialog() {
        Dialoghelper.getMessageDialog(this, "点赞", "去项目地址给作者个Star，鼓励下作者୧(๑•̀⌄•́๑)૭✧", (dialogInterface, position) -> {
            Uri uri = Uri.parse(getString(R.string.app_html));  //指定网址
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);               //指定Action
            intent.setData(uri);                                //设置Uri
            startActivity(intent);
        }).show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!DoubleClickExit.check()) {
                ToastUtils.showToastShort(this, getString(R.string.double_exit));
            } else {
                super.onBackPressed();
            }
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
