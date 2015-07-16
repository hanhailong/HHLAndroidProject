package com.hhl.hhlandroidproject.ui.activity;

import android.os.Bundle;

import com.hhl.hhlandroidproject.R;
import com.hhl.hhlandroidproject.ui.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle("关于我");
    }

}
