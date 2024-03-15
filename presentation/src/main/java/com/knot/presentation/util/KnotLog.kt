package com.knot.presentation.util

import android.util.Log
object KnotLog {

    fun D(msg : String) {
        Log.d("knot log", msg)
    }

    fun D(log : String, msg : String) {
        Log.d(log, msg)
    }
}