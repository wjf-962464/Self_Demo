package com.wjf.self_demo.activity

import com.wjf.self_demo.databinding.ActivityFishBinding
import com.wjf.self_demo.view.FishDrawable
import org.jxxy.debug.corekit.common.BaseActivity

/** @author WJF
 */
class FishActivity : BaseActivity<ActivityFishBinding>() {
    override fun initView() {
        view.fishImg.setImageDrawable(FishDrawable())
    }

    override fun doSomethingAfterGranted() {
        super.doSomethingAfterGranted()
    }

    override fun bindLayout(): ActivityFishBinding {
        return ActivityFishBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {}
}
