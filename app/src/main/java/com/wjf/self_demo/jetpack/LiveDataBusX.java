package com.wjf.self_demo.jetpack;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.wjf.self_demo.BuildConfig;
import com.wjf.self_library.util.log.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/** @author asus */
public class LiveDataBusX {
    private static volatile LiveDataBusX instance;
    private final Map<String, BusMutableLiveData> map;

    private LiveDataBusX() {
        map = new HashMap<>();
    }

    public static LiveDataBusX getInstance() {
        if (instance == null) {
            synchronized (LiveDataBusX.class) {
                if (instance == null) {
                    instance = new LiveDataBusX();
                }
            }
        }
        return instance;
    }

    public <T> BusMutableLiveData<T> with(String key, Class<T> tClass) {
        if (!map.containsKey(key)) {
            map.put(key, new BusMutableLiveData());
        }
        return (BusMutableLiveData<T>) map.get(key);
    }

    public static class BusMutableLiveData<T> extends MutableLiveData<T> {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
            super.observe(owner, observer);
            hook(observer);
        }

        private void hook(Observer<? super T> observer) {
            try {
                Class<LiveData> liveDataClass = LiveData.class;
                Field mObserverField = liveDataClass.getDeclaredField("mObservers");
                mObserverField.setAccessible(true);

                // mObservers对象
                Object mObserversObject = mObserverField.get(this);
                // mObservers类
                Class<?> mObserversClass = mObserversObject.getClass();

                Method get = mObserversClass.getDeclaredMethod("get", Object.class);
                get.setAccessible(true);
                // 获取entry对象
                Object invokeEntry = get.invoke(mObserversObject, observer);

                Object observerWrapper = null;
                if (invokeEntry != null && invokeEntry instanceof Map.Entry) {
                    // 获取observerWrapper对象
                    observerWrapper = ((Map.Entry<?, ?>) invokeEntry).getValue();
                }
                if (observerWrapper == null) {
                    throw new NullPointerException("observerWrapper is null");
                }

                Class<?> superClass = observerWrapper.getClass().getSuperclass();
                Field mLastVersion = superClass.getDeclaredField("mLastVersion");
                mLastVersion.setAccessible(true);
                Field mVersion = liveDataClass.getDeclaredField("mVersion");
                mVersion.setAccessible(true);
                // 根据类对象反射执行，需要两个object，一是需要执行对象object，二是参数对象object
                mLastVersion.set(observerWrapper, mVersion.get(this));

            } catch (Exception e) {
                LogUtil.e(BuildConfig.TAG, this.getClass(), "hook: ", e);
            }
        }
    }
}
