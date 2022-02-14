package com.example.opengl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.opengl.GLSurfaceView;
import android.view.View
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private var gl_view: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 秀出 GL 版本
        val txt_hint = findViewById<View>(R.id.txt_hint) as TextView
        txt_hint.text = "current GL version: " + Utils.currentGLVersion(this)

        // 初始化Surface
        initGLView()

    }

    /* GLSurfaceView 一定要搭配GLSurfaceView.Renderer才能渲染出圖形
       Renderer的責任包含了 "視角的轉換" 以及 "圖形的繪製" ...
       原始的GLSurfaceView.Renderer是abstract class
       當手機進行休眠狀態時，onSurfaceCreated會去重新呼叫一次，
       如果想避免他重新呼叫，
       就要在GLSurfaceView初始化時setPreserveEGLContextOnPause(boolean) 設為true
       並且在Activity 的onPause(),onResume()中加入
       GLSurfaceView.onPause(), GLSurfaceView.onResume()*/
    private fun initGLView() {
        if (Utils.has20GLSupport(this)) {
            gl_view = findViewById<View>(R.id.gl_view) as GLSurfaceView
            gl_view!!.setEGLContextClientVersion(2)     // ??
            gl_view!!.preserveEGLContextOnPause = true
            gl_view!!.setRenderer(GLES20Renderer())
        } else {
            // Time to get a new phone, OpenGL ES 2.0 not supported.
        }
    }

}