package com.wjf.self_demo.activity

import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityHealthyCodeBinding
import com.wjf.self_library.common.BaseActivity
import org.jxxy.debug.corekit.util.ScheduleTask
import org.jxxy.debug.corekit.util.singleClick
import org.jxxy.debug.corekit.util.startActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HealthyCodeActivity : BaseActivity<ActivityHealthyCodeBinding>() {

    companion object {
        const val TOTAL = 1000L * 60 * 60L
        const val INTERVAL = 1000L
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm: ss")
    }

    override fun initView() {
        val curDate = Date(System.currentTimeMillis())
        val sbs2 = SpannableString(formatter.format(curDate))
        sbs2.setSpan(RelativeSizeSpan(1.3f), 17, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.timeDownTv.text = sbs2

        view.submitBtn.singleClick {
            if (view.inputTop.text.toString().isNullOrEmpty()) {
                view.locationTopTv.text = "西郊九韵城71"
            } else {
                view.locationTopTv.text = view.inputTop.text
            }
            if (view.inputBottom.text.toString().isNullOrEmpty()) {
                view.locationBottomTv.text = "上海市/闵行区/华漕镇/繁兴路300弄"
            } else {
                view.locationBottomTv.text = view.inputBottom.text
            }
            view.inputCard.visibility = View.GONE
        }
        view.healthyCodeImg.singleClick {
            startActivity<FishActivity>()
        }
    }

    override fun setLayout(): Int = R.layout.activity_healthy_code

    override fun initData() {
        ScheduleTask(lifecycle, 1, TimeUnit.SECONDS) {
            val curDate = Date(System.currentTimeMillis())
            val sbs2 = SpannableString(formatter.format(curDate))
            sbs2.setSpan(RelativeSizeSpan(1.3f), 17, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.timeDownTv.text = sbs2
        }.startTask()
    }
}
