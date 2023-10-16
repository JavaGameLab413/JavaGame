package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class BackPack : AppCompatActivity() {
    private var te =1
    private val map: Map<String, Int> = mapOf("M1" to R.drawable.head, "Bonnie" to 20, "Cynthia" to 30)

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
                    for (entry in data.entries) {
                        val fieldName = entry.key
                        val itemRef = db.collection(itemDatabaseCollectionName).document(fieldName)
                        itemRef.get()
                            .addOnSuccessListener { document ->

                                val picName=document.getString("Picture")

                                val a = map[picName]
                                if (a != null) {
                                    addItem(R.id.ItemList,a)
                                }
                            }

                    }
                }
            }

//        for (i in 1..8) { 測試
//            addItem(R.id.ItemList,R.drawable.healing_potion)
//        }
    }

    private fun addItem(viewId:Int,imgId:Int){
        val scrollViewLayout = findViewById<LinearLayout>(viewId)

        val customView = BackpackItems(this, null)
        customView.setImageResource(imgId)
        customView.tag = te
        te++

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

        }

        scrollViewLayout.addView(customView)
    }


}