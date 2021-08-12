package com.wjf.self_demo.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.wjf.barcode.Logger
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityPhoneCallBinding
import com.wjf.self_demo.entity.PhoneCallBean
import com.wjf.self_demo.util.AccessibilitySampleService
import com.wjf.self_demo.util.CallingStateListener
import com.wjf.self_demo.util.click
import com.wjf.self_demo.view.EditTextDialog
import com.wjf.self_library.common.BaseActivity
import kotlinx.android.synthetic.main.activity_phone_call.*


class PhoneCallActivity : BaseActivity<ActivityPhoneCallBinding>(),
    CallingStateListener.OnCallStateChangedListener {
    private var phoneCallListener: CallingStateListener? = null
    private var mHandler: Handler? = null
    private var mIntent: Intent? = null
    private var phoneCallBean: PhoneCallBean = PhoneCallBean(false, "01062300568")
    private val PERMISSIONS_REQUEST: Int = 0x001
    private val permissionString = arrayOf(
        Manifest.permission.CALL_PHONE
    )
    private var dialog: EditTextDialog? = null

    override fun setLayout(): Int = R.layout.activity_phone_call

    override fun initView() {
        mIntent = Intent(Intent.ACTION_CALL)
        phoneCallBean.phoneNumData.observe(this,
            object : Observer<String?> {
                override fun onChanged(t: String?) {
                    mIntent?.data = Uri.parse("tel:$t")
                }
            })

        phoneCallListener = CallingStateListener(this)
        phoneCallListener?.setOnCallStateChangedListener(this)

        mHandler = Handler(Looper.getMainLooper())
        dialog = EditTextDialog.Builder(this).setListener {
            phoneCallBean.phoneNum = it
        }.width(WindowManager.LayoutParams.MATCH_PARENT)
            .height(WindowManager.LayoutParams.WRAP_CONTENT)
            .cancelOnTouch(true)
            .gravity(Gravity.BOTTOM)
            .build()

        view.bean = phoneCallBean
        btn.click {
            if (!phoneCallBean.isCallFlag) {
                startActivity(mIntent)
            }
            phoneCallBean.opposeFlag()
        }
        contentHint.click {
            dialog?.show()
        }
    }

    override fun initData() {
        requestPermission()
        phoneCallBean.phoneNum = "01062300568"
    }


    override fun onCallStateChanged(state: Int, number: String?) {
        when (state) {
            0 -> {
                AccessibilitySampleService.HAND_FREE = false
                if (phoneCallBean.isCallFlag) {
                    mHandler?.postDelayed({
                        if (phoneCallBean.isCallFlag) {
                            startActivity(mIntent)
                        }
                    }, 5 * 1000L)
                }

                Logger.d("STATE_IDLE")
            }
            2 -> {
                Logger.d("STATE_OUT")
            }
            1 -> {
                Logger.d("STATE_IN")
            }
            3 -> {
                Logger.d("STATE_RINGING")
            }
        }
    }

    private fun requestPermission() {
        Logger.d("requestPermission")
        if (checkPermissionAllGranted(permissionString)) {
            phoneCallListener?.startListener()
            Logger.d("onRequestPermissionsResult granted")
        } else {
            ActivityCompat.requestPermissions(
                this,
                permissionString, PERMISSIONS_REQUEST
            );
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private fun checkPermissionAllGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Logger.e("error $permission 没有授权")
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                var isAllGranted = true
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        isAllGranted = false
                    }
                }
                if (isAllGranted) {
                    Logger.d("onRequestPermissionsResult granted")
                    phoneCallListener?.startListener()
                } else {
                    Logger.d("onRequestPermissionsResult denied")
                    showWaringDialog()
                }
                return
            }
        }
    }

    private fun showWaringDialog() {
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("警告！")
            .setMessage("请前往设置->应用->PermissionDemo->权限中打开相关权限，否则功能无法正常运行！")
            .setPositiveButton(
                "确定"
            ) { dialog, which -> // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                finish()
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        phoneCallBean.isCallFlag = false
        mHandler?.removeCallbacksAndMessages(null)
    }
}