package com.hhl.hhlandroidproject.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.hhl.hhlandroidproject.R;
import com.hhl.hhlandroidproject.entity.Person;
import com.hhl.hhlandroidproject.ui.base.BaseSwipeBackActivity;
import com.hhl.hhlandroidproject.ui.view.stickylistheaders.StickyListHeadersAdapter;
import com.hhl.hhlandroidproject.ui.view.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class StickyListHeaderActivity extends BaseSwipeBackActivity {

    @Bind(R.id.sticky_listview)
    StickyListHeadersListView mStickyListView;

    //数据源
    private List<Person> mDataList = new ArrayList<>();
    //适配器
    private StickyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_list_header);

        init();
    }

    private void init() {
        mAdapter = new StickyAdapter();
        mStickyListView.setAdapter(mAdapter);

        mDataList.clear();
        //初始化数据
        for (int i = 0; i < 20; i++) {
            Person person = new Person();
            person.setAge(i);
            person.setUsername("小明啊" + i);
            person.setPassword("12345");
            mDataList.add(person);
        }

        mAdapter.notifyDataSetChanged();
    }


    public class StickyAdapter extends BaseSwipeAdapter implements StickyListHeadersAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Person getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getSwipeLayoutResourceId(int i) {
            return R.id.swipe;
        }

        @Override
        public View generateView(int i, ViewGroup viewGroup) {
            return LayoutInflater.from(StickyListHeaderActivity.this).inflate(R.layout.sticky_swipe_item
                    , null);
        }

        @Override
        public void fillValues(int position, View convertView) {
            TextView t = (TextView)convertView.findViewById(R.id.position);
            t.setText(getItem(position).getUsername());
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(StickyListHeaderActivity.this);
            textView.setText("欢迎");
            textView.setGravity(Gravity.CENTER_VERTICAL);
            return textView;
        }

        @Override
        public long getHeaderId(int position) {
            return 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sticky_list_header, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isDragEnabled() {
        return false;
    }
}
