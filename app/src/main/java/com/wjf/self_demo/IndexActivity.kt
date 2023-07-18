package com.wjf.self_demo

import android.content.Intent
import android.os.*
import android.util.Log
import android.view.FrameMetrics
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.MyAsyncLayoutInflater
import com.orhanobut.logger.Logger
import com.wjf.self_demo.activity.*
import com.wjf.self_demo.adapter.IndexListAdapter
import com.wjf.self_demo.databinding.ActivityIndexBinding
import com.wjf.self_demo.databinding.ItemListIndexBinding
import com.wjf.self_demo.entity.IndexListMenu
import kotlinx.coroutines.*
import org.jxxy.debug.barcode.CaptureActivity
import org.jxxy.debug.corekit.common.BaseActivity
import org.jxxy.debug.corekit.recyclerview.SpanItemDecoration

/** @author Wangjf2-DESKTOP
 */
@RequiresApi(Build.VERSION_CODES.N)
class IndexActivity : BaseActivity<ActivityIndexBinding>(), Window.OnFrameMetricsAvailableListener {
    private val data: MutableList<IndexListMenu> = ArrayList()
    private var adapter: IndexListAdapter? = null

    override fun initView() {
//        view.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = IndexListAdapter()
        view.recyclerView.layoutManager = GridLayoutManager(this, 5)
        view.recyclerView.addItemDecoration(SpanItemDecoration(20f, 15f, 5))
        view.recyclerView.adapter = adapter
    }

    override fun bindLayout(): ActivityIndexBinding {
        return ActivityIndexBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
        data.add(IndexListMenu(BehaviorActivity::class.java, "behavior"))
        data.add(IndexListMenu(NestedRecyclerActivity::class.java, "列表嵌套"))
        data.add(IndexListMenu(MainActivity::class.java, "自定义流式布局"))
        data.add(IndexListMenu(FishActivity::class.java, "灵动的锦鲤"))
        data.add(IndexListMenu(CaptureActivity::class.java, "二维码"))
        data.add(IndexListMenu(ViewActivity::class.java, "自定义控件"))
        data.add(IndexListMenu(PaletteActivity::class.java, "调色板"))
        data.add(IndexListMenu(ConstraintLayoutStateActivity::class.java, "约束布局状态"))
        data.add(IndexListMenu(LoadLayoutActivity::class.java, "LoadLayout"))
        data.add(IndexListMenu(BezierActivity::class.java, "贝塞尔曲线"))
        data.add(IndexListMenu(EventDispatchActivity::class.java, "事件分发"))
        data.add(IndexListMenu(PicLoadActivity::class.java, "图片加载"))
        CaptureActivity.setDecodeResultCallback { result: String ->
            Logger.d("扫描结果：$result")
        }

        val flag = false
//        view.recyclerView.itemAnimator = null
        if (flag) {
//            view.recyclerView.itemAnimator = null
            view.recyclerView.recycledViewPool.setMaxRecycledViews(1, 20)
            repeat(20) {
//                Log.d("wjftc", "布局解析$it")
                MyAsyncLayoutInflater(this@IndexActivity).inflate(R.layout.item_list_index, view.recyclerView) { view, resid, parent ->
                    val holder = IndexListAdapter.IndexListViewHolder(ItemListIndexBinding.bind(view))
                    holder.bindItemViewType(1)
                    this@IndexActivity.view.recyclerView.recycledViewPool.putRecycledView(holder)
//                    Log.d("wjftc", "布局解析好了$it")
                }
            }
        }

        lifecycleScope.launch(
            Dispatchers.Default + CoroutineExceptionHandler { _, e ->
                Log.e("wjftc", "出错了", e)
            }
        ) {
            Log.d("wjftc", "subscribeUi: in ${Thread.currentThread().name}")
            val a = data.asReversed()
            Log.d("wjftc", "subscribeUi: out ")
            delay(2000L)
            Log.d("wjftc", "网络请求结束了")
            withContext(Dispatchers.Main) {
                adapter?.addAll(a)
            }
        }
        val a = HandlerThread("帧率监控")
        a.start()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            window.addOnFrameMetricsAvailableListener(this, Handler(a.looper))
        }

        val START = ">>>>> Dispatching"
        val END = "<<<<< Finished"
        var time = 0L
        Looper.getMainLooper().setMessageLogging {
            if (it.startsWith(START)) {
                time = System.currentTimeMillis()
            }
            if (it.startsWith(END)) {
                val dif = System.currentTimeMillis() - time
                if (dif > 16.6f) {
                    Log.i("wjftc", "主线程耗时$dif ms")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("ActivityA - onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        println("ActivityA - onPause")
    }

    override fun onStop() {
        super.onStop()
        println("ActivityA - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("ActivityA - onDestroy")
    }

    override fun onStart() {
        super.onStart()
        println("ActivityA - onStart")
    }

    override fun onRestart() {
        super.onRestart()
        println("ActivityA - onRestart")
    }

    override fun onResume() {
        super.onResume()
        println("ActivityA - onResume")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        println("ActivityA - onNewIntent")
    }

    override fun onFrameMetricsAvailable(window: Window?, frameMetrics: FrameMetrics?, dropCountSinceLastInvocation: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.i("wjftc", "主线程 ${(frameMetrics?.getMetric(FrameMetrics.UNKNOWN_DELAY_DURATION) ?: 0) / 1000000.0f} ms\n" + "帧耗时 ${(frameMetrics?.getMetric(FrameMetrics.TOTAL_DURATION) ?: 0) / 1000000.0f} ms\n" + "绘制耗时 ${((frameMetrics?.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION) ?: 0) + (frameMetrics?.getMetric(FrameMetrics.DRAW_DURATION) ?: 0)) / 1000000.0f} ms")
        }
    }
}
