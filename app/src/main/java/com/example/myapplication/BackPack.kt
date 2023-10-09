package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout

class BackPack : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_pack)

        val scrollViewLayout = findViewById<LinearLayout>(R.id.ItemList)
        val scrollViewLayout1 = findViewById<LinearLayout>(R.id.ItemList1)
        val scrollViewLayout2 = findViewById<LinearLayout>(R.id.ItemList2)

        for (i in 1..5) {
            val customView = BackpackItems(this, null)
            customView.setImageResource(R.drawable.healing_potion)
            val customView1 = BackpackItems(this, null)
            customView1.setImageResource(R.drawable.powerup)
            val customView2 = BackpackItems(this, null)
            customView2.setImageResource(R.drawable.healing_potion)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER
            layoutParams.bottomMargin = 20

            // 添加 CustomImageViewTextView 到 ScrollView 的直接子视图中
            customView.layoutParams = layoutParams
            customView1.layoutParams = layoutParams
            customView2.layoutParams = layoutParams

            scrollViewLayout.addView(customView)
            scrollViewLayout1.addView(customView1)
            scrollViewLayout2.addView(customView2)
        }
    }


}