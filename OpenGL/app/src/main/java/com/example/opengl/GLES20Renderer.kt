package com.example.opengl

import android.opengl.GLES20

// ok
class GLES20Renderer() : GLRenderer() {     // 實作2個method onCreate, and onDrawFrame

    override fun onCreate(width: Int, height: Int, contextLost: Boolean) {
        GLES20.glClearColor(0f, 1f, 0f, 1f);    // Get clear color (R, G, B, Alpha) MaxValue: 1f 自己去對應三原色RGB(3-255)
    }

    override fun onDrawFrame(firstDraw: Boolean) {
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT )
    }
}