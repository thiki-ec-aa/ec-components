package net.thiki.ec.component.common.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.URL
import java.net.URLConnection

/**
 * Http工具类。
 */
object HttpUtil {

    /**
     * 发送Get请求。
     */
    fun sendGet(url: String): String {
        // 打开和URL之间的连接。
        val conn = URL(url).openConnection()
        // 设置通用属性。
        conn.setRequestProperty("accept", "*/*")
        conn.setRequestProperty("connection", "Keep-Alive")
        conn.connect()

        return getResult(conn)
    }

    /**
     * 发送Post请求。
     */
    fun sendPost(url: String, params: Map<String, String>): String {
        // 打开和URL之间的连接。
        val conn = URL(url).openConnection()
        // 发送POST请求必须设置如下两行。
        conn.doOutput = true;
        conn.doInput = true;

        if (params.isNotEmpty()) {
            // 获取URLConnection对象对应的输出流
            val out = PrintWriter(conn.getOutputStream())
            // 发送请求参数
            out.print(jacksonObjectMapper().writeValueAsString(params));
            // flush输出流的缓冲
            out.flush();
        }

        return getResult(conn)
    }

    private fun getResult(conn: URLConnection): String {
        val result = StringBuilder()
        // 定义BufferedReader输入流来读取URL的响应。
        val reader = BufferedReader(InputStreamReader(conn.getInputStream()))
        do {
            val line = reader.readLine()
            line?.let { result.append(line) }
        } while (line != null)
        return result.toString()
    }

}