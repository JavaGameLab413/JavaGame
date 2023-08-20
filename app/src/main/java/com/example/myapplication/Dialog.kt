package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.*


class Dialog : AppCompatActivity(){
    private lateinit var textView: TextView
    private val textToDisplay = "我是一段超級長的測試文字，我主要用來測試逐字動畫還有TextView的寬度，但是我還是有一點點問題，所以暫時無法實用。"
    private val delayInMillis = 50L
    private var isRunning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        val speakName = findViewById<TextView>(R.id.name)
        speakName.text="小狸"
        textView = findViewById(R.id.context)
        animateTextWithHandler()
        val chat :ImageView = findViewById(R.id.chat)
        chat.setOnClickListener {
            if(!isRunning){
                animateTextWithHandler()
            }
            else{
                textView.text = textToDisplay
                isRunning=false

            }
        }


    }
    private fun animateTextWithHandler() {
        val handler = Handler(Looper.getMainLooper())
        var charIndex = 0
        isRunning = true
        handler.post(object : Runnable {
            override fun run() {

                if (isRunning) {
                    if (charIndex < textToDisplay.length) {
                        textView.text = textToDisplay.substring(0, charIndex + 1)
                        charIndex++
                        handler.postDelayed(this, delayInMillis) // Delay between characters
                    } else {
                        isRunning = false
                    }
                }
            }
        })

    }
}