package org.jxxy.debug.corekit.http.bean

class ErrorResponse(
    val errorType: ErrorType, // 错误类型
    val errorCode: Int, // 错误代码
    override val message: String?, // 错误信息
    val data: Any? = null
) : Exception(message) {
    companion object {
        private const val NONE_CODE = 666
        private const val NULL_DATA = 555
        private const val ANALYSIS_ERROR = 777
        private const val ANALYSIS_MSG = "BaseResp层gson解析错误"
        fun otherCode(
            code: Int? = NULL_DATA,
            msg: String?
        ): ErrorResponse {
            return ErrorResponse(ErrorType.SPECIAL_CODE, code ?: NONE_CODE, msg)
        }

        fun analysisError(): ErrorResponse {
            return ErrorResponse(ErrorType.SPECIAL_CODE, ANALYSIS_ERROR, ANALYSIS_MSG)
        }
    }
}
