package com.wjf.self_demo.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wjf.self_demo.R;
import com.wjf.self_demo.adapter.DemoListAdapter;
import com.wjf.self_demo.databinding.ActivityDemoBinding;
import com.wjf.self_library.common.BaseActivity;

public class DemoActivity extends BaseActivity<ActivityDemoBinding> {

    DemoListAdapter adapter;

    @Override
    public int setLayout() {
        return R.layout.activity_demo;
    }

    @Override
    protected void initView() {
        view.recyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new DemoListAdapter();
        view.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {}
}
