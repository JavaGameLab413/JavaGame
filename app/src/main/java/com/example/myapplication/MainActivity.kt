package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //創建 entry為按鈕 start_button 後設定按下去執行的方法
        val entry: ImageButton = findViewById(R.id.start_button)
        //按下entry後 執行的方法
        entry.setOnClickListener { entryGame()}

    }
    //重開始畫面進入遊戲畫面的功能
    private fun entryGame() {
        // 創建一個 Intent 對象，指定要啟動的 Activity 類
        val intent = Intent(this, Start::class.java)
        // 啟動新的 Activity
        startActivity(intent)
    }

}