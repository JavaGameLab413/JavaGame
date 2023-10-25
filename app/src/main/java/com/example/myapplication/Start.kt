package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Handler
import android.os.Looper



class Start : AppCompatActivity(), View.OnClickListener {
    private val playerInfoDatabaseCollectionName = "PlayerInfo"
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btDatabase: Button
    private lateinit var btGPT: Button

    // 宣告一個 CoroutineScope
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var loadingAnimation: LoadingAnimation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //實作按鈕
        val fight: ImageButton = findViewById(R.id.fight)
        val history: ImageButton = findViewById(R.id.history)
        val shop: ImageButton = findViewById(R.id.shop)
        val backPack: ImageButton = findViewById(R.id.backPack)

        //loading動畫
        loadingAnimation = LoadingAnimation(this)

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
                // 執行loading動畫
                loadingAnimation.start()
                simulateLoadingComplete(Fight::class.java)
            }
            R.id.history -> {
                loadingAnimation.start()
                simulateLoadingComplete(Record::class.java)
            }
            R.id.shop -> {
                loadingAnimation.start()
                simulateLoadingComplete(Shop::class.java)
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
    private fun simulateLoadingComplete(targetActivityClass: Class<*>) {
        handler.postDelayed({
            // 加載完成後停止
            loadingAnimation.stop()

            // 啟動目標
            val intent = Intent(this, targetActivityClass)
            startActivity(intent)

        }, 1000)
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

        val serialNumber = sharedPreferences.getString("ID", "-1").toString()

        db.collection(playerInfoDatabaseCollectionName).document(serialNumber).get()
            .addOnSuccessListener { documents ->
                playerName.text = documents.getString("PlayerId").toString()
                Log.d("name",documents.getString("PlayerId").toString())
                playerMoney.text = String.format("%s G",documents.getLong("Gold").toString())
                playerLevel.text = String.format("Lv: %s",documents.getLong("Level").toString())
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