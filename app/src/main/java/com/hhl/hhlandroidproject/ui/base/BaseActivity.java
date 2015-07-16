package com.hhl.hhlandroidproject.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hhl.hhlandroidproject.R;
import com.hhl.hhlandroidproject.util.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 所有Activity的抽象基类
 * Created by hailonghan on 15/7/8.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    @Bind(R.id.title_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar();
    }

    private void setTransparentStatusBar() {
        if (isTransparentStatusBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window win = getWindow();
                WindowManager.LayoutParams winParams = win.getAttributes();
                final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                winParams.flags |= bits;
                win.setAttributes(winParams);
            }
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.app_title_bg_color));
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (isLeftBackFinish()) {
                mToolbar.setNavigationIcon(R.drawable.ic_back);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leftPerformAction();
                    }
                });
            }
        }
    }

    /**
     * 执行左侧按钮行为
     */
    protected void leftPerformAction() {
        finish();
    }

    /**
     * 是否支持透明状态栏，默认为true
     * ，不过不支持，就重写此方法
     *
     * @return
     */
    protected boolean isTransparentStatusBar() {
        return true;
    }

    /**
     * 是否点击左上角按钮返回
     *
     * @return
     */
    protected boolean isLeftBackFinish() {
        return true;
    }
}
