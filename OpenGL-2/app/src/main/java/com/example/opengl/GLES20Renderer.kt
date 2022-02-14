package com.example.opengl

import android.opengl.GLES20


class GLES20Renderer() : GLRenderer() {     // 實作2個method onCreate, and onDrawFrame

    private var triangle: Triangle? = null      // 三角形變數

    override fun onCreate(width: Int, height: Int, contextLost: Boolean) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        // New
        triangle = Triangle()
    }

    override fun onDrawFrame(firstDraw: Boolean) {
        GLES20.glClear(
            GLES20.GL_COLOR_BUFFER_BIT
                    or GLES20.GL_DEPTH_BUFFER_BIT
        )

        // Draw
        triangle!!.draw()
    }
}