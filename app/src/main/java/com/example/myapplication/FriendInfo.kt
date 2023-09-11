package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class FriendInfo(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val head: ImageView
    private val name: TextView
    private val lv: TextView

    init {
        // 从 XML 布局文件中加载自定义视图的布局
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.friend_info, this, true)

        // 获取 ImageView 和 TextView 的引用
        head = findViewById(R.id.f_head)
        name = findViewById(R.id.f_name)
        lv = findViewById(R.id.f_lv)

    }

    // 可以在这里自定义 ImageView 和 TextView 的属性、事件等
    fun setImageResource(resId: Int) {
        head.setImageResource(resId)
    }

    fun setText(f_name: String,f_lv:String) {
        name.text = f_name
        lv.text= f_lv
    }
}
