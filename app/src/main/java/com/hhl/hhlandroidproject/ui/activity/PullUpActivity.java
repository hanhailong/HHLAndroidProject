package com.hhl.hhlandroidproject.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

import com.hhl.hhlandroidproject.R;
import com.hhl.hhlandroidproject.entity.Person;
import com.hhl.hhlandroidproject.ui.base.BaseSwipeBackActivity;
import com.hhl.hhlandroidproject.ui.view.PullLoadMoreListener;
import com.hhl.hhlandroidproject.ui.view.PullRefreshLayout.PullRefreshLayout;
import com.hhl.hhlcommonlibs.adapter.HHLBaseAdapterHelper;
import com.hhl.hhlcommonlibs.adapter.HHLQuickAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class PullUpActivity extends BaseSwipeBackActivity {

    @Bind(R.id.pull_refresh_layout)
    PullRefreshLayout mRefreshLayout;

    @Bind(android.R.id.list)
    ListView mListView;

    @Bind(R.id.fab)
    FloatingActionButton mFAB;

    private HHLQuickAdapter<Person> mQuickAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_up);

        mFAB.attachToListView(mListView);

        setupOneItemViews();

        initData();
    }

    private void initData() {
        List<Person> personList = new ArrayList<Person>();
        for (int i = 0 ;i < 20;i++){
            Person person = new Person();
            person.setUsername("大海"+i);
            personList.add(person);
        }

        mQuickAdapter.addAll(personList);

        mQuickAdapter.showIndeterminateProgress(true);
    }

    private void setupOneItemViews() {
        setTitle("单个Item的适配器");

        mQuickAdapter = new HHLQuickAdapter<Person>(this,R.layout.person_item_1) {
            @Override
            protected void convert(HHLBaseAdapterHelper adapterHelper, Person item) {
                adapterHelper.setText(R.id.tv_username,item.getUsername());
            }
        };

        mListView.setAdapter(mQuickAdapter);

        mRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
        mRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandle.sendEmptyMessageDelayed(0, 2000);
            }
        });

        mListView.setOnScrollListener(new PullLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //TODO
                Toast.makeText(PullUpActivity.this,"开始加载更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mRefreshLayout.setRefreshing(false);
        }
    };
}
