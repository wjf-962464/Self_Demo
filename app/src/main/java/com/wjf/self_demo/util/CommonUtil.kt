package com.wjf.self_demo.util

import android.view.View

fun View.click(block: View.() -> Unit) {
    this.setOnClickListener(block)
}
