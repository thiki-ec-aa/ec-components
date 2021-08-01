package net.thiki.ec.component.common.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimeUtilsTest {

    @Test
    void test_shiftTimeToDate() throws ParseException {
        TimeUtils cut = new TimeUtils();

        ZoneId zoneOfChina = ZoneId.of("Asia/Shanghai");

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateStr = "2021-06-17 13:23:11";
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, df);
        ZonedDateTime zdt = ZonedDateTime.of(dateTime, zoneOfChina);

        long epochSeconds = cut.shiftTimeToDate(zdt.toEpochSecond(), "2021-07-01");

        LocalDateTime expectedLdt = LocalDateTime.parse("2021-07-01 13:23:11", df);
        ZonedDateTime expectedZdt = ZonedDateTime.of(expectedLdt, zoneOfChina);

        assertEquals(expectedZdt.toEpochSecond(), epochSeconds);

    }

    @Test
    void split_days() {
        TimeUtils cut = new TimeUtils();
        final List<TimeDuration> result = cut.split(
                cut.parse("2021-07-01 00:00:00"),
                cut.parse("2021-07-04 00:00:00"),
                ChronoUnit.DAYS
        );
        assertEquals(3, result.size());
        for (TimeDuration td: result) {
            System.out.println(String.format("begin=%s, end=%s",
                    cut.format(td.getBegin()),
                    cut.format(td.getEnd())
            ));
        }
    }
    @Test
    void split_hours() {
        TimeUtils cut = new TimeUtils();
        final List<TimeDuration> result = cut.split(
                cut.parse("2021-07-01 11:00:59"),
                cut.parse("2021-07-01 12:00:01"),
                ChronoUnit.HOURS
        );
        for (TimeDuration td: result) {
            System.out.println(String.format("begin=%s, end=%s",
                    cut.format(td.getBegin()),
                    cut.format(td.getEnd())
            ));
        }
        assertEquals(1, result.size());
    }
}