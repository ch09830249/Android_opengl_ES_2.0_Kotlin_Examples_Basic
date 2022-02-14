package com.example.opengl

import android.opengl.GLES20
import android.opengl.Matrix


class GLES20Renderer() : GLRenderer() {     // 實作2個method onCreate, and onDrawFrame
    // private var triangle: Triangle? = null
    private var square: Square? = null

    private val mProjectionMatrix = FloatArray(16)

    override fun onCreate(width: Int, height: Int, contextLost: Boolean) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)     // 清空情況下背景的顏色
        initProjectionMatrix(width, height)
        square = Square()
    }

    override fun onDrawFrame(firstDraw: Boolean) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 畫正方形
        val mMVPMatrix = getMVPMatrix()
        square!!.setmMVPMatrix(mMVPMatrix!!)
        square!!.draw()
    }

    // 初始化投影矩陣 (在GLES20Randerer 的 onCreate有View的長寬資訊:width, height 將width, height相除得到長寬的比值ratio)
    private fun initProjectionMatrix(width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height    // 得到寬和高的比例

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f)       // 得一 4 * 4 矩陣
    }

    private fun getMVPMatrix(): FloatArray? {
        val mMVPMatrix = FloatArray(16)
        val mViewMatrix = getDefaultViewMatrix()
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)
        return mMVPMatrix
    }

    private fun getDefaultViewMatrix(): FloatArray {
        val eyeX = 0f
        val eyeY = 0f
        val eyeZ = -3f
        val centerX = 0f
        val centerY = 0f
        val centerZ = 0f
        val upX = 0f
        val upY = 1f
        val upZ = 0f
        val mViewMatrix = FloatArray(16)
        Matrix.setLookAtM(
            mViewMatrix,
            0,
            eyeX,
            eyeY,
            eyeZ,
            centerX,
            centerY,
            centerZ,
            upX,
            upY,
            upZ
        )
        return mViewMatrix
    }
}