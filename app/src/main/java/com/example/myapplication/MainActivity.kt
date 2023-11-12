package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.view.WindowInsets.Type.*
import android.view.KeyEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import android.os.Handler
import android.os.Looper


class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    @Override
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val home = Intent(Intent.ACTION_MAIN)
            home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            home.addCategory(Intent.CATEGORY_HOME)
            startActivity(home)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    // 宣告一個 CoroutineScope
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var loadingAnimation: LoadingAnimation


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //將畫面設定為按鈕
        val entry: ImageButton = findViewById(R.id.put_data)
        val signOut = findViewById<Button>(R.id.sign_out)
        //讀取本地資料庫User
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)

        //loading動畫
        loadingAnimation = LoadingAnimation(this)

        //朝畫面點擊後切換畫面
        entry.setOnClickListener {
            //判斷先前有無登入過
            //抓ID，如果沒有回傳-1
            sharedPreferences.getString("ID", "-1")
            //若為-1，登入
            if (sharedPreferences.getString("ID", "-1").toString() == "-1") {
                // 執行xml檔
                val intent = Intent(this, Login::class.java)
                // 啟動新的 Activity
                startActivity(intent)
            } else {
                // 執行loading動畫
                loadingAnimation.start()
                simulateLoadingComplete(Start::class.java)
            }
        }

        signOut.setOnClickListener {
            sharedPreferences.edit().putString("ID", "-1").apply()
            Toast.makeText(this, "登出成功!", Toast.LENGTH_SHORT).show()
            onResume()
        }


    }

    private fun simulateLoadingComplete(targetActivityClass: Class<*>) {
        handler.postDelayed({
            // 加載完成後停止
            loadingAnimation.stop()

            // 啟動目標
            val intent = Intent(this, targetActivityClass)
            startActivity(intent)

        }, 1000)
    }

    override fun onResume() {
        super.onResume()
        //音樂
        mediaPlayer = MediaPlayer.create(this, R.raw.main)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)

        if (sharedPreferences.getString("ID", "-1") == "-1") {
            val singOut = findViewById<Button>(R.id.sign_out)
            singOut.isVisible = false
        } else {
            val singOut = findViewById<Button>(R.id.sign_out)
            singOut.isVisible = true
        }

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.release()
    }
}
