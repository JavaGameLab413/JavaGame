package com.example.myapplication

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

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

        // Access Firebase Firestorm
        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        val newDocRef = db.collection("users").document()

        signup.setOnClickListener{



            Log.d("MainActivity", name.text.toString())
            Log.d("MainActivity", account.text.toString())
            Log.d("MainActivity", password.text.toString())


        }



    }

}