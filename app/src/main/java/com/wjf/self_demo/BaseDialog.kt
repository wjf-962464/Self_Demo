package com.wjf.self_demo

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface

open class BaseDialog : Dialog {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}
    protected constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)


}