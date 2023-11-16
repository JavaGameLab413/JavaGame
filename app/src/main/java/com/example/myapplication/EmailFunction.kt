package com.example.myapplication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailFunction {
    private var tag = "LoginFunction"
    fun send():String{
        var mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser

        user?.sendEmailVerification()
            ?.addOnCompleteListener { verificationTask ->
                if (verificationTask.isSuccessful) {
                    // 驗證郵件已成功發送
                    Log.d(tag, "發送成功")
                } else {
                    // 發送驗證郵件失敗
                    val exception = verificationTask.exception
                    Log.d(tag, "發送失敗$exception")
                }
            }
        return "成功"
    }

}