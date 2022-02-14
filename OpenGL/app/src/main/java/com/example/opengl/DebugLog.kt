package com.example.opengl

import android.util.Log

// Bebug log print (ok)
class DebugLog {
    companion object{
        private val debugOn = true

        fun print(logObject: Any, log: String?) {
            if (debugOn) Log.d(logObject.javaClass.simpleName, log!!)
        }

        fun isDebugOn(): Boolean {
            return debugOn
        }
    }
}