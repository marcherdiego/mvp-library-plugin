package com.nerdscorner.mvp.utils

import com.nerdscorner.mvp.utils.gradle.GradleUtils
import com.squareup.okhttp.Call
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

object LibraryUtils {
    private var versionFetchCall: Call? = null
    private val client = OkHttpClient()

    fun fetchLatestVersions() {
        versionFetchCall?.cancel()
        val request = Request
                .Builder()
                .url("https://raw.githubusercontent.com/marcherdiego/android_mvp/develop/build.gradle")
                .get()
                .build()
        versionFetchCall = client.enqueue(
                request,
                success = {
                    val eventsVersion = extractVersion(it, "mvpLibraryVersion")
                    val coroutinesVersion = extractVersion(it, "coroutinesLibraryVersion")
                    GradleUtils.setLatestEventsLibVersion(eventsVersion)
                    GradleUtils.setLatestCoroutinesLibVersion(coroutinesVersion)
                },
                fail = {
                    System.err.println("Error while fetching latest versions: $it")
                }
        )
    }

    private fun extractVersion(text: String, propertyName: String): String? {
        val start = text.indexOf(propertyName)
        if (start == -1) {
            return null
        }
        val end = text.indexOf("\n", startIndex = start)
        if (end == -1) {
            return null
        }
        return text.substring(start + propertyName.length + 3, end).replace("\"", "")
    }
}
