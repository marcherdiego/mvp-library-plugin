package com.nerdscorner.mvp.utils

import com.squareup.okhttp.Call
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException

fun OkHttpClient.enqueue(request: Request, success: (String) -> Unit = {}, fail: (String?) -> Unit = {}): Call {
    return with(newCall(request)) {
        enqueue(object : Callback {
            override fun onResponse(response: Response?) {
                if (response?.isSuccessful == true) {
                    success(response.body().string())
                } else {
                    fail("${response?.code()} - ${response?.message()}")
                }
            }

            override fun onFailure(request: Request?, exception: IOException?) {
                if (isCanceled.not()) {
                    fail(exception?.message)
                }
            }
        })
        this
    }
}

fun Any.ifAny(a: Boolean, b: Boolean, block: (Boolean, Boolean) -> Unit): Unit? {
    return if (a || b) {
        block(a, b)
    } else {
        null
    }
}
