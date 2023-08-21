package com.example.myapplication

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects

class FightMain : AppCompatActivity() {
    private var answer = ""
    private var enemyAnimator: ObjectAnimator? = null
    private lateinit var enemyHp: ProgressBar
    private lateinit var playerHp: ProgressBar
    private var dataSet = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_main)
        //畫面中四種選項的按鈕
        val btOptionsA = findViewById<Button>(R.id.OptionsA)
        val btOptionsB = findViewById<Button>(R.id.OptionsB)
        val btOptionsC = findViewById<Button>(R.id.OptionsC)
        val btOptionsD = findViewById<Button>(R.id.OptionsD)
        val correctOutput = "答案正確!"
        val errorOutput = "答案錯誤!"
        dataSet = intent.getStringExtra("questionTitle").toString()
        Log.d(TAG, "DataSet : $dataSet")        //測試顯示資料庫是讀取哪一個
        enemyHp = findViewById(R.id.enemyHp)//敵對血條
        playerHp = findViewById(R.id.playerHp)//我方血條
        enemyHp.progress = enemyHp.max //設定值在設定畫面的設定檔中，目前設置為6
        playerHp.progress = playerHp.max //設定值在設定畫面的設定檔中，目前設置為6
        //設置選項按下去的行為
        btOptionsA.setOnClickListener {
            if (answer == "SelectA") {
                Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The correct answer!")

                correct()
            } else {
                Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The answer wrong!")
                playerHp.progress -= 1
            }
            checkFinish()
        }
        btOptionsB.setOnClickListener {
            if (answer == "SelectB") {
                Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The correct answer!")
                correct()
            } else {
                Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The answer wrong!")
                playerHp.progress -= 1
            }
            checkFinish()
        }
        btOptionsC.setOnClickListener {
            if (answer == "SelectC") {
                Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The correct answer!")
                correct()
            } else {
                Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The answer wrong!")
                playerHp.progress -= 1
            }
            checkFinish()
        }
        btOptionsD.setOnClickListener {
            if (answer == "SelectD") {
                Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The correct answer!")
                correct()
            } else {
                Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "The answer wrong!")
                playerHp.progress -= 1
            }
            checkFinish()

        }

    }
    private fun checkFinish(){
        if (playerHp.progress == 0 || enemyHp.progress == 0) {
            finish()
        } else {
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        val btOptionsA = findViewById<Button>(R.id.OptionsA)
        val btOptionsB = findViewById<Button>(R.id.OptionsB)
        val btOptionsC = findViewById<Button>(R.id.OptionsC)
        val btOptionsD = findViewById<Button>(R.id.OptionsD)
        val mainQuestion = findViewById<TextView>(R.id.question)
        val db = FirebaseFirestore.getInstance()
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

    override fun onDestroy() {
        super.onDestroy()

        //釋放動畫資源
        enemyAnimator?.cancel()
        enemyAnimator = null
    }
    private fun correct() {

        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val propertiesDatabaseCollectionName = "properties"

        val db = FirebaseFirestore.getInstance()
        val information = db.collection(propertiesDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())
        val writeData = db.collection(propertiesDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())
        information.get().addOnSuccessListener { documents ->
            var money: Int = Integer.parseInt(documents.getLong("money").toString())
            val addMoney = 10
            money += addMoney
            writeData.update("money", money)

        }
        enemyHp.progress -= 1
        startAnimation()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val window = this.window

        val decorView = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.also {
                it.hide(statusBars())
                it.hide(navigationBars())
            }

        } else {
            // 如果设备不支持 WindowInsetsController，则可以尝试使用旧版方法  <版本低於Android 11>
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
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






