package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        //啟用自定義的主題
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //將畫面設定為按鈕
        val entry: ImageButton = findViewById(R.id.put_data)
        val signOut = findViewById<Button>(R.id.sign_out)
        val btGPT = findViewById<Button>(R.id.gpt)
        val insert = findViewById<Button>(R.id.insert)
        //讀取本地資料庫User
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)


        //朝畫面點擊後切換畫面
        entry.setOnClickListener {

            //判斷先前有無登入過

            //抓ID，如果沒有回傳-1
            sharedPreferences.getString("ID", "-1")
            //若為-1，登入
            if(sharedPreferences.getString("ID", "-1").toString()=="-1"){
                // 執行xml檔
                val intent = Intent(this, Login::class.java)
                // 啟動新的 Activity
                startActivity(intent)
            }
            else{
                // 執行xml檔
                val intent = Intent(this, Start::class.java)
                // 啟動新的 Activity
                startActivity(intent)
            }

        }

        signOut.setOnClickListener{
            sharedPreferences.edit().putString("ID","-1").apply()
            Toast.makeText(this, "登出成功!", Toast.LENGTH_SHORT).show()
        }

    }

    fun entry(view: View) {}

    override fun onResume() {
        super.onResume()
        //音樂
        mediaPlayer = MediaPlayer.create(this, R.raw.main)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.release()
    }
}