package com.wjf.self_demo.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.wjf.self_demo.BR;

/**
 * @author WJF
 */
public class PhoneCallBean extends BaseObservable {
    private boolean callFlag;
    private MutableLiveData<String> phoneNum;


    public PhoneCallBean(boolean callFlag, String phoneNum) {
        this.callFlag = callFlag;
        this.phoneNum = new MutableLiveData<>(phoneNum);
    }

    @Bindable
    public boolean isCallFlag() {
        return callFlag;
    }

    public void setCallFlag(boolean callFlag) {
        this.callFlag = callFlag;
        notifyPropertyChanged(BR.callFlag);
    }

    public void opposeFlag(){
        this.callFlag=!callFlag;
        notifyPropertyChanged(BR.callFlag);
    }

    @Bindable
    public String getPhoneNum() {
        return phoneNum.getValue();
    }

    public MutableLiveData<String> getPhoneNumData() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum.setValue(phoneNum);
        notifyPropertyChanged(BR.phoneNum);
    }
}
