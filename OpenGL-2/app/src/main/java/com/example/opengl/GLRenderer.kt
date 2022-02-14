package com.example.opengl

import javax.microedition.khronos.opengles.GL10

import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig

// ok
abstract class GLRenderer : Renderer {   // 實作3個method (onSurfaceChanged, onDrawFrame, and onSurfaceCreated)
    private val TAG = "GLRenderer"
    private var mFirstDraw: Boolean         // 是否為第一次繪圖
    private var mSurfaceCreated: Boolean    // Surface是否已經建立
    private var mWidth: Int
    private var mHeight: Int
    private var mLastTime: Long     // 紀錄上次時間 (繪圖時需要)
    private var fPS: Int            //frame per second

    // 建立初始化
    init {
        mFirstDraw = true
        mSurfaceCreated = false
        mWidth = -1
        mHeight = -1
        mLastTime = System.currentTimeMillis()
        fPS = 0
    }

    // 建立時呼叫 (ok)
    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        Log.v(TAG, "Surface created.")
        mSurfaceCreated = true
        mWidth = -1
        mHeight = -1
    }

    // 改變大小時呼叫 (ok)
    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {        // p1: width   p2: height
        if (!mSurfaceCreated && p1 == mWidth && p2 == mHeight) {        // 檢查Surface是否create
            DebugLog.print(this, "Surface changed but already handled.")    // 已經有Surface且寬高不變
            return
        }
        if (DebugLog.isDebugOn()) {         // 這只是 Debug Log 是否能正常印出
            // Android honeycomb has an option to keep the
            // context.
            var msg = ("Surface changed width:" + p1
                    + " height:" + p2)
            msg += if (mSurfaceCreated) {
                " context lost."
            } else {
                "."
            }
            DebugLog.print(this, msg)
        }
        // 重新指定寬高
        mWidth = p1
        mHeight = p2
        onCreate(mWidth, mHeight, mSurfaceCreated)
        mSurfaceCreated = false
    }

    // 繪圖時呼叫 (ok)
    override fun onDrawFrame(p0: GL10?) {
        onDrawFrame(mFirstDraw)
        if (DebugLog.isDebugOn()) {
            fPS++               // 紀錄每秒多少frame
            val currentTime = System.currentTimeMillis()
            if (currentTime - mLastTime >= 1000) {      // 超過1秒重記
                fPS = 0
                mLastTime = currentTime     // 記錄時間
            }
        }
        if (mFirstDraw) {
            mFirstDraw = false
        }
    }

    abstract fun onCreate(width: Int, height: Int, contextLost: Boolean)

    abstract fun onDrawFrame(firstDraw: Boolean)

}