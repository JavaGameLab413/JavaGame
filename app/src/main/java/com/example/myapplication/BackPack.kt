package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class BackPack : AppCompatActivity() {
    private val map: Map<String, Int> = mapOf("M1" to R.drawable.healing_potion, "M2" to R.drawable.power_up)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_pack)

        readData()

    }

    private fun readData(){
        val backPageDatabaseCollectionName = "BackPage"
        val itemDatabaseCollectionName = "Item"

        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(backPageDatabaseCollectionName).document(sharedPreferences.getString("ID", "-1").toString())

        docRef.get()
            .addOnSuccessListener { doc ->
                doc.data?.let { data ->
                    val sortedData = data.keys.sorted()

                    var count =1
                    for (entry in sortedData) {
                        val itemRef = db.collection(itemDatabaseCollectionName).document(entry)
                        itemRef.get()
                            .addOnSuccessListener {
                                val a = map[entry]


                                if (a != null) {
                                    when(count){
                                        1-> {
                                            addItem(R.id.ItemList, a,entry)
                                            count += 1
                                        }
                                        2-> {
                                            addItem(R.id.ItemList1, a,entry)
                                            count += 1
                                        }
                                        3-> {
                                            addItem(R.id.ItemList2, a,entry)
                                            count =1
                                        }
                                    }
                                }
                            }

                    }
                }
            }

//        for (i in 1..8) { 測試
//            addItem(R.id.ItemList,R.drawable.healing_potion)
//        }
    }

    private fun addItem(viewId:Int,imgId:Int,tag:String){
        val scrollViewLayout = findViewById<LinearLayout>(viewId)

        val customView = BackpackItems(this, null)
        customView.setImageResource(imgId)
        customView.tag = tag

        //View布局
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.CENTER
        layoutParams.bottomMargin = 20

        // 添加 CustomImageViewTextView 到 ScrollView 的子視圖中
        customView.layoutParams = layoutParams

        //設置每個動作
        customView.setOnClickListener { view ->
            val a = view.tag
            Toast.makeText(this, a.toString(), Toast.LENGTH_SHORT).show()


            val infoView = InfoView(this, null)
            val icon = map[a]
            if (icon != null) {
                infoView.setView(icon,"HIHI","裝備")
            }

            //彈窗設定
            val popupWindow = PopupWindow(this).apply {
                contentView = infoView
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                //沒添加會一直創建新的
                isFocusable = true
                //全屏背景
                isClippingEnabled = true
                //透明背景
                setBackgroundDrawable(ColorDrawable(Color.BLACK))

            }
            popupWindow.isOutsideTouchable = false // true 表示外部可触摸关闭，false 表示外部不可触摸关闭

            infoView.findViewById<Button>(R.id.sure).setOnClickListener{

            }


            //出現位置
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        }

        scrollViewLayout.addView(customView)


    }


}