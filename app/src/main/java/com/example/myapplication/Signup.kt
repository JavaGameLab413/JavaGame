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
import com.google.firebase.firestore.Query

class Signup : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //按鈕
        val signup = findViewById<Button>(R.id.ButtonSignup)

        //輸入的文字框(帳號密碼)
        val name = findViewById<EditText>(R.id.InputName)
        val account = findViewById<EditText>(R.id.InputAccount)
        val password = findViewById<EditText>(R.id.InputPassword)

        // 存取資料庫
        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        val newDocRef = db.collection("users")
        val writeUser = db.collection("users").document()
        val writeData = db.collection("property").document()

        var serialNumber:Int =-1

        signup.setOnClickListener{

            //由大到小排序並取得第一位的值
             db.collection("users").orderBy("serialNumber",Query.Direction.DESCENDING)
                .limit(1).get()
                 .addOnSuccessListener { documents ->
                    serialNumber = Integer.parseInt(documents.first().getLong("serialNumber").toString())
                    Log.d("流水號最大值 :", serialNumber.toString())
                 }

            newDocRef.whereEqualTo("account",account.text.toString()).get()
                .addOnSuccessListener { documents ->
                    if(documents.size()==0){
                        serialNumber ++
                        Log.d("新增的流水號", serialNumber.toString())
                        // Set the document data
                        val data = hashMapOf(
                            "account" to account.text.toString(),
                            "password" to password.text.toString(),
                            "serialNumber" to serialNumber
                        )

                        writeUser.set(data)

                        val data2 = hashMapOf(
                            "name" to name.text.toString(),
                            "lv" to 1,
                            "history" to 0,
                            "money" to 0,
                            "serialNumber" to serialNumber
                        )

                        writeData.set(data2)

                        Toast.makeText(this,"註冊成功!",Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Signup success!")

                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)

                    }
                    else{
                        Toast.makeText(this,"帳號已存在!",Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Signup fail!")
                    }


                }


        }



    }

}