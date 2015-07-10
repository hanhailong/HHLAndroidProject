package com.hhl.hhlandroidproject.ui.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.hhl.hhlandroidproject.R;
import com.hhl.hhlandroidproject.entity.Person;
import com.hhl.hhlandroidproject.ui.base.BaseSwipeBackActivity;
import com.hhl.hhlcommonlibs.adapter.HHLBaseAdapterHelper;
import com.hhl.hhlcommonlibs.adapter.HHLQuickAdapter;
import com.hhl.hhlcommonlibs.adapter.MultiItemTypeSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hailonghan on 15/7/10.
 */
public class MultiItemActivity extends BaseSwipeBackActivity {

    @Bind(R.id.listview)
    ListView mListView;

    private HHLQuickAdapter<Person> mQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiitem);

        setMultiItemViews();


        initData();
    }

    private void initData() {
        List<Person> personList = new ArrayList<Person>();
        for (int i = 0 ;i < 20;i++){
            Person person = new Person();
            person.setUsername("大海" + i);

            if (i % 2 == 0){
                person.setIsMsgCome(true);
            }else{
                person.setIsMsgCome(false);
            }

            personList.add(person);
        }

        mQuickAdapter.addAll(personList);

        mQuickAdapter.showIndeterminateProgress(true);
    }



    private void setMultiItemViews(){
        setTitle("多个Item的适配器");

        MultiItemTypeSupport<Person> multiItemTypeSupport = new MultiItemTypeSupport<Person>() {
            @Override
            public int getLayoutId(int position, Person person) {
                if (person.isMsgCome()){
                    return R.layout.person_item_1;
                }else{
                    return R.layout.person_item_2;
                }
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position, Person person) {
                if (person.isMsgCome()){
                    return 1;
                }else{
                    return 2;
                }
            }
        };

        mQuickAdapter = new HHLQuickAdapter<Person>(this,multiItemTypeSupport) {
            @Override
            protected void convert(HHLBaseAdapterHelper adapterHelper, Person item) {
                switch (adapterHelper.layoutId){
                    case R.layout.person_item_1:
                        adapterHelper.setText(R.id.tv_username,item.getUsername());
                        break;
                    case R.layout.person_item_2:
                        adapterHelper.setText(R.id.tv_username,item.getUsername());
                        break;
                }
            }
        };

        mListView.setAdapter(mQuickAdapter);
    }
}
