package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Toast

class BackPack : AppCompatActivity() {
    private var te =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_pack)

        readData()

    }

    private fun readData(){
        for (i in 1..8) {
            addItem()
        }
    }

    private fun addItem(){
        val scrollViewLayout = findViewById<LinearLayout>(R.id.ItemList)
        val scrollViewLayout1 = findViewById<LinearLayout>(R.id.ItemList1)
        val scrollViewLayout2 = findViewById<LinearLayout>(R.id.ItemList2)


        val customView = BackpackItems(this, null)
        te++
        customView.setImageResource(R.drawable.healing_potion)
        customView.tag = te
        val customView1 = BackpackItems(this, null)
        customView1.id=te
        te++
        customView1.tag=te
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

        // 设置点击事件监听器
        customView.setOnClickListener { view ->
            val a = view.tag
            // 这里写入你想要执行的点击事件处理逻辑
            // 例如：
            Toast.makeText(this, a.toString(), Toast.LENGTH_SHORT).show()

        }

        customView1.setOnClickListener { view ->
            val a = view.tag
            // 这里写入你想要执行的点击事件处理逻辑
            // 例如：
            Toast.makeText(this, a.toString(), Toast.LENGTH_SHORT).show()

        }

        scrollViewLayout.addView(customView)
        scrollViewLayout1.addView(customView1)
        scrollViewLayout2.addView(customView2)
    }



}