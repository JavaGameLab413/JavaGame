package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class BackPack : AppCompatActivity() {
    private var te =1
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
                            .addOnSuccessListener { document ->

                                val a = map[entry]


                                if (a != null) {
                                    when(count){
                                        1-> {
                                            addItem(R.id.ItemList, a)
                                            count += 1
                                        }
                                        2-> {
                                            addItem(R.id.ItemList1, a)
                                            count += 1
                                        }
                                        3-> {
                                            addItem(R.id.ItemList2, a)
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
            val infoView = InfoView(this, null)


        }

        scrollViewLayout.addView(customView)
    }


}