package com.example.myapplication

import android.content.Intent
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

class FightSelect : AppCompatActivity(), View.OnClickListener {
    private val propertiesDatabaseCollectionName = "properties"
    private var dataSet = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        val intent = intent
        dataSet = intent.getStringExtra("questionTitle").toString()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_select)
        val back: ImageButton = findViewById(R.id.back)
        val btq1 = findViewById<Button>(R.id.buttonQ1)
        val btq2 = findViewById<Button>(R.id.buttonQ2)
        val btq3 = findViewById<Button>(R.id.buttonQ3)
        val btq4 = findViewById<Button>(R.id.buttonQ4)
        val btq5 = findViewById<Button>(R.id.buttonQ5)
        val btAddQuestion: ImageButton = findViewById<ImageButton>(R.id.addQuestionButton)


        back.setOnClickListener() {
            finish()
        }
        btq1.setOnClickListener(this)
        btq2.setOnClickListener(this)
        btq3.setOnClickListener(this)
        btq4.setOnClickListener(this)
        btq5.setOnClickListener(this)
        btAddQuestion.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonQ1 -> {
                val btq1 = findViewById<Button>(R.id.buttonQ1)
                val intent = Intent(this, FightMain::class.java)
                intent.putExtra("questionTitle", dataSet+btq1.text.toString());
                startActivity(intent)
            }
            R.id.buttonQ2 -> {
                Toast.makeText(this, "此關卡尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
            }
            R.id.buttonQ3 -> {
                Toast.makeText(this, "此關卡尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
            }
            R.id.buttonQ4 -> {
                Toast.makeText(this, "此關卡尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
            }
            R.id.buttonQ5 -> {
                Toast.makeText(this, "此關卡尚未開啟，敬請期待!!", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun openQuestionActivity(questionTitle: String) {
        val intent = Intent(this, FightMain::class.java)
        intent.putExtra("questionTitle", questionTitle)
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        //實作文本(名稱)
        val playerName = findViewById<TextView>(R.id.playerId)
        val playerMoney = findViewById<TextView>(R.id.gold)
        val playerLevel = findViewById<TextView>(R.id.level)
        val playerTitle = findViewById<TextView>(R.id.userTitle)
        //讀取本地資料庫User
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        //取得名稱
        val db = FirebaseFirestore.getInstance()

        //設置自定義文字格式
        playerName.setTextAppearance(R.style.AppTheme)
        playerMoney.setTextAppearance(R.style.AppTheme)
        playerLevel.setTextAppearance(R.style.AppTheme)
        playerTitle.setTextAppearance(R.style.AppTheme)
        Log.d("ERR", sharedPreferences.getString("ID", "-1").toString())

        db.collection(propertiesDatabaseCollectionName).whereEqualTo(
            "serialNumber",
            Integer.parseInt(sharedPreferences.getString("ID", "-1").toString())
        )
            .get()
            .addOnSuccessListener { documents ->
                playerName.text = documents.first().getString("name").toString()
                playerMoney.text =
                    String.format("%s G", documents.first().getLong("money").toString())
                playerLevel.text =
                    String.format("Lv: %s", documents.first().getLong("lv").toString())
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