package net.thiki.ec.component.exception

import java.util.*

/**
 */
object StringFormatter {

    internal const val DELIM_START = '{'
    internal val DELIM_STOP = '}'
    internal val DELIM_STR = "{}"
    internal val ESCAPE_CHAR = '\\'


    fun format(messagePattern: String, vararg args: Any): String {
        return arrayFormat(messagePattern, args)
    }

//    fun format(messagePattern: String, arg: Any): String {
//        return arrayFormat(messagePattern, arrayOf(arg))
//    }
//
//    fun format(messagePattern: String, arg1: Any, arg2: Any): String {
//        return arrayFormat(messagePattern, arrayOf(arg1, arg2))
//    }

    internal fun getThrowableCandidate(argArray: Array<Any>?): Throwable? {
        if (argArray == null || argArray.size == 0) {
            return null
        }

        val lastEntry = argArray[argArray.size - 1]
        return lastEntry as? Throwable
    }

    /**
     * Same principle as the [.format] and
     * [.format] methods except that any number of
     * arguments can be passed in an array.
     *
     * @param messagePattern
     * The message pattern which will be parsed and formatted
     * @param argArray
     * An array of arguments to be substituted in place of formatting
     * anchors
     * @return The formatted message
     */
    fun arrayFormat(
        messagePattern: String,
        argArray: Array<out Any>
    ): String {

//        val throwableCandidate = getThrowableCandidate(argArray)

        if (argArray == null) {
            return messagePattern
        }

        var i = 0
        var j: Int
        val sbuf = StringBuffer(messagePattern.length + 50)

        var L: Int
        L = 0
        while (L < argArray.size) {

            j = messagePattern.indexOf(DELIM_STR, i)

            if (j == -1) {
                // no more variables
                if (i == 0) { // this is a simple string
                    return messagePattern
                } else { // add the tail string which contains no variables and return
                    // the result.
                    sbuf.append(messagePattern.substring(i, messagePattern.length))
                    return sbuf.toString()
                }
            } else {
                if (isEscapedDelimiter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        L-- // DELIM_START was escaped, thus should not be incremented
                        sbuf.append(messagePattern.substring(i, j - 1))
                        sbuf.append(DELIM_START)
                        i = j + 1
                    } else {
                        // The escape character preceding the delimiter start is
                        // itself escaped: "abc x:\\{}"
                        // we have to consume one backward slash
                        sbuf.append(messagePattern.substring(i, j - 1))
                        deeplyAppendParameter(sbuf, argArray[L], HashMap())
                        i = j + 2
                    }
                } else {
                    // normal case
                    sbuf.append(messagePattern.substring(i, j))
                    deeplyAppendParameter(sbuf, argArray[L], HashMap())
                    i = j + 2
                }
            }
            L++
        }
        // append the characters following the last {} pair.
        sbuf.append(messagePattern.substring(i, messagePattern.length))
        return sbuf.toString()
    }

    internal fun isEscapedDelimiter(messagePattern: String, delimiterStartIndex: Int): Boolean {

        if (delimiterStartIndex == 0) {
            return false
        }
        val potentialEscape = messagePattern[delimiterStartIndex - 1]
        return potentialEscape == ESCAPE_CHAR
    }

    internal fun isDoubleEscaped(messagePattern: String, delimeterStartIndex: Int): Boolean {
        return delimeterStartIndex >= 2 && messagePattern[delimeterStartIndex - 2] == ESCAPE_CHAR
    }

    // special treatment of array values was suggested by 'lizongbo'
    private fun deeplyAppendParameter(sbuf: StringBuffer, o: Any?, seenMap: MutableMap<Any, Any?>) {
        if (o == null) {
            sbuf.append("null")
            return
        }
        if (!o.javaClass.isArray) {
            safeObjectAppend(sbuf, o)
        } else {
            // check for primitive array types because they
            // unfortunately cannot be cast to Object[]
            if (o is BooleanArray) {
                booleanArrayAppend(sbuf, (o as BooleanArray?)!!)
            } else if (o is ByteArray) {
                byteArrayAppend(sbuf, (o as ByteArray?)!!)
            } else if (o is CharArray) {
                charArrayAppend(sbuf, (o as CharArray?)!!)
            } else if (o is ShortArray) {
                shortArrayAppend(sbuf, (o as ShortArray?)!!)
            } else if (o is IntArray) {
                intArrayAppend(sbuf, (o as IntArray?)!!)
            } else if (o is LongArray) {
                longArrayAppend(sbuf, (o as LongArray?)!!)
            } else if (o is FloatArray) {
                floatArrayAppend(sbuf, (o as FloatArray?)!!)
            } else if (o is DoubleArray) {
                doubleArrayAppend(sbuf, (o as DoubleArray?)!!)
            } else if (o != null) {
                objectArrayAppend(sbuf, o as Array<Any>, seenMap)
            }
        }
    }

    private fun safeObjectAppend(sbuf: StringBuffer, o: Any) {
        try {
            val oAsString = o.toString()
            sbuf.append(oAsString)
        } catch (t: Throwable) {
//            loggerFrom(this).error("SLF4J: Failed toString() invocation on an object of type [${o.javaClass}]", t)
            sbuf.append("[FAILED toString()]")
        }
    }

    private fun objectArrayAppend(sbuf: StringBuffer, a: Array<Any>, seenMap: MutableMap<Any, Any?>) {
        sbuf.append('[')

//        val testMap: MutableMap<Any?, Any?> = HashMap<Any?, Any?>()
//        testMap[a] = null

        if (!seenMap.containsKey(a)) {

            seenMap[a] = null
            val len = a.size
            for (i in 0 until len) {
                deeplyAppendParameter(sbuf, a[i], seenMap)
                if (i != len - 1)
                    sbuf.append(", ")
            }
            // allow repeats in siblings
            seenMap.remove(a)
        } else {
            sbuf.append("...")
        }
        sbuf.append(']')
    }

    private fun booleanArrayAppend(sbuf: StringBuffer, a: BooleanArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1)
                sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun byteArrayAppend(sbuf: StringBuffer, a: ByteArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i].toInt())
            if (i != len - 1)
                sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun charArrayAppend(sbuf: StringBuffer, a: CharArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1)
                sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun shortArrayAppend(sbuf: StringBuffer, a: ShortArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i].toInt())
            if (i != len - 1)
                sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun intArrayAppend(sbuf: StringBuffer, a: IntArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1)
                sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun longArrayAppend(sbuf: StringBuffer, a: LongArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1)
                sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun floatArrayAppend(sbuf: StringBuffer, a: FloatArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1)
                sbuf.append(", ")
        }
        sbuf.append(']')
    }

    private fun doubleArrayAppend(sbuf: StringBuffer, a: DoubleArray) {
        sbuf.append('[')
        val len = a.size
        for (i in 0 until len) {
            sbuf.append(a[i])
            if (i != len - 1)
                sbuf.append(", ")
        }
        sbuf.append(']')
    }
}
