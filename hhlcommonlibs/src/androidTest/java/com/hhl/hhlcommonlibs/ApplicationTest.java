package com.hhl.hhlcommonlibs;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.hhl.hhlcommonlibs.adapter.HHLBaseAdapterHelper;
import com.hhl.hhlcommonlibs.adapter.HHLQuickAdapter;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public void testHHLBaseAdapter(){
        HHLQuickAdapter<Object> quickAdapter = new HHLQuickAdapter<Object>(getContext(),R.layout.abc_action_bar_view_list_nav_layout) {
            @Override
            protected void convert(HHLBaseAdapterHelper adapterHelper, Object item) {
                adapterHelper.getView(R.id.action_bar).setTag("");
            }
        };
    }
}