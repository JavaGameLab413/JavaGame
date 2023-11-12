package com.example.myapplication

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class FightMain : AppCompatActivity() {
    private var answer = ""
    private var enemyAnimator: ObjectAnimator? = null
    private lateinit var enemyHp: ProgressBar
    private lateinit var playerHp: ProgressBar
    private var dataSet = ""
    private var bossLevelSet = ""
    private val db = FirebaseFirestore.getInstance()




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_main)
        //畫面中四種選項的按鈕
        val btOptionsA = findViewById<Button>(R.id.OptionsA)
        val btOptionsB = findViewById<Button>(R.id.OptionsB)
        val btOptionsC = findViewById<Button>(R.id.OptionsC)
        val btOptionsD = findViewById<Button>(R.id.OptionsD)
        dataSet = intent.getStringExtra("questionTitle").toString()
        Log.d(TAG, "DataSet : $dataSet")
        bossLevelSet = intent.getStringExtra("bossLevel").toString()

        val bossDocumentRef = db.collection("Boss").document(bossLevelSet)

        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val userId =sharedPreferences.getString("ID", "-1").toString()
        val playerInfoRef = db.collection("PlayerInfo").document(userId)
        playerInfoRef.get().addOnSuccessListener { document ->
            val userLevel: Int =
                Integer.parseInt(document.getLong("Level").toString())

            val userDocumentRef = db.collection("Level").document(userLevel.toString())
            bossDocumentRef.get()
                .addOnSuccessListener { bossDocumentSnapshot ->
                    if (bossDocumentSnapshot.exists()) {
                        val bossHp: Int =
                            Integer.parseInt(bossDocumentSnapshot.getLong("healthPoint").toString())
                        userDocumentRef.get()
                            .addOnSuccessListener { userDocumentSnapshot ->
                                if (userDocumentSnapshot.exists()) {
                                    val userHp: Int = Integer.parseInt(
                                        userDocumentSnapshot.getLong("HP").toString()
                                    )

                                    enemyHp = findViewById(R.id.enemyHp)//敵對血條
                                    playerHp = findViewById(R.id.playerHp)//我方血條
                                    enemyHp.max = bossHp
                                    playerHp.max = userHp
                                    enemyHp.progress = enemyHp.max //設定值在設定畫面的設定檔中，目前設置為6
                                    playerHp.progress = playerHp.max //設定值在設定畫面的設定檔中，目前設置為6
                                }
                            }
                    }
                }
        }
        //設置選項按下去的行為
        btOptionsA.setOnClickListener {
            checkChoiceIsAns("SelectA")
        }
        btOptionsB.setOnClickListener {
            checkChoiceIsAns("SelectB")
        }
        btOptionsC.setOnClickListener {
            checkChoiceIsAns("SelectC")
        }
        btOptionsD.setOnClickListener {
            checkChoiceIsAns("SelectD")
        }
    }

    //每次更新會做的事情，固定放在onCreate下方，其他方法往下放
    override fun onResume() {
        super.onResume()
        val btOptionsA = findViewById<Button>(R.id.OptionsA)
        val btOptionsB = findViewById<Button>(R.id.OptionsB)
        val btOptionsC = findViewById<Button>(R.id.OptionsC)
        val btOptionsD = findViewById<Button>(R.id.OptionsD)
        val mainQuestion = findViewById<TextView>(R.id.question)
        val collectionRef = db.collection(dataSet)

        //自定義的字體
        mainQuestion.setTextAppearance(R.style.AppTheme)

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
                val question = randomDocument.getString("Info")
                val aOption = randomDocument.getString("SelectA")
                val bOption = randomDocument.getString("SelectB")
                val cOption = randomDocument.getString("SelectC")
                val dOption = randomDocument.getString("SelectD")
                answer = randomDocument.getString("Answer").toString()
                Log.d(TAG, "Question : $question")
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

    private fun checkChoiceIsAns(btn: String) {

        val bossDocumentRef = db.collection("Boss").document(bossLevelSet)

        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val userId =sharedPreferences.getString("ID", "-1").toString()
        val playerInfoRef = db.collection("PlayerInfo").document(userId)
        playerInfoRef.get().addOnSuccessListener { document ->
            val userLevel: Int =
                Integer.parseInt(document.getLong("Level").toString())

            val userDocumentRef = db.collection("Level").document(userLevel.toString())
            bossDocumentRef.get()
                .addOnSuccessListener { bossDocumentSnapshot ->
                    if (bossDocumentSnapshot.exists()) {
                        // 从文档中读取数据
                        val bossHp: Int =
                            Integer.parseInt(bossDocumentSnapshot.getLong("healthPoint").toString())
                        val bossAttack: Int =
                            Integer.parseInt(bossDocumentSnapshot.getLong("combatPower").toString())

                        userDocumentRef.get()
                            .addOnSuccessListener { userDocumentSnapshot ->
                                if (userDocumentSnapshot.exists()) {
                                    // 从文档中读取数据
                                    val userHp: Int = Integer.parseInt(
                                        userDocumentSnapshot.getLong("HP").toString()
                                    )
                                    val userAttack: Int = Integer.parseInt(
                                        userDocumentSnapshot.getLong("Attack").toString()
                                    )

                                    val correctOutput = "答案正確!"
                                    val errorOutput = "答案錯誤!"
                                    if (userHp > 0 && bossHp > 0) {
                                        if (answer == btn) {
                                            Log.d(TAG, "Boss HP: $bossHp")
                                            Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT)
                                                .show()
                                            enemyHp.progress -= userAttack
                                            correct()
                                        } else {
                                            Log.d(TAG, "User HP: $userHp")
                                            Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT)
                                                .show()
                                            playerHp.progress -= bossAttack
                                        }
                                        checkFinish()

                                    }

                                }
                            }
                    }
                }
        }
    }

    private fun checkLevel() {
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val propertiesDatabaseCollectionName = "PlayerInfo"
        val writeData = db.collection(propertiesDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())
        val userId =sharedPreferences.getString("ID", "-1").toString()
        val playerInfoRef = db.collection("PlayerInfo").document(userId)
        val db = FirebaseFirestore.getInstance()


        playerInfoRef.get().addOnSuccessListener { document ->
            val userLevel: Int = document.getLong("Level").toString().toInt()
            var userExp: Int = document.getLong("exp").toString().toInt()

            val bossExpRef = db.collection("Boss").document(userLevel.toString())
            val levelRef = db.collection("Level").document(userLevel.toString())

            bossExpRef.get().addOnSuccessListener { bossDocument ->
                val bossExp = bossDocument.getLong("EXP").toString().toInt()
                levelRef.get().addOnSuccessListener { levelDocument ->
                    val userNeedExp: Int = levelDocument.getLong("Need").toString().toInt()

                    userExp += bossExp


                    if (userExp >= userNeedExp) {
                        val newLevel = userLevel + 1
                        val newExp = userExp - userNeedExp
                        writeData.update("Level", newLevel)
                        writeData.update("exp", newExp)
                    }else{
                        writeData.update("exp", userExp)
                    }
                }
            }
        }
    }

    private fun checkFinish(){
        if (playerHp.progress == 0 || enemyHp.progress == 0) {

            finish()
            checkLevel()
        } else {
            onResume()
        }
    }





    override fun onDestroy() {
        super.onDestroy()

        //釋放動畫資源
        enemyAnimator?.cancel()
        enemyAnimator = null
    }
    private fun correct() {

        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val playerInfoDatabaseCollectionName = "PlayerInfo"

        val db = FirebaseFirestore.getInstance()
        val information = db.collection(playerInfoDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())
        val writeData = db.collection(playerInfoDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())
        information.get().addOnSuccessListener { documents ->
            var money: Int = Integer.parseInt(documents.getLong("Gold").toString())
            val addMoney = 10
            money += addMoney
            writeData.update("Gold", money)



        }

        startAnimation()
    }
    private fun startAnimation(){
        enemyAnimator?.cancel()

        val enemy: ImageView = findViewById(R.id.enemy)
        enemyAnimator = ObjectAnimator.ofFloat(enemy, "translationX", -15f, 15f)
            .apply {
                duration = 200
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE

            }
        enemyAnimator?.start()
    }
}






