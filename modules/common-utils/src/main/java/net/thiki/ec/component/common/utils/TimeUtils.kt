package net.thiki.ec.component.common.utils

import net.thiki.ec.component.exception.AssertionException
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.*

class TimeDuration( val begin: Instant, val end: Instant )

class TimeUtils(
    var DefaultZoneId: ZoneId = ZoneId.of("Asia/Shanghai")
) {

    private val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(DefaultZoneId)
    private val df = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(DefaultZoneId)

    fun epochMilliToString(milli: Long): String {
        return Instant.ofEpochMilli(milli).toString()
    }

    /**
     * 给time加上delta的时间，以unit为单位，返回结果时间
     * @param time
     * @param delta
     * @param unit
     * @return
     */
    fun plus(time: Date, delta: Long, unit: TemporalUnit): Date {
        return Date(time.toInstant().plus(delta, unit).toEpochMilli())
    }

    fun plus(time: Instant, delta: Long, unit: TemporalUnit): Instant {
        return time.plus(delta, unit)
    }

//    public static Instant _parse(String dateTimeStr) {
//        return LocalDateTime.parse(dateTimeStr, dtf)
//                .atZone(defaultZoneId)
//                .toInstant();
//    }

    //    public static Instant _parse(String dateTimeStr) {
    //        return LocalDateTime.parse(dateTimeStr, dtf)
    //                .atZone(defaultZoneId)
    //                .toInstant();
    //    }
    /**
     * parse string to Instant at default zone.
     * @param dateTimeStr
     * @return
     */
    fun parse(dateTimeStr: String): Instant {
        assert(dateTimeStr.isNotEmpty())
        return LocalDateTime.parse(dateTimeStr, dtf)
            .atZone(DefaultZoneId)
            .toInstant()
    }

    fun parseDate(dateTimeStr: String): Instant {
        return LocalDateTime.parse(dateTimeStr, dtf)
            .atZone(DefaultZoneId)
            .toInstant()
    }

    fun format(instant: Instant): String {
        val dateTime = instant.atZone(DefaultZoneId)
        return dateTime.format(dtf)
    }

    fun format(epochSeconds: Long): String {
        return format(Instant.ofEpochSecond(epochSeconds))
    }


    /**
     * 将 epochSeconds 代表的时间戳的日期改为dateStr所代表的北京时间的日期，返回时间戳（秒）
     * set zoneId = "Asia/Shanghai"
     *
     * @param epochSeconds
     * @param dateStr
     * @return epochSeconds with date for dateStr
     */
    fun shiftTimeToDate(epochSeconds: Long, dateStr: String): Long {
        return shiftTimeToDate(epochSeconds, dateStr, DefaultZoneId)
    }

    /**
     * 将 epochSeconds 代表的时间戳的日期改为dateStr所代表的zoneId所在时区的时间的日期，返回时间戳（秒）
     *
     * @param epochSeconds
     * @param dateStr
     * @param zoneId
     * @return epochSeconds with date for dateStr
     */
    fun shiftTimeToDate(epochSeconds: Long, dateStr: String, zoneId: ZoneId): Long {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val zonedDateTime = instant.atZone(zoneId)
        val hour = zonedDateTime.hour
        val minute = zonedDateTime.minute
        val second = zonedDateTime.second
        val date = LocalDate.parse(dateStr, df)
        val localDateTime = date.atTime(hour, minute, second)
        val zdt = ZonedDateTime.of(localDateTime, zoneId)
        //            zdt.toInstant();
        return zdt.toEpochSecond()
    }

    /**
     * 将begin和end之间的时间按 unit 切分成列表
     * @param begin 总是清除unit之后的时间单位
     * @param end
     * @param unit
     * @return
     */
    fun split(begin: Instant, end: Instant, unit: ChronoUnit): List<TimeDuration> {
        val ldtEnd = end.atZone(DefaultZoneId).toLocalDateTime()
        var zBegin = trimEnd(begin, unit)
        var zEnd = zBegin.plus(1, unit)
        val list: MutableList<TimeDuration> = ArrayList<TimeDuration>()

        // <=
        while (!zEnd.isAfter(ldtEnd)) {
            val iBegin = zBegin.atZone(DefaultZoneId).toInstant()
            val iEnd = zEnd.atZone(DefaultZoneId).toInstant()
            list.add(
                TimeDuration(
                    iBegin,
                    iEnd
                )
            )
            zBegin = zBegin.plus(1, unit)
            zEnd = zEnd.plus(1, unit)
        }
        return list
    }

    /**
     * 去掉unit之内的时间，例如Unit是hour，就去除分，秒，毫秒
     * @param instant
     * @param fromUnit
     * @return
     */
    private fun trimEnd(instant: Instant, fromUnit: ChronoUnit): LocalDateTime {
        val ldt: LocalDateTime = when (fromUnit) {
            ChronoUnit.DAYS -> instant.atZone(DefaultZoneId).toLocalDate().atTime(0, 0, 0, 0)
            ChronoUnit.HOURS -> instant.atZone(DefaultZoneId).toLocalDateTime()
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.NANO_OF_SECOND, 0)
            else -> throw AssertionException("not supported unit:$fromUnit")
        }
        return ldt
    }
}

