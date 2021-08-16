package com.wjf.self_demo.activity;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityDataBindingBinding;
import com.wjf.self_demo.jetpack.User;
import com.wjf.self_demo.jetpack.UserModel;
import com.wjf.self_library.common.BaseActivity;

/** @author asus */
public class DataBindingActivity extends BaseActivity<ActivityDataBindingBinding> {
    private User user;
    private UserModel userModel;

    @Override
    public int setLayout() {
        return R.layout.activity_data_binding;
    }

    @Override
    protected void initView() {
        user = new User("王佳峰", "11");
        view.setUser(user);
        view.setLifecycleOwner(this);
        userModel=new UserModel("王佳峰",10);

        view.setViewModel(userModel);

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
        userModel.setName("章志民");
    }

    @Override
    protected void initData() {}
}
