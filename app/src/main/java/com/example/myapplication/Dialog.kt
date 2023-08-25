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

    //設置文字跑條動畫
    private lateinit var textView: TextView
    private lateinit var textToDisplay : String
    private lateinit var blinkAnimation: Animation
    private var isRunning = false

    //控制跑條速度
    private val delayInMillis = 50L

    //文字輸入
    private val queue: Queue<String> = LinkedList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        addText()   //添加劇情

        //講話者名稱
        val speakName = findViewById<TextView>(R.id.name)
        speakName.text="小狸"


        //下一段劇情閃爍
        val next = findViewById<ImageView>(R.id.next)
        blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_animation)//自訂動畫

        //內文動畫
        textView = findViewById(R.id.context)
        animateTextWithHandler()

        //點擊進行下一段
        val chat :ImageView = findViewById(R.id.chat)
        chat.setOnClickListener {

            //判斷這段話是否跑完
            if(!isRunning){
                //確認劇情是否跑完
                if(queue.isEmpty()){
                    finish()
                }else{
                    next.visibility = View.INVISIBLE
                    stopAnimation()
                    animateTextWithHandler()
                }
            }
            else{
                //把這段話瞬間完全顯示
                textView.text = textToDisplay
                isRunning=false
                next.visibility = View.VISIBLE
                next.startAnimation(blinkAnimation)

            }
        }

    }

    //文字跑條動畫
    private fun animateTextWithHandler() {
        val handler = Handler(Looper.getMainLooper())
        val next = findViewById<ImageView>(R.id.next)
        var charIndex = 0
        isRunning = true
        textToDisplay = queue.poll() as String

        //排流程
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

    //停止閃爍動畫
    private fun stopAnimation() {
        blinkAnimation.cancel()
    }

    //添加劇情
    private fun addText(){
        queue.add("我是一段超級長的測試文字，我主要用來測試逐字動畫還有TextView的寬度，但是我還是有一點點問題，所以暫時無法實用。")
        queue.add("當你看到這段話時，代表大家都很棒。")
        queue.add("還看，說的就是你。")
    }
}