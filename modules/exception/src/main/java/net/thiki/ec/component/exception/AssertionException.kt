package net.thiki.ec.component.exception

import net.thiki.ec.component.exception.AssertionException.Companion.SystemParams_Key_HttpStatus

open class AssertionException(
    /** message of ex */
    message: String,
    /** cause */
    cause: Throwable? = null,
    /** internal code */
    val code: Int = 0,
    @Suppress("MemberVisibilityCanBePrivate")
    val showStackTrace: Boolean = false,

    /** parameters for system level if needed, e.g. http-status-code */
    val systemParams: MutableMap<String, String> = mutableMapOf(),
    /** parameters for biz level if needed */
    val bizParams: MutableMap<String, String> = mutableMapOf()
) : RuntimeException(cause) {

    constructor(message: String): this(CodeUnknown, message)

    constructor(
        /** internal code */
        code: Int = 0,
        /** message of ex */
        message: String
    ) : this(message, null, code)

    override var message: String = message
        get() {
            val fill1 = StringFormatter.format(field, systemParams)
            val fill2 = StringFormatter.format(field, bizParams)
            if (showStackTrace){
                return fill2 + "\n" + getThrowsLine()
            }else{
                return fill2
            }

        }

    fun httpStatus(status: Int): AssertionException {
        systemParams[SystemParams_Key_HttpStatus] = status.toString()
        return this
    }

    private fun getThrowsLine(): String {
        val element = this.stackTrace[0]
        return "\tat $element"
    }

    companion object {
        const val SystemParams_Key_HttpStatus = "http-status"
        const val CodeUnknown: Int = -999
    }

}

@SafeVarargs
fun badRequestError(code: Int, msg: String, vararg params: Pair<String, String>): Nothing {
    throw AssertionException(msg, null, code, false).also {
        it.systemParams[SystemParams_Key_HttpStatus] = "400"
        it.bizParams.putAll(params)
    }
}


@SafeVarargs
fun notFoundError(code: Int, msg: String, vararg params: Pair<String, String>): Nothing {
    throw AssertionException(msg, null, code, false).also {
        it.systemParams[SystemParams_Key_HttpStatus] = "404"
        it.bizParams.putAll(params)
    }
}

fun unexpectedError(msg: String): Nothing = throw AssertionException(msg)

fun unexpectedError(msg: String, block: (AssertionException) -> Unit): Nothing = throw AssertionException(msg).apply {
    block(this)
}

fun unexpectedError(httpStatusCode: Int, msg: String): Nothing = unexpectedError(msg) {
    it.systemParams[SystemParams_Key_HttpStatus] = httpStatusCode.toString()
}

fun unexpectedError(httpStatusCode: Int, code: Int, msg: String): Nothing =
    throw AssertionException(msg, null, code, false).also {
        it.systemParams[SystemParams_Key_HttpStatus] = httpStatusCode.toString()
    }

fun assertThat(shouldBeTrue: Boolean, failMsg: String = "The assertion failed.") {
    if (!shouldBeTrue) {
        unexpectedError(failMsg)
    }
}

fun assertThat(failMsg: String = "The assertion failed.", block: (() -> Boolean)) {
    if (!block()) {
        unexpectedError(failMsg)
    }
}