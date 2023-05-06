package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Shop : AppCompatActivity(), View.OnClickListener {

    private val propertysDatabaseCollectionName = "propertys"


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //啟用自定義的主題
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_shop)

        //實作按鈕
        val commodity1: ImageButton = findViewById(R.id.commodity1)
        val commodity2: ImageButton = findViewById(R.id.commodity2)


        //設置按鈕監聽
        commodity1.setOnClickListener(this)
        commodity2.setOnClickListener(this)
        val back: ImageButton = findViewById(R.id.back)
        back.setOnClickListener (){
            finish()
        }
    }

    //施行按鈕方法

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick(view: View?) {
        // Access Firebase Firestorm
        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        val information = db.collection(propertysDatabaseCollectionName).document(GlobalVariable.getNumber())
        val writeData = db.collection(propertysDatabaseCollectionName).document(GlobalVariable.getNumber())

        val commodity1 = findViewById<ImageView>(R.id.commodity1)
        val commodity2 = findViewById<ImageView>(R.id.commodity2)
        val money_healing = findViewById<TextView>(R.id.moneyHealing)
        val money_healing2 = findViewById<TextView>(R.id.moneyHealing2)
        val refresh = findViewById<Button>(R.id.refresh)
        when (view?.id) {
            R.id.commodity1 -> {
                val myContentView = layoutInflater.inflate(R.layout.shop_confirm, findViewById(android.R.id.content),false)
                myContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)


                val popupWindow = PopupWindow(this).apply {
                    contentView = myContentView
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    //沒添加會一直創建新的
                    isFocusable = true
                    //視窗外是否可觸碰
                    isTouchModal=false
                    //全屏背景
                    isClippingEnabled=true
                    //透明背景
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }

                //點選按鈕動作
                myContentView.findViewById<ImageButton>(R.id.yes).setOnClickListener {

                    popupWindow.dismiss()

                    information.get().addOnSuccessListener { documents ->
                        var money :Int = Integer.parseInt(documents.getLong("money").toString())
                        if(money>=50){
                            money -= 50
                            writeData.update("money",money)
                            Toast.makeText(this, "購買成功!!", Toast.LENGTH_SHORT).show()
                            changeMoney()
                            commodity1.visibility = View.GONE
                            money_healing.visibility = View.GONE

                        }else{
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
            R.id.commodity2 -> {
                val myContentView = layoutInflater.inflate(R.layout.shop_confirm, findViewById(android.R.id.content),false)
                myContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)


                val popupWindow = PopupWindow(this).apply {
                    contentView = myContentView
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    //沒添加會一直創建新的
                    isFocusable = true
                    //視窗外是否可觸碰
                    isTouchModal=false
                    //全屏背景
                    isClippingEnabled=true
                    //透明背景
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }

                //點選按鈕動作
                myContentView.findViewById<ImageButton>(R.id.yes).setOnClickListener {

                    popupWindow.dismiss()

                    information.get().addOnSuccessListener { documents ->
                        var money :Int = Integer.parseInt(documents.getLong("money").toString())
                        if(money>=50){
                            money -= 50
                            writeData.update("money",money)
                            Toast.makeText(this, "購買成功!!", Toast.LENGTH_SHORT).show()
                            changeMoney()
                            commodity2.visibility = View.GONE
                            money_healing2.visibility = View.GONE

                        }else{
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
            money_healing.visibility = View.VISIBLE
            commodity2.visibility = View.VISIBLE
            money_healing2.visibility = View.VISIBLE
        }



    }


    private fun changeMoney(){
        val playerMoney = findViewById<TextView>(R.id.gold)
        val db = FirebaseFirestore.getInstance()
        db.collection(propertysDatabaseCollectionName).whereEqualTo("serialNumber",Integer.parseInt(GlobalVariable.getNumber()))
            .get()
            .addOnSuccessListener { documents ->
                playerMoney.text = String.format("%s G",documents.first().getLong("money").toString())
            }

    }

    override fun onResume() {
        super.onResume()
        changeMoney()
        //實作文本(名稱)
        val playerName = findViewById<TextView>(R.id.playerId)
        val playerLevel = findViewById<TextView>(R.id.level)

        //取得名稱
        val db = FirebaseFirestore.getInstance()

        db.collection(propertysDatabaseCollectionName).whereEqualTo("serialNumber",Integer.parseInt(GlobalVariable.getNumber()))
            .get()
            .addOnSuccessListener { documents ->
                playerName.text = documents.first().getString("name").toString()
                playerLevel.text = String.format("Lv: %s",documents.first().getString("lv").toString())
            }
    }
}