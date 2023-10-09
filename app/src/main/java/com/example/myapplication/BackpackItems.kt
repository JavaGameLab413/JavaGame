package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout

class BackpackItems(context: Context, attrs: AttributeSet?) :LinearLayout (context, attrs){

    private val imageView1: ImageView

    init {
        // 从 XML 布局文件中加载自定义视图的布局
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.component_backage_items, this, true)

        // 获取 ImageView 的引用
        imageView1 = findViewById(R.id.Items)
    }

    fun setImageResource(resId: Int) {
        imageView1.setImageResource(resId)

    }
}