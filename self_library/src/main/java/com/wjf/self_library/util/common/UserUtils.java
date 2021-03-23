package com.wjf.self_library.util.common;

import android.os.UserHandle;

import com.wjf.self_library.util.log.LogHandler;

public class UserUtils {

    private static int userId = -1;

    public static int getUserId() {

        if (userId == -1) {
            try {
                UserHandle userHandle = android.os.Process.myUserHandle();
                String userStr = userHandle.toString();
                userId = Integer.parseInt(userStr.replace("UserHandle{", "").replace("}", ""));
            } catch (Exception e) {
                LogHandler.e("UserUtils", e);
            }
        }
        return userId;
    }
}
