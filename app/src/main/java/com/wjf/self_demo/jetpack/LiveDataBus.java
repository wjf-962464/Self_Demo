package com.wjf.self_demo.jetpack;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

/** @author asus */
public class LiveDataBus {
    private static volatile LiveDataBus instance;
    private final Map<String, MutableLiveData> map;

    private LiveDataBus() {
        map = new HashMap<>();
    }

    public static LiveDataBus getInstance() {
        if (instance == null) {
            synchronized (LiveDataBus.class) {
                if (instance == null) {
                    instance = new LiveDataBus();
                }
            }
        }
        return instance;
    }

    public <T> MutableLiveData<T> with(String key, Class<T> tClass) {
        if (!map.containsKey(key)) {
            map.put(key, new MutableLiveData());
        }
        return (MutableLiveData<T>) map.get(key);
    }
}
