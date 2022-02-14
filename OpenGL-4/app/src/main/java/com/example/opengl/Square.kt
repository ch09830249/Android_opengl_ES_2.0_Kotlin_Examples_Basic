package com.example.opengl

import android.opengl.GLES20

import java.nio.ByteOrder

import java.nio.ByteBuffer

import java.nio.FloatBuffer
import java.nio.ShortBuffer

// 更改看看frustumM(), setLookAtM()的參數，會更加了解上面這幾張圖所代表的意義
class Square {
    private var vertexBuffer: FloatBuffer? = null
    private var drawListBuffer: ShortBuffer? = null     // 多一個ShortBuffer
    private val vertexStride = COORDS_PER_VERTEX * BYTES_PER_FLOAT  // 配置記憶體用
    private val vertexCount = squareCoords.size / COORDS_PER_VERTEX // 點的數量
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)      // 代表繪圖的順序
    private val color = floatArrayOf(1.0f, 0.0f, 0.0f, 1f)  //  RGBA
    private val mProgram: Int

    private lateinit var mMVPMatrix: FloatArray

    init {
        initVertexBuffer()  // 儲存座標點的buffer
        initDrawListBuffer()    // 儲存順序的Buffer
        mProgram = GLShaderFactory.defaultShader
    }

    companion object {
        private const val COORDS_PER_VERTEX = 3
        private const val BYTES_PER_FLOAT = 4
        private const val BYTES_PER_SHORT = 2
        private val squareCoords = floatArrayOf(
            -0.5f, 0.5f, 0.0f,  //top left  (1)
            -0.5f, -0.5f, 0.0f,  //bottom left  (2)
            0.5f, -0.5f, 0.0f,  //bottom right  (3)
            0.5f, 0.5f, 0.0f //top right    (4)
        )
    }

    private var mDrawMode = -1  // 以哪種模式去畫  (GL_TRIANGLE_FAN: 6, GL_TRIANGLE_STRIP: 5 , GL_TRIANGLES: 4)

    // 設定繪畫模式
    fun setDrawMode(mode: Int) {
        mDrawMode = mode
    }

    fun setmMVPMatrix(matrix: FloatArray) {
        mMVPMatrix = matrix
    }

    // 儲存座標點
    private fun initVertexBuffer() {
        // 配置記憶體空間並設定
        val byteBuffer = ByteBuffer.allocateDirect(squareCoords.size * BYTES_PER_FLOAT)
        byteBuffer.order(ByteOrder.nativeOrder())
        vertexBuffer = byteBuffer.asFloatBuffer()   // ByteBuffer => FloatBuffer
        // Assgin 座標
        vertexBuffer!!.put(squareCoords)
        vertexBuffer!!.position(0)
    }

    // 儲存點的順序
    private fun initDrawListBuffer() {
        // 配置記憶體空間並設定
        val byteBuffer = ByteBuffer.allocateDirect(drawOrder.size * BYTES_PER_SHORT)
        byteBuffer.order(ByteOrder.nativeOrder())
        drawListBuffer = byteBuffer.asShortBuffer()
        // Assign 繪圖的順序
        drawListBuffer!!.put(drawOrder)
        drawListBuffer!!.position(0)
    }

    fun draw() {
        GLES20.glUseProgram(mProgram)

        val positionHandle = GLES20.glGetAttribLocation(mProgram, GLShaderFactory.FIELD_POSITION)
        val colorHandle = GLES20.glGetUniformLocation(mProgram, GLShaderFactory.FIELD_COLOR)
        //多這個
        val mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, GLShaderFactory.FIELD_MATRIX_MVP)

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false, vertexStride, vertexBuffer
        )
        GLES20.glUniform4fv(colorHandle, 1, color, 0)
        // 和這個
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        drawTriangles()

        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    private fun drawTriangles() {
        if (mDrawMode == GLES20.GL_TRIANGLE_FAN || mDrawMode == GLES20.GL_TRIANGLE_STRIP || mDrawMode == GLES20.GL_TRIANGLES)
            GLES20.glDrawArrays(mDrawMode, 0, vertexCount)
        else
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.size, GLES20.GL_UNSIGNED_SHORT, drawListBuffer)
    }
}