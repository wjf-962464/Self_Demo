package com.wjf.self_demo

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wjf.barcode.CaptureActivity
import com.wjf.self_demo.activity.*
import com.wjf.self_demo.adapter.IndexListAdapter
import com.wjf.self_demo.databinding.ActivityIndexBinding
import com.wjf.self_demo.entity.IndexListMenu
import com.wjf.self_library.common.BaseActivity
import kotlinx.android.synthetic.main.activity_index.*
import java.util.*

/** @author Wangjf2-DESKTOP
 */
class IndexActivity :
    BaseActivity<ActivityIndexBinding?>() {
    private val data: MutableList<IndexListMenu> =
        ArrayList()
    private var adapter: IndexListAdapter? = null
    override fun setLayout(): Int {
        return R.layout.activity_index
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = IndexListAdapter(this, data)
        recyclerView.adapter = adapter
    }

    override fun initData() {
        data.add(IndexListMenu(MainActivity::class.java, "自定义流式布局"))
        data.add(IndexListMenu(FishActivity::class.java, "灵动的锦鲤"))
        data.add(IndexListMenu(DemoActivity::class.java, "案例"))
        data.add(IndexListMenu(CaptureActivity::class.java, "二维码"))
        data.add(IndexListMenu(ViewActivity::class.java, "自定义控件"))
        data.add(IndexListMenu(PhoneCallActivity::class.java,"自动拨号"))
        data.add(IndexListMenu(AccessibilityActivity::class.java,"辅助功能"))
        CaptureActivity.setDecodeResultCallback { result: String ->
            Log.d(
                "WJF_DEBUG",
                "扫描结果：$result"
            )
        }
        adapter!!.notifyDataSetChanged()
    }
}