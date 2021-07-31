package net.thiki.ec.component.common.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtils {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"))

    fun epochMilliToString(milli: Long): String {
        return Instant.ofEpochMilli(milli).toString()
    }

    fun parse(dateStr: String?): Long {
        return if (dateStr.isNullOrBlank()) -1L else formatter.parse(dateStr, Instant::from).toEpochMilli()
    }

    fun format(millis: Long): String {
        return if (millis > 0) formatter.format(Instant.ofEpochMilli(millis)) else ""
    }

}

