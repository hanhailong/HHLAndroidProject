package com.hhl.hhlandroidproject.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhl.hhlandroidproject.R;
import com.hhl.hhlandroidproject.entity.Person;
import com.hhl.hhlandroidproject.ui.base.BaseSwipeBackActivity;
import com.hhl.hhlandroidproject.ui.view.stickylistheaders.StickyListHeadersAdapter;
import com.hhl.hhlandroidproject.ui.view.stickylistheaders.StickyListHeadersListView;
import com.hhl.hhlandroidproject.ui.view.stickylistheaders.WrapperViewList;
import com.hhl.hhlandroidproject.ui.view.swipemenulistview.SwipeMenu;
import com.hhl.hhlandroidproject.ui.view.swipemenulistview.SwipeMenuCreator;
import com.hhl.hhlandroidproject.ui.view.swipemenulistview.SwipeMenuItem;
import com.hhl.hhlandroidproject.util.UIScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class StickyListHeaderActivity2 extends BaseSwipeBackActivity {

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

        //
        SwipeMenuCreator menuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(UIScreenUtil.dp2px(StickyListHeaderActivity2.this, 90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(UIScreenUtil.dp2px(StickyListHeaderActivity2.this, 90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_action_back_selected);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        mStickyListView.setMenuCreator(menuCreator);

        mStickyListView.setOnMenuItemClickListener(new WrapperViewList.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                return false;
            }
        });

        mStickyListView.setOnSwipeListener(new WrapperViewList.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

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


    public class StickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(StickyListHeaderActivity2.this);
            textView.setMinHeight(120);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setText(getItem(position).getUsername());
            return textView;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(StickyListHeaderActivity2.this);
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
    protected boolean isDragEnabled() {
        return false;
    }
}
