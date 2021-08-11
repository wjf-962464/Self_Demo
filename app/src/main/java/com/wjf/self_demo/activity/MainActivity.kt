package com.wjf.self_demo.activity

import android.view.Gravity
import android.view.WindowManager
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityMainBinding
import com.wjf.self_demo.util.click
import com.wjf.self_demo.view.MyDialog
import com.wjf.self_library.common.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @author Wangjf2-DESKTOP
 */
class MainActivity :
    BaseActivity<ActivityMainBinding?>() {
    override fun setLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        val myDialog1 =
            MyDialog.Builder(this).address("中骏广场").hint("已为您切换收货地址为")
                .height(WindowManager.LayoutParams.WRAP_CONTENT)
                .width(WindowManager.LayoutParams.WRAP_CONTENT).gravity(Gravity.CENTER)
                .cancelOnTouch(true).build()
        val myDialog2 =
            MyDialog.Builder(this).address("中骏广场超长文案超长文案超长文案超长文案超长文案").hint("已为您切换收货地址为")
                .height(WindowManager.LayoutParams.WRAP_CONTENT)
                .width(WindowManager.LayoutParams.WRAP_CONTENT).gravity(Gravity.CENTER)
                .cancelOnTouch(true).build()
        btn1.click {
            myDialog1.show()
        }
        btn2.click {
            myDialog2.show()
        }

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

    override fun initData() {}
}