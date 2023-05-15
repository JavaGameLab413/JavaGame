package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class Fight_01 : AppCompatActivity() {
    private val propertiesDatabaseCollectionName = "properties"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight01)
        val btq1 = findViewById<Button>(R.id.buttonQ1)

        val Q_TypeDatabaseCollectionName = "qtype"
        val Q_TypeDatabaseQuestionField = "Question"
        val Q_TypeDatabaseanswerField = "answer"

        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        val questionNumber = Random.nextInt(10) + 1
        val QuestionNumber =
            db.collection(Q_TypeDatabaseCollectionName).document(questionNumber.toString())
        val readDocRed = db.collection(Q_TypeDatabaseCollectionName)

        btq1.setOnClickListener {
            val intent = Intent(this, FightMain::class.java)
            startActivity(intent)

            readDocRed.whereEqualTo(
                Q_TypeDatabaseQuestionField,
                Q_TypeDatabaseanswerField.toString()
            ).get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {

                        val Question = documents.first()
                        val answer = Question.getString(Q_TypeDatabaseanswerField)

                    }
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

}