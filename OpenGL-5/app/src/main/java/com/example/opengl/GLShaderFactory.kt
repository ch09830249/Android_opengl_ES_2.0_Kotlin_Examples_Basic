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

        // (頂點著色器) (頂點著色器負責處理3D物件的頂點)
        // gl_Position, gl_FragColor則是openGL ES所認得的變數名稱
        // 用來處理點座標
        private const val SHADER_CODE_VERTEX =
            "attribute vec4 " + FIELD_POSITION + ";" +          // attribute vec4: 從Java代碼接收頂點位置資料
            "void main(){" +
            "   gl_Position = " + FIELD_POSITION + ";" +        // 該變數決定頂點的最終位置
            "}"

        // (片段著色器) (負責著色3D物件的圖元)
        // 用來處理渲染效果
        private const val SHADER_CODE_FRAGMENT =
            "precision mediump float;" +
            "uniform vec4 " + FIELD_COLOR + ";" +
            "void main(){" +
            "   gl_FragColor = " + FIELD_COLOR + ";" +
            "}"

        // 將我們的座標點經由矩陣運算投影
        private const val SHADER_CODE_VERTEX_MVP =
            "uniform mat4 " + FIELD_MATRIX_MVP + ";" +      // uniform mat4: 從Java代碼接收視圖投影矩陣
            "attribute vec4 " + FIELD_POSITION + ";" +
            "void main() {" +
            "  gl_Position = " + FIELD_MATRIX_MVP + " * " + FIELD_POSITION + ";" +  // OpenGL ES坐標系是一個正方形, 若畫布非正方形, 需要靠矩陣投影向量將點到對的位置
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
        // 新產生著色器物件  (type有兩種: GLES20.GL_VERTEX_SHADER, and GLES20.GL_FRAGMENT_SHADER)
        val shader = GLES20.glCreateShader(type)    // 該方法返回一個整數，該整數作為著色器物件的引用

        // 新創建的著色器物件不包含任何代碼。要將著色器代碼添加到著色器物件，必須使用glShaderSource()方法
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)      // 可以將著色器物件傳遞給glCompileShader()方法來編譯它們包含的代碼 (必須將他撰寫成String, 再交給OpenGL去編譯)

        return shader
    }

    // 不要直接使用著色器渲染3D物件。而是將它們附加到程式然後再使用
    private fun createShaderProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {

        // 透過上面函式產生兩種著色器
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // 創建一個新程式
        val shaderProgram = GLES20.glCreateProgram()

        // 將頂點和片段著色器物件附加到程式
        GLES20.glAttachShader(shaderProgram, vertexShader)
        GLES20.glAttachShader(shaderProgram, fragmentShader)

        // 連結程式
        GLES20.glLinkProgram(shaderProgram)

        return shaderProgram
    }
}