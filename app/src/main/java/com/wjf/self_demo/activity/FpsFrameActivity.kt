package com.wjf.self_demo.activity

import android.os.Handler
import android.os.Looper
import com.orhanobut.logger.Logger
import com.wjf.self_demo.databinding.ActivityFpsFrameBinding
import org.jxxy.debug.corekit.common.BaseActivity

class FpsFrameActivity :
    BaseActivity<ActivityFpsFrameBinding>() {
    override fun initView() {
    }

    override fun bindLayout(): ActivityFpsFrameBinding {
        return ActivityFpsFrameBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
        var handler: Handler? = null
        Looper.myLooper()?.let {
            handler = Handler(it)
        }
        handler?.let {
            it.post {
                Logger.d("测试代码")
            }
            it.postDelayed({
                Logger.d("开始了")
                var count = 0
                for (i in 0..Int.MAX_VALUE) {
                    count++
                }
                Logger.d("结束了")
            }, 3000L)
        }
    }
}
