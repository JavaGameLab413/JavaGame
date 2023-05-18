package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class FightSelect : AppCompatActivity() {
    private val propertiesDatabaseCollectionName = "properties"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_select)
        val btq1 = findViewById<Button>(R.id.buttonQ1)

        btq1.setOnClickListener {
            val intent = Intent(this, FightMain::class.java)
            startActivity(intent)

        }
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
        //設置自定義文字格式
        playerName.setTextAppearance(R.style.AppTheme)
        playerMoney.setTextAppearance(R.style.AppTheme)
        playerLevel.setTextAppearance(R.style.AppTheme)
        playerTitle.setTextAppearance(R.style.AppTheme)
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

}