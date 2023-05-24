package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class Start : AppCompatActivity(), View.OnClickListener {
    private val propertiesDatabaseCollectionName = "properties"
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btDatabase: Button
    private lateinit var btGPT: Button


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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //實作按鈕
        val fight: ImageButton = findViewById(R.id.fight)
        val history: ImageButton = findViewById(R.id.history)
        val shop: ImageButton = findViewById(R.id.shop)
        val backPack: ImageButton = findViewById(R.id.backPack)
        btDatabase = findViewById(R.id.insert)
        btGPT = findViewById(R.id.gpt)


        //設置按鈕監聽
        fight.setOnClickListener(this)
        history.setOnClickListener(this)
        shop.setOnClickListener(this)
        backPack.setOnClickListener(this)
        btDatabase.setOnClickListener {
            val intent = Intent(this, Insert::class.java)
            startActivity(intent)
        }
        btGPT.setOnClickListener{
            val intent = Intent(this, ChatGPT::class.java)
            startActivity(intent)
        }

    }
    //施行按鈕方法
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fight -> {
                val intent = Intent(this, Fight::class.java)
                startActivity(intent)
            }
            R.id.history -> {
                Toast.makeText(this, "此功能尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, History::class.java)
//                startActivity(intent)
            }
            R.id.shop -> {
                val intent = Intent(this, Shop::class.java)
                startActivity(intent)
                // 關閉頁面
                // finish()
                Log.d("test", "This is Debug.")
            }
            R.id.backPack -> {
                Toast.makeText(this, "此功能尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, BackPack::class.java)
//                startActivity(intent)
            }

        }
    }


    //刷新頁面
    override fun onResume() {
        super.onResume()
        //實作文本(名稱)
        val playerName = findViewById<TextView>(R.id.playerId)
        val playerMoney = findViewById<TextView>(R.id.gold)
        val playerLevel = findViewById<TextView>(R.id.level)
        //讀取本地資料庫User
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        Log.d("ERR",sharedPreferences.getString("ID", "-1").toString())

        //取得名稱
        val db = FirebaseFirestore.getInstance()

        db.collection(propertiesDatabaseCollectionName).whereEqualTo("serialNumber",Integer.parseInt(sharedPreferences.getString("ID", "-1").toString()))
            .get()
            .addOnSuccessListener { documents ->
                playerName.text = documents.first().getString("name").toString()
                playerMoney.text = String.format("%s G",documents.first().getLong("money").toString())
                playerLevel.text = String.format("Lv: %s",documents.first().getLong("lv").toString())
                if (playerName.text == "a"){
                    Log.d("game","是測試者")

                }else{
                    if (btDatabase.visibility == View.VISIBLE or btGPT.visibility){
                        btDatabase.visibility = View.INVISIBLE
                        btGPT.visibility = View.INVISIBLE
                    }

                }
            }

        //音樂
        mediaPlayer = MediaPlayer.create(this, R.raw.start)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        if (playerName.toString() == "a"){
            Log.d("test","test")
        }else{

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