package dev.redicloud.api.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration

val DEFAULT_DATE_FORMAT = SimpleDateFormat("HH:mm dd.MM.yyyy")
val DEFAULT_COUNTER_FORMAT = SimpleDateFormat("mm:ss:SS")

fun getMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

fun getDay(): Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

fun getYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

fun Long.remainTimeAsString(
    days: Boolean = true,
    hours: Boolean = true,
    minutes: Boolean = true,
    seconds: Boolean = false,
    milliSeconds: Boolean = false,
    shortFormat: Boolean = true,
): String = (System.currentTimeMillis() - this).timeAsString(days, hours, minutes, seconds, milliSeconds, shortFormat)

fun Long.timeAsString(
    days: Boolean = true,
    hours: Boolean = true,
    minutes: Boolean = true,
    seconds: Boolean = false,
    milliSeconds: Boolean = false,
    shortFormat: Boolean = true
): String {
    if ((this == -1L) || (this == 0L)) return "∞"
    var time = this
    var dayCounter = 0
    if (days) {
        dayCounter = (time / 86400000).toInt()
        time -= dayCounter * 86400000
    }
    var hourCounter = 0
    if (hours) {
        hourCounter = (time / 3600000).toInt()
        time -= hourCounter * 3600000
    }
    var minuteCounter = 0
    if (minutes) {
        minuteCounter = (time / 60000).toInt()
        time -= minuteCounter * 60000
    }
    var secondCounter = 0
    if (seconds) {
        secondCounter = (time / 1000).toInt()
        time -= secondCounter * 1000
    }
    var milliSecondCounter = 0
    if (milliSeconds) {
        milliSecondCounter = time.toInt()
    }
    var result = ""

    if (days && dayCounter > 0) result += "$dayCounter${if (shortFormat) "d" else " days"} "
    if (hours && hourCounter > 0) result += "$hourCounter${if (shortFormat) "h" else " hours"} "
    if (minutes && minuteCounter > 0) result += "$minuteCounter${if (shortFormat) "m" else " minutes"} "
    if (seconds && secondCounter > 0) result += "$secondCounter${if (shortFormat) "s" else " seconds"} "
    if (milliSeconds && milliSecondCounter > 0) result += "$milliSecondCounter${if (shortFormat) "ms" else " milli seconds"} "
    if (result.isEmpty()) result = "0${if (shortFormat) "s" else " seconds"}"

    return result
}

fun Long.toTicks(): Long = this / 50

fun Duration.toTicks(): Long = this.inWholeMilliseconds.toTicks()