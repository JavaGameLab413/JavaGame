package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Shop : AppCompatActivity(), View.OnClickListener {

    private val propertiesDatabaseCollectionName = "properties"
    private var itemcase: String = ""
    private lateinit var descriptionTextView: TextView
    // 每個商品的初始可購買數量
    private val remainingPurchaseCounts = mutableMapOf(
        "M1" to 5,
        "M2" to 5,
        "M3" to 5,
        "M4" to 5,
        "M5" to 5,
        "M6" to 5
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        // 返回按鈕
        val back: ImageButton = findViewById(R.id.back)
        back.setOnClickListener {
            finish()
        }

        // 初始化商品圖片
        val commodity1 = findViewById<ImageView>(R.id.commodity1)
        val commodity2 = findViewById<ImageView>(R.id.commodity2)
        val commodity3 = findViewById<ImageView>(R.id.commodity3)
        val commodity4 = findViewById<ImageView>(R.id.commodity4)
        val commodity5 = findViewById<ImageView>(R.id.commodity5)
        val commodity6 = findViewById<ImageView>(R.id.commodity6)

        // 刷新按鈕
        val refresh = findViewById<Button>(R.id.refresh)
        refresh.setOnClickListener {
            // 顯示所有商品
            commodity1.visibility = View.VISIBLE
            commodity2.visibility = View.VISIBLE
            commodity3.visibility = View.VISIBLE
            commodity4.visibility = View.VISIBLE
            commodity5.visibility = View.VISIBLE
            commodity6.visibility = View.VISIBLE
        }

        // 設置按鈕監聽
        commodity1.setOnClickListener(this)
        commodity2.setOnClickListener(this)
        commodity3.setOnClickListener(this)
        commodity4.setOnClickListener(this)
        commodity5.setOnClickListener(this)
        commodity6.setOnClickListener(this)
    }

    @SuppressLint("MissingInflatedId", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick(view: View?) {
        // 獲取使用者資訊
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        // 造訪 Firebase Firestore
        val db = FirebaseFirestore.getInstance()
        // 使用產生的 ID 建立新文檔
        val information = db.collection(propertiesDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())
        val writeData = db.collection(propertiesDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())
        // 購買數量
        var counter = 1

        // 設置購買數量的視窗
        val myPurchaseView = layoutInflater.inflate(
            R.layout.purchase_quantity,
            findViewById(android.R.id.content),
            false
        )
        myPurchaseView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )

        descriptionTextView = myPurchaseView.findViewById(R.id.descriptionTextView)
        // 根據點選的商品設定itemcase
        when (view?.id) {
            R.id.commodity1 -> {
                itemcase = "M1"
            }
            R.id.commodity2 -> {
                itemcase = "M2"
            }
            R.id.commodity3 -> {
                itemcase = "M3"
            }
            R.id.commodity4 -> {
                itemcase = "M4"
            }
            R.id.commodity5 -> {
                itemcase = "M5"
            }
            R.id.commodity6 -> {
                itemcase = "M6"
            }
        }


        // 設定彈出視窗的屬性
        val popupWindow = PopupWindow(this).apply {
            contentView = myPurchaseView
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            isFocusable = true
            isTouchable = true
            isClippingEnabled = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        }
        val ref = db.collection("Item").document(itemcase)
        ref.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val itemDescription: String? = document.getString("Description")
                descriptionTextView.text = itemDescription
            } else {
                Log.d("ShopActivity", "文檔不存在")
            }
        }
        // 設定購買數量的按鈕監聽
        val counterTextView = myPurchaseView.findViewById<TextView>(R.id.counterTextView)
        myPurchaseView.findViewById<ImageButton>(R.id.addNumber).setOnClickListener {
            if (counter < 5) {
                counter++
                counterTextView.text = "$counter"
            } else {
                counter = 5
                Toast.makeText(this, "已超過購買數量", Toast.LENGTH_SHORT).show()
            }
        }

        myPurchaseView.findViewById<ImageButton>(R.id.minusNumber).setOnClickListener {
            if (counter > 0) {
                counter--
                counterTextView.text = "$counter"
            } else {
                counter = 0
                Toast.makeText(this, "已超過購買數量", Toast.LENGTH_SHORT).show()
            }
        }

        // 確認購買按鈕

        myPurchaseView.findViewById<TextView>(R.id.descriptionTextView)
        myPurchaseView.findViewById<ImageButton>(R.id.yes).setOnClickListener {
            popupWindow.dismiss()
            information.get().addOnSuccessListener { documents ->
                var userMoney: Int =
                    Integer.parseInt(documents.getLong("money").toString())

                val ref = db.collection("Item").document(itemcase)
                ref.get().addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val itemMoney: Int = document.getLong("Money")?.toInt() ?: 0
                        val purchaseMoney = itemMoney * counter

                        if (userMoney >= purchaseMoney) {
                            userMoney -= purchaseMoney
                            writeData.update("money", userMoney)
                            Toast.makeText(
                                this,
                                "購買成功!!總共花費 $purchaseMoney G",
                                Toast.LENGTH_SHORT
                            ).show()
                            changeMoney()


                                // 從文件中獲取 "itemcase" 欄位的值
                            val backpackItemName: String? = document.getString("backpackItemName")

                                // 獲取資料庫中 "BackpackTest" 集合的文檔，ID 為 "1"
                            val backpackRef = db.collection("BackpackTest").document("1")

                                // 獲取異步操作的成功監聽器
                            backpackRef.get().addOnSuccessListener { backpackDocument ->
                                // 檢查 "itemcase" 是否等於當前的 "itemcase"
                                if (backpackItemName == null) {
                                    // 如果相等，獲取現有的數量，並加上 counter
                                    val existingCounter = backpackDocument.getLong(itemcase)?.toInt() ?: 0
                                    val updatedCounter = existingCounter + counter

                                    // 建立要更新到資料庫的資料
                                    val updateData = hashMapOf(
                                        itemcase to updatedCounter
                                    )
                                    // 將更新的資料設定到資料庫中，並使用 SetOptions.merge() 來合併資料
                                    backpackRef.set(updateData, SetOptions.merge())
                                } else {
                                    // 如果 "itemcase" 不相等，則新增一個新的 "itemcase" 到資料庫中
                                    val newData = hashMapOf(
                                        itemcase to 5
                                    )

                                    // 將新的資料設定到資料庫中，並使用 SetOptions.merge() 來合併資料
                                    backpackRef.set(newData, SetOptions.merge())
                                }
                            }







                            val remainingCount = remainingPurchaseCounts[itemcase] ?: 0
                            if (remainingCount >= counter) {
                                remainingPurchaseCounts[itemcase] = remainingCount - counter

                                if (remainingPurchaseCounts[itemcase] == 0) {
                                    // 如果商品購買數量為0，隱藏該商品
                                    val commodity1 = findViewById<ImageView>(R.id.commodity1)
                                    val commodity2 = findViewById<ImageView>(R.id.commodity2)
                                    val commodity3 = findViewById<ImageView>(R.id.commodity3)
                                    val commodity4 = findViewById<ImageView>(R.id.commodity4)
                                    val commodity5 = findViewById<ImageView>(R.id.commodity5)
                                    val commodity6 = findViewById<ImageView>(R.id.commodity6)
                                    when (itemcase) {
                                        "M1" -> commodity1.visibility = View.INVISIBLE
                                        "M2" -> commodity2.visibility = View.INVISIBLE
                                        "M3" -> commodity3.visibility = View.INVISIBLE
                                        "M4" -> commodity4.visibility = View.INVISIBLE
                                        "M5" -> commodity5.visibility = View.INVISIBLE
                                        "M6" -> commodity6.visibility = View.INVISIBLE
                                    }
                                }
                            } else {
                                Toast.makeText(this, "購買數量已超過庫存!", Toast.LENGTH_SHORT).show()
                                // 如果商品購買數量超過庫存，隱藏該商品
                                val commodity1 = findViewById<ImageView>(R.id.commodity1)
                                val commodity2 = findViewById<ImageView>(R.id.commodity2)
                                val commodity3 = findViewById<ImageView>(R.id.commodity3)
                                val commodity4 = findViewById<ImageView>(R.id.commodity4)
                                val commodity5 = findViewById<ImageView>(R.id.commodity5)
                                val commodity6 = findViewById<ImageView>(R.id.commodity6)
                                when (itemcase) {
                                    "M1" -> commodity1.visibility = View.INVISIBLE
                                    "M2" -> commodity2.visibility = View.INVISIBLE
                                    "M3" -> commodity3.visibility = View.INVISIBLE
                                    "M4" -> commodity4.visibility = View.INVISIBLE
                                    "M5" -> commodity5.visibility = View.INVISIBLE
                                    "M6" -> commodity6.visibility = View.INVISIBLE
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, "餘額不足!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 取消購買按鈕
        myPurchaseView.findViewById<ImageButton>(R.id.no).setOnClickListener {
            popupWindow.dismiss()
            Toast.makeText(this, "已取消購買", Toast.LENGTH_SHORT).show()
        }

        // 顯示彈出視窗
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }



    // 更新使用者金幣數量
    private fun changeMoney() {
        val playerMoney = findViewById<TextView>(R.id.gold)
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val db = FirebaseFirestore.getInstance()
        db.collection(propertiesDatabaseCollectionName).whereEqualTo(
            "serialNumber",
            Integer.parseInt(sharedPreferences.getString("ID", "-1").toString())
        )
            .get()
            .addOnSuccessListener { documents ->
                playerMoney.text =
                    String.format("%s G", documents.first().getLong("money").toString())
            }
    }

    // 進入頁面時刷新
    override fun onResume() {
        super.onResume()
        changeMoney()
        // 顯示使用者名稱和等級
        val playerName = findViewById<TextView>(R.id.playerId)
        val playerLevel = findViewById<TextView>(R.id.level)
        val db = FirebaseFirestore.getInstance()
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)

        // 讀取使用者資訊
        db.collection(propertiesDatabaseCollectionName).whereEqualTo(
            "serialNumber",
            Integer.parseInt(sharedPreferences.getString("ID", "-1").toString())
        )
            .get()
            .addOnSuccessListener { documents ->
                playerName.text = documents.first().getString("name").toString()
                playerLevel.text =
                    String.format("Lv: %s", documents.first().getLong("lv").toString())
            }
    }

    // 隱藏系統狀態欄和導航欄
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val window = this.window
        val decorView = window.decorView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.also {
                it.hide(statusBars())
                it.hide(navigationBars())
            }
        }
        else {
            // 如果設備不支援 WindowInsetsController，使用舊版方法（版本低於Android 11）
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
