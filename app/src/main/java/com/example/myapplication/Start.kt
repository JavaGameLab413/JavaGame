package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Start : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //實作按鈕
        val fight :ImageButton = findViewById(R.id.fight)
        val history :ImageButton = findViewById(R.id.history)
        val shop :ImageButton = findViewById(R.id.shop)
        val backPack :ImageButton = findViewById(R.id.backPack)
        //設置按鈕監聽
        fight.setOnClickListener {
            entryFight()
        }
        history.setOnClickListener {
            entryHistory()
        }
        shop.setOnClickListener {
            entryShop()
        }
        backPack.setOnClickListener {
            entryBackPack()
        }

    }
    private fun entryFight(){
        //施做跳轉畫面
        val intent = Intent(this, Fight::class.java)
        startActivity(intent)
    }
    private fun entryHistory(){
        //施做跳轉畫面
        val intent = Intent(this, History::class.java)
        startActivity(intent)
    }
    private fun entryShop(){
        //施做跳轉畫面
        val intent = Intent(this, Shop::class.java)
        startActivity(intent)
    }
    private fun entryBackPack(){
        //施做跳轉畫面
        val intent = Intent(this, BackPack::class.java)
        startActivity(intent)
    }


}