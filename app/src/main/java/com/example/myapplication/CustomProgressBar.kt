package com.example.myapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation

class CustomProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint: Paint = Paint()
    private var progress: Float = 0f
    private val animationDuration: Long = 3000 // 持續時間，單位：毫秒

    //初始樣式
    init {
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // 繪製條的底部
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        Log.e("B",progress.toString())
        // 繪製條的進度
        val progressWidth = width.toFloat() * progress
        canvas?.drawRect(0f, 0f, progressWidth, height.toFloat(), paint)
    }

    // 設定進度，值在0.0到1.0之間
    fun setProgress(value: Float) {
        progress = value
        invalidate() // 強制重繪View
    }

    // 開始動畫
    fun startAnimation() {
        val progressBarAnimation = ProgressBarAnimation(this, 0f, 1f)
        progressBarAnimation.duration = animationDuration
        progressBarAnimation.interpolator = LinearInterpolator()
        startAnimation(progressBarAnimation)
    }

    // 自訂義動畫類
    private inner class ProgressBarAnimation(
        private val view: CustomProgressBar,
        private val from: Float,
        private val to: Float
    ) : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            Log.e("CC",to.toString())
            Log.e("FF",from.toString())
            Log.e("V",view.toString())
            val value = from + (to - from) * interpolatedTime
            view.setProgress(value)
        }
    }
}
