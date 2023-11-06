package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets.Type.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button

import android.widget.ImageButton


class Record : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

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
                duration = 800
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(layout2, "translationX", 0f).apply {
                duration = 800
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }

        // Animate layout1 to the left and layout2 to the right
        fun animateToRight() {
            ObjectAnimator.ofFloat(layout1, "translationX", 0f).apply {
                duration = 800
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            ObjectAnimator.ofFloat(layout2, "translationX", 800f).apply {
                duration = 800
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


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val window = this.window

        val decorView = window.decorView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.also {
                it.hide(statusBars())
                it.hide(navigationBars())
            }

        }
        else {
            // 如果设备不支持 WindowInsetsController，则可以尝试使用旧版方法  <版本低於Android 11>
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}