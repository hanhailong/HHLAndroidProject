package com.hhl.hhlandroidproject.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.hhl.hhlandroidproject.R;
import com.hhl.hhlandroidproject.entity.Person;
import com.hhl.hhlandroidproject.ui.base.BaseSwipeBackActivity;
import com.hhl.hhlcommonlibs.adapter.HHLBaseAdapterHelper;
import com.hhl.hhlcommonlibs.adapter.HHLQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SimpleItemActivity extends BaseSwipeBackActivity {

    @Bind(R.id.listview)
    ListView mListView;

    private HHLQuickAdapter<Person> mQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_item, menu);
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
    protected boolean isLeftBackFinish() {
        return false;
    }
}
