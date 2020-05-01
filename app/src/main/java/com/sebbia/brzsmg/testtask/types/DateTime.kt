package com.sebbia.brzsmg.testtask.types
//package com.swlibs.common.types

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Класс для работы с датой и временем, взамен кривого стандартного в старых версиях Java.
 * TODO: Сконвертирован из Java.
 *
 * @author brzsmg
 */
class DateTime {
    var systemType: Date? = null
        private set
    private var mFormatUTC: SimpleDateFormat? = null
    private fun initFormat() {
        mFormatUTC = SimpleDateFormat(
            pattern2,
            Locale.getDefault()
        )
        mFormatUTC!!.timeZone = TimeZone.getTimeZone("GMT")
        /*mFormatSql = new SimpleDateFormat(patternSql);
        mFormatSql.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));*/
    }

    constructor() {
        initFormat()
        systemType = Date()
    }

    constructor(date: String?) {
        initFormat()
        try {
            systemType = mFormatUTC!!.parse(date)
        } catch (e: ParseException) {
            systemType = Date()
        }
    }

    //TODO: isToday для какой timeZone?
    val isToday: Boolean
        get() {
            val c = Calendar.getInstance()
            c[Calendar.HOUR_OF_DAY] = 0
            c[Calendar.MINUTE] = 0
            c[Calendar.SECOND] = 0
            c[Calendar.MILLISECOND] = 0
            val today = c.time
            val todayInMillis = c.timeInMillis
            val c2 = Calendar.getInstance()
            c2.time = systemType
            c2[Calendar.HOUR_OF_DAY] = 0
            c2[Calendar.MINUTE] = 0
            c2[Calendar.SECOND] = 0
            c2[Calendar.MILLISECOND] = 0
            val inMillis = c2.timeInMillis
            return todayInMillis == inMillis
        }

    val time: Long
        get() = systemType!!.time

    override fun toString(): String {
        //return String.format("%1$tY-%1$tm-%1$tdT%1$tT", mDate);
        return mFormatUTC!!.format(systemType)
    }

    fun getDifference(date: DateTime): Long {
        return date.time - systemType!!.time
    }

    fun toFormat(format: String?): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(systemType)
    }

    companion object {
        //YYYY-MM-DD HH24:MI:SS
        private const val pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val pattern2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        //private SimpleDateFormat mFormatSql;
        //final static String patternText = "dd.MM.yyyy HH:mm:ss";
        //final static String patternSql = "yyyy-MM-dd HH:mm:ss";
        const val patternFileName = "yyyyMMdd_HHmmss"

    }
}