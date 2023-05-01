package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Fight : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        val btSection1 = findViewById<Button>(R.id.buttonSection1)
        btSection1.setOnClickListener {
            val intent = Intent(this, Fight_01::class.java)
            startActivity(intent)
        }
    }
}