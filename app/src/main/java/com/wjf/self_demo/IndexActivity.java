package com.wjf.self_demo;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wjf.barcode.CaptureActivity;
import com.wjf.self_demo.activity.DemoActivity;
import com.wjf.self_demo.activity.FishActivity;
import com.wjf.self_demo.activity.MainActivity;
import com.wjf.self_demo.activity.ViewActivity;
import com.wjf.self_demo.adapter.IndexListAdapter;
import com.wjf.self_demo.databinding.ActivityIndexBinding;
import com.wjf.self_demo.entity.IndexListMenu;
import com.wjf.self_library.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/** @author Wangjf2-DESKTOP */
public class IndexActivity extends BaseActivity<ActivityIndexBinding> {
    private List<IndexListMenu> data = new ArrayList<>();
    private IndexListAdapter adapter;

    @Override
    public int setLayout() {
        return R.layout.activity_index;
    }

    @Override
    protected void initView() {
        view.recyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new IndexListAdapter(this, data);
        view.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        data.add(new IndexListMenu(MainActivity.class, "自定义流式布局"));
        data.add(new IndexListMenu(FishActivity.class, "灵动的锦鲤"));
        data.add(new IndexListMenu(DemoActivity.class, "案例"));
        data.add(new IndexListMenu(CaptureActivity.class, "二维码"));
        data.add(new IndexListMenu(ViewActivity.class, "自定义控件"));
        CaptureActivity.setDecodeResultCallback(result -> Log.d("WJF_DEBUG", "扫描结果：" + result));
        adapter.notifyDataSetChanged();
    }
}
