package com.example.opengl

import android.opengl.GLES20


class GLShaderFactory {

    companion object {
        /*
            這邊的vPosition, vColor是我們定義的變數名稱
            用來連結openGL ES及我們撰寫的java
        */
        const val FIELD_POSITION = "vPosition"
        const val FIELD_COLOR = "vColor"

        // gl_Position, gl_FragColor則是openGL ES所認得的變數名稱
        // 用來處理點座標
        private const val SHADER_CODE_VERTEX =
                "attribute vec4 " + FIELD_POSITION + ";" +
                "void main(){" +
                "   gl_Position = " + FIELD_POSITION + ";" +
                "}"

        // 用來處理渲染效果
        private const val SHADER_CODE_FRAGMENT =
                "precision mediump float;" +
                "uniform vec4 " + FIELD_COLOR + ";" +
                "void main(){" +
                "   gl_FragColor = " + FIELD_COLOR + ";" +
                "}"

        // shader: 著色器
        val defaultShader: Int
            get() {
                val shaderFactory = GLShaderFactory()
                return shaderFactory.createShaderProgram(
                    SHADER_CODE_VERTEX,
                    SHADER_CODE_FRAGMENT
                )
            }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)

        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)      // 必須將他撰寫成String, 再交給OpenGL去編譯

        return shader
    }

    private fun createShaderProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        val shaderProgram = GLES20.glCreateProgram()

        GLES20.glAttachShader(shaderProgram, vertexShader)
        GLES20.glAttachShader(shaderProgram, fragmentShader)

        GLES20.glLinkProgram(shaderProgram)

        return shaderProgram
    }
}