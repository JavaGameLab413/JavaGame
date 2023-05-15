package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class Start : AppCompatActivity(), View.OnClickListener {
    private val propertiesDatabaseCollectionName = "properties"
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        //啟用自定義的主題
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //實作按鈕
        val fight: ImageButton = findViewById(R.id.fight)
        val history: ImageButton = findViewById(R.id.history)
        val shop: ImageButton = findViewById(R.id.shop)
        val backPack: ImageButton = findViewById(R.id.backPack)

        //設置按鈕監聽
        fight.setOnClickListener(this)
        history.setOnClickListener(this)
        shop.setOnClickListener(this)
        backPack.setOnClickListener(this)




    }
    //施行按鈕方法
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fight -> {
                val intent = Intent(this, Fight::class.java)
                startActivity(intent)
            }
            R.id.history -> {
                val intent = Intent(this, History::class.java)
                startActivity(intent)
            }
            R.id.shop -> {
                val intent = Intent(this, Shop::class.java)
                startActivity(intent)
                // 關閉頁面
                // finish()
                Log.d("test", "This is Debug.")
            }
            R.id.backPack -> {
                val intent = Intent(this, BackPack::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        supportFragmentManager.popBackStack()
    }
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
            }

        //音樂
        mediaPlayer = MediaPlayer.create(this, R.raw.start)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.release()
    }

}