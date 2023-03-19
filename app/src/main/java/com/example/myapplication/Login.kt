package com.example.myapplication

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
        val account = findViewById<EditText>(R.id.InputAccount)
        val password = findViewById<EditText>(R.id.InputPassword)

        // Access Firebase Firestorm
        val db = FirebaseFirestore.getInstance()
        // Create a new document with a generated ID
        val newDocRef = db.collection("users").document()
        var serialNumber: Int = 0

        add.setOnClickListener {
            serialNumber += 1



            // Set the document data
            val data = hashMapOf(
                "account" to account.text.toString(),
                "password" to password.text.toString(),
                "serialNumber" to serialNumber
            )

            // Write the data to the document
            newDocRef.set(data)
                .addOnSuccessListener {
                    Log.d("MainActivity", "Data added to Firestorm")
                }
                .addOnFailureListener {
                    Log.e("MainActivity", "Error adding data to Firestorm")
                }
        }
    }
}