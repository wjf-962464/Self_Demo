package com.wjf.self_demo.activity

import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityHealthyCodeBinding
import com.wjf.self_library.common.BaseActivity
import org.jxxy.debug.corekit.util.singleClick
import java.text.SimpleDateFormat
import java.util.*

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
        val timer = MyCountDownTimer(TOTAL, INTERVAL, view)
        timer.start()

        view.submitBtn.singleClick {
            view.locationTopTv.text = view.inputTop.text
            view.locationBottomTv.text = view.inputBottom.text
            view.inputCard.visibility = View.GONE
        }
    }

    class MyCountDownTimer(
        millisUntilFinished: Long,
        millisUntilInterval: Long,
        val view: ActivityHealthyCodeBinding
    ) :
        CountDownTimer(millisUntilFinished, millisUntilInterval) {
        override fun onTick(millisUntilFinished: Long) {
            val curDate = Date(System.currentTimeMillis())
            val sbs2 = SpannableString(formatter.format(curDate))
            sbs2.setSpan(RelativeSizeSpan(1.3f), 17, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.timeDownTv.text = sbs2
        }

        override fun onFinish() {
        }
    }

    override fun setLayout(): Int = R.layout.activity_healthy_code

    override fun initData() {
    }
}
