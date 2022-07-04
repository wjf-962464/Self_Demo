package org.jxxy.debug.http

interface LiveDataCallback<LiveData, Response> {
    fun success(emit: ResLiveData<LiveData>, msg: String?, data: Response?)
    fun otherCode(emit: ResLiveData<LiveData>, code: Int?, msg: String?, data: Response?)
    fun error(emit: ResLiveData<LiveData>, e: ErrorResponse)
}
