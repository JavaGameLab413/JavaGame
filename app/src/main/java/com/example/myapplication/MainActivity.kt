package com.example.myapplication

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //將畫面設定為按鈕
        val entry: ImageButton = findViewById(R.id.put_data)
        //朝畫面點擊後切換畫面
        entry.setOnClickListener {
            // 執行xml檔
            val intent = Intent(this, Login::class.java)
            // 啟動新的 Activity
            startActivity(intent)
        }

    }


}