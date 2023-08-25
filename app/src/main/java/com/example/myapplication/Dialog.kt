package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.*
import java.util.LinkedList
import java.util.Queue


class Dialog : AppCompatActivity(){
    private lateinit var textView: TextView
    private lateinit var textToDisplay : String
    private val delayInMillis = 50L
    private var isRunning = false
    private lateinit var blinkAnimation: Animation
    private val queue: Queue<String> = LinkedList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        val speakName = findViewById<TextView>(R.id.name)
        blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_animation)
        val next = findViewById<ImageView>(R.id.next)
        speakName.text="小狸"
        textView = findViewById(R.id.context)
        addText()
        animateTextWithHandler()
        val chat :ImageView = findViewById(R.id.chat)
        chat.setOnClickListener {
            if(!isRunning){
                if(queue.isEmpty()){
                    finish()
                }else{
                    next.visibility = View.INVISIBLE
                    stopAnimation()
                    animateTextWithHandler()
                }
            }
            else{
                textView.text = textToDisplay
                isRunning=false
                next.visibility = View.VISIBLE
                next.startAnimation(blinkAnimation)

            }
        }


    }
    private fun animateTextWithHandler() {
        val handler = Handler(Looper.getMainLooper())
        val next = findViewById<ImageView>(R.id.next)
        var charIndex = 0
        isRunning = true
        textToDisplay = queue.poll() as String
        handler.post(object : Runnable {
            override fun run() {

                if (isRunning) {
                    if (charIndex < textToDisplay.length) {
                        textView.text = textToDisplay.substring(0, charIndex + 1)
                        charIndex++
                        handler.postDelayed(this, delayInMillis) // Delay between characters
                    } else {
                        isRunning = false
                        next.visibility = View.VISIBLE
                        next.startAnimation(blinkAnimation)
                    }
                }
            }
        })

    }

    private fun stopAnimation() {
        blinkAnimation.cancel()
    }

    private fun addText(){
        queue.add("我是一段超級長的測試文字，我主要用來測試逐字動畫還有TextView的寬度，但是我還是有一點點問題，所以暫時無法實用。")
        queue.add("當你看到這段話時，代表大家都很棒。")
        queue.add("還看，說的就是你。")
    }
}