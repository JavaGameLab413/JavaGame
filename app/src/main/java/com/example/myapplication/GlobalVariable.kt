package com.example.myapplication

import android.app.Application

class GlobalVariable : Application(){
    companion object{
        //存放全域變數
        private var playername: String = ""
        //修改 變數値
        fun setName(name: String){
            this.playername = name
        }
        //取得 變數值
        fun getName(): String{
            return playername
        }
    }
}