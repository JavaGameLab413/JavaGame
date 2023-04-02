package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Start : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //實作按鈕
        val fight: ImageButton = findViewById(R.id.fight)
        val history: ImageButton = findViewById(R.id.history)
        val shop: ImageButton = findViewById(R.id.shop)
        val backPack: ImageButton = findViewById(R.id.backPack)

        //實作文本(名稱)
        var textView = findViewById<TextView>(R.id.playerId)
        textView.text = GlobalVariable.getName()

        //設置按鈕監聽
        fight.setOnClickListener(this)
        history.setOnClickListener(this)
        shop.setOnClickListener(this)
        backPack.setOnClickListener(this)

    }
    //施行按鈕方法
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fight -> {
                val intent = Intent(this, Fight::class.java)
                startActivity(intent)
            }
            R.id.history -> {
                val intent = Intent(this, History::class.java)
                startActivity(intent)
            }
            R.id.shop -> {
                val intent = Intent(this, Shop::class.java)
                startActivity(intent)
                Log.d("test", "This is Debug.");
            }
            R.id.backPack -> {
                val intent = Intent(this, BackPack::class.java)
                startActivity(intent)
            }
        }
    }


}