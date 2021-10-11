package com.wjf.self_library.common

import android.view.View
import com.wjf.self_library.http.BaseSubscriber
import com.wjf.self_library.http.HttpResult
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * RxJava快捷订阅
 */
fun <T> Observable<HttpResult<T>>.toSubscribe(
    isLoading: Boolean = false,
    block: (HttpResult<T>) -> Unit
) {
    val subscriber = BaseSubscriber(isLoading, block)

    subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber)
}

/**
 * 点击事件监听订阅
 */
fun View.click(block: View.() -> Unit) {
    this.setOnClickListener(block)
}

inline fun repeat(rounds: Int, block: (index: Int) -> Unit) {
    if (rounds <= 0) {
        throw IllegalArgumentException("=== fun repeat does not allow rounds smaller than num one ===")
    }
    for (i in 0 until rounds) {
        block(i)
    }
}
