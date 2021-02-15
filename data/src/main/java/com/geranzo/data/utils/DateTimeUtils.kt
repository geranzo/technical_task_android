package com.geranzo.data.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    private const val ISO_8601_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    private val ISO_8601_DATE_TIME_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(ISO_8601_DATE_TIME_PATTERN, Locale.ROOT).apply {
                timeZone = TimeZone.getTimeZone("GMT")
            }
        }
    }

    @Throws(ParseException::class)
    fun parseIso8601DateTime(dateString: String) = parse(ISO_8601_DATE_TIME_FORMAT, dateString)

    @Throws(ParseException::class)
    private fun parse(df: ThreadLocal<DateFormat>, dateString: String) = df.get()!!.parse(dateString)
}
