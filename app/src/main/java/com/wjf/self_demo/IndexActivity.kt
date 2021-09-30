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
import java.util.*
import kotlinx.android.synthetic.main.activity_index.*

/** @author Wangjf2-DESKTOP
 */
class IndexActivity :
    BaseActivity<ActivityIndexBinding>() {
    private val data: MutableList<IndexListMenu> =
        ArrayList()
    private var adapter: IndexListAdapter? = null
    override fun setLayout(): Int {
        return R.layout.activity_index
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = IndexListAdapter()
        recyclerView.adapter = adapter
    }

    override fun initData() {
        data.add(IndexListMenu(MainActivity::class.java, "自定义流式布局"))
        data.add(IndexListMenu(FishActivity::class.java, "灵动的锦鲤"))
        data.add(IndexListMenu(DemoActivity::class.java, "案例"))
        data.add(IndexListMenu(CaptureActivity::class.java, "二维码"))
        data.add(IndexListMenu(ViewActivity::class.java, "自定义控件"))
        data.add(IndexListMenu(LiveDataActivity::class.java, "LiveData&&LiveDataBus"))
        data.add(IndexListMenu(DataBindingActivity::class.java, "DataBinding"))
        data.add(IndexListMenu(ViewModelActivity::class.java, "ViewModel"))
        data.add(IndexListMenu(PhoneCallActivity::class.java, "自动拨号"))
        data.add(IndexListMenu(AccessibilityActivity::class.java, "辅助功能"))
        data.add(IndexListMenu(PaletteActivity::class.java, "调色板"))
        data.add(IndexListMenu(HttpActivity::class.java, "网络请求"))
        data.add(IndexListMenu(TabActivity::class.java, "Tab分页"))
        data.add(IndexListMenu(ConstraintLayoutStateActivity::class.java, "约束布局状态"))
        data.add(IndexListMenu(LoadLayoutActivity::class.java, "LoadLayout"))
        CaptureActivity.setDecodeResultCallback { result: String ->
            Log.d(
                "WJF_DEBUG",
                "扫描结果：$result"
            )
        }
        adapter?.submitList(data)
    }
}
