package com.example.opengl

import android.app.ActivityManager
import android.content.Context


object Utils {
    // 是否支援 20000 版本 (ok)
    fun has20GLSupport(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = am.deviceConfigurationInfo
        return info.reqGlEsVersion >= 0x20000
    }

    // 檢查手機的openGL ES版本  (20000)
    /**
    (public int reqGlEsVersion)
    The GLES version used by an application. The upper order 16 bits represent the major
    version and the lower order 16 bits the minor version.
     */
    fun currentGLVersion(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = am.deviceConfigurationInfo
        return Integer.toHexString(info.reqGlEsVersion)
    }
}