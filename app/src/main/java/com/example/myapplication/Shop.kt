package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Shop : AppCompatActivity(), View.OnClickListener {

    private val propertiesDatabaseCollectionName = "properties"
    private var itemcase: String = ""

    private var numberOne  = 0
    private var numberTwo = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        //返回按鈕
        val back: ImageButton = findViewById(R.id.back)
        back.setOnClickListener {
            finish()
        }

        val commodity1 = findViewById<ImageView>(R.id.commodity1)
        val commodity2 = findViewById<ImageView>(R.id.commodity2)
        val commodity3 = findViewById<ImageView>(R.id.commodity3)
        val commodity4 = findViewById<ImageView>(R.id.commodity4)
        val commodity5 = findViewById<ImageView>(R.id.commodity5)
        val commodity6 = findViewById<ImageView>(R.id.commodity6)

        val refresh = findViewById<Button>(R.id.refresh)

        //設置按鈕監聽
        commodity1.setOnClickListener(this)
        commodity2.setOnClickListener(this)
        commodity3.setOnClickListener(this)
        commodity4.setOnClickListener(this)
        commodity5.setOnClickListener(this)
        commodity6.setOnClickListener(this)
    }

    //施行按鈕方法
    @SuppressLint("MissingInflatedId", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick(view: View?) {
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        // Access Firebase Firestore
        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        val information = db.collection(propertiesDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())
        val writeData = db.collection(propertiesDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())

        var counter = 1

        val myPurchaseView = layoutInflater.inflate(
            R.layout.purchase_quantity,
            findViewById(android.R.id.content),
            false
        )
        myPurchaseView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )

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
            // 添加其他商品的處理...
        }

        val popupWindow = PopupWindow(this).apply {
            contentView = myPurchaseView
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            //沒添加會一直創建新的
            isFocusable = true
            //視窗外是否可觸碰
            isTouchable = true
            //全屏背景
            isClippingEnabled = true
            //透明背景
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        myPurchaseView.findViewById<ImageButton>(R.id.addNumber).setOnClickListener {
            if (counter < 5) {
                counter++
                Toast.makeText(
                    this,
                    "以選購 $counter 個商品",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                counter = 5
                Toast.makeText(this, "已超過購買數量", Toast.LENGTH_SHORT).show()
            }
        }

        myPurchaseView.findViewById<ImageButton>(R.id.minusNumber).setOnClickListener {
            if (counter > 0) {
                counter--
                Toast.makeText(
                    this,
                    "以選購 $counter 個商品",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                counter = 0
                Toast.makeText(this, "已超過購買數量", Toast.LENGTH_SHORT).show()
            }
        }

        myPurchaseView.findViewById<ImageButton>(R.id.yes).setOnClickListener {
            popupWindow.dismiss()
            information.get().addOnSuccessListener { documents ->
                var userMoney: Int =
                    Integer.parseInt(documents.getLong("money").toString())

                val ref = db.collection("Item").document(itemcase)
                ref.get().addOnSuccessListener { document ->

                    val itemMoney: Int =
                        Integer.parseInt(document.getLong("Money").toString())

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
                        numberOne += counter

                    } else {
                        Toast.makeText(this, "餘額不足!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        myPurchaseView.findViewById<ImageButton>(R.id.no).setOnClickListener {
            popupWindow.dismiss()
            Toast.makeText(this, "已取消購買", Toast.LENGTH_SHORT).show()
        }

        //出現位置
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

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

    //每次進入頁面刷新
    override fun onResume() {
        super.onResume()
        changeMoney()
        //實作文本(名稱)
        val playerName = findViewById<TextView>(R.id.playerId)
        val playerLevel = findViewById<TextView>(R.id.level)
        //取得名稱
        val db = FirebaseFirestore.getInstance()

        //商品

        //讀取本地資料庫User
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)

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
            // 如果设备不支持 WindowInsetsController，则可以尝试使用旧版方法  <版本低於Android 11>
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
