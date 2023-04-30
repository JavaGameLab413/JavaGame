package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Shop : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //啟用自定義的主題
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_shop)
    }
}