package com.rittmann.leagueoflegendschamps.util

import android.util.Log
import com.rittmann.leagueoflegendschamps.BuildConfig

object LogUtil {

    private const val TAG = "RobbieLog"

    fun log(
        msg: String,
        tag: String = TAG,
        logType: LogType = LogType.VERBOSE,
        clause: Clause = Clause.ALL,
        beautiful: Boolean = false
    ) {
        val msgToShow = if (beautiful) beautifulMsg(msg) else msg
        when (clause) {
            Clause.DEBUG -> {
                if (BuildConfig.DEBUG) {

                    doLog(logType, tag, msgToShow)
                }
            }
            Clause.ALL -> doLog(logType, tag, msgToShow)
        }
    }

    // todo: see it later
    private fun beautifulMsg(msg: String): String {
        val line = "#################################################################"
        return "Robbie log beautiful -> \n$line\n\n" +
                msg +
                "\n\n$line"
    }

    private fun doLog(type: LogType, tag: String, msg: String) {
        when (type) {
            LogType.VERBOSE -> Log.i(tag, msg)
            LogType.DEBUG -> Log.d(tag, msg)
            LogType.ERROR -> Log.e(tag, msg)
            LogType.WARN -> Log.w(tag, msg)
        }
    }
}

fun String.log(
    type: LogType = LogType.VERBOSE,
    clause: Clause = Clause.ALL,
    beautiful: Boolean = false
) {
    LogUtil.log(this, logType = type, clause = clause, beautiful = beautiful)
}

fun Int.log(concat: String = "", type: LogType = LogType.VERBOSE, clause: Clause = Clause.ALL) {
    LogUtil.log("$concat$this", logType = type, clause = clause)
}

enum class Clause {
    ALL, DEBUG
}

enum class LogType {
    VERBOSE, DEBUG, ERROR, WARN
}