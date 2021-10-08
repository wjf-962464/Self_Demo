package com.wjf.self_demo.activity

import android.view.View
import com.wjf.loadLayout.callback.ICallback.CallOnListener
import com.wjf.loadLayout.core.LoadManager
import com.wjf.loadLayout.core.LoadService
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityLoadLayoutBinding
import com.wjf.self_demo.databinding.CallbackEmptyBinding
import com.wjf.self_demo.loadlayout.EmptyCallback
import com.wjf.self_demo.loadlayout.LoadingCallback
import com.wjf.self_library.common.BaseActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/** @author WJF
 */
class LoadLayoutActivity2 : BaseActivity<ActivityLoadLayoutBinding>() {
    private lateinit var loadService: LoadService
    override fun setLayout(): Int {
        return R.layout.activity_load_layout
    }

    override fun initView() {
        val singleThreadPool: ExecutorService = ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue(1024)
        )
        val manager = LoadManager.Builder()
            .addCallBack(
                EmptyCallback(
                    object : CallOnListener {
                        override fun statusResponse() {
                            loadService.showCallback(LoadingCallback::class.java)
                            singleThreadPool.execute {
                                try {
                                    Thread.sleep(1500L)
                                    loadService.showDefault()
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    })
            )
            .addCallBack(LoadingCallback())
            .build()
        loadService = manager.bind(this, lifecycle)
        loadService.setCallback(
            EmptyCallback::class.java
        ) { view: View ->
            val binding = CallbackEmptyBinding.bind(view)
            binding.callbackMsg.text = "这是改过的消息"
            binding.callbackTitle.text = "这是改过的标题"
        }
        loadService.showCallback(LoadingCallback::class.java)
        singleThreadPool.execute {
            try {
                Thread.sleep(1500L)
                loadService.showCallback(EmptyCallback::class.java)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun initData() {}
}
