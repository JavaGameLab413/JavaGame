package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        //啟用自定義的主題
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //將畫面設定為按鈕
        val entry: ImageButton = findViewById(R.id.put_data)
        val btLogin = findViewById<Button>(R.id.buttonLogin2)
        val btGPT = findViewById<Button>(R.id.gpt)
        //朝畫面點擊後切換畫面
        entry.setOnClickListener {
            // 執行xml檔
            val intent = Intent(this, Start::class.java)
            // 啟動新的 Activity
            startActivity(intent)
            //test
        }

        btLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        btGPT.setOnClickListener{
            val intent = Intent(this, ChatGPT::class.java)
            startActivity(intent)
        }

    }


}