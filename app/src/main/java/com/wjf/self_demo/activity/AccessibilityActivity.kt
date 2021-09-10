package com.wjf.self_demo.activity

import android.content.Intent
import android.provider.Settings
import com.orhanobut.logger.Logger
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityAccessibilityBinding
import com.wjf.self_demo.util.AccessibilityClockService
import com.wjf.self_library.common.BaseActivity
import com.wjf.self_library.common.click

class AccessibilityActivity : BaseActivity<ActivityAccessibilityBinding>() {

    override fun setLayout(): Int = R.layout.activity_accessibility

    override fun initView() {
        view.clockBtn.click {
            if (!AccessibilityClockService.isStart) {
                try {
                    Logger.d("not start")
                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                } catch (e: Exception) {
                    Logger.e("already start", e)
                    startActivity(Intent(Settings.ACTION_SETTINGS))
                    e.printStackTrace()
                }
            } else {
                Logger.d("已启动")
                AccessibilityClockService.mService?.clockProcess()
            }
        }
    }

    override fun initData() {
    }
}
