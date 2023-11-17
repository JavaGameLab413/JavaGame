package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button

import android.widget.ImageButton


class Record : AppCompatActivity() {
    // 宣告一個 CoroutineScope
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var loadingAnimation: LoadingAnimation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        //loading動畫
        loadingAnimation = LoadingAnimation(this)
        loadingAnimation.start()

        //返回按鈕
        val back: ImageButton = findViewById(R.id.back)
        back.setOnClickListener{
            finish()
        }
        
        val plot1: Button = findViewById(R.id.plot1)
        val teach1: Button = findViewById(R.id.teach1)

        plot1.setOnClickListener{
            val intent = Intent(this, Dialog::class.java)
            intent.putExtra("Title","Plot1")
            startActivity(intent)
        }
        teach1.setOnClickListener{
            val intent = Intent(this, Dialog::class.java)
            startActivity(intent)
        }

// Switch between the two layouts with an animation
        val layout1 = findViewById<View>(R.id.layout1)
        val layout2 = findViewById<View>(R.id.layout2)

        // Animate layout1 to the right and layout2 to the left
        fun animateToLeft() {
            ObjectAnimator.ofFloat(layout1, "translationX", -800f).apply {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(layout2, "translationX", 0f).apply {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }



        // Animate layout1 to the left and layout2 to the right
        fun animateToRight() {
            ObjectAnimator.ofFloat(layout1, "translationX", 0f).apply {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(layout2, "translationX", 800f).apply {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }

        // Switch between the two layouts when clicking a button or any other event trigger
        // Example: a button click listener
        val button = findViewById<View>(R.id.change)
        button.setOnClickListener {
            if (layout1.translationX == 0f) {
                animateToLeft()
            } else {
                animateToRight()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        simulateLoadingComplete()
    }

    private fun simulateLoadingComplete() {
        handler.postDelayed({
            // 加載完成後停止
            loadingAnimation.stop()
        }, 800)
    }
    
}