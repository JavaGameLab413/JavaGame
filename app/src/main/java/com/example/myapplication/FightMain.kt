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

    private val map: Map<String, Int> =
        mapOf("M1" to R.drawable.healing_potion, "M2" to R.drawable.powerup1) //物品圖片位置
    private var equipmentNum = ArrayList<String>(5) //裝備中的物品名稱

    private var userHp=0
    private var userAtk=0
    private var bossHp=0
    private var bossAtk=0
    private var tampAtk=0


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_main)

        getEquipment()

        //畫面中四種選項的按鈕
        val btOptionsA = findViewById<Button>(R.id.OptionsA)
        val btOptionsB = findViewById<Button>(R.id.OptionsB)
        val btOptionsC = findViewById<Button>(R.id.OptionsC)
        val btOptionsD = findViewById<Button>(R.id.OptionsD)
        dataSet = intent.getStringExtra("questionTitle").toString()
        Log.d(TAG, "DataSet : $dataSet")
        //Log.d("Dataset", "DataSet : $dataSet")
        bossLevelSet = intent.getStringExtra("bossLevel").toString()

        //裝備
        val equipment1: ImageButton = findViewById(R.id.equipment1)
        val equipment2: ImageButton = findViewById(R.id.equipment2)
        val equipment3: ImageButton = findViewById(R.id.equipment3)
        val equipment4: ImageButton = findViewById(R.id.equipment4)
        val equipment5: ImageButton = findViewById(R.id.equipment5)

        getValue()

        //設置選項按下去的行為
        btOptionsA.setOnClickListener {
            checkChoiceIsAns("SelectA")
            Toast.makeText(this, "123", Toast.LENGTH_SHORT).show()
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
        //裝備
        equipment1.setOnClickListener {
            usePotion(equipment1.tag.toString())
        }
        equipment2.setOnClickListener {
            usePotion(equipment2.tag.toString())
        }
        equipment3.setOnClickListener {
            usePotion(equipment3.tag.toString())
        }
        equipment4.setOnClickListener {
            usePotion(equipment3.tag.toString())
        }
        equipment5.setOnClickListener {
            usePotion(equipment5.tag.toString())
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

        val correctOutput = "答案正確!"
        val errorOutput = "答案錯誤!"
        if (userHp > 0 && bossHp > 0) {
            if (answer == btn) {
                Log.d(TAG, "Boss HP: $bossHp")
                Toast.makeText(this, correctOutput, Toast.LENGTH_SHORT)
                    .show()
                enemyHp.progress -= (userAtk+tampAtk)
                tampAtk = 0
                correct()
            } else {
                Log.d(TAG, "User HP: $userHp")
                Toast.makeText(this, errorOutput, Toast.LENGTH_SHORT)
                    .show()
                playerHp.progress -= bossAtk
                tampAtk = 0
            }
            checkFinish()

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

    //從資料庫取得裝備中的物品
    private fun getEquipment() {
        clearEquipment()

        val playerInfoDatabaseCollectionName = "PlayerInfo"
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(playerInfoDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())

        docRef.get().addOnSuccessListener {doc ->
            val text = doc.getString("Equipment")
            if (text != null) {
                for(title in text.split(Regex(","))){
                    if(title!=""&&!(equipmentNum.contains(title))){
                        equipmentNum.add(title)
                    }

                }
                showWearEquipment()
            }

        }

    }

    private fun showWearEquipment(){

        //裝備顯示
        var count = 1
        for (i in equipmentNum) {
            when (count) {
                1 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment1, i)
                    }
                    count++
                }
                2 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment2, i)
                    }
                    count++
                }
                3 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment3, i)
                    }
                    count++
                }
                4 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment4, i)
                    }
                    count++
                }
                5 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment5, i)
                    }
                    count++
                }

            }
        }
    }

    //裝備後顯示
    private fun showEquipment(id: Int, viewId: Int, tag: String) {
        val equipment = findViewById<ImageButton>(viewId)
        equipment.tag = tag
        equipment.setImageResource(id)
        equipment.visibility = View.VISIBLE
    }

    //取得玩家、BOSS數值
    private fun getValue(){
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
                        bossHp=Integer.parseInt(bossDocumentSnapshot.getLong("healthPoint").toString())
                        bossAtk=Integer.parseInt(bossDocumentSnapshot.getLong("combatPower").toString())
                        userDocumentRef.get()
                            .addOnSuccessListener { userDocumentSnapshot ->
                                if (userDocumentSnapshot.exists()) {
                                    userHp= Integer.parseInt(userDocumentSnapshot.getLong("HP").toString()                                    )
                                    userAtk=Integer.parseInt(userDocumentSnapshot.getLong("Attack").toString())
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

    }

    private fun usePotion(s:String){

        db.collection("Item").document(s).get().addOnSuccessListener { doc->
            val text = doc.getString("Effect")
            val name = doc.getString("Name")
            if(text!=null){
                val effect = text.split(Regex(" "))

                Log.e("a",effect[0])
                Log.e("a",effect[1])

                when(effect[0]){
                    "Hp"->{
                        playerHp.progress += Integer.parseInt(effect[1])
                        Toast.makeText(this, "已使用 $name", Toast.LENGTH_SHORT).show()
                        deletePotion(s)
                    }
                    "Atk"->{
                        if(tampAtk==0){
                            tampAtk = Integer.parseInt(effect[1])
                            Toast.makeText(this, "已使用 $name", Toast.LENGTH_SHORT).show()
                            deletePotion(s)
                        }
                        else{
                            Toast.makeText(this, "$name 每次攻擊只能用一個", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }

        }


    }

    private fun deletePotion(tag:String){
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val userId =sharedPreferences.getString("ID", "-1").toString()
        val documentReference=db.collection("BackPage").document(userId)
        val userReference=db.collection("PlayerInfo").document(userId)
        documentReference.get().addOnSuccessListener {doc ->
            var num = Integer.parseInt(doc.getLong(tag).toString())
            num--
            documentReference.update(tag,num)
            if(num==0){
                equipmentNum.remove(tag)
                var equipment=""
                //稱號寫入格式
                for(i in equipmentNum){
                    equipment +="$i,"
                }
                userReference.update("Equipment",equipment)
                getEquipment()
            }else{
                getEquipment()
            }

        }

    }

    private fun clearEquipment(){
        val wear = findViewById<LinearLayout>(R.id.wear)
        for (i in 0 until wear.childCount){
            val child: View = wear.getChildAt(i)
            child.visibility=View.INVISIBLE
        }
    }

}






