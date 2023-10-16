package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class InfoView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val imageView: ImageView
    private val textView: TextView
    private val button: Button

    init {
        // 從 XML 布局文件中加载自定義視圖的布局
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.info_view, this, true)

        // 获取 ImageView 的引用
        imageView = findViewById(R.id.Items)
        textView = findViewById(R.id.Items)
        button = findViewById(R.id.Items)
    }

    fun setView(resId: Int,info:String,equip:String) {
        imageView.setImageResource(resId)
        textView.text = info
        button.text = equip
    }
}