package net.thiki.ec.component.common.utils

import net.thiki.ec.component.log.loggerFrom
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class DigestUtils {
    companion object {
        fun encode(text: String): String {
            try {
                //获取md5加密对象
                val instance: MessageDigest = MessageDigest.getInstance("MD5")
                //对字符串加密，返回字节数组
                val digest: ByteArray = instance.digest(text.toByteArray())
                val sb = StringBuffer()
                for (b in digest) {
                    //获取低八位有效值
                    val i: Int = b.toInt() and 0xff
                    //将整数转化为16进制
                    var hexString = Integer.toHexString(i)
                    if (hexString.length < 2) {
                        //如果是一位的话，补0
                        hexString = "0$hexString"
                    }
                    sb.append(hexString)
                }
                return sb.toString()

            } catch (e: NoSuchAlgorithmException) {
                loggerFrom(this).error("Error: ${e.message}", e)
            }

            return ""
        }

        private const val HEX_CHARS = "0123456789abcdef"

        /**
         * Algorithm	Supported API Levels
         * MD5          1+
         * SHA-1	    1+
         * SHA-224	    1-8,22+
         * SHA-256	    1+
         * SHA-384	    1+
         * SHA-512	    1+
         */
        fun hashString(type: String, input: String): String {
            val bytes = MessageDigest
                    .getInstance(type)
                    .digest(input.toByteArray(Charset.forName("UTF-8")))
            val result = StringBuilder(bytes.size * 2)

            bytes.forEach {
                val i = it.toInt()
                result.append(HEX_CHARS[i shr 4 and 0x0f])
                result.append(HEX_CHARS[i and 0x0f])
            }
            return result.toString()
        }
    }
}