package net.thiki.ec.component.exception

import net.thiki.ec.component.exception.AssertionException.Companion.SystemParams_Key_HttpStatus

open class AssertionException @JvmOverloads constructor(
    /** message of ex */
    message: String,
    /** cause */
    cause: Throwable? = null,
    /** internal code */
    val code: Int = CodeUnknown,
    @Suppress("MemberVisibilityCanBePrivate")
    val showStackTrace: Boolean = false,

    /** parameters for system level if needed, e.g. http-status-code */
    val systemParams: MutableMap<String, String> = mutableMapOf(),
    /** parameters for biz level if needed */
    val bizParams: MutableMap<String, String> = mutableMapOf(),
    private val useMap: Boolean = false
) : RuntimeException(cause) {

    /**
     * default http-status is 500
     *
     * use httpStatus(status) to specify http-status if not 500
     */
    @JvmOverloads
    constructor(
        /** internal code */
        code: Int,
        /** message of ex */
        message: String,
        /** parameters for biz level if needed */
        bizParams: MutableMap<String, String> = mutableMapOf()
    ): this(message, null, code, false, mutableMapOf(), bizParams, false){
        systemParams[SystemParams_Key_HttpStatus] = "500"
    }

    override var message: String = message
        get() {
//            val fill1 = StringFormatter.format(field, systemParams)
            // only fill bizParams into the message.
            val fill2 = if (useMap){
                StringFormatter.format(field,  bizParams)
            }else{
                StringFormatter.arrayFormat(field,  bizParams.values.toTypedArray())
            }
            if (showStackTrace){
                return fill2 + "\n" + getThrowsLine()
            }else{
                return fill2
            }

        }

    /**
     * Specify the http status.
     */
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

/**
 * bad request error: http-status = 400
 */
@SafeVarargs
fun badRequestError(code: Int, msg: String, vararg params: Pair<String, String>): Nothing {
    throw AssertionException(msg, null, code, false).also {
        it.systemParams[SystemParams_Key_HttpStatus] = "400"
        it.bizParams.putAll(params)
    }
}


/**
 * not found error: http-status = 404
 */
@SafeVarargs
fun notFoundError(code: Int, msg: String, vararg params: Pair<String, String>): Nothing {
    throw AssertionException(msg, null, code, false).also {
        it.systemParams[SystemParams_Key_HttpStatus] = "404"
        it.bizParams.putAll(params)
    }
}

/**
 * unexpected error: http-status = 500
 */
fun unexpectedError(msg: String): Nothing = throw AssertionException(msg)

@JvmOverloads
fun unexpectedError(code: Int, msg: String, bizParams: MutableMap<String, String> = mutableMapOf()): Nothing = throw AssertionException(code, msg, bizParams)



fun unexpectedError(msg: String, block: (AssertionException) -> Unit): Nothing = throw AssertionException(msg).apply {
    block(this)
}

fun unexpectedErrorWithHttpStatus(httpStatusCode: Int, msg: String): Nothing = unexpectedError(msg) {
    it.systemParams[SystemParams_Key_HttpStatus] = httpStatusCode.toString()
}

fun unexpectedErrorWithHttpStatus(httpStatusCode: Int, code: Int, msg: String): Nothing =
    throw AssertionException(msg, null, code, false).also {
        it.systemParams[SystemParams_Key_HttpStatus] = httpStatusCode.toString()
    }

@JvmOverloads
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