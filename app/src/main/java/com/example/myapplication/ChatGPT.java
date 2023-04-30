package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ChatGPT extends AppCompatActivity {
    public static final String YOUR_KEY = "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";
    public static final String URL = "https://api.openai.com/v1/completions";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_gpt);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvAnswer = findViewById(R.id.textViewAnswer);
        Button buttonSend = findViewById(R.id.btSend);
        Log.e("test", "Create");
        buttonSend.setOnClickListener((View view) -> {
            String question = ((EditText)findViewById(R.id.edittextInput)).getText().toString();
            if (question.isEmpty()) return;
            ((TextView)findViewById(R.id.textViewQuestion)).setText(question);
            tvAnswer.setText("請稍候..");
            //設置Header中的Content-Type
            MediaType mediaType = MediaType.parse("application/json");
            //寫入body
            String content = new Gson().toJson(makeRequest(question));
            RequestBody requestBody = RequestBody.create(mediaType, content);
            //發送請求
            new HttpRequest().sendPOST(URL, requestBody, new HttpRequest.OnCallback() {
                @Override
                public void onOKCall(String respond) {
                    Log.d("TAG", "onOKCall: "+respond);
                    ChatGPTRespond chatGPTRespond = new Gson().fromJson(respond,ChatGPTRespond.class);
                    runOnUiThread(()->{
                        tvAnswer.setText(chatGPTRespond.getChoices().get(0).getText());
                    });

                }
                @Override
                public void onFailCall(String error) {
                    Log.e("TAG", "onFailCall: "+error);
                    tvAnswer.setText(error);
                }
            });
        });
    }
    //寫入body
    private Object makeRequest(String input){
        WeakHashMap<String,Object> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("model","text-davinci-003");
        weakHashMap.put("prompt",input);
        weakHashMap.put("temperature",0.5);
        weakHashMap.put("max_tokens",1000);
        weakHashMap.put("top_p",1);
        weakHashMap.put("frequency_penalty",0);
        weakHashMap.put("presence_penalty",0);
        return weakHashMap;
    }
}