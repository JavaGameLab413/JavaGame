package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class Friend: AppCompatActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val linearLayout = findViewById<LinearLayout>(R.id.friends)
        val addFriend = findViewById<Button>(R.id.addFriend)

        addFriend.setOnClickListener(this)

        // 创建多个 CustomImageViewTextView 对象并设置属性
        for (i in 1..10) {
            val customView = FriendInfo(this, null)
            customView.setImageResource(R.drawable.head2)
            customView.setText("Item","Lv $i",true)
            // 添加 CustomImageViewTextView 到 LinearLayout 中
            linearLayout.addView(customView)
        }


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.addFriend -> {
                val intent = Intent(this, Fight::class.java)
                startActivity(intent)
            }


        }
    }
}