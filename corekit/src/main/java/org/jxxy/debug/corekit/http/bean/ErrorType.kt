package org.jxxy.debug.corekit.http.bean

enum class ErrorType {
    NETWORK_ERROR, // 网络出错
    SERVICE_ERROR, // 服务器访问异常
    OTHER_ERROR, // 其它错误
    SPECIAL_CODE, // 特殊返回
}
