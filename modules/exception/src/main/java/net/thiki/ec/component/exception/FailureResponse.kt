package net.thiki.ec.component.exception

/**
 * The Response Body for exceptions or failures.
 */
class FailureResponseBody<T>(
        val code: String = "",
        val msg: String = "",
        data: T
): ResponseBody<T>(data)


open class ResponseBody<T>(
        val data: T
) {
}