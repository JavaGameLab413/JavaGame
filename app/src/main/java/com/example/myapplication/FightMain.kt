package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class FightMain : AppCompatActivity() {



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_main)
        val btOptionsA = findViewById<Button>(R.id.buttonOptions1)
        val btOptionsB = findViewById<Button>(R.id.buttonOptions2)
        val btOptionsC = findViewById<Button>(R.id.buttonOptions3)
        val btOptionsD = findViewById<Button>(R.id.buttonOptions4)
        val mainQuestion = findViewById<TextView>(R.id.QView4)
        val questionTypeDatabaseCollectionName = "questionsType"

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection(questionTypeDatabaseCollectionName)


        // 使用get()方法取得集合中所有的文檔快照
        collectionRef.get()
            .addOnSuccessListener { documents ->
                // 集合中文檔的總數
                val totalDocuments = documents.size()
                // 生成一個隨機數字，作為要讀取的文檔的索引
                val randomIndex = (0 until totalDocuments).random()
                // 取得指定索引的文檔
                val randomDocument = documents.documents[randomIndex]
                // 從文檔中讀取欄位值
                val question = randomDocument.getString("Question")
                val aOption = randomDocument.getString("a")
                val bOption = randomDocument.getString("b")
                val cOption = randomDocument.getString("c")
                val dOption = randomDocument.getString("d")
                val answer = randomDocument.getString("ans").toString()

                // TODO: 使用讀取到的變數
                mainQuestion.text = question.toString()
                btOptionsA.text = aOption.toString()
                btOptionsB.text = bOption.toString()
                btOptionsC.text = cOption.toString()
                btOptionsD.text = dOption.toString()

                if (answer=="a"){
                    btOptionsA.setOnClickListener {
                        Toast.makeText(this, "答案正確!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The correct answer!")
                    }
                    btOptionsB.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                    btOptionsC.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                    btOptionsD.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                }else if(answer =="b"){
                    btOptionsA.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                    btOptionsB.setOnClickListener {
                        Toast.makeText(this, "答案正確!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The correct answer!")
                    }
                    btOptionsC.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                    btOptionsD.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                }else if(answer.toString()=="c"){
                    btOptionsA.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                    btOptionsB.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                    btOptionsC.setOnClickListener {
                        Toast.makeText(this, "答案正確!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The correct answer!")

                    }
                    btOptionsD.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }

                }else if(answer =="d"){
                    btOptionsA.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                    btOptionsB.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                    btOptionsC.setOnClickListener {
                        Toast.makeText(this, "答案錯誤!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The answer wrong!")
                    }
                    btOptionsD.setOnClickListener {
                        Toast.makeText(this, "答案正確!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "The correct answer!")
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting random document: ", exception)
            }
         val back: ImageButton = findViewById(R.id.back)
 back.setOnClickListener (){
     finish()
 }

    }
}
