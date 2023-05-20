package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class FightSelect : AppCompatActivity() , View.OnClickListener{
    private val propertiesDatabaseCollectionName = "properties"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_select)
        val btq1 = findViewById<Button>(R.id.buttonQ1)
        val back: ImageButton = findViewById(R.id.back)
        back.setOnClickListener (){
            finish()
        }

 btq1.setOnClickListener {
            val intent = Intent(this, FightMain::class.java)
            startActivity(intent)
        }

        val btq2 = findViewById<Button>(R.id.buttonQ2)
        val btq3 = findViewById<Button>(R.id.buttonQ3)
        val btq4 = findViewById<Button>(R.id.buttonQ4)
        val btq5 = findViewById<Button>(R.id.buttonQ5)

        btq2.setOnClickListener (this)
        btq3.setOnClickListener (this)
        btq4.setOnClickListener (this)
        btq5.setOnClickListener (this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
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