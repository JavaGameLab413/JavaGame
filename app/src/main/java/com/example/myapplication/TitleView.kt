package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class TitleView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val imageView1: ImageView
    private val textView1: TextView

    init {
        // 从 XML 布局文件中加载自定义视图的布局
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.title_view, this, true)

        // 获取 ImageView 的引用
        imageView1 = findViewById(R.id.select)
        textView1 = findViewById(R.id.titleName)
    }

    fun setting(con: String) {
        textView1.text = con
    }
    fun visible(visible:Int){
        imageView1.visibility= visible
    }
}