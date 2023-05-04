package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class Fight_01 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight01)
        val btq1 = findViewById<Button>(R.id.buttonQ1)
        /*val back: ImageButton = findViewById(R.id.back)
        back.setOnClickListener (){
            finish()
        }*/

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
}