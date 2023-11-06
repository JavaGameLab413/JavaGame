package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private val userDatabaseCollectionName = "PlayerAccount"
    private val playerInfoDatabaseCollectionName = "PlayerInfo"
    private val playerAccountDatabaseAccount = "Account"
    private val playerAccountDatabasePasswordField = "PWD"
    override fun onCreate(savedInstanceState: Bundle?) {
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
        val readDocRed = db.collection(userDatabaseCollectionName)

        //設置登入按鈕功能
        login.setOnClickListener {
            val account = inputAccount.text.toString()
            //Log.d("test", inputAccount.text.toString())
            readDocRed.document(account).get()
                .addOnSuccessListener { documents ->
                    // 找到使用者，檢查密碼
                    val password = documents.getString(playerAccountDatabasePasswordField)
                    if (password == inputPassword.text.toString()) {
                        // 密碼正確，登錄成功
                        Toast.makeText(this, "登入成功!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Login success!")
                        //切換畫面
                        finish()


                        //抓流水號
                        val serialNumber = documents.getLong("SerialNumber").toString()
                        Log.d(TAG, serialNumber)
                        //將ID寫入本地資料庫PlayerInfo
                        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
                        sharedPreferences.edit().putString("ID", serialNumber).apply()
                        sharedPreferences.edit().putString("Title", "初心者").apply()

                    } else {
                        // 密碼錯誤
                        Toast.makeText(this, "登入失敗!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Incorrect password!")
                    }

                }.addOnFailureListener {
                    // 讀取資料失敗
                    Toast.makeText(this, "登入失敗!", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "User not found!")

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
                .whereEqualTo(playerAccountDatabaseAccount, userAccount)
                .whereEqualTo(playerAccountDatabasePasswordField, userPassword)

            query.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    // 刪除符合條件的文檔
                    document.reference.delete().addOnSuccessListener {
                        FirebaseFirestore.getInstance()
                            .collection(playerInfoDatabaseCollectionName)
                            .document(document.id).delete()
                        Toast.makeText(this, "已刪除資料", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { e ->
                        Log.e(TAG, "刪除資料失敗: ", e)
                        Toast.makeText(this, "刪除資料失敗", Toast.LENGTH_SHORT).show()
                    }

                }
            }.addOnFailureListener { e ->
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val window = this.window

        val decorView = window.decorView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.also {
                it.hide(statusBars())
                it.hide(navigationBars())
            }

        }
        else {
            // 如果设备不支持 WindowInsetsController，则可以尝试使用旧版方法  <版本低於Android 11>
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}