package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class FightSelect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //啟用自定義的主題
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_select)
        val btq1 = findViewById<Button>(R.id.buttonQ1)

        val questionDataCollectionName = "qtype"
        val questionDataQuestionField = "Question"
        val questionDataAnswerField = "answer"

        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        val questionNumber = Random.nextInt(10) + 1
        val QuestionNumber =
            db.collection(questionDataCollectionName).document(questionNumber.toString())
        val readDocRed = db.collection(questionDataCollectionName)

        btq1.setOnClickListener {
            val intent = Intent(this, FightMain::class.java)
            startActivity(intent)

            readDocRed.whereEqualTo(
                questionDataQuestionField,
                questionDataAnswerField.toString()
            ).get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {

                        val Question = documents.first()
                        val answer = Question.getString(questionDataAnswerField)

                    }
                }
        }
    }
}