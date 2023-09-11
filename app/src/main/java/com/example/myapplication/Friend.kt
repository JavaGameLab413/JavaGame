package com.example.myapplication

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class Friend: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val linearLayout = findViewById<LinearLayout>(R.id.friends)


        // 创建多个 CustomImageViewTextView 对象并设置属性
        for (i in 1..5) {
            val customView = FriendInfo(this, null)
            customView.setImageResource(R.drawable.head2)
            customView.setText("Item","Lv $i")

            // 添加 CustomImageViewTextView 到 LinearLayout 中
            linearLayout.addView(customView)
        }
    }
}