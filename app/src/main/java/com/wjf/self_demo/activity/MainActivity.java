package com.wjf.self_demo.activity;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityMainBinding;
import com.wjf.self_library.common.BaseActivity;

/**
 * @author Wangjf2-DESKTOP
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
/*        view.recyclerViewune.setAdapter(
                new ListAdapter(
                        this,
                        Arrays.asList(
                                new UserInfo("王佳峰", "大帅哥"),
                                new UserInfo("章志民", "大帅哥"),
                                new UserInfo("吴一鸣", "大帅哥"))));
        view.recyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));*/
    }

    @Override
    protected void initData() {
    }
}
