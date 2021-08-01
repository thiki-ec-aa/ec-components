package net.thiki.ec.component.common.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.text.ParseException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TimeUtilsTest {
    @Test
    @Throws(ParseException::class)
    fun test_shiftTimeToDate() {
        val cut = TimeUtils()
        val zoneOfChina = ZoneId.of("Asia/Shanghai")
        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateStr = "2021-06-17 13:23:11"
        val dateTime = LocalDateTime.parse(dateStr, df)
        val zdt = ZonedDateTime.of(dateTime, zoneOfChina)
        val epochSeconds = cut.shiftTimeToDate(zdt.toEpochSecond(), "2021-07-01")
        val expectedLdt = LocalDateTime.parse("2021-07-01 13:23:11", df)
        val expectedZdt = ZonedDateTime.of(expectedLdt, zoneOfChina)
        Assertions.assertEquals(expectedZdt.toEpochSecond(), epochSeconds)
    }

    @Test
    fun split_days() {
        val cut = TimeUtils()
        val result = cut.split(
            cut.parse("2021-07-01 00:00:00"),
            cut.parse("2021-07-04 00:00:00"),
            ChronoUnit.DAYS
        )
        Assertions.assertEquals(3, result.size)
        for (td in result) {
            println(
                String.format(
                    "begin=%s, end=%s",
                    cut.format(td.begin),
                    cut.format(td.end)
                )
            )
        }
    }

    @Test
    fun split_hours() {
        val cut = TimeUtils()
        val result = cut.split(
            cut.parse("2021-07-01 11:00:59"),
            cut.parse("2021-07-01 12:00:01"),
            ChronoUnit.HOURS
        )
        for (td in result) {
            println(
                String.format(
                    "begin=%s, end=%s",
                    cut.format(td.begin),
                    cut.format(td.end)
                )
            )
        }
        Assertions.assertEquals(1, result.size)
    }

    @Test
    fun test_calTimeDuration_4_hours() {
        val cut = TimeUtils()

        val a: TimeDuration = cut.calTimeDuration(
            cut.parse("2021-07-01 12:01:00"),
            ChronoUnit.HOURS
        )

        assertEquals(
            cut.parse("2021-07-01 12:00:00"),
            a.begin
        )
        assertEquals(
            cut.parse("2021-07-01 13:00:00"),
            a.end
        )
    }

}