package com.example.opengl

import android.opengl.GLES20


class GLES20Renderer() : GLRenderer() {     // 實作2個method onCreate, and onDrawFrame
    // private var triangle: Triangle? = null
    private var square: Square? = null

    override fun onCreate(width: Int, height: Int, contextLost: Boolean) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)     // 清空情況下背景的顏色
        // triangle = Triangle()
        square = Square()
    }

    override fun onDrawFrame(firstDraw: Boolean) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 畫三角形
        // triangle!!.draw()

        // 畫正方形
        square!!.setDrawMode(5)     // 設定畫點的模式
        square!!.draw()
    }
}