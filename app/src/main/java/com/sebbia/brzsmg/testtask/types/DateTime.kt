package com.sebbia.brzsmg.testtask.types
//package com.swlibs.kotlin-common.types

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Класс для работы с датой и временем, взамен кривого стандартного в старых версиях Java.
 *
 * @author brzsmg
 */
class DateTime : Serializable {
    private var mSystemDate: Date
    private lateinit var mFormatUTC: SimpleDateFormat
    private fun initFormat() {
        mFormatUTC = SimpleDateFormat(
            PATTERN_UTC,
            Locale.getDefault()
        )
        mFormatUTC.timeZone = TimeZone.getTimeZone("GMT")
    }

    constructor() {
        initFormat()
        mSystemDate = Date()
    }

    constructor(date: String) {
        initFormat()
        mSystemDate = mFormatUTC.parse(date) as Date
    }

    constructor(date: String, customPattern : String) {
        initFormat()
        val customFormat = SimpleDateFormat(customPattern, Locale.getDefault())
        customFormat.timeZone = TimeZone.getTimeZone("GMT")
        mSystemDate = customFormat.parse(date) as Date
    }

    //TODO: isToday только для UTC
    fun isToday(): Boolean {
            val c = Calendar.getInstance()
            c[Calendar.HOUR_OF_DAY] = 0
            c[Calendar.MINUTE] = 0
            c[Calendar.SECOND] = 0
            c[Calendar.MILLISECOND] = 0
            val today = c.time
            val todayInMillis = c.timeInMillis
            val c2 = Calendar.getInstance()
            c2.time = mSystemDate
            c2[Calendar.HOUR_OF_DAY] = 0
            c2[Calendar.MINUTE] = 0
            c2[Calendar.SECOND] = 0
            c2[Calendar.MILLISECOND] = 0
            val inMillis = c2.timeInMillis
            return todayInMillis == inMillis
    }

    val timestamp: Long
        get() = mSystemDate.time

    override fun toString(): String {
        return mFormatUTC.format(mSystemDate)
    }

    fun getDifference(date: DateTime): Long {
        return date.timestamp - mSystemDate.time
    }

    fun toFormat(format: String): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(mSystemDate)
    }

    fun toFormat(format: String, timeZone : TimeZone): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        dateFormat.timeZone = timeZone
        return dateFormat.format(mSystemDate)
    }

    companion object {
        const val PATTERN_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val PATTERN_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val patternText = "dd.MM.yyyy HH:mm:ss";
        const val patternSql = "yyyy-MM-dd HH:mm:ss";
        const val patternOracle = "YYYY-MM-DD HH24:MI:SS"
        const val patternFileName = "yyyyMMdd_HHmmss"
        val TIMEZONE_YEKATERINBURG = TimeZone.getTimeZone("Asia/Yekaterinburg")
    }
}