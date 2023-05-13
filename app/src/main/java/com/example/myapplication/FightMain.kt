package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class FightMain : AppCompatActivity() {
    private var sum =0
    private var answer =""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_main)
        val btOptionsA = findViewById<Button>(R.id.OptionsA)
        val btOptionsB = findViewById<Button>(R.id.OptionsB)
        val btOptionsC = findViewById<Button>(R.id.OptionsC)
        val btOptionsD = findViewById<Button>(R.id.OptionsD)
        val correctOutput = "答案正確!"
        val errorOutput = "答案錯誤!"


        btOptionsA.setOnClickListener {
            if(answer=="a"){
                Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The correct answer!")
                correct()
            }
            else{
                Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The answer wrong!")
            }

            sum++
            Log.d(TAG, sum.toString())
            if(sum==6){
                finish()
                sum =0
            }else{
                onResume()
            }


        }
        btOptionsB.setOnClickListener {
            if(answer=="b"){
                Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The correct answer!")
                correct()
            }
            else{
                Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The answer wrong!")
            }
            sum++
            Log.d(TAG, sum.toString())
            if(sum==6){
                finish()
                sum =0
            }else{
                onResume()
            }
        }
        btOptionsC.setOnClickListener {
            if(answer == "c"){
                Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The correct answer!")
                correct()
            }
            else{
                Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The answer wrong!")
            }
            sum++
            Log.d(TAG, sum.toString())
            if(sum==6){
                finish()
                sum =0
            }else{
                onResume()
            }
        }
        btOptionsD.setOnClickListener {
            if(answer=="d"){
                Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The correct answer!")
                correct()
            }
            else{
                Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The answer wrong!")
            }
            sum++
            Log.d(TAG, sum.toString())
            if(sum==6){
                finish()
                sum =0
            }else{
                onResume()
            }
        }


    }
    override fun onResume() {
        super.onResume()
        val btOptionsA = findViewById<Button>(R.id.OptionsA)
        val btOptionsB = findViewById<Button>(R.id.OptionsB)
        val btOptionsC = findViewById<Button>(R.id.OptionsC)
        val btOptionsD = findViewById<Button>(R.id.OptionsD)
        val mainQuestion = findViewById<TextView>(R.id.question)
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
                answer = randomDocument.getString("ans").toString()

                // TODO: 使用讀取到的變數
                mainQuestion.text = question.toString()
                btOptionsA.text = aOption.toString()
                btOptionsB.text = bOption.toString()
                btOptionsC.text = cOption.toString()
                btOptionsD.text = dOption.toString()

                Log.d(TAG, answer)



            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting random document: ", exception)
            }
    }
    private fun correct() {
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val propertiesDatabaseCollectionName = "properties"

        val db = FirebaseFirestore.getInstance()
        val information = db.collection(propertiesDatabaseCollectionName).document(sharedPreferences.getString("ID", "-1").toString())
        val writeData = db.collection(propertiesDatabaseCollectionName).document(sharedPreferences.getString("ID", "-1").toString())
        information.get().addOnSuccessListener { documents ->
            var money: Int = Integer.parseInt(documents.getLong("money").toString())
            val addMoney = 10
            money += addMoney
            writeData.update("money", money)

        }
    }
}






