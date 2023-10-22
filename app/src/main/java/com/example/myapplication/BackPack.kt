package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class BackPack : AppCompatActivity(), View.OnClickListener {
    private val map: Map<String, Int> =
        mapOf("M1" to R.drawable.healing_potion, "M2" to R.drawable.powerup1)
    private var equipmentNum = ArrayList<String>(5)
    private var wear: String = "初心者"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_pack)

        readData()


        //測試
        equipmentNum.add("M2")
        equipmentNum.add("M1")
        equipmentNum.remove("M2")

        val equipment1: ImageButton = findViewById(R.id.equipment1)
        val equipment2: ImageButton = findViewById(R.id.equipment2)
        val equipment3: ImageButton = findViewById(R.id.equipment3)
        val equipment4: ImageButton = findViewById(R.id.equipment4)
        val equipment5: ImageButton = findViewById(R.id.equipment5)
        val equipmentButton: Button = findViewById(R.id.equipmentButton)
        val titleButton: Button = findViewById(R.id.titleButton)

        equipment1.setOnClickListener(this)
        equipment2.setOnClickListener(this)
        equipment3.setOnClickListener(this)
        equipment4.setOnClickListener(this)
        equipment5.setOnClickListener(this)
        equipmentButton.setOnClickListener(this)
        titleButton.setOnClickListener(this)

        val back: ImageButton = findViewById(R.id.back)
        back.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()


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

        //重置稱號顯示
        clearTitleView()

        //稱號
        val title = findViewById<TextView>(R.id.titleNames)
        title.text = wear
        addTitle("初心者")
        addTitle("老手")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.equipment1 -> {
                val equipment = findViewById<ImageButton>(R.id.equipment1)
                equipmentNum.remove(equipment.tag.toString())
            }
            R.id.equipment2 -> {
                val equipment = findViewById<ImageButton>(R.id.equipment2)
                equipmentNum.remove(equipment.tag)
            }
            R.id.equipment3 -> {
                val equipment = findViewById<ImageButton>(R.id.equipment3)
                equipmentNum.remove(equipment.tag)
            }
            R.id.equipment4 -> {
                val equipment = findViewById<ImageButton>(R.id.equipment4)
                equipmentNum.remove(equipment.tag)
            }
            R.id.equipment5 -> {
                val equipment = findViewById<ImageButton>(R.id.equipment5)
                equipmentNum.remove(equipment.tag)
            }
            R.id.equipmentButton -> {
                change(R.id.equipment, R.id.title)
            }
            R.id.titleButton -> {
                change(R.id.title, R.id.equipment)
            }

        }
        closeEquipment()
        onResume()

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
            //Toast.makeText(this, tagInfo.toString(), Toast.LENGTH_SHORT).show()


            val infoView = InfoView(this, null)
            val icon = map[tagInfo]
            if (icon != null) {
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection("Item")
                    .document(tagInfo.toString())

                docRef.get().addOnSuccessListener { doc ->
                    val info = doc.getString("Description")
                    if (equipmentNum.contains(tagInfo)) {
                        infoView.setView(icon, info.toString(), "已裝備")
                        infoView.setClick(click = false, focus = false)
                    } else {
                        infoView.setView(icon, info.toString(), "裝備")
                        infoView.setClick(click = true, focus = true)
                    }
                }

            }

            //彈窗設定
            //dp轉換(設寬度用)
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
                //沒添加會一直創建新的
                isFocusable = true
                //全屏背景
                isClippingEnabled = true
                setBackgroundDrawable(ColorDrawable(Color.BLACK))
            }


            popupWindow.isOutsideTouchable = false // true 表示外部可触摸关闭，false 表示外部不可触摸关闭

            infoView.findViewById<Button>(R.id.sure).setOnClickListener {
                equipmentNum.add(tagInfo as String)
                onResume()
                popupWindow.dismiss()
            }


            //出現位置
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        }

        scrollViewLayout.addView(customView)


    }


    private fun showEquipment(id: Int, viewId: Int, tag: String) {
        val equipment = findViewById<ImageButton>(viewId)
        equipment.tag = tag
        equipment.setImageResource(id)
        equipment.visibility = View.VISIBLE
    }

    private fun closeEquipment() {
        val equipment1: ImageButton = findViewById(R.id.equipment1)
        val equipment2: ImageButton = findViewById(R.id.equipment2)
        val equipment3: ImageButton = findViewById(R.id.equipment3)
        val equipment4: ImageButton = findViewById(R.id.equipment4)
        val equipment5: ImageButton = findViewById(R.id.equipment5)
        equipment1.visibility = View.INVISIBLE
        equipment2.visibility = View.INVISIBLE
        equipment3.visibility = View.INVISIBLE
        equipment4.visibility = View.INVISIBLE
        equipment5.visibility = View.INVISIBLE
    }

    private fun addTitle(title: String) {
        val scrollViewLayout = findViewById<LinearLayout>(R.id.showTitle)

        val customView = TitleView(this, null)
        customView.setting(title)
        customView.tag = title

        //View布局
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.CENTER
        layoutParams.bottomMargin = 20

        //背景顏色
        customView.setBackgroundColor(Color.parseColor("#CCFFFFFF"))

        // 添加 CustomImageViewTextView 到 ScrollView 的子視圖中
        customView.layoutParams = layoutParams

        //如果是穿戴中的打勾
        if (title == wear) {
            customView.visible(View.VISIBLE)
        } else {
            customView.visible(View.INVISIBLE)
        }

        //設置每個動作
        customView.setOnClickListener { view ->
            wear = view.tag.toString()
            onResume()
        }

        scrollViewLayout.addView(customView)


    }

    private fun clearTitleView() {
        val view = findViewById<LinearLayout>(R.id.showTitle)
        view.removeAllViews()
    }

    private fun change(open: Int, close: Int) {
        val closeView = findViewById<LinearLayout>(close)
        val openView = findViewById<LinearLayout>(open)

        closeView.visibility = View.INVISIBLE
        openView.visibility = View.VISIBLE
    }

}