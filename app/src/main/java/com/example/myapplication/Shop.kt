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
import com.google.firebase.firestore.model.Document


class Shop : AppCompatActivity(), View.OnClickListener {

    private val propertiesDatabaseCollectionName = "properties"


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        //實作按鈕
        val commodity1: ImageButton = findViewById(R.id.commodity1)
        val commodity2: ImageButton = findViewById(R.id.commodity2)
        val commodity3: ImageButton = findViewById(R.id.commodity3)
        val commodity4: ImageButton = findViewById(R.id.commodity4)
        val commodity5: ImageButton = findViewById(R.id.commodity5)
        val commodity6: ImageButton = findViewById(R.id.commodity6)



        //設置按鈕監聽
        commodity1.setOnClickListener(this)
        commodity2.setOnClickListener(this)
        commodity3.setOnClickListener(this)
        commodity4.setOnClickListener(this)
        commodity5.setOnClickListener(this)
        commodity6.setOnClickListener(this)

        //TextView

        //返回按鈕
        val back: ImageButton = findViewById(R.id.back)
        back.setOnClickListener() {
            finish()
        }
    }
        //記數
        var number  = 0
        var numbertwo = 0

    //施行按鈕方法

    @SuppressLint("MissingInflatedId", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick(view: View?) {
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        // Access Firebase Firestorm
        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        val information = db.collection(propertiesDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())
        val writeData = db.collection(propertiesDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())

        val commodity1 = findViewById<ImageView>(R.id.commodity1)
        val commodity2 = findViewById<ImageView>(R.id.commodity2)
        val commodity3 = findViewById<ImageView>(R.id.commodity3)
        val commodity4 = findViewById<ImageView>(R.id.commodity4)
        val commodity5 = findViewById<ImageView>(R.id.commodity5)
        val commodity6 = findViewById<ImageView>(R.id.commodity6)
        val refresh = findViewById<Button>(R.id.refresh)

        when (view?.id) {

            R.id.commodity1 -> {
                val myPurchaseView = layoutInflater.inflate(
                    R.layout.purchase_quantity,
                    findViewById(android.R.id.content),
                    false
                )
                myPurchaseView.measure(
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
                )
                var counter = 1



                val popupWindow = PopupWindow(this).apply {
                    contentView = myPurchaseView
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    //沒添加會一直創建新的
                    isFocusable = true
                    //視窗外是否可觸碰
                    isTouchModal = false
                    //全屏背景
                    isClippingEnabled = true
                    //透明背景
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                }
                            myPurchaseView.findViewById<ImageButton>(R.id.addnumber).setOnClickListener {

                                if (counter < 5) {
                                    counter++
                                    counter.toString()
                                    Toast.makeText(
                                        this,
                                        "以選購" + counter.toString() + "個商品",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    counter = 5
                                    Toast.makeText(this, "已超過購買數量", Toast.LENGTH_SHORT).show()
                                }
                            }
                            myPurchaseView.findViewById<ImageButton>(R.id.minusnumber).setOnClickListener {
                                if (counter > 0) {
                                    counter--
                                    counter.toString()
                                    Toast.makeText(
                                        this,
                                        "以選購" + counter.toString() + "個商品",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else {
                                    counter = 0
                                    Toast.makeText(this, "已超過購買數量", Toast.LENGTH_SHORT).show()
                                }
                            }



                        myPurchaseView.findViewById<ImageButton>(R.id.yes).setOnClickListener {
                            popupWindow.dismiss()
                            information.get().addOnSuccessListener { documents ->
                                var money: Int =
                                    Integer.parseInt(documents.getLong("money").toString())

                                val ref = db.collection("commodity").document("1")
                                ref.get().addOnSuccessListener { document ->

                                 val commodityMoney : Int =
                                     Integer.parseInt(document.getLong("commodityMoney").toString())

                                val purchasemoner = commodityMoney * counter
                                if (money >= purchasemoner) {
                                    money -= purchasemoner
                                    writeData.update("money", money)
                                    Toast.makeText(
                                        this,
                                        "購買成功!!總共花費" + purchasemoner + "G",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    changeMoney()
                                    number = number + counter
                                    if(number ==5 ) {
                                        commodity1.visibility = View.INVISIBLE
                                    }
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


            R.id.commodity2 -> {
                val myPurchaseView = layoutInflater.inflate(
                    R.layout.purchase_quantity,
                    findViewById(android.R.id.content),
                    false
                )
                myPurchaseView.measure(
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
                )

                var counter = 1


                val popupWindow = PopupWindow(this).apply {
                    contentView = myPurchaseView
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    //沒添加會一直創建新的
                    isFocusable = true
                    //視窗外是否可觸碰
                    isTouchModal = false
                    //全屏背景
                    isClippingEnabled = true
                    //透明背景
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                myPurchaseView.findViewById<ImageButton>(R.id.addnumber).setOnClickListener {
                    if (counter < 5) {
                        counter++
                        counter.toString()
                        Toast.makeText(
                            this,
                            "以選購" + counter.toString() + "個商品",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        counter = 5
                        Toast.makeText(this, "已超過購買數量", Toast.LENGTH_SHORT).show()
                    }
                }
                myPurchaseView.findViewById<ImageButton>(R.id.minusnumber).setOnClickListener {
                    if (counter > 0) {
                        counter--
                        counter.toString()
                        Toast.makeText(
                            this,
                            "以選購" + counter.toString() + "個商品",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else {
                        counter = 0
                        Toast.makeText(this, "已超過購買數量", Toast.LENGTH_SHORT).show()
                    }
                }



                myPurchaseView.findViewById<ImageButton>(R.id.yes).setOnClickListener {
                    popupWindow.dismiss()
                    information.get().addOnSuccessListener { documents ->
                        var money: Int =
                            Integer.parseInt(documents.getLong("money").toString())

                        var atk: Int = Integer.parseInt(documents.getLong("atk").toString())

                        val ref = db.collection("commodity").document("2")
                        ref.get().addOnSuccessListener { document ->

                            val commodityMoney : Int =
                                Integer.parseInt(document.getLong("commodityMoney").toString())

                            val attackpower : Int =
                                Integer.parseInt(document.getLong("attackpower").toString())

                            val purchasemoner = commodityMoney * counter
                            if (money >= purchasemoner) {
                                money -= purchasemoner
                                writeData.update("money", money)
                                atk += attackpower*counter
                                writeData.update("atk",atk)
                                Toast.makeText(
                                    this,
                                    "購買成功!!總共花費" + purchasemoner + "G",
                                    Toast.LENGTH_SHORT
                                ).show()
                                changeMoney()

                                numbertwo = numbertwo + counter
                                if(numbertwo ==5 ) {
                                    commodity2.visibility = View.INVISIBLE
                                }
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

            R.id.commodity3 -> {
                val myContentView = layoutInflater.inflate(
                    R.layout.shop_confirm,
                    findViewById(android.R.id.content),
                    false
                )
                myContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)


                val popupWindow = PopupWindow(this).apply {
                    contentView = myContentView
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    //沒添加會一直創建新的
                    isFocusable = true
                    //視窗外是否可觸碰
                    isTouchModal = false
                    //全屏背景
                    isClippingEnabled = true
                    //透明背景
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }

                //點選按鈕動作
                myContentView.findViewById<ImageButton>(R.id.yes).setOnClickListener {

                    popupWindow.dismiss()

                    information.get().addOnSuccessListener { documents ->
                        var money: Int = Integer.parseInt(documents.getLong("money").toString())
                        if (money >= 50) {
                            money -= 50
                            writeData.update("money", money)
                            Toast.makeText(this, "購買成功!!", Toast.LENGTH_SHORT).show()
                            changeMoney()
                            commodity3.visibility = View.INVISIBLE


                        } else {
                            Toast.makeText(this, "餘額不足!!", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
                myContentView.findViewById<ImageButton>(R.id.no).setOnClickListener {
                    popupWindow.dismiss()
                    Toast.makeText(this, "已取消購買", Toast.LENGTH_SHORT).show()
                }

                //出現位置
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)


            }
            R.id.commodity4 -> {
                val myContentView = layoutInflater.inflate(
                    R.layout.shop_confirm,
                    findViewById(android.R.id.content),
                    false
                )
                myContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)


                val popupWindow = PopupWindow(this).apply {
                    contentView = myContentView
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    //沒添加會一直創建新的
                    isFocusable = true
                    //視窗外是否可觸碰
                    isTouchModal = false
                    //全屏背景
                    isClippingEnabled = true
                    //透明背景
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }

                //點選按鈕動作
                myContentView.findViewById<ImageButton>(R.id.yes).setOnClickListener {

                    popupWindow.dismiss()

                    information.get().addOnSuccessListener { documents ->
                        var money: Int = Integer.parseInt(documents.getLong("money").toString())
                        if (money >= 50) {
                            money -= 50
                            writeData.update("money", money)
                            Toast.makeText(this, "購買成功!!", Toast.LENGTH_SHORT).show()
                            changeMoney()
                            commodity4.visibility = View.INVISIBLE


                        } else {
                            Toast.makeText(this, "餘額不足!!", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
                myContentView.findViewById<ImageButton>(R.id.no).setOnClickListener {
                    popupWindow.dismiss()
                    Toast.makeText(this, "已取消購買", Toast.LENGTH_SHORT).show()
                }

                //出現位置
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)


            }
            R.id.commodity5 -> {
                val myContentView = layoutInflater.inflate(
                    R.layout.shop_confirm,
                    findViewById(android.R.id.content),
                    false
                )
                myContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)


                val popupWindow = PopupWindow(this).apply {
                    contentView = myContentView
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    //沒添加會一直創建新的
                    isFocusable = true
                    //視窗外是否可觸碰
                    isTouchModal = false
                    //全屏背景
                    isClippingEnabled = true
                    //透明背景
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }//

                //點選按鈕動作
                myContentView.findViewById<ImageButton>(R.id.yes).setOnClickListener {

                    popupWindow.dismiss()

                    information.get().addOnSuccessListener { documents ->
                        var money: Int = Integer.parseInt(documents.getLong("money").toString())
                        if (money >= 50) {
                            money -= 50
                            writeData.update("money", money)
                            Toast.makeText(this, "購買成功!!", Toast.LENGTH_SHORT).show()
                            changeMoney()
                            commodity5.visibility = View.INVISIBLE


                        } else {
                            Toast.makeText(this, "餘額不足!!", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
                myContentView.findViewById<ImageButton>(R.id.no).setOnClickListener {
                    popupWindow.dismiss()
                    Toast.makeText(this, "已取消購買", Toast.LENGTH_SHORT).show()
                }

                //出現位置
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)


            }
            R.id.commodity6 -> {
                val myContentView = layoutInflater.inflate(
                    R.layout.shop_confirm,
                    findViewById(android.R.id.content),
                    false
                )
                myContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)


                val popupWindow = PopupWindow(this).apply {
                    contentView = myContentView
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    //沒添加會一直創建新的
                    isFocusable = true
                    //視窗外是否可觸碰
                    isTouchModal = false
                    //全屏背景
                    isClippingEnabled = true
                    //透明背景
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }

                //點選按鈕動作
                myContentView.findViewById<ImageButton>(R.id.yes).setOnClickListener {

                    popupWindow.dismiss()

                    information.get().addOnSuccessListener { documents ->
                        var money: Int = Integer.parseInt(documents.getLong("money").toString())
                        if (money >= 50) {
                            money -= 50
                            writeData.update("money", money)
                            Toast.makeText(this, "購買成功!!", Toast.LENGTH_SHORT).show()
                            changeMoney()
                            commodity6.visibility = View.INVISIBLE


                        } else {
                            Toast.makeText(this, "餘額不足!!", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
                myContentView.findViewById<ImageButton>(R.id.no).setOnClickListener {
                    popupWindow.dismiss()
                    Toast.makeText(this, "已取消購買", Toast.LENGTH_SHORT).show()
                }

                //出現位置
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)


            }

        }
        refresh.setOnClickListener {
            commodity1.visibility = View.VISIBLE
            commodity2.visibility = View.VISIBLE
            commodity3.visibility = View.VISIBLE
            commodity4.visibility = View.VISIBLE
            commodity5.visibility = View.VISIBLE
            commodity6.visibility = View.VISIBLE

            number = 0
        }


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