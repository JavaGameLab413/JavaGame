package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets.Type.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

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
        val newDocRef = db.collection("PlayerAccount")

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
                db.collection("PlayerInfo").get()
                    .addOnSuccessListener { documents ->
                        serialNumber = documents.size()
                        Log.d("流水號最大值", serialNumber.toString())
                    }

                val documentName = account.text.toString()
                //看帳號是否存在，如果不存在就可以建立帳號
                newDocRef.document(documentName).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            Toast.makeText(this, "帳號已存在!", Toast.LENGTH_SHORT).show()

                        } else {
                            Log.d("新增的流水號", serialNumber.toString())

                            //查是否重複名稱
                            db.collection("PlayerInfo")
                                .whereEqualTo("PlayerId", name.text.toString())
                                .get()
                                .addOnSuccessListener { doc ->
                                    if (doc.size() == 0) {
                                        // 將資料存放在data
                                        val data = hashMapOf(
                                            "PWD" to password.text.toString(),
                                            "serialNumber" to serialNumber
                                        )

                                        //將帳號設為文件名稱
                                        val writeUser =
                                            db.collection("PlayerAccount").document(documentName)
                                        val writeData = db.collection("PlayerInfo")
                                            .document(serialNumber.toString())
                                        val writeBag = db.collection("BackPage")
                                            .document(serialNumber.toString())

                                        //將 data 寫入資料庫
                                        writeUser.set(data)

                                        val data2 = hashMapOf(
                                            "PlayerId" to name.text.toString(),
                                            "Level" to 1,
                                            "Gold" to 0,
                                            "TitleNumber" to 0,
                                            "exp" to 0
                                        )

                                        //將資料寫入資料庫
                                        Log.d("test", "success!")
                                        writeData.set(data2)

                                        val data3 = hashMapOf(
                                            "M1" to 0,
                                            "M2" to 0,
                                            "M3" to 0,
                                            "M4" to 0,
                                            "M5" to 0,
                                            "M6" to 0
                                        )
                                        Log.d("testBackPage", "success!")
                                        writeBag.set(data3)

                                        //顯示註冊成功的彈窗
                                        Toast.makeText(this, "註冊成功!", Toast.LENGTH_SHORT).show()
                                        Log.d(TAG, "Signup success!")

                                        //切換畫面至開始
                                        finish()
                                        val close = Intent(this, MainActivity::class.java)
                                        startActivity(close)
                                        finish()
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)

                                        //將ID寫入本地資料庫User
                                        val sharedPreferences =
                                            getSharedPreferences("User", MODE_PRIVATE)
                                        sharedPreferences.edit()
                                            .putString("ID", serialNumber.toString()).apply()

                                    }
                                    else{
                                        Toast.makeText(this, "名稱重複", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
            }
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