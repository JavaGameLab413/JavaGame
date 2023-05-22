package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Signup : AppCompatActivity() {

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

        var serialNumber: Int = -1

        signup.setOnClickListener {

            //判斷空值
            if (name.text.toString() == "") {
                Toast.makeText(this, "暱稱不可為空!!!", Toast.LENGTH_SHORT).show()
            } else if (account.text.toString() == "") {
                Toast.makeText(this, "帳號不可為空!!!", Toast.LENGTH_SHORT).show()
            } else if (password.text.toString() == "") {
                Toast.makeText(this, "密碼不可為空!!!", Toast.LENGTH_SHORT).show()
            } else {
                //由大到小排序並取得流水號的最大值
                db.collection("users").orderBy("serialNumber", Query.Direction.DESCENDING)
                    .limit(1).get()
                    .addOnSuccessListener { documents ->
                        serialNumber = Integer.parseInt(
                            documents.first().getLong("serialNumber").toString()
                        ) + 1
                        Log.d("流水號最大值 :", serialNumber.toString())
                    }
                //看帳號是否存在，如果不存在就可以建立帳號
                newDocRef.whereEqualTo("account", account.text.toString()).get()
                    .addOnSuccessListener { documents ->
                        if (documents.size() == 0) {
                            serialNumber++
                            Log.d("新增的流水號", serialNumber.toString())

                            //查是否重複名稱
                            db.collection("properties").whereEqualTo("name", name.text.toString())
                                .get()
                                .addOnSuccessListener { doc ->
                                    if (doc.size() == 0) {
                                        // 將資料存放在data
                                        val data = hashMapOf(
                                            "account" to account.text.toString(),
                                            "password" to password.text.toString(),
                                            "serialNumber" to serialNumber
                                        )

                                        val writeUser =
                                            db.collection("users").document(serialNumber.toString())
                                        val writeData = db.collection("properties")
                                            .document(serialNumber.toString())

                                        //將 data 寫入資料庫
                                        writeUser.set(data)

                                        val data2 = hashMapOf(
                                            "name" to name.text.toString(),
                                            "lv" to 1,
                                            "history" to 0,
                                            "money" to 0,
                                            "serialNumber" to serialNumber
                                        )
                                        //將資料寫入資料庫
                                        Log.d("test", "success!")
                                        writeData.set(data2)

                                        //顯示註冊成功的彈窗
                                        Toast.makeText(this, "註冊成功!", Toast.LENGTH_SHORT).show()
                                        Log.d(TAG, "Signup success!")

                                        //切換畫面至登入
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)

                                        //將ID寫入本地資料庫User
                                        val sharedPreferences =
                                            getSharedPreferences("User", MODE_PRIVATE)
                                        sharedPreferences.edit()
                                            .putString("ID", serialNumber.toString()).apply()
                                    } else {
                                        Toast.makeText(this, "此名稱已存在!", Toast.LENGTH_SHORT).show()
                                    }
                                }

                        } else {
                            //顯示註冊失敗的彈窗
                            Toast.makeText(this, "帳號已存在!", Toast.LENGTH_SHORT).show()
                        }
                    }

            }


        }
    }

}