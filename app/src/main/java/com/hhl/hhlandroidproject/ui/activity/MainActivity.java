package com.hhl.hhlandroidproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.hhl.hhlandroidproject.R;
import com.hhl.hhlandroidproject.ui.base.BaseActivity;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lv_left_menu)
    ListView mLeftMenuLv;
    @Bind(R.id.id_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initViews();
    }

    public void initViews() {


        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //初始化菜单
        initLeftMenu();
    }

    /**
     * 初始化菜单
     */
    private void initLeftMenu() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,AboutActivity.class));
            return true;
        }

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
