package com.wjf.self_library.http

import android.util.Log
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.wjf.self_library.BuildConfig
import com.wjf.self_library.util.common.TimeUtils
import java.nio.charset.Charset
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer

/**
 * @author WJF
 */
class LogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        Log.d(BuildConfig.TAG, generateRequestLog(request))
        Logger.json(generateRequestLog(request))
        val response = chain.proceed(request)
        getResponseText(response)?.let {
            Logger.json(it)
        }
        return response
    }

    private fun generateRequestLog(request: Request): String {
        /*val requestParams = getRequestParams(request)
        val needPrintRequestParams = requestParams.contains("IsFile").not()
        val paramSting = if (needPrintRequestParams)
            requestParams
        else
            "文件上传，不打印请求参数"*/

        val result = HttpRequest(
            TimeUtils.millis2String(System.currentTimeMillis()),
            request.url().toString(),
            request.method(),
            getRequestParams(request)
        )
        return Gson().toJson(result)
    }

    /**
     * URL 解码
     *
     * @return String
     * @author lifq
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private fun getRequestHeaders(request: Request): String {
        val headers = request.headers()
        return headers.toString()
    }

    /**
     * 获取请求参数
     */
    private fun getRequestParams(request: Request): MutableMap<String, String> {
        var result: MutableMap<String, String> = mutableMapOf()
        var str = ""
        try {
            request.body()?.let {
                val buffer = Buffer()
                it.writeTo(buffer)
                val charset = it.contentType()?.charset(Charset.forName("UTF-8"))
                    ?: Charset.forName("UTF-8")
                str = buffer.readString(charset)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (str.isNotEmpty()) {
            val array = str.split("&")
            for (params in array) {
                val array2 = params.split("=")
                result.put(array2[0], array2[1])
            }
        }
        return result
    }

    /**
     * 获取返回数据字符串
     */
    private fun getResponseText(response: Response): String? {
        try {
            response.body()?.let {
                val source = it.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer()
                val charset = it.contentType()?.charset(Charset.forName("UTF-8"))
                    ?: Charset.forName("UTF-8")
                if (it.contentLength().toInt() != 0) {
                    buffer.clone().readString(charset).let { result ->
                        return result
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}
