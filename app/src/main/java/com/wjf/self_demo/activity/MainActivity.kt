package com.wjf.self_demo.activity

import com.orhanobut.logger.Logger
import com.wjf.self_demo.databinding.ActivityMainBinding
import org.jxxy.debug.corekit.common.BaseActivity

/**
 * @author Wangjf2-DESKTOP
 */
class MainActivity :
    BaseActivity<ActivityMainBinding>() {

    override fun initView() {
        /*        view.recyclerViewune.setAdapter(
                new ListAdapter(
                        this,
                        Arrays.asList(
                                new UserInfo("王佳峰", "大帅哥"),
                                new UserInfo("章志民", "大帅哥"),
                                new UserInfo("吴一鸣", "大帅哥"))));
        view.recyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));*/

        val sp = getSharedPreferences(packageName, MODE_PRIVATE)
        val edit = sp.edit()
        edit.putString("key", "")
        edit.commit()
        val value = sp.getString("key", "sss")
        Logger.d("key is ${value ?: "aaa"}")
        Logger.d(
            "key is ${
            value?.ifEmpty {
                "aaaaa"
            }
            }"
        )
    }

    override fun bindLayout(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
    }
}
