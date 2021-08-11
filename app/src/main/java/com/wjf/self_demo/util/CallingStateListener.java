package com.wjf.self_demo.util;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallingStateListener extends PhoneStateListener {

    private boolean isListening = false; //是否正在回调
    private int CALL_STATE = TelephonyManager.CALL_STATE_IDLE; //电话状态

    private TelephonyManager mTelephonyManager = null;
    /**
     * 回调
     */
    private OnCallStateChangedListener mOnCallStateChangedListener = null;

    /**
     * @param context 上下文
     */
    public CallingStateListener(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 启动监听
     *
     * @return
     */
    public boolean startListener() {
        if (isListening) {
            return false;
        }
        isListening = true;
        mTelephonyManager.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
        return true;
    }

    /**
     * 结束监听
     *
     * @return
     */
    public boolean stopListener() {
        if (!isListening) {
            return false;
        }
        isListening = false;
        mTelephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
        return true;
    }

    /**
     * @param state       状态
     * @param mobilePhone 手机号
     */
    @Override
    public void onCallStateChanged(int state, String mobilePhone) {
        Log.d("ZZM","当前state为"+state);
        switch (state) {
            //当前状态为挂断
            case TelephonyManager.CALL_STATE_IDLE:
                if (mOnCallStateChangedListener != null) {
                    mOnCallStateChangedListener.onCallStateChanged(OnCallStateChangedListener.STATE_IDLE, mobilePhone);
                }
                CALL_STATE = state;
                break;
            //当前状态为接听或拨打
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (mOnCallStateChangedListener != null) {
                    mOnCallStateChangedListener.onCallStateChanged(CALL_STATE == TelephonyManager.CALL_STATE_RINGING ?
                            OnCallStateChangedListener.STATE_IN : OnCallStateChangedListener.STATE_OUT, mobilePhone);
                }
                CALL_STATE = state;
                break;
            //当前状态为响铃
            case TelephonyManager.CALL_STATE_RINGING:
                if (mOnCallStateChangedListener != null) {
                    mOnCallStateChangedListener.onCallStateChanged(OnCallStateChangedListener.STATE_RINGING, mobilePhone);
                }
                CALL_STATE = state;
                break;
        }
    }


    /**
     * 监听回调
     *
     * @param onCallStateChangedListener OnCallStateChangedListener
     */
    public void setOnCallStateChangedListener(OnCallStateChangedListener onCallStateChangedListener) {
        this.mOnCallStateChangedListener = onCallStateChangedListener;
    }

    /**
     * 监听回调
     */
    public interface OnCallStateChangedListener {

        int STATE_IDLE = 0;//已挂断
        int STATE_IN = 1;//正在接听（已接通）
        int STATE_OUT = 2;//正在拨打（已接通或未接通）
        int STATE_RINGING = 3;//未接听，正在响铃

        /**
         * @param state  状态
         * @param number 手机号
         */
        void onCallStateChanged(int state, String number);
    }
}