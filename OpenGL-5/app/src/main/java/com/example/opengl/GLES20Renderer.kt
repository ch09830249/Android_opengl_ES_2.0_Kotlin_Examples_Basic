package com.example.opengl

import android.opengl.GLES20
import android.opengl.Matrix
import android.os.SystemClock





class GLES20Renderer() : GLRenderer() {     // 實作2個method onCreate, and onDrawFrame
    // private var triangle: Triangle? = null   三角形變數
    private var square: Square? = null

    private val mProjectionMatrix = FloatArray(16)

    override fun onCreate(width: Int, height: Int, contextLost: Boolean) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)     // 清空情況下背景的顏色 Get clear color (R, G, B, Alpha) MaxValue: 1f 自己去對應三原色RGB(3-255)
        initProjectionMatrix(width, height)

        // triangle = Triangle() 三角形
        square = Square()
    }

    override fun onDrawFrame(firstDraw: Boolean) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 畫正方形
        val mMVPMatrix = getMVPMatrix()
        // 視圖投影矩陣: (視圖矩陣允許你指定相機的位置和它正在查看的點) (投影矩陣不僅能讓你將OpenGL ES的平方坐標系映射到Android設備的矩形螢幕，還可以指定觀察 viewing frustum的近平面和遠平面)
        square!!.setmMVPMatrix(mMVPMatrix!!)
        square!!.draw()

        //triangle!!.draw()
    }

    // 初始化投影矩陣 (在 GLES20Randerer 的 onCreate有View的長寬資訊:width, height 將 width, height 相除得到長寬的比值ratio)
    // 投影矩陣不僅能讓你將OpenGL ES的平方坐標系映射到Android設備的矩形螢幕，還可以指定觀察 viewing frustum的近平面和遠平面
    private fun initProjectionMatrix(width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height    // 得到寬和高的比例

        // 初始化投影矩陣  (Input: (float[] m, int offset, float left, float right, float bottom, float top, float near, float far))
        // 若畫布已經是一個正方形，左和右的值為-1和1，頂和底的值也為-1和1
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f)       // 得一 4 * 4 矩陣  (Normalization)
    }

    // 更動 getMVPMatrix 使正方形旋轉
    private fun getMVPMatrix(): FloatArray? {
        // 產品矩陣
        val mMVPMatrix = FloatArray(16)
        // 旋轉矩陣
        val mRotationMatrix = FloatArray(16)
        // 產生視圖矩陣並初始化
        val mViewMatrix = getDefaultViewMatrix()
        val time = SystemClock.uptimeMillis()
        val angle = 0.001f * time.toFloat()
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0f, 0f, -1.0f)
        // 計算旋轉後的視圖矩陣 (視圖矩陣*旋轉矩陣)
        Matrix.multiplyMM(mViewMatrix, 0, mRotationMatrix, 0, mViewMatrix, 0)
        // 計算產品矩陣   (旋轉後視圖矩陣*投影矩陣)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)
        return mMVPMatrix
    }

    // 初始化視圖矩陣 (它需要相機的位置和它正在看的點)
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