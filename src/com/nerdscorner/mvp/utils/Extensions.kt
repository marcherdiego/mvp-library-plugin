package com.nerdscorner.mvp.utils

fun Any.ifAny(a: Boolean, b: Boolean, block: (Boolean, Boolean) -> Unit): Unit? {
    return if (a || b) {
        block(a, b)
    } else {
        null
    }
}
