package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Start : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val fight :ImageButton = findViewById(R.id.fight)
        val history :ImageButton = findViewById(R.id.history)
        val shop :ImageButton = findViewById(R.id.shop)
        val backPack :ImageButton = findViewById(R.id.backPack)

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
        val intent = Intent(this, Fight::class.java)
        // 啟動新的 Activity
        startActivity(intent)
    }
    private fun entryHistory(){
        val intent = Intent(this, History::class.java)
        // 啟動新的 Activity
        startActivity(intent)
    }
    private fun entryShop(){
        val intent = Intent(this, Shop::class.java)
        // 啟動新的 Activity
        startActivity(intent)
    }
    private fun entryBackPack(){
        val intent = Intent(this, BackPack::class.java)
        // 啟動新的 Activity
        startActivity(intent)
    }

}