package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class Friend: AppCompatActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val addFriend = findViewById<Button>(R.id.addFriend)

        addFriend.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.addFriend -> {

            }

        }
    }

    override fun onResume() {
        super.onResume()
        add(R.drawable.head2,"AA",20,true)
    }

    //添加好友欄位
    private fun add(head:Int,name:String,lv:Int,state:Boolean){
        val linearLayout = findViewById<LinearLayout>(R.id.friends)
        // 创建多个 CustomImageViewTextView 对象并设置属性
        for (i in 1..10) {
            val customView = FriendInfo(this, null)
            customView.setImageResource(head)
            customView.setText(name,"Lv $lv",state)
            // 添加 CustomImageViewTextView 到 LinearLayout 中
            linearLayout.addView(customView)
        }
    }
}