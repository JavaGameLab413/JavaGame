package com.example.myapplication

import android.app.Application

class GlobalVariable : Application(){

    companion object{
        //存放全域變數
        private var playernumber: String = "-1"
        //修改 變數値
        fun setNumber(name: String){
            this.playernumber = name
        }
        //取得 變數值
        fun getNumber(): String{
            return playernumber
        }


    }
}