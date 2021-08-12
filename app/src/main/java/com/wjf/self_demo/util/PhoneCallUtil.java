package com.wjf.self_demo.util;

import android.media.AudioManager;

import com.wjf.barcode.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PhoneCallUtil {
    public static void openVoice(AudioManager audioManager) {
        if (audioManager.getMode() != AudioManager.MODE_IN_COMMUNICATION) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        }
        try {
            Class clazz = Class.forName("android.media.AudioSystem");
            Method m = clazz.getMethod("setForceUse", int.class, int.class);
            m.setAccessible(true);
            m.invoke(null, 1, 1);
            Logger.d("免提hook111");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (!audioManager.isSpeakerphoneOn()) {
            audioManager.setSpeakerphoneOn(true);
        }
        Logger.d("免提hook222");
    }
}