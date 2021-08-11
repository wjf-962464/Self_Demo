package com.wjf.self_demo.activity;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityDataBindingBinding;
import com.wjf.self_demo.jetpack.User;
import com.wjf.self_library.common.BaseActivity;

/** @author asus */
public class DataBindingActivity extends BaseActivity<ActivityDataBindingBinding> {
    private User user;

    @Override
    public int setLayout() {
        return R.layout.activity_data_binding;
    }

    @Override
    protected void initView() {
        user = new User("王佳峰", "11");
        view.setUser(user);

        new Thread(
                        () -> {
                            for (int i = 0; i < 10; i++) {
                                user.setName(user.getName() + "1");
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                .start();
    }

    @Override
    protected void initData() {}
}
