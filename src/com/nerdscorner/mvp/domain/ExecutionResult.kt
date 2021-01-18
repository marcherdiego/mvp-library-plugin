package com.nerdscorner.mvp.domain

data class ExecutionResult(var successful: Boolean, var message: String? = null) {
    var chainedResults = mutableListOf<ExecutionResult>()

    fun getChainedMessages(): String {
        return StringBuilder().apply {
            append(HTML_NEW_LINE)
            chainedResults.forEach {
                it.message?.let { message ->
                    append(HTML_NEW_LINE)
                    append(message)
                }
            }
        }.toString()
    }

    operator fun plus(other: ExecutionResult): ExecutionResult {
        return ExecutionResult(successful && other.successful).apply {
            chainedResults.addAll(chainedResults)
            chainedResults.add(other)
        }
    }

    companion object {
        val EMPTY = ExecutionResult(true)
        const val HTML_NEW_LINE = "<br/>"
    }
}