package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton


class Fight : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        val back: ImageButton = findViewById(R.id.back)
        val btSection1 = findViewById<Button>(R.id.buttonSection1)

        btSection1.setOnClickListener {
            val intent = Intent(this, Fight_01::class.java)
            startActivity(intent)
        }

        back.setOnClickListener (){
            val intent = Intent(this, Start::class.java)
            startActivity(intent)
        }
    }
    }
