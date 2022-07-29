package com.wjf.self_demo.activity

import android.view.MotionEvent
import com.orhanobut.logger.Logger
import com.wjf.self_demo.databinding.ActivityEventDispatchBinding
import org.jxxy.debug.corekit.common.BaseActivity

class EventDispatchActivity : BaseActivity<ActivityEventDispatchBinding>() {

    override fun bindLayout(): ActivityEventDispatchBinding {
        return ActivityEventDispatchBinding.inflate(layoutInflater)
    }

    override fun initView() {
    }

    override fun subscribeUi() {
    }
}
