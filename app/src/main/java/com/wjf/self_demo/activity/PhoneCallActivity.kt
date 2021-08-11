package com.wjf.self_demo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityPhoneCallBinding
import com.wjf.self_demo.util.CallingStateListener
import com.wjf.self_demo.util.PhoneCallUtil
import com.wjf.self_demo.util.click
import com.wjf.self_library.common.BaseActivity
import kotlinx.android.synthetic.main.activity_phone_call.*
import kotlinx.android.synthetic.main.activity_phone_call.view.*


class PhoneCallActivity : BaseActivity<ActivityPhoneCallBinding>(),
    CallingStateListener.OnCallStateChangedListener {
    private var phoneCallListener: CallingStateListener? = null
    private var mHandler: Handler? = null
    private var mIntent: Intent? = null
    private var stopFlag: Boolean = true
    private var audioManager: AudioManager? = null
    private var currVolume: Int? = null

    override fun setLayout(): Int = R.layout.activity_phone_call

    override fun initView() {
        var phoneNum = "01062300568"
//        phoneNum = "17374131093"
        mIntent = Intent(Intent.ACTION_CALL)
        mIntent?.data = Uri.parse("tel:$phoneNum")

        phoneCallListener = CallingStateListener(this)
        phoneCallListener?.setOnCallStateChangedListener(this)
        phoneCallListener?.startListener()

        audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        audioManager?.mode = AudioManager.MODE_IN_CALL
        currVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)

        mHandler = Handler(Looper.getMainLooper())

        stopFlag = true
        btn.text = "停止连续呼叫"
        btn.click {
            if (!stopFlag) {
                btn.text = "停止连续呼叫"
                startActivity(mIntent)
            } else {
                btn.text = "开始连续呼叫"
            }
            stopFlag = !stopFlag
        }
    }

    override fun initData() {}

    override fun onCallStateChanged(state: Int, number: String?) {
        when (state) {
            0 -> {
                if (stopFlag) {
                    mHandler?.postDelayed({
                        if (stopFlag) {
                            startActivity(mIntent)
                        }
                    }, 2 * 1000)
                }

                Log.d("ZZM", "STATE_IDLE")
            }
            2 -> {
                Log.d("ZZM", "STATE_OUT")
                openSpeaker()
            }
            1 -> {
                Log.d("ZZM", "STATE_IN")
            }
            3 -> {
                Log.d("ZZM", "STATE_RINGING")
            }
        }
    }

    @SuppressLint("WrongConstant")
    fun openSpeaker() {
        try {
            if (!audioManager?.isSpeakerphoneOn!!) {
                audioManager?.isSpeakerphoneOn = true
                currVolume?.let {
                    audioManager?.setStreamVolume(
                        AudioManager.STREAM_VOICE_CALL,
                        it,
                        AudioManager.STREAM_VOICE_CALL
                    )
                }
                Log.d("ZZM", "开启免提")
                PhoneCallUtil.openVoice(audioManager)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ZZM", "异常免提")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopFlag = false
        mHandler?.removeCallbacksAndMessages(null)
    }
}