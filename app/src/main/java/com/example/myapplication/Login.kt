package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //按鈕
        val login = findViewById<Button>(R.id.ButtonLogin)
        val change = findViewById<Button>(R.id.ButtonChange)
        val add = findViewById<Button>(R.id.ButtonAdd)
        //輸入的文字框(帳號密碼)
        val inputAccount = findViewById<EditText>(R.id.InputAccount)
        val inputPassword = findViewById<EditText>(R.id.InputPassword)

        // Access Firebase Firestorm
        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        val newDocRef = db.collection("users").document()
        val readDocRed = db.collection("users")

        //設置登入按鈕功能
        login.setOnClickListener {
            Log.d("test", inputAccount.text.toString())
            readDocRed.whereEqualTo("account", inputAccount.text.toString()).get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        // 找到使用者，檢查密碼
                        val user = documents.first()
                        val password = user.getString("password")
                        if (password == inputPassword.text.toString()) {
                            // 密碼正確，登錄成功
                            Toast.makeText(this,"登入成功!",Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "Login success!")

                            //抓流水號
                            val serialNumber = user.getLong("serialNumber").toString()
                            //設全域變數
                            GlobalVariable.setName(serialNumber)


                        } else {
                            // 密碼錯誤
                            Toast.makeText(this,"登入失敗!",Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "Incorrect password!")
                        }
                    } else {
                        // 找不到使用者
                        Toast.makeText(this,"登入失敗!",Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "User not found!")
                    }
                }
                .addOnFailureListener { exception ->
                    // 讀取資料失敗
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
        //新增帳號功能按鈕監聽
        add.setOnClickListener {

            // Set the document data
//            val data = hashMapOf(
//                "account" to inputAccount.text.toString(),
//                "password" to inputPassword.text.toString(),
//                "serialNumber" to serialNumber
//            )

            // Write the data to the document
//            newDocRef.set(data)
//                .addOnSuccessListener {
//                    Log.d("MainActivity", "Data added to Firestorm")
//                }
//                .addOnFailureListener {
//                    Log.e("MainActivity", "Error adding data to Firestorm")
//                }

            val intent = Intent(this, Signup::class.java)
            startActivity(intent)

        }


    }
}