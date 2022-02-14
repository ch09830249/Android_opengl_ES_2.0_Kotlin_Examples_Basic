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
        const val FIELD_MATRIX_MVP = "uMVPMatrix"   // Model View Projection

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

        // 將我們的座標點經由矩陣運算投影
        private const val SHADER_CODE_VERTEX_MVP =
                "uniform mat4 " + FIELD_MATRIX_MVP + ";" +      // 多了這一項
                "attribute vec4 " + FIELD_POSITION + ";" +
                "void main() {" +
                "  gl_Position = " + FIELD_MATRIX_MVP + " * " + FIELD_POSITION + ";" +  // 相乘
                "}"

        // shader: 著色器
        val defaultShader: Int
            get() {
                val shaderFactory = GLShaderFactory()
                return shaderFactory.createShaderProgram(
                    SHADER_CODE_VERTEX_MVP,
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