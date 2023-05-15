package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class FightSelect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //啟用自定義的主題
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_select)
        val btQ1 = findViewById<Button>(R.id.buttonQ1)
        val title : TextView = findViewById(R.id.title)
        title.setTextAppearance(R.style.AppTheme)

        btQ1.setOnClickListener {
            val intent = Intent(this, FightMain::class.java)
            startActivity(intent)

        }
    }
}