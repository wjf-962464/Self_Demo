package com.wjf.self_demo.activity

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.provider.Settings
import com.orhanobut.logger.Logger
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityAccessibilityBinding
import com.wjf.self_demo.receiver.AdminReceiver
import com.wjf.self_demo.util.AccessibilityClockService
import com.wjf.self_library.common.BaseActivity
import com.wjf.self_library.common.click

class AccessibilityActivity : BaseActivity<ActivityAccessibilityBinding>() {
    val DPM_REQUEST_CODE = 0x002
    private val mComponentName: ComponentName by lazy {
        ComponentName(
            this,
            AdminReceiver::class.java
        )
    }
    private val devicePolicyManager: DevicePolicyManager by lazy {
        getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    override fun setLayout(): Int = R.layout.activity_accessibility

    override fun initView() {
        addPermission(Manifest.permission.DISABLE_KEYGUARD).addPermission(Manifest.permission.WAKE_LOCK)
        requestPermission()

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
            }
        }
    }

    override fun doSomethingAfterGranted() {
        requestDeviceAdmin()
    }

    private fun requestDeviceAdmin() {
        Logger.d("requestDeviceAdmin")
        if (devicePolicyManager.isAdminActive(mComponentName)) {
            Logger.d("防盗保护已经开启222")
        } else {
            // 激活设备管理器
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "提示文字")
            startActivityForResult(intent, DPM_REQUEST_CODE)
        }
    }

    override fun initData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DPM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Logger.d("防盗保护已经开启")
            } else {
                Logger.d("防盗保护没有开启")
            }
        }
    }
}
