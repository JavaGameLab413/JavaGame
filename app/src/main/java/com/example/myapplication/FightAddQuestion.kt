package com.example.myapplication

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class FightAddQuestion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight_add_question)
        var database: EditText = findViewById(R.id.dataBaseInput);
        var question: EditText = findViewById(R.id.questionInput);
        var selectA: EditText = findViewById(R.id.selectAInput);
        var selectB: EditText = findViewById(R.id.selectBInput);
        var selectC: EditText = findViewById(R.id.selectCInput);
        var selectD: EditText = findViewById(R.id.selectDInput);
        var answer: Spinner = findViewById(R.id.answerInput);
        var send:Button = findViewById(R.id.send);

        send.setOnClickListener {
            // 存取資料庫
            val dbCollection = FirebaseFirestore.getInstance().collection(database.text.toString())

            val data: MutableMap<String, Any> = HashMap()
            data["Info"] = question.text.toString()
            data["SelectA"] = selectA.text.toString()
            data["SelectB"] = selectB.text.toString()
            data["SelectC"] = selectC.text.toString()
            data["SelectD"] = selectD.text.toString()
            //data["Answer"] = answer.text.toString()

            dbCollection.add(data)
                .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference -> // 新增成功
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
                    question.setText("")
                    selectA.setText("")
                    selectB.setText("")
                    selectC.setText("")
                    selectD.setText("")
                    //answer.setText("")
                    Toast.makeText(this, "新增題目成功!", Toast.LENGTH_SHORT).show()
                })
                .addOnFailureListener(OnFailureListener { e -> // 新增失敗
                    Log.w(TAG, "Error adding document", e)
                })
        }
    }
}