package com.example.myapplication

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class Fight : AppCompatActivity() , View.OnClickListener{
    private val propertiesDatabaseCollectionName = "properties"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        val btSection1 = findViewById<Button>(R.id.buttonSection1)
        val btSection2 = findViewById<Button>(R.id.buttonSection2)
        val btSection3 = findViewById<Button>(R.id.buttonSection3)
        val btSection4 = findViewById<Button>(R.id.buttonSection4)
        val btSection5 = findViewById<Button>(R.id.buttonSection5)

        btSection1.setOnClickListener(this)
        btSection2.setOnClickListener(this)
        btSection3.setOnClickListener(this)
        btSection4.setOnClickListener(this)
        btSection5.setOnClickListener(this)

        val back: ImageButton = findViewById(R.id.back)
        back.setOnClickListener (){
            finish()
        }
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonSection1 -> {
                val intent = Intent(this, FightSelect::class.java)
                startActivity(intent)
            }
            R.id.buttonSection2 -> {
                Toast.makeText(this, "此關卡尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
            }
            R.id.buttonSection3 -> {
                Toast.makeText(this, "此關卡尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
            }
            R.id.buttonSection4 -> {
                Toast.makeText(this, "此關卡尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
            }
            R.id.buttonSection5 -> {
                Toast.makeText(this, "此關卡尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
            }

        }
    }

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