package com.nerdscorner.mvp.domain

data class ExecutionResult(var successful: Boolean, var message: String? = null) {
    var chainedResults = mutableListOf<ExecutionResult>()

    fun getChainedMessages(): String {
        val chainedMessages = StringBuilder()
        chainedMessages.append(HTML_NEW_LINE)
        chainedResults.forEach {
            it.message?.let { message ->
                chainedMessages
                        .append(HTML_NEW_LINE)
                        .append(message)
            }
        }
        return chainedMessages.toString()
    }

    operator fun plus(other: ExecutionResult): ExecutionResult {
        val sumExecutionResult = ExecutionResult(successful && other.successful)
        sumExecutionResult.chainedResults.addAll(chainedResults)
        sumExecutionResult.chainedResults.add(other)
        return sumExecutionResult
    }

    companion object {
        val EMPTY = ExecutionResult(true)
        const val HTML_NEW_LINE = "<br/>"
    }
}