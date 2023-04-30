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

class Login : AppCompatActivity() {

    private val userDatabaseCollectionName = "users"
    private val propertysDatabaseCollectionName = "propertys"
    private val userDatabaseAccountField = "account"
    private val userDatabasePasswordField = "password"
    override fun onCreate(savedInstanceState: Bundle?) {
        //啟用自定義的主題
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //按鈕
        val login = findViewById<Button>(R.id.ButtonLogin)
        val delete = findViewById<Button>(R.id.ButtonDeleteAccount)
        val add = findViewById<Button>(R.id.ButtonAdd)

        //輸入的文字框(帳號密碼)
        val inputAccount = findViewById<EditText>(R.id.InputAccount)
        val inputPassword = findViewById<EditText>(R.id.InputPassword)

        // Access Firebase Firestorm
        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        db.collection(userDatabaseCollectionName).document()
        val readDocRed = db.collection(userDatabaseCollectionName)

        //設置登入按鈕功能
        login.setOnClickListener {
            //Log.d("test", inputAccount.text.toString())
            readDocRed.whereEqualTo(userDatabaseAccountField , inputAccount.text.toString()).get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        // 找到使用者，檢查密碼
                        val user = documents.first()
                        val password = user.getString(userDatabasePasswordField)
                        if (password == inputPassword.text.toString()) {
                            // 密碼正確，登錄成功
                            Toast.makeText(this, "登入成功!", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "Login success!")
                            //切換畫面
                            val intent = Intent(this, Start::class.java)
                            startActivity(intent)
                            //抓流水號
                            val serialNumber = user.getLong("serialNumber").toString()
                            //設全域變數
                            GlobalVariable.setNumber(serialNumber)
                            Log.d("test", GlobalVariable.getNumber())


                        } else {
                            // 密碼錯誤
                            Toast.makeText(this, "登入失敗!", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "Incorrect password!")
                        }
                    } else {
                        // 找不到使用者
                        Toast.makeText(this, "登入失敗!", Toast.LENGTH_SHORT).show()
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
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
        //刪除帳號功能按鈕監聽
        delete.setOnClickListener {
            val userAccount = inputAccount.text.toString()
            val userPassword = inputPassword.text.toString()

            val query = FirebaseFirestore.getInstance().collection(userDatabaseCollectionName)
                .whereEqualTo(userDatabaseAccountField ,userAccount)
                .whereEqualTo(userDatabasePasswordField ,userPassword)

            query.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // 刪除符合條件的文檔
                        document.reference.delete()
                            .addOnSuccessListener {
                                FirebaseFirestore.getInstance().collection(propertysDatabaseCollectionName)
                                    .document(document.id)
                                    .delete()
                                Toast.makeText(this, "已刪除資料", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "刪除資料失敗: ", e)
                                Toast.makeText(this, "刪除資料失敗", Toast.LENGTH_SHORT).show()
                            }

                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "查無此帳號: ", e)
                    Toast.makeText(this, "查詢資料失敗", Toast.LENGTH_SHORT).show()
                }
        }
        //新增帳號功能按鈕監聽
        add.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)

        }


    }
}