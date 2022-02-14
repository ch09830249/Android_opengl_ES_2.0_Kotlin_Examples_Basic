package com.example.opengl

import android.opengl.GLES20

import java.nio.ByteOrder

import java.nio.ByteBuffer

import java.nio.FloatBuffer

// 此例沒用到
class Triangle {
    private var vertexBuffer: FloatBuffer? = null
    private val vertexStride = COORDS_PER_VERTEX * BYTES_PER_FLOAT          // 每個點大小    3 * 4 = 12 bytes
    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX       // 有機個點
    private val color = floatArrayOf(1.0f, 0.0f, 0.0f, 1f)      // RGBA (0.0 ~ 1.0) 設定顏色
    private val mProgram: Int

    init {
        initVertexBuffer()
        mProgram = GLShaderFactory.defaultShader
    }

    companion object {
        private const val COORDS_PER_VERTEX = 3     // 每個點三個座標
        private const val BYTES_PER_FLOAT = 4       // 單精度Size

        // 三角形的3個點的座標
        private val triangleCoords = floatArrayOf(
            0.0f, 0.62008459f, 0.0f,  //top
            -0.5f, -0.311004243f, 0.0f,  //bottom left
            0.5f, -0.311004243f, 0.0f
        )
    }

    // ok
    private fun initVertexBuffer() {
        // 先將記憶體空間配置並設定好
        val byteBuffer = ByteBuffer.allocateDirect(triangleCoords.size * BYTES_PER_FLOAT)   // Allocate memory space    (12個座標 * 單精度浮點數容量)
        byteBuffer.order(ByteOrder.nativeOrder())       //  Java默認字節順序: big-endian     nativeOrder(): 返回本地jvm運行的硬體的字節順序.使用和硬體一致的字節順序
        vertexBuffer = byteBuffer.asFloatBuffer()

        // 將資料放入 FloatBuffer
        vertexBuffer!!.put(triangleCoords)
        vertexBuffer!!.position(0)
    }

    //
    fun draw() {
        GLES20.glUseProgram(mProgram)       // mProgram 是剛剛GLShaderFactory裡得到的ID

        // 藉由此ID我可以拿到vPosition, vColor的ID(positionHandle, colorHandle)
        val positionHandle = GLES20.glGetAttribLocation(mProgram, GLShaderFactory.FIELD_POSITION)
        val colorHandle = GLES20.glGetUniformLocation(mProgram, GLShaderFactory.FIELD_COLOR)

        // 再來呼叫 glVertexAttribPointer(), glUniform4fv() 將數值帶進去
        GLES20.glEnableVertexAttribArray(positionHandle)    // Enable
        GLES20.glVertexAttribPointer(
            positionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false, vertexStride, vertexBuffer
        )
        GLES20.glUniform4fv(colorHandle, 1, color, 0)

        // 最後呼叫 glDrawArrays()
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(positionHandle)   // Disable
    }
}