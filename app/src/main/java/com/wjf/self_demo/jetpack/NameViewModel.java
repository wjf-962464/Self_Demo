package com.wjf.self_demo.jetpack;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author : Wangjf
 * @date : 2021-06-14
 */
public class NameViewModel extends ViewModel {
    private MutableLiveData<String> name;

    public MutableLiveData<String> getCurrentName() {
        if (name == null) {
            name = new MutableLiveData<>("1");
        }
        return name;
    }
}
