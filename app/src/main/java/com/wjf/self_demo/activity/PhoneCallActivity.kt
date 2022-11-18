package com.wjf.self_demo.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import com.wjf.self_demo.databinding.ActivityPhoneCallBinding
import com.wjf.self_demo.entity.PhoneCallBean
import com.wjf.self_demo.util.AccessibilitySampleService
import com.wjf.self_demo.util.CallingStateListener
import com.wjf.self_demo.view.EditTextDialog
import org.jxxy.debug.corekit.common.BaseActivity
import org.jxxy.debug.corekit.util.singleClick

class PhoneCallActivity :
    BaseActivity<ActivityPhoneCallBinding>(),
    CallingStateListener.OnCallStateChangedListener {
    private var phoneCallListener: CallingStateListener? = null
    private var mHandler: Handler? = null
    private var mIntent: Intent? = null
    private var phoneCallBean: PhoneCallBean = PhoneCallBean(false, "01062300568")
    private var dialog: EditTextDialog? = null

    override fun initView() {
        mIntent = Intent(Intent.ACTION_CALL)
        phoneCallBean.phoneNumData.observe(
            this,
            object : Observer<String?> {
                override fun onChanged(t: String?) {
                    mIntent?.data = Uri.parse("tel:$t")
                }
            }
        )

        phoneCallListener = CallingStateListener(this)
        phoneCallListener?.setOnCallStateChangedListener(this)

        mHandler = Handler(Looper.getMainLooper())
        dialog =
            EditTextDialog().setSubmitListener { phoneCallBean.phoneNum = it }

        view.bean = phoneCallBean
        view.btn.singleClick {
            if (!phoneCallBean.isCallFlag) {
                startActivity(mIntent)
            }
            phoneCallBean.opposeFlag()
        }
        view.contentHint.singleClick {
            dialog?.show(supportFragmentManager, it.toString())
        }
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

    override fun doSomethingAfterGranted() {
        phoneCallListener?.startListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        phoneCallBean.isCallFlag = false
        mHandler?.removeCallbacksAndMessages(null)
    }

    override fun bindLayout(): ActivityPhoneCallBinding {
        return ActivityPhoneCallBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
        addPermission(Manifest.permission.CALL_PHONE).requestPermission()
        phoneCallBean.phoneNum = "01062300568"
    }
}
