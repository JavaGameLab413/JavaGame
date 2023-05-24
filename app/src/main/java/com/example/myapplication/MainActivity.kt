package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.WindowInsets.Type.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer


    class MainActivity : AppCompatActivity(), View.OnKeyListener {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_main)

            // 将当前 Activity 设置为 OnKeyListener
            window.decorView.setOnKeyListener(this)
        }

        override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
            // 检查按下的键是否是返回键，并在这种情况下调用 onBackPressed() 方法
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP) {
                onBackPressed()
                return true
            }

            return false
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //將畫面設定為按鈕
        val entry: ImageButton = findViewById(R.id.put_data)
        val signOut = findViewById<Button>(R.id.sign_out)
        //讀取本地資料庫User
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)

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
                // 執行xml檔
                val intent = Intent(this, Start::class.java)
                // 啟動新的 Activity
                startActivity(intent)
            }

        }

        signOut.setOnClickListener {
            sharedPreferences.edit().putString("ID", "-1").apply()
            Toast.makeText(this, "登出成功!", Toast.LENGTH_SHORT).show()
            onResume()
        }


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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val window = this.window

        val decorView = window.decorView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.also {
                it.hide(statusBars())
                it.hide(navigationBars())
            }

        }
        else {
            // 如果设备不支持 WindowInsetsController，则可以尝试使用旧版方法  <版本低於Android 11>
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
