package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore

class BackPack : AppCompatActivity() {
    private val map: Map<String, Int> =
        mapOf("M1" to R.drawable.healing_potion, "M2" to R.drawable.powerup1)
    private var equipmentNum = ArrayList<String>(5)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_pack)

        readData()

        //測試
        equipmentNum.add("M2")
        equipmentNum.add("M1")
        equipmentNum.add("M1")
        equipmentNum.remove("M2")


        var count = 1
        for (i in equipmentNum) {
            when (count) {
                1 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment1)
                    }
                    count++

                }
                2 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment2)
                    }
                    count++

                }
                3 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment3)
                    }
                    count++

                }
                4 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment4)
                    }
                    count++

                }
                5 -> {
                    val equipmentId = map[i]
                    if (equipmentId != null) {
                        showEquipment(equipmentId, R.id.equipment5)
                    }
                    count++

                }

            }
        }

    }

    private fun readData() {
        val backPageDatabaseCollectionName = "BackPage"
        val itemDatabaseCollectionName = "Item"

        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(backPageDatabaseCollectionName)
            .document(sharedPreferences.getString("ID", "-1").toString())

        //抓背包持有物
        docRef.get()
            .addOnSuccessListener { doc ->
                doc.data?.let { data ->
                    //排序欄位資料
                    val sortedData = data.keys.sorted()

                    var count = 1
                    for (entry in sortedData) {
                        val itemRef = db.collection(itemDatabaseCollectionName).document(entry)
                        itemRef.get()
                            .addOnSuccessListener {
                                val imageId = map[entry]


                                if (imageId != null) {
                                    when (count) {
                                        1 -> {
                                            addItem(R.id.ItemList, imageId, entry)
                                            count += 1
                                        }
                                        2 -> {
                                            addItem(R.id.ItemList1, imageId, entry)
                                            count += 1
                                        }
                                        3 -> {
                                            addItem(R.id.ItemList2, imageId, entry)
                                            count = 1
                                        }
                                    }
                                }
                            }

                    }
                }
            }

    }

    private fun addItem(viewId: Int, imgId: Int, tag: String) {
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
            val tagInfo = view.tag
            Toast.makeText(this, tagInfo.toString(), Toast.LENGTH_SHORT).show()


            val infoView = InfoView(this, null)
            val icon = map[tagInfo]
            if (icon != null) {
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection("Item")
                    .document(tagInfo.toString())

                docRef.get().addOnSuccessListener { doc ->
                    val info = doc.getString("Description")
                    if (equipmentNum.contains(tagInfo)){
                        infoView.setView(icon, info.toString(), "已裝備")
                        infoView.setClick(click = false, focus = false)
                    }else{
                        infoView.setView(icon, info.toString(), "裝備")
                        infoView.setClick(click = true, focus = true)
                    }
                }

            }

            //彈窗設定
            val marginInDp = 20 // 20dp
            val marginInPixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                marginInDp.toFloat(),
                resources.displayMetrics
            ).toInt()

            val popupWindow = PopupWindow(this).apply {
                contentView = infoView
                width = resources.displayMetrics.widthPixels - 2 * marginInPixels
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                isFocusable = true
                isClippingEnabled = true
                setBackgroundDrawable(ColorDrawable(Color.BLACK))
            }


//            val popupWindow = PopupWindow(this).apply {
//                contentView = infoView
//                width = ViewGroup.LayoutParams.MATCH_PARENT
//                height = ViewGroup.LayoutParams.WRAP_CONTENT
//                //沒添加會一直創建新的
//                isFocusable = true
//                //全屏背景
//                isClippingEnabled = true
//                //透明背景
//                setBackgroundDrawable(ColorDrawable(Color.BLACK))
//
//            }
            popupWindow.isOutsideTouchable = false // true 表示外部可触摸关闭，false 表示外部不可触摸关闭

            infoView.findViewById<Button>(R.id.sure).setOnClickListener {

            }


            //出現位置
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        }

        scrollViewLayout.addView(customView)


    }


    private fun showEquipment(id: Int, viewId: Int) {
        val equipment = findViewById<ImageButton>(viewId)
        equipment.setImageResource(id)
        equipment.visibility = View.VISIBLE
    }

}