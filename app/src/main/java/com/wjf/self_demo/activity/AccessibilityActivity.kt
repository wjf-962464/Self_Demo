package com.wjf.self_demo.activity

import android.content.Intent
import android.provider.Settings
import com.wjf.barcode.Logger
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityAccessibilityBinding
import com.wjf.self_demo.util.AccessibilitySampleService
import com.wjf.self_library.common.BaseActivity

class AccessibilityActivity : BaseActivity<ActivityAccessibilityBinding>() {

    override fun setLayout(): Int = R.layout.activity_accessibility

    override fun initView() {
        if (!AccessibilitySampleService.isStart) {
            try {
                Logger.d("not start")
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            } catch (e: Exception) {
                Logger.d("already start")
                startActivity(Intent(Settings.ACTION_SETTINGS))
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (AccessibilitySampleService.isStart){
            startActivity(Intent(this,PhoneCallActivity::class.java))
            finish()
        }
    }

    override fun initData() {

    }
}